/*
 * © Copyright
 *
 * PublicController.java is part of shashkiserver.
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
import com.workingbit.shashkiapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * User: aleksey
 * Date: 2018-12-02
 * Time: 10:52
 */
@RestController
@RequestMapping("api/public")
public class PublicController {

  private final UserService userService;

  public PublicController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("greet")
  public Mono<Map> greet() {
    return Mono.just(Map.of("greet", "Hi everybody"));
  }

  @PostMapping("guest")
  public Mono<ResponseEntity<JwtAuthResponse>> guest(@RequestBody Map<String, Object> fingerprintMap) {
    String fingerprint = (String) fingerprintMap.get("fingerprint");
    return userService.guest(fingerprint)
        .map(ResponseEntity::ok);
  }

}
