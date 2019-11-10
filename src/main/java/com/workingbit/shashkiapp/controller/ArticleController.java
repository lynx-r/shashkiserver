/*
 * © Copyright
 *
 * ArticleController.java is part of shashkiserver.
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

import com.workingbit.shashkiapp.domain.Article;
import com.workingbit.shashkiapp.domain.ArticlesResponse;
import com.workingbit.shashkiapp.service.ArticleBlockService;
import com.workingbit.shashkiapp.service.ArticleService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/article")
public class ArticleController {

  private final ArticleService articleService;
  private final ArticleBlockService articleBlockService;

  public ArticleController(ArticleService articleService,
                           ArticleBlockService articleBlockService) {
    this.articleService = articleService;
    this.articleBlockService = articleBlockService;
  }

  @GetMapping("list")
  @PreAuthorize("hasAnyRole('GUEST', 'USER')")
  public Mono<ArticlesResponse> findAllPublicArticles(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
      @RequestParam(value = "sort", required = false, defaultValue = "updatedAt") String sort,
      @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
      @RequestParam(value = "contains", required = false) String contains
  ) {
    return articleService.findAllPublicArticles(page, pageSize, sort, sortDirection, contains);
  }

  @GetMapping("{hru}")
  public Mono<ResponseEntity<Article>> getArticleByHru(@PathVariable String hru) {
    return articleService
        .findArticleByHru(hru)
        .map(ResponseEntity::ok);
  }

  @GetMapping("{articleId}/fetch")
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<Article>> authFetchArticle(@PathVariable ObjectId articleId) {
    return articleService.fetchArticle(articleId)
        .map(ResponseEntity::ok);
  }

}
