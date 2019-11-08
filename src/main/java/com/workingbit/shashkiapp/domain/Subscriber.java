


/*
 * © Copyright
 *
 * Subscriber.java is part of shashkiserver.
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by Aleksey Popryadukhin on 18/06/2018.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("Subscriber")
public class Subscriber extends BaseDomain {

  private LocalDateTime unsubscribeDate;

  @Size(max = 200)
  private String name;

  @Size(max = 200)
  @Email(message = "Не верный адрес электронной почты")
  private String email;

  private boolean subscribed;

  @JsonCreator
  public Subscriber(@JsonProperty("email") String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return name + "<" + email + ">" + (subscribed ? " подписан" : " отписан");
  }
}
