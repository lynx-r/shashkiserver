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
import com.workingbit.shashkiapp.util.JsonUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Created by Aleksey Popryaduhin on 09:05 28/09/2017.
 */
@Service
public class ArticleBlockService {

  private final ArticleBlockRepo articleBlockRepo;
  private final AuthArticleBlockRepo authArticleBlockRepo;

  public ArticleBlockService(
      ArticleBlockRepo articleBlockRepo,
      AuthArticleBlockRepo authArticleBlockRepo
  ) {
    this.articleBlockRepo = articleBlockRepo;
    this.authArticleBlockRepo = authArticleBlockRepo;
  }

  // For public users

  Flux<ArticleBlock> findByIds(List<ObjectId> articleBlockIds) {
    return articleBlockRepo.findAllById(articleBlockIds);
  }

  // For authenticated users

  public Mono<ArticleBlock> authSaveArticleBlock(ArticleBlock articleBlockClient) {
    return authArticleBlockRepo.findById(articleBlockClient.getId())
        .flatMap(articleBlock -> {
          articleBlock.setTitle(articleBlockClient.getTitle());
          articleBlock.setContent(articleBlockClient.getContent());
          articleBlock.setTask(articleBlockClient.isTask());
          articleBlock.setState(articleBlockClient.getState());
          if (articleBlockClient.getNotation() != null) {
            // client can send notation independently of text
            articleBlock.setNotation(articleBlockClient.getNotation());
          }
          return articleBlockRepo.save(articleBlock);
        });
  }

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

}
