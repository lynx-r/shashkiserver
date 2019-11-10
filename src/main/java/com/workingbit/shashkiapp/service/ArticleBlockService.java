/*
 * © Copyright
 *
 * ArticleBlockService.java is part of shashkiserver.
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
import com.workingbit.shashkiapp.domain.ArticleBlock;
import com.workingbit.shashkiapp.domain.BoardCell;
import com.workingbit.shashkiapp.domain.EnumArticleBlockState;
import com.workingbit.shashkiapp.repo.ArticleBlockRepo;
import com.workingbit.shashkiapp.repo.AuthArticleBlockRepo;
import com.workingbit.shashkiapp.repo.AuthArticleRepo;
import com.workingbit.shashkiapp.repo.UserRepository;
import com.workingbit.shashkiapp.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Created by Aleksey Popryaduhin on 09:05 28/09/2017.
 */
@Service
public class ArticleBlockService {

  private final Logger logger = LoggerFactory.getLogger(ArticleBlockService.class);

  private final AuthArticleRepo authArticleRepo;
  private final ArticleBlockRepo articleBlockRepo;
  private final AuthArticleBlockRepo authArticleBlockRepo;
  //  private final BoardBoxService boardBoxService;
  private final UserRepository userRepository;

  public ArticleBlockService(
      AuthArticleRepo authArticleRepo,
      ArticleBlockRepo articleBlockRepo,
      AuthArticleBlockRepo authArticleBlockRepo,
      UserRepository userRepository
  ) {
    this.authArticleRepo = authArticleRepo;
    this.articleBlockRepo = articleBlockRepo;
    this.authArticleBlockRepo = authArticleBlockRepo;
    this.userRepository = userRepository;
  }

  // Public

  public Flux<ArticleBlock> findAllArticleBlockByIdsAndArticleId(List<ObjectId> articleBlockIds, ObjectId articleId) {
    return authArticleRepo.existsById(articleId)
        .filter(exists -> exists)
        .thenMany(articleBlockRepo.findAllById(articleBlockIds));
  }

//  public Mono<ArticlesResponse> findAllPublicArticles(Integer page, Integer pageSize,
//                                                      String sort, String sortDirection,
//                                                      String contains) {
//    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
//    return Mono
//        .zip(articleRepo.countByPublished(),
//            StringUtils.isBlank(contains)
//                ? articleRepo.findAllByStatusPublished(pageable)
//                .collectList()
//                : articleRepo.findAllByStatusPublishedAndContains(contains, pageable)
//                .collectList())
//        .map(ArticlesResponse::fromTuple2);
//  }

//  public Mono<Article> findArticleByHru(String articleHru) {
//    return articleRepo.findByHumanReadableUrl(articleHru);
//  }

  // Private

//  public Mono<Article> authFindArticleByHruAndAuthorId(ObjectId userId, String hru) {
//    return authArticleRepo.findByAuthorIdAndHumanReadableUrl(userId, hru);
//  }

  public Mono<ArticleBlock> authSaveArticle(ArticleBlock articleBlockClient) {
    return authArticleBlockRepo.findById(articleBlockClient.getId())
        .flatMap(articleBlock -> {
          if (StringUtils.isNotBlank(articleBlockClient.getTitle())) {
            String title = articleBlockClient.getTitle().trim();
            articleBlock.setTitle(title);
          }
          if (StringUtils.isNotBlank(articleBlockClient.getContent())) {
            String content = articleBlockClient.getContent().trim();
            articleBlock.setContent(content);
          }
          articleBlock.setTask(articleBlockClient.isTask());
          articleBlock.setNotation(articleBlockClient.getNotation());
          articleBlock.setState(articleBlockClient.getState());
          return articleBlockRepo.save(articleBlock);
        });
  }

//  public Mono<ArticlesResponse> authFindAllByAuthor(ObjectId userId, Integer page, Integer pageSize,
//                                                    String sort, String sortDirection,
//                                                    String contains) {
//    PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(sortDirection), sort);
//    if (StringUtils.isBlank(contains)) {
//      return Mono.zip(authArticleRepo.countByAuthorId(userId),
//          authArticleRepo.findAllByAuthorId(userId,
//              pageable)
//              .collectList())
//          .single()
//          .map(ArticlesResponse::fromTuple2);
//    } else {
//      return Mono.zip(authArticleRepo.countAllByAuthorIdAndContains(userId, contains),
//          authArticleRepo.findAllByAuthorIdAndContains(userId, contains, pageable)
//              .collectList())
//          .single()
//          .map(ArticlesResponse::fromTuple2);
//    }
//  }

  public Mono<JsonNode> authBoardCellTouch(BoardCell boardCell, ObjectId articleId) {
    return authArticleBlockRepo.findById(articleId)
        .flatMap(article -> {
          var gameNotationNode = JsonUtils.dataToJsonNode(article.getNotation());
          var gnService = GameNotationService.fromGameNotation(article.getNotation());
          var gameNotationTouched = gnService.cellTouch(boardCell, false);
          article.setNotation(gameNotationTouched);
          return authArticleBlockRepo.save(article)
              .thenReturn(JsonUtils.asJsonDiff(gameNotationNode, gameNotationTouched));
        });
  }

  Mono<ArticleBlock> authCreateArticleBlock(ArticleBlock articleBlock, ObjectId articleId) {
    articleBlock.setState(EnumArticleBlockState.OPENED);
    articleBlock.setTitle(articleBlock.getTitle());
    articleBlock.setArticleId(articleId);

    GameNotationService gnService;
    if (articleBlock.getNotation().getNotationFen() != null) {
      gnService = GameNotationService.fromGameNotation(articleBlock.getNotation());
    } else {
      gnService = GameNotationService.forRulesRussianCheckers();
    }
    articleBlock.setNotation(gnService.getGameNotation());
    return articleBlockRepo.save(articleBlock);
  }

  public Flux<ArticleBlock> findByIds(List<ObjectId> articleBlockIds) {
    return articleBlockRepo.findAllById(articleBlockIds);
  }
}
