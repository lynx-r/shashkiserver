/*
 * © Copyright
 *
 * ArticleService.java is part of shashkiserver.
 *
 * shashkiserver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * shashkiserver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with shashkiserver.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.workingbit.shashkiapp.service;

import com.workingbit.shashkiapp.config.ErrorMessages;
import com.workingbit.shashkiapp.domain.Article;
import com.workingbit.shashkiapp.domain.ArticleBlock;
import com.workingbit.shashkiapp.domain.ArticlesResponse;
import com.workingbit.shashkiapp.domain.EnumArticleStatus;
import com.workingbit.shashkiapp.repo.ArticleRepo;
import com.workingbit.shashkiapp.repo.AuthArticleRepo;
import com.workingbit.shashkiapp.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class ArticleService {

  private final ArticleBlockService articleBlockService;
  private final ArticleRepo articleRepo;
  private final AuthArticleRepo authArticleRepo;

  public ArticleService(ArticleBlockService articleBlockService,
                        ArticleRepo articleRepo,
                        AuthArticleRepo authArticleRepo) {
    this.articleBlockService = articleBlockService;
    this.articleRepo = articleRepo;
    this.authArticleRepo = authArticleRepo;
  }

//  public Mono<ArticlesContainer> authArticlesContainer(ObjectId containerId, ObjectId authorId) {
//    return authArticlesContainerRepo.findByIdAndAuthorId(containerId, authorId);
//  }

  public Mono<ArticleBlock> authAddArticleBlockToArticle(ObjectId articleId, ObjectId authorId, ArticleBlock articleBlock) {
    return authArticleRepo.findByAuthorIdAndId(authorId, articleId)
        .zipWhen(article -> articleBlockService.authCreateArticle(articleBlock, articleId))
        .flatMap(articleArticleTuple2 -> {
          var article = articleArticleTuple2.getT1();
          var articleNew = articleArticleTuple2.getT2();
          article.getArticlesIds().add(articleNew.getId());
          return articleRepo.save(article).thenReturn(articleNew);
        });
  }

  public Mono<Article> authCreateArticle(ObjectId userId, Article article) {
    article.setHumanReadableUrl(article.getTitle());
    article.setAuthorId(userId);
    return authArticleRepo
        .existsByHumanReadableUrl(article.getHumanReadableUrl())
        .flatMap(exists -> {
          Utils.setArticleHru(article, exists);
          return articleRepo.save(article);
        })
        .flatMap(articleNew -> {
          var articleBlock = new ArticleBlock();
          return authAddArticleBlockToArticle(articleNew.getId(), userId, articleBlock)
              .map(articleBlockNew -> {
                articleNew.getArticleBlocks().add(articleBlockNew);
                return articleNew;
              });
        });
  }

  public Mono<Article> authSaveArticle(ObjectId userId, Article articleClient) {
    return authArticleRepo.findByAuthorIdAndId(userId, articleClient.getId())
        .flatMap(article -> {
          if (article.getStatus().equals(EnumArticleStatus.REMOVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ARTICLE_IS_DELETED);
          }
          if (StringUtils.isNotBlank(articleClient.getTitle())) {
            String title = articleClient.getTitle().trim();
            article.setTitle(title);
          }
          if (StringUtils.isNotBlank(articleClient.getIntro())) {
            String intro = articleClient.getIntro().trim();
            article.setIntro(intro);
          }
          article.setTask(articleClient.isTask());
          article.setStatus(articleClient.getStatus());
          return articleRepo.save(article);
        });
  }

  public Mono<ArticlesResponse> authFindAllByAuthor(ObjectId userId, Integer page, Integer pageSize,
                                                    String sort, String sortDirection,
                                                    String contains) {
    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
    if (StringUtils.isBlank(contains)) {
      return Mono.zip(authArticleRepo.countByAuthorId(userId),
          authArticleRepo.findAllByAuthorId(userId,
              pageable)
              .collectList())
          .single()
          .map(ArticlesResponse::fromTuple2);
    } else {
      return Mono.zip(authArticleRepo.countAllByAuthorIdAndContains(userId, contains),
          authArticleRepo.findAllByAuthorIdAndContains(userId, contains, pageable)
              .collectList())
          .single()
          .map(ArticlesResponse::fromTuple2);
    }
  }

  public Mono<Article> authFindArticleByHruAndAuthorId(ObjectId userId, String hru) {
    return authArticleRepo.findByAuthorIdAndHumanReadableUrl(userId, hru);
  }

  public Mono<ArticlesResponse> findAllPublicArticles(Integer page,
                                                      Integer pageSize,
                                                      String sort,
                                                      String sortDirection,
                                                      String contains) {
    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
    return Mono
        .zip(articleRepo.countByPublished(),
            StringUtils.isBlank(contains)
                ? articleRepo.findAllByStatusPublished(pageable)
                .collectList()
                : articleRepo.findAllByStatusPublishedAndContains(contains, pageable)
                .collectList())
        .map(ArticlesResponse::fromTuple2);

  }

  public Mono<Article> findArticleByHru(String articleHru) {
    return articleRepo.findByHumanReadableUrl(articleHru);
  }
}
