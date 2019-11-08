/*
 * © Copyright
 *
 * JwtService.java is part of shashkiserver.
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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.workingbit.shashkiapp.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

@Service
public class JwtService {

  private static final String AUTHORITIES_CLAIM = "auths";
  private static final String USER_ID_CLAIM = "userId";

  private static final String BEARER = "Bearer ";
  private static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.HS256;
  private static final String SECRET_KEY_ALGORITHM = "HMAC";
  private final Logger logger = LoggerFactory.getLogger(JwtService.class);
  private final ApplicationConfig applicationConfig;

  public JwtService(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  public String getHttpAuthHeaderValue(Authentication authentication) {
    String token = getTokenFromAuthentication(authentication);
    return String.join(" ", "Bearer", token);
  }

  public String getTokenFromAuthentication(Authentication authentication) {
    return generateToken(
        (String) authentication.getCredentials(),
        authentication.getName(),
        authentication.getAuthorities());
  }

  public String generateToken(String credentials, String subjectName, Collection<? extends GrantedAuthority> authorities) {
    Date expirationTime = Date.from(Instant.now().plus(applicationConfig.getTokenExpirationMinutes(), ChronoUnit.MINUTES));
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(subjectName)
        .issuer(applicationConfig.getTokenIssuer())
        .expirationTime(expirationTime)
        .claim(AUTHORITIES_CLAIM,
            authorities
                .parallelStream()
                .map(auth -> (GrantedAuthority) auth)
                .map(GrantedAuthority::getAuthority)
                .collect(joining(",")))
        .claim(USER_ID_CLAIM, credentials)
        .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWS_ALGORITHM), claimsSet);

    try {
      final SecretKey key = new SecretKeySpec(applicationConfig.getTokenSecret().getBytes(), SECRET_KEY_ALGORITHM);
      signedJWT.sign(new MACSigner(key));
    } catch (JOSEException e) {
      logger.error("ERROR while signing JWT", e);
      return null;
    }

    return signedJWT.serialize();
  }

  public String getAuthorizationPayload(ServerWebExchange serverWebExchange) {
    String token = serverWebExchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);
    return token == null ? "" : token;
  }

  public Predicate<String> matchBearerLength() {
    return authValue -> authValue.length() > BEARER.length();
  }

  public Function<String, String> getBearerValue() {
    return authValue -> authValue.substring(BEARER.length());
  }

  public Mono<JWTClaimsSet> verifySignedJWT(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(applicationConfig.getTokenSecret());
      boolean valid = signedJWT.verify(verifier);
      if (valid) {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector((header, context) -> {
          final SecretKey key = new SecretKeySpec(applicationConfig.getTokenSecret().getBytes(), SECRET_KEY_ALGORITHM);
          return List.of(key);
        });
        JWTClaimsSet claimsSet = jwtProcessor.process(signedJWT, null);
        return Mono.just(claimsSet);
      } else {
        logger.error("ERROR TOKEN invalid " + token);
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid token"));
      }
    } catch (ParseException | JOSEException | BadJOSEException e) {
      logger.error("ERROR while verify JWT: " + token);
      return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "unable to verify token"));
    }
  }

  public Mono<Authentication> getUsernamePasswordAuthenticationToken(Mono<JWTClaimsSet> claimsSetMono) {
    return claimsSetMono
        .map((claimsSet -> {
          String subject = claimsSet.getSubject();
          String auths = (String) claimsSet.getClaim(AUTHORITIES_CLAIM);
          String userId = (String) claimsSet.getClaim(USER_ID_CLAIM);
          List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auths.split(","));
          return new UsernamePasswordAuthenticationToken(subject, userId, authorities);
        }));
  }
}
