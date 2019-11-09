/*
 * © Copyright
 *
 * ArticleBlockRepo.java is part of shashkiserver.
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

import com.workingbit.shashkiapp.domain.ArticleBlock;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by Aleksey Popryadukhin on 27/08/2018.
 */
public interface ArticleBlockRepo extends ReactiveMongoRepository<ArticleBlock, ObjectId> {

//  Mono<Boolean> existsByHumanReadableUrl(String hru);

//  Mono<Article> findByHumanReadableUrl(String hru);

//  default Flux<ArticleBlock> findAllByStatusPublished(Pageable pageable) {
//    return findAllByStatusIn(List.of(EnumArticleStatus.PUBLISHED), pageable);
//  }

//  default Flux<Article> findAllByStatusPublishedAndContains(String content, Pageable pageable) {
//    String contentRegex = "(?i).*" + content + ".*";
//    return findAllByStatusInAndIntroMatchesRegexOrStatusInAndTitleMatchesRegex(List.of(EnumArticleStatus.PUBLISHED), contentRegex, List.of(EnumArticleStatus.PUBLISHED), contentRegex, pageable);
//  }

//  default Mono<Long> countByPublished() {
//    return countByStatusIn(List.of(EnumArticleStatus.PUBLISHED));
//  }

//  Mono<Long> countByStatusIn(List<EnumArticleStatus> statuses);

//  Flux<ArticleBlock> findAllByStatusIn(List<EnumArticleStatus> status, Pageable pageable);

//  Flux<Article> findAllByStatusInAndIntroMatchesRegexOrStatusInAndTitleMatchesRegex(List<EnumArticleStatus> status, String content, List<EnumArticleStatus> status2, String intro, Pageable pageable);

}
