/*
 * © Copyright
 *
 * ApplicationClientsProperties.java is part of shashkiserver.
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: aleksey
 * Date: 2018-12-03
 * Time: 13:17
 */
@Data
@Component
@ConfigurationProperties("appclients")
public class ApplicationClientsProperties {
  private List<ApplicationClient> clients = new ArrayList<>();

  @Data
  public static class ApplicationClient {
    private String username;
    private String password;
    private String[] roles;
  }

}

