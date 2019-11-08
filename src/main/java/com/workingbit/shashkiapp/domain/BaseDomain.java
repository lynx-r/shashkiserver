/*
 * © Copyright
 *
 * BaseDomain.java is part of shashkiserver.
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

/**
 * Created by Aleksey Popryaduhin on 18:06 15/08/2017.
 */
@Data
@EqualsAndHashCode(of = {"id"})
abstract class BaseDomain {

  @Id
  private ObjectId id;

  @JsonIgnore
  @Version
  private Long version;

//  @JsonIgnore
//  @CreatedBy
//  private String creatorPrincipal;
//
//  @JsonIgnore
//  @LastModifiedBy
//  private String modifierPrincipal;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

}
