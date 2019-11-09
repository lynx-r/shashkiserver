/*
 * © Copyright
 *
 * PrivateArticleController.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.workingbit.shashkiapp.domain.Article;
import com.workingbit.shashkiapp.domain.ArticleBlock;
import com.workingbit.shashkiapp.domain.ArticlesResponse;
import com.workingbit.shashkiapp.domain.BoardCell;
import com.workingbit.shashkiapp.service.ArticleBlockService;
import com.workingbit.shashkiapp.service.ArticleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/{userId}/article")
public class PrivateArticleController {

  private final ArticleBlockService articleBlockService;
  private final ArticleService articleService;

  public PrivateArticleController(ArticleBlockService articleBlockService,
                                  ArticleService articleService) {
    this.articleBlockService = articleBlockService;
    this.articleService = articleService;
  }

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<Article>> authCreateArticlesContainer(
      @PathVariable ObjectId userId,
      @RequestBody Article container
  ) {
    return articleService
        .authCreateArticlesContainer(userId, container)
        .map(ResponseEntity::ok);
  }

  @PutMapping("{containerId}/add")
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<ArticleBlock>> authAddArticleToContainer(
      @PathVariable ObjectId userId,
      @PathVariable ObjectId containerId,
      @RequestBody ArticleBlock articleBlock
  ) {
    return articleService
        .authAddArticleToContainer(containerId, userId, articleBlock)
        .map(ResponseEntity::ok);
  }

  @PutMapping("item")
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<ArticleBlock>> saveArticle(@RequestBody ArticleBlock articleBlock) {
    return articleBlockService
        .authSaveArticle(articleBlock)
        .map(ResponseEntity::ok);
  }

  @GetMapping("list")
  @PreAuthorize("hasRole('USER')")
  public Mono<ArticlesResponse> findAllByAuthorArticles(
      @PathVariable ObjectId userId,
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
      @RequestParam(value = "sort", required = false, defaultValue = "updatedAt") String sort,
      @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
      @RequestParam(value = "contains", required = false) String contains
  ) {
    return articleService.authFindAllByAuthor(userId, page, pageSize, sort, sortDirection, contains);
  }

  @GetMapping("{hru}")
  public Mono<ResponseEntity<Article>> getArticleByHruAndAuthorId(@PathVariable ObjectId userId, @PathVariable String hru) {
    return articleService
        .authFindArticleByHruAndAuthorId(userId, hru)
        .map(ResponseEntity::ok);
  }

  @PutMapping("{articleId}/board")
  public Mono<ResponseEntity<JsonNode>> boardCellTouched(
      @PathVariable ObjectId articleId,
      @RequestBody BoardCell boardCell
  ) {
    return articleBlockService.privateBoardCellTouch(boardCell, articleId)
        .map(ResponseEntity::ok);
  }

}
