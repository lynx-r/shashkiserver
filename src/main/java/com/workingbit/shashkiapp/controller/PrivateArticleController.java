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
import com.workingbit.shashkiapp.domain.ArticlesResponse;
import com.workingbit.shashkiapp.domain.BoardCell;
import com.workingbit.shashkiapp.service.ArticleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/{userId}/article")
public class PrivateArticleController {

  private final ArticleService articleService;

  public PrivateArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<Article>> createArticle(
      @RequestBody Article article,
      Authentication authentication
  ) {
    return articleService
        .privateCreateArticle(article, authentication)
        .map(ResponseEntity::ok);
  }

  @PutMapping
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<Article>> saveArticle(@PathVariable ObjectId userId, @RequestBody Article article) {
    return articleService
        .privateSaveArticle(userId, article)
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
    return articleService.privateFindAllByAuthor(userId, page, pageSize, sort, sortDirection, contains);
  }

  @GetMapping("{hru}")
  public Mono<ResponseEntity<Article>> getArticleByHruAndAuthorId(@PathVariable ObjectId userId, @PathVariable String hru) {
    return articleService
        .privateFindArticleByHruAndAuthorId(userId, hru)
        .map(ResponseEntity::ok);
  }

  @PutMapping("{articleId}/board")
  public Mono<ResponseEntity<JsonNode>> boardCellTouched(
      @PathVariable ObjectId userId,
      @PathVariable ObjectId articleId,
      @RequestBody BoardCell boardCell
  ) {
    return articleService.privateBoardCellTouch(boardCell, userId, articleId)
        .map(ResponseEntity::ok);
  }

}
