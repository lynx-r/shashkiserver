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
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by Aleksey Popryadukhin on 27/08/2018.
 */
public interface AuthArticleRepo extends ReactiveMongoRepository<Article, ObjectId> {

//  Mono<Article> findByAuthorIdAndHumanReadableUrl(ObjectId authorId, String hru);

//  Mono<Article> findByAuthorIdAndId(ObjectId articleId, ObjectId authorId);

//  Mono<Long> countByAuthorId(ObjectId authorId);

//  Flux<Article> findAllByAuthorId(ObjectId authorId, Pageable pageable);

//  default Flux<Article> findAllByAuthorIdAndContains(ObjectId authorId, String content, Pageable pageable) {
//    String contentRegex = "(?i).*" + content + ".*";
//    return findAllByAuthorIdAndIntroMatchesRegexOrAuthorIdAndTitleMatchesRegex(authorId, contentRegex, authorId, contentRegex, pageable);
//  }

//  Flux<Article> findAllByAuthorIdAndIntroMatchesRegexOrAuthorIdAndTitleMatchesRegex(ObjectId authorId, String content, ObjectId authorId2, String intro, Pageable pageable);

//  default Mono<Long> countAllByAuthorIdAndContains(ObjectId authorId, String content) {
//    String contentRegex = "(?i).*" + content + ".*";
//    return countAllByAuthorIdAndIntroMatchesRegexOrAuthorIdAndTitleMatchesRegex(authorId, contentRegex, authorId, contentRegex);
//  }

//  Mono<Long> countAllByAuthorIdAndIntroMatchesRegexOrAuthorIdAndTitleMatchesRegex(ObjectId userId, String contains, ObjectId userId1, String contains1);

}
