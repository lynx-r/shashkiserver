/*
 * © Copyright
 *
 * CorsConfigurationSourceAdapter.java is part of shashkiserver.
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

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

class CorsConfigurationSourceAdapter {
  private final String clientUrl;
  private final String[] headers;
  private final String[] methods;
  private final String[] exposedHeaders;

  CorsConfigurationSourceAdapter(String clientUrl, String headers, String methods, String exposedHeaders) {
    this.clientUrl = clientUrl;
    this.headers = headers.split(",");
    this.methods = methods.split(",");
    this.exposedHeaders = exposedHeaders.split(",");
  }

  CorsConfigurationSource corsFilter(boolean allowCredentials) {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin(clientUrl);
    for (String header : headers) {
      config.addAllowedHeader(header);
    }
    for (String method : methods) {
      config.addAllowedMethod(method);
    }
    for (String header : exposedHeaders) {
      config.addExposedHeader(header);
    }
    config.setAllowCredentials(allowCredentials);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
