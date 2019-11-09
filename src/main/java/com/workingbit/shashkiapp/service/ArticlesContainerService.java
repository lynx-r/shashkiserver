/*
 * © Copyright
 *
 * ArticlesContainerService.java is part of shashkiserver.
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
import com.workingbit.shashkiapp.domain.ArticlesContainer;
import com.workingbit.shashkiapp.domain.ArticlesResponse;
import com.workingbit.shashkiapp.domain.EnumArticleStatus;
import com.workingbit.shashkiapp.repo.ArticlesContainerRepo;
import com.workingbit.shashkiapp.repo.AuthArticlesContainerRepo;
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
public class ArticlesContainerService {

  private final ArticleService articleService;
  private final ArticlesContainerRepo articlesContainerRepo;
  private final AuthArticlesContainerRepo authArticlesContainerRepo;

  public ArticlesContainerService(ArticleService articleService,
                                  ArticlesContainerRepo articlesContainerRepo,
                                  AuthArticlesContainerRepo authArticlesContainerRepo) {
    this.articleService = articleService;
    this.articlesContainerRepo = articlesContainerRepo;
    this.authArticlesContainerRepo = authArticlesContainerRepo;
  }

//  public Mono<ArticlesContainer> authArticlesContainer(ObjectId containerId, ObjectId authorId) {
//    return authArticlesContainerRepo.findByIdAndAuthorId(containerId, authorId);
//  }

  public Mono<Article> authAddArticleToContainer(ObjectId containerId, ObjectId authorId, Article article) {
    return authArticlesContainerRepo.findByAuthorIdAndId(authorId, containerId)
        .zipWhen(container -> articleService.authCreateArticle(article, containerId))
        .flatMap(containerArticleTuple2 -> {
          var container = containerArticleTuple2.getT1();
          var articleNew = containerArticleTuple2.getT2();
          container.getArticlesIds().add(articleNew.getId());
          return articlesContainerRepo.save(container).thenReturn(articleNew);
        });
  }

  public Mono<ArticlesContainer> authCreateArticlesContainer(ObjectId userId, ArticlesContainer container) {
    container.setHumanReadableUrl(container.getTitle());
    container.setAuthorId(userId);
    return authArticlesContainerRepo
        .existsByHumanReadableUrl(container.getHumanReadableUrl())
        .flatMap(exists -> {
          Utils.setArticleHru(container, exists);
          return articlesContainerRepo.save(container);
        })
        .flatMap(containerNew -> {
          var article = new Article();
          return authAddArticleToContainer(containerNew.getId(), userId, article)
              .map(articleNew -> {
                containerNew.getArticles().add(articleNew);
                return containerNew;
              });
        });
  }

  public Mono<ArticlesContainer> authSaveArticlesContainer(ObjectId userId, ArticlesContainer container) {
    return authArticlesContainerRepo.findByAuthorIdAndId(userId, container.getId())
        .flatMap(article -> {
          if (article.getStatus().equals(EnumArticleStatus.REMOVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ARTICLE_IS_DELETED);
          }
          if (StringUtils.isNotBlank(container.getTitle())) {
            String title = container.getTitle().trim();
            article.setTitle(title);
          }
          if (StringUtils.isNotBlank(container.getIntro())) {
            String intro = container.getIntro().trim();
            article.setIntro(intro);
          }
          article.setTask(container.isTask());
          article.setStatus(container.getStatus());
          return articlesContainerRepo.save(article);
        });
  }

  public Mono<ArticlesResponse> authFindAllByAuthor(ObjectId userId, Integer page, Integer pageSize,
                                                    String sort, String sortDirection,
                                                    String contains) {
    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
    if (StringUtils.isBlank(contains)) {
      return Mono.zip(authArticlesContainerRepo.countByAuthorId(userId),
          authArticlesContainerRepo.findAllByAuthorId(userId,
              pageable)
              .collectList())
          .single()
          .map(ArticlesResponse::fromTuple2);
    } else {
      return Mono.zip(authArticlesContainerRepo.countAllByAuthorIdAndContains(userId, contains),
          authArticlesContainerRepo.findAllByAuthorIdAndContains(userId, contains, pageable)
              .collectList())
          .single()
          .map(ArticlesResponse::fromTuple2);
    }
  }

  public Mono<ArticlesContainer> authFindArticleByHruAndAuthorId(ObjectId userId, String hru) {
    return authArticlesContainerRepo.findByAuthorIdAndHumanReadableUrl(userId, hru);
  }

  public Mono<ArticlesResponse> findAllPublicArticles(Integer page,
                                                      Integer pageSize,
                                                      String sort,
                                                      String sortDirection,
                                                      String contains) {
    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
    return Mono
        .zip(articlesContainerRepo.countByPublished(),
            StringUtils.isBlank(contains)
                ? articlesContainerRepo.findAllByStatusPublished(pageable)
                .collectList()
                : articlesContainerRepo.findAllByStatusPublishedAndContains(contains, pageable)
                .collectList())
        .map(ArticlesResponse::fromTuple2);

  }

  public Mono<ArticlesContainer> findArticleByHru(String articleHru) {
    return articlesContainerRepo.findByHumanReadableUrl(articleHru);
  }
}
