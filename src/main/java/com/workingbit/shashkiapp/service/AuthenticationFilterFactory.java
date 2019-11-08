/*
 * © Copyright
 *
 * AuthenticationFilterFactory.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationFilterFactory {

  private final JwtService jwtService;
  @Value("${jwtTokenApiMatchUrls}")
  private String[] jwtTokenApiMatchUrls;
  @Value("${tokenMatchUrls}")
  private String[] tokenMatchUrl;
  private ReactiveUserDetailsService userDetailsService;


  public AuthenticationFilterFactory(
      JwtService jwtService,
      @Qualifier("userDetailsRepository") ReactiveUserDetailsService userDetailsService
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  public AuthenticationWebFilter createTokenAuthenticationWebFilter() {
    ReactiveAuthenticationManager userDetailsAuthenticationManager
        = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

    AuthenticationWebFilter tokenFilter = new AuthenticationWebFilter(userDetailsAuthenticationManager);
    tokenFilter.setServerAuthenticationConverter(exchange ->
        Mono.justOrEmpty(exchange)
            .flatMap(ServerWebExchange::getFormData)
            .filter(formData -> !formData.isEmpty())
            .map((formData) -> {
              String email = formData.getFirst("email");
              String password = formData.getFirst("password");
              return new UsernamePasswordAuthenticationToken(email, password);
            })
    );
    tokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(jwtService));
//    tokenFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> Mono.empty());
    tokenFilter.setRequiresAuthenticationMatcher(getAuthMatcher(tokenMatchUrl));
    return tokenFilter;
  }

  public AuthenticationWebFilter createJwtAuthenticationWebFilter() {
    ReactiveAuthenticationManager jwtAuthenticationManager = Mono::just;
    AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
    jwtFilter.setServerAuthenticationConverter(exchange -> Mono.justOrEmpty(exchange)
        .map(jwtService::getAuthorizationPayload)
        .filter(jwtService.matchBearerLength())
        .map(jwtService.getBearerValue())
        .filter(token -> !token.isEmpty())
        .map(jwtService::verifySignedJWT)
        .flatMap(jwtService::getUsernamePasswordAuthenticationToken));
    jwtFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(jwtService));
    jwtFilter.setRequiresAuthenticationMatcher(getAuthMatcher(jwtTokenApiMatchUrls));
    return jwtFilter;
  }

  private ServerWebExchangeMatcher getAuthMatcher(String[] matchersStrings) {
    List<ServerWebExchangeMatcher> matchers = Arrays.stream(matchersStrings)
        .map(PathPatternParserServerWebExchangeMatcher::new)
        .collect(Collectors.toList());
    return ServerWebExchangeMatchers.matchers(new OrServerWebExchangeMatcher(matchers));
  }

  public static class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtService jwtService;

    JwtAuthenticationSuccessHandler(JwtService jwtService) {
      this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
      ServerWebExchange exchange = webFilterExchange.getExchange();
      exchange.getResponse()
          .getHeaders()
          .add(HttpHeaders.AUTHORIZATION, jwtService.getHttpAuthHeaderValue(authentication));
      return webFilterExchange.getChain().filter(exchange);
    }
  }

}
