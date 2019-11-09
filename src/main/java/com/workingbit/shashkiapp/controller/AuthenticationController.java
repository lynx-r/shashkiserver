/*
 * © Copyright
 *
 * AuthenticationController.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.controller;

import com.workingbit.shashkiapp.domain.JwtAuthResponse;
import com.workingbit.shashkiapp.domain.User;
import com.workingbit.shashkiapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.workingbit.shashkiapp.config.ErrorMessages.ERROR_HEADER_NAME;
import static com.workingbit.shashkiapp.config.ErrorMessages.USERNAME_BUSY;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

  private final UserService userService;

  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("register")
  @PreAuthorize("hasRole('GUEST')")
  public Mono<ResponseEntity<JwtAuthResponse>> register(@RequestBody User user, Authentication authentication) {
    return userService.register(user, authentication)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(badRequest().header(ERROR_HEADER_NAME, USERNAME_BUSY).build());
  }

  @PostMapping("token/{fingerprint}")
  @PreAuthorize("isAuthenticated()")
  public Mono<ResponseEntity<JwtAuthResponse>> token(@PathVariable String fingerprint, Authentication authentication)
      throws AuthenticationException {
    return userService.token(fingerprint, authentication)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

}
