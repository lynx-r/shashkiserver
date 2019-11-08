/*
 * © Copyright
 *
 * ApplicationConfig.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 08:33
 */
@Data
@Configuration
public class ApplicationConfig {

  @Value("${articlesLimit}")
  private Integer articlesLimit;

  @Value("${guestName}")
  private String guestName;

  @Value("${tokenExpirationMinutes:60}")
  private Integer tokenExpirationMinutes;

  @Value("${tokenIssuer:workingbit-example.com}")
  private String tokenIssuer;

  @Value("${tokenSecret:secret}") // length minimum 256 bites
  private String tokenSecret;

}
