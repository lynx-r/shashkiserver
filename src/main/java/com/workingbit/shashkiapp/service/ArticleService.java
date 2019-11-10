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
import com.workingbit.shashkiapp.domain.*;
import com.workingbit.shashkiapp.repo.ArticleRepo;
import com.workingbit.shashkiapp.repo.AuthArticleRepo;
import com.workingbit.shashkiapp.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        .zipWhen(article -> articleBlockService.authCreateArticleBlock(articleBlock, articleId))
        .flatMap(articleArticleTuple2 -> {
          var article = articleArticleTuple2.getT1();
          var articleBlockNew = articleArticleTuple2.getT2();
          article.getArticleBlockIds().add(0, articleBlockNew.getId());
          article.setSelectedArticleBlockId(articleBlockNew.getId());
          return articleRepo.save(article).thenReturn(articleBlockNew);
        });
  }

  public Mono<Article> authCreateArticle(ObjectId userId, ArticleCreateRequest articleCreateRequest) {
    var article = articleCreateRequest.getArticle();
    var gameNotation = articleCreateRequest.getNotation();
    article.setHumanReadableUrl(article.getTitle());
    article.setAuthorId(userId);
    return authArticleRepo
        .existsByHumanReadableUrl(article.getHumanReadableUrl())
        .flatMap(exists -> {
          Utils.setArticleHru(article, exists);
          article.setId(ObjectId.get());
          article.setStatus(EnumArticleStatus.DRAFT);
          var articleBlock = new ArticleBlock();
          articleBlock.setNotation(gameNotation);
          return articleBlockService.authCreateArticleBlock(articleBlock, article.getId())
              .flatMap(articleBlockNew -> {
                article.getArticleBlockIds().add(articleBlockNew.getId());
                article.getArticleBlocks().add(articleBlockNew);
                article.setSelectedArticleBlockId(articleBlockNew.getId());
                article.setSelectedArticleBlock(articleBlockNew);
                return articleRepo.save(article);
              });
        });
  }

  public Mono<Article> authSaveArticle(ObjectId userId, Article articleClient) {
    return authArticleRepo.findByAuthorIdAndId(userId, articleClient.getId())
        .flatMap(article -> {
          if (article.getStatus().equals(EnumArticleStatus.REMOVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ARTICLE_IS_DELETED);
          }
          article.setTitle(articleClient.getTitle());
          article.setIntro(articleClient.getIntro());
          article.setArticleBlockIds(articleClient.getArticleBlockIds());
          article.setSelectedArticleBlockId(articleClient.getSelectedArticleBlockId());
          article.setTask(articleClient.isTask());
          article.setStatus(articleClient.getStatus());
          return articleRepo.save(article);
        })
        .flatMap(this::fetchArticle);
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

  public Mono<Article> fetchArticle(Article articleClient) {
    return authArticleRepo.findById(articleClient.getId())
        .zipWhen(a -> articleBlockService.findByIds(a.getArticleBlockIds()).collectList())
        .map(this::fillArticle);
  }

  public Mono<Article> authFetchArticle(Article articleClient, ObjectId userId) {
    return authArticleRepo.findByAuthorIdAndId(userId, articleClient.getId())
        .zipWhen(a -> articleBlockService.findByIds(a.getArticleBlockIds()).collectList())
        .map(this::fillArticle);
  }

  @NotNull
  private Article fillArticle(Tuple2<Article, List<ArticleBlock>> tuple2) {
    var article = tuple2.getT1();
    var articleBlocks = tuple2.getT2();
    Map<ObjectId, ArticleBlock> abMap = articleBlocks
        .stream()
        .collect(Collectors.toMap(ab -> ab.getId(), ab -> ab));
    var abOrdered = article.getArticleBlockIds()
        .stream()
        .map(abMap::get)
        .collect(Collectors.toCollection(LinkedList::new));
    article.setArticleBlocks(abOrdered);
    var selABId = article.getSelectedArticleBlockId();
    articleBlocks
        .stream()
        .filter(ab -> selABId.equals(ab.getId()))
        .findFirst()
        .ifPresent(article::setSelectedArticleBlock);
    return article;
  }

  public Mono<Void> authDeleteArticleBlock(ObjectId articleId, ObjectId articleBlockId, ObjectId userId) {
    return authArticleRepo
        .findByAuthorIdAndId(userId, articleId)
        .flatMap(article -> {
          article.getArticleBlockIds().remove(articleBlockId);
          if (article.getSelectedArticleBlockId() != null && article.getSelectedArticleBlockId().equals(articleBlockId)) {
            article.setSelectedArticleBlockId(null);
          }
          return authArticleRepo.save(article);
        })
        .then();
  }
}
