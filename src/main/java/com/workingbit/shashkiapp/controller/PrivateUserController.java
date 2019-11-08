/*
 * © Copyright
 *
 * PrivateUserController.java is part of shashkiserver.
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
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/{userId}/account")
public class PrivateUserController {

  private final UserService userService;

  public PrivateUserController(UserService userService) {
    this.userService = userService;
  }

  @PutMapping
  @PreAuthorize("hasAuthority('USER')")
  public Mono<ResponseEntity<User>> saveUser(@PathVariable ObjectId userId, @RequestBody User user) {
    return userService.saveUser(userId, user)
        .map(ResponseEntity::ok);
  }

  @GetMapping("userdetails")
  @PreAuthorize("isAuthenticated()")
  public Mono<ResponseEntity<User>> token(@PathVariable ObjectId userId)
      throws AuthenticationException {
    return userService.findById(userId)
        .map(ResponseEntity::ok);
  }

  @PostMapping("logout/{fingerprint}")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public Mono<ResponseEntity<JwtAuthResponse>> logout(@PathVariable String fingerprint, Authentication authentication) {
    return userService.logout(fingerprint, authentication)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

}
