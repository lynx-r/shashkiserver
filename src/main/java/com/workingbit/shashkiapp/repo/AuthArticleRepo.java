/*
 * © Copyright
 *
 * AuthArticleRepo.java is part of shashkiserver.
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

import static com.workingbit.shashkiapp.repo.RepoConstants.userAllowedStatuses;

/**
 * Created by Aleksey Popryadukhin on 27/08/2018.
 */
public interface AuthArticleRepo extends ReactiveMongoRepository<Article, ObjectId> {

  default Mono<Boolean> existsByHumanReadableUrl(String hru) {
    return existsByHumanReadableUrlAndStatusIn(hru, userAllowedStatuses);
  }

  Mono<Boolean> existsByHumanReadableUrlAndStatusIn(String hru, List<EnumArticleStatus> userStatuses);

  default Mono<Article> findByAuthorIdAndHumanReadableUrl(ObjectId authorId, String hru) {
    return findByAuthorIdAndHumanReadableUrlAndStatusIn(authorId, hru, userAllowedStatuses);
  }

  Mono<Article> findByAuthorIdAndHumanReadableUrlAndStatusIn(ObjectId authorId, String hru, List<EnumArticleStatus> userStatuses);

  default Mono<Article> findByAuthorIdAndId(ObjectId articleId, ObjectId authorId) {
    return findByAuthorIdAndIdAndStatusIn(articleId, authorId, userAllowedStatuses);
  }

  Mono<Article> findByAuthorIdAndIdAndStatusIn(ObjectId articleId, ObjectId authorId, List<EnumArticleStatus> userStatuses);

  default Mono<Long> countByAuthorId(ObjectId authorId) {
    return countByAuthorIdAndStatusIn(authorId, userAllowedStatuses);
  }

  Mono<Long> countByAuthorIdAndStatusIn(ObjectId authorId, List<EnumArticleStatus> userStatuses);

  default Flux<Article> findAllByAuthorId(ObjectId authorId, Pageable pageable) {
    return findAllByAuthorIdAndStatusIn(authorId, userAllowedStatuses, pageable);
  }

  Flux<Article> findAllByAuthorIdAndStatusIn(ObjectId authorId, List<EnumArticleStatus> userStatuses, Pageable pageable);

  default Flux<Article> findAllByAuthorIdAndContains(ObjectId authorId, String content, Pageable pageable) {
    String contentRegex = "(?i).*" + content + ".*";
    return findAllByAuthorIdAndIntroMatchesRegexAndStatusInOrAuthorIdAndTitleMatchesRegexAndStatusIn(
        authorId, contentRegex, userAllowedStatuses,
        authorId, contentRegex, userAllowedStatuses,
        pageable);
  }

  Flux<Article> findAllByAuthorIdAndIntroMatchesRegexAndStatusInOrAuthorIdAndTitleMatchesRegexAndStatusIn(
      ObjectId authorId, String content, List<EnumArticleStatus> userStatuses,
      ObjectId authorId2, String intro, List<EnumArticleStatus> userStatuses2,
      Pageable pageable);

  default Mono<Long> countAllByAuthorIdAndContains(ObjectId authorId, String content) {
    String contentRegex = "(?i).*" + content + ".*";
    return countAllByAuthorIdAndIntroMatchesRegexAndStatusInOrAuthorIdAndTitleMatchesRegexAndStatusIn(
        authorId, contentRegex, userAllowedStatuses, authorId, contentRegex, userAllowedStatuses);
  }

  Mono<Long> countAllByAuthorIdAndIntroMatchesRegexAndStatusInOrAuthorIdAndTitleMatchesRegexAndStatusIn(
      ObjectId userId, String contains, List<EnumArticleStatus> userStatuses,
      ObjectId userId1, String contains1, List<EnumArticleStatus> userStatuses2);

}
