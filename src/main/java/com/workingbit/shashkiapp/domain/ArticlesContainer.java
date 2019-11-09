/*
 * © Copyright
 *
 * ArticlesContainer.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedList;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class ArticlesContainer extends BaseDomain {

  @NotNull
  private ObjectId authorId;

  @Size(min = 4, max = 200)
  private String title;

  @Size(min = 20, max = 250)
  private String intro;

  @Size(min = 4, max = 2000)
  private String humanReadableUrl;

  @NotNull
  private EnumArticleStatus status;

  @Transient
  private LinkedList<Article> articles;

  private LinkedList<ObjectId> articlesIds;

  private boolean task;

  public ArticlesContainer() {
    articles = new LinkedList<>();
    articlesIds = new LinkedList<>();
  }
}
