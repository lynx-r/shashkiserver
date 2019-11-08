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

import com.fasterxml.jackson.databind.JsonNode;
import com.workingbit.shashkiapp.config.ErrorMessages;
import com.workingbit.shashkiapp.domain.Article;
import com.workingbit.shashkiapp.domain.ArticlesResponse;
import com.workingbit.shashkiapp.domain.BoardCell;
import com.workingbit.shashkiapp.domain.EnumArticleStatus;
import com.workingbit.shashkiapp.repo.ArticleRepo;
import com.workingbit.shashkiapp.repo.PrivateArticleRepo;
import com.workingbit.shashkiapp.repo.UserRepository;
import com.workingbit.shashkiapp.util.JsonUtils;
import com.workingbit.shashkiapp.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


/**
 * Created by Aleksey Popryaduhin on 09:05 28/09/2017.
 */
@Service
public class ArticleService {

  private final Logger logger = LoggerFactory.getLogger(ArticleService.class);

  private final ArticleRepo articleRepo;
  private final PrivateArticleRepo privateArticleRepo;
  //  private final BoardBoxService boardBoxService;
  private final UserRepository userRepository;

  public ArticleService(
      ArticleRepo articleRepo,
      PrivateArticleRepo privateArticleRepo,
//      BoardBoxService boardBoxService,
      UserRepository userRepository
  ) {
    this.articleRepo = articleRepo;
    this.privateArticleRepo = privateArticleRepo;
//    this.boardBoxService = boardBoxService;
    this.userRepository = userRepository;
  }

  // Public

  public Mono<ArticlesResponse> findAllPublicArticles(Integer page, Integer pageSize,
                                                      String sort, String sortDirection,
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

  // Private

  public Mono<Article> privateFindArticleByHruAndAuthorId(ObjectId userId, String hru) {
    return privateArticleRepo.findByAuthorIdAndHumanReadableUrl(userId, hru);
  }

  public Mono<Article> privateSaveArticle(ObjectId userId, Article articleClient) {
    return privateArticleRepo.findByAuthorIdAndId(userId, articleClient.getId())
        .flatMap(article -> {
          if (article.getStatus().equals(EnumArticleStatus.REMOVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.ARTICLE_IS_DELETED);
          }
          if (StringUtils.isNotBlank(articleClient.getTitle())) {
            String title = articleClient.getTitle().trim();
            article.setTitle(title);
          }
          if (StringUtils.isNotBlank(articleClient.getContent())) {
            String content = articleClient.getContent().trim();
            article.setContent(content);
          }
          if (StringUtils.isNotBlank(articleClient.getIntro())) {
            String intro = articleClient.getIntro().trim();
            article.setIntro(intro);
          }
          article.setTask(articleClient.isTask());
          article.setStatus(articleClient.getStatus());
          article.setNotation(articleClient.getNotation());
          return articleRepo.save(article);
        });
  }

  public Mono<ArticlesResponse> privateFindAllByAuthor(ObjectId userId, Integer page, Integer pageSize,
                                                       String sort, String sortDirection,
                                                       String contains) {
    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
    if (StringUtils.isBlank(contains)) {
      return Mono.zip(privateArticleRepo.countByAuthorId(userId),
          privateArticleRepo.findAllByAuthorId(userId,
              pageable)
              .collectList())
          .single()
          .map(ArticlesResponse::fromTuple2);
    } else {
      return Mono.zip(privateArticleRepo.countAllByAuthorIdAndContains(userId, contains),
          privateArticleRepo.findAllByAuthorIdAndContains(userId, contains, pageable)
              .collectList())
          .single()
          .map(ArticlesResponse::fromTuple2);
    }
  }

  public Mono<JsonNode> privateBoardCellTouch(BoardCell boardCell, ObjectId userId, ObjectId articleId) {
    return privateArticleRepo.findByAuthorIdAndId(userId, articleId)
        .flatMap(article -> {
          var gameNotationNode = JsonUtils.dataToJsonNode(article.getNotation());
          var gnService = GameNotationService.fromGameNotation(article.getNotation());
          var gameNotationTouched = gnService.cellTouch(boardCell, false);
          article.setNotation(gameNotationTouched);
          return privateArticleRepo.save(article)
              .thenReturn(JsonUtils.asJsonDiff(gameNotationNode, gameNotationTouched));
        });
  }

  public Mono<Article> privateCreateArticle(Article article, Authentication authentication) {
    ObjectId articleId = ObjectId.get();
    article.setId(articleId);
    article.setStatus(EnumArticleStatus.DRAFT);
    article.setTitle(article.getTitle());

//    CreateBoardBoxRequest createBoardBoxRequest = createArticleRequest.getBoardBoxRequest();
//    createBoardBoxRequest.setArticleId(articleId);
//    createBoardBoxRequest.setBoardBoxId(boardBoxId);

    return Mono
        .zip(
            userRepository.findByEmail(authentication.getName()),
            articleRepo.existsByHumanReadableUrl(article.getHumanReadableUrl())
        )
        .flatMap(ueTuple -> {
          ObjectId authorId = ueTuple.getT1().getId();
          article.setAuthorId(authorId);

          Utils.setArticleHru(article, ueTuple.getT2());

          GameNotationService gnService;
          if (article.getNotation().getNotationFen() != null) {
            gnService = GameNotationService.fromGameNotation(article.getNotation());
          } else {
            gnService = GameNotationService.forRulesRussianCheckers();
          }
          article.setNotation(gnService.getGameNotation());
          return articleRepo.save(article);
        });
  }

}
