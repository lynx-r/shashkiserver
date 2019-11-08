/*
 * © Copyright
 *
 * SpringSecurityWebFluxConfig.java is part of shashkiserver.
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

import com.workingbit.shashkiapp.service.AuthenticationFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
@EnableMongoAuditing
public class SpringSecurityWebFluxConfig {

  @Value("${whiteListedAuthUrls}")
  private String[] whiteListedAuthUrls;

  @Value("${originUrl}")
  private String originUrl;
  @Value("${headers}")
  private String headers;
  @Value("${methods}")
  private String methods;
  @Value("${exposedHeaders}")
  private String exposedHeaders;

  @Bean
  public SecurityWebFilterChain securityFilterChain(
      ServerHttpSecurity http,
      AuthenticationFilterFactory authenticationFilterFactory
  ) {

    AuthenticationWebFilter tokenWebFilter = authenticationFilterFactory.createTokenAuthenticationWebFilter();
    AuthenticationWebFilter webApiJwtServiceWebFilter = authenticationFilterFactory.createJwtAuthenticationWebFilter();

    http.csrf().disable();

    http
        .authorizeExchange()
        .pathMatchers(whiteListedAuthUrls)
        .permitAll()
        .pathMatchers("/actuator/**").hasRole("SYSTEM")
        .pathMatchers("/api/user/{user}/**").access(this::currentUserMatchesPath)
        .and()
        .addFilterAt(webApiJwtServiceWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .addFilterAt(tokenWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .authorizeExchange()
        .anyExchange()
        .authenticated();

    http
        .cors()
        .configurationSource(corsSource());

    return http.build();
  }

  private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication, AuthorizationContext context) {
    return authentication
        .map(a -> context.getVariables().get("user").equals(a.getCredentials()))
        .map(AuthorizationDecision::new);
  }

  private CorsConfigurationSource corsSource() {
    return new CorsConfigurationSourceAdapter(
        originUrl,
        headers,
        methods,
        exposedHeaders
    ).corsFilter(false);
  }
}
