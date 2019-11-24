/*
 * © Copyright
 *
 * ArticleRepo.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.repo;

import com.workingbit.shashkiapp.domain.Article;
import com.workingbit.shashkiapp.domain.EnumArticleStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.workingbit.shashkiapp.repo.RepoConstants.excludeStatuses;
import static com.workingbit.shashkiapp.repo.RepoConstants.includeStatuses;

/**
 * Created by Aleksey Popryadukhin on 27/08/2018.
 */
public interface ArticleRepo extends ReactiveMongoRepository<Article, ObjectId> {

  default Mono<Article> findByHumanReadableUrl(String hru) {
    return findByHumanReadableUrlAndStatusNotIn(hru, excludeStatuses);
  }

  Mono<Article> findByHumanReadableUrlAndStatusNotIn(String hru, List<EnumArticleStatus> exclude);

  default Flux<Article> findAllByStatusPublished(Pageable pageable) {
    return findAllByStatusInAndStatusNotIn(includeStatuses, excludeStatuses, pageable);
  }

  default Flux<Article> findAllByStatusPublishedAndContains(String content, Pageable pageable) {
    String contentRegex = "(?i).*" + content + ".*";
    return findAllByStatusInAndStatusNotInAndIntroMatchesRegexOrStatusInAndStatusNotInAndTitleMatchesRegex(
        includeStatuses, excludeStatuses, contentRegex, includeStatuses, excludeStatuses,
        contentRegex, pageable);
  }

  default Mono<Long> countByPublished() {
    return countByStatusInAndStatusNotIn(includeStatuses, excludeStatuses);
  }

  Mono<Long> countByStatusInAndStatusNotIn(List<EnumArticleStatus> statuses, List<EnumArticleStatus> exclude);

  Flux<Article> findAllByStatusInAndStatusNotIn(List<EnumArticleStatus> status, List<EnumArticleStatus> exclude, Pageable pageable);

  Flux<Article> findAllByStatusInAndStatusNotInAndIntroMatchesRegexOrStatusInAndStatusNotInAndTitleMatchesRegex(
      List<EnumArticleStatus> status, List<EnumArticleStatus> exclude, String content,
      List<EnumArticleStatus> status2, List<EnumArticleStatus> exclude2, String intro,
      Pageable pageable);

}
