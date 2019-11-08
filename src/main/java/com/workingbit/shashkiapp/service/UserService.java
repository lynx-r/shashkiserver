/*
 * © Copyright
 *
 * UserService.java is part of shashkiserver.
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

import com.workingbit.shashkiapp.domain.JwtAuthResponse;
import com.workingbit.shashkiapp.domain.User;
import com.workingbit.shashkiapp.repo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public UserService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     JwtService jwtService
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public Mono<JwtAuthResponse> guest(String fingerprint) {
    return userRepository.findByUserFingerprintAndLogged(fingerprint)
        .map(this::getJwtAuthResponse)
        .switchIfEmpty(userRepository.findByFingerprint(fingerprint)
            .map(this::getJwtAuthResponse)
            .switchIfEmpty(createGuestUser(fingerprint)
                .flatMap(userRepository::save)
                .map(this::getJwtAuthResponse)));
  }

  private Mono<User> createGuestUser(String fingerprint) {
    User guest = User.builder()
        .fingerprint(fingerprint)
        .email(fingerprint)
        .password(passwordEncoder.encode(""))
        .userAuthorities(List.of("ROLE_GUEST"))
        .guest(true)
        .build();
    guest.loggedInTimestamp();
    return Mono.just(guest);
  }

  public Mono<JwtAuthResponse> token(String fingerprint, Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
        .filter(user -> !user.isGuest())
        .doOnNext((e) -> logoutUsersWithFingerprint(fingerprint))
        .flatMap(user -> loginInternal(user, fingerprint))
        .map(this::getJwtAuthResponse);
  }

  private Mono<User> loginInternal(User user, String fingerprint) {
    user.loggedInTimestamp();
    user.setLoggedIn(true);
    user.addFingerprint(fingerprint, true);
    return userRepository.save(user);
  }


  public Mono<JwtAuthResponse> register(User user, Authentication authentication) {
    return userRepository
        .existsByEmail(user.getEmail())
        .filter(exists -> !exists)
        .doOnNext((e) -> logoutUsersWithFingerprint(authentication.getName()))
        .flatMap((e) -> {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          if (user.getUserAuthorities().isEmpty()) {
            user.addAuthority("ROLE_USER");
          } else {
            user.retainAuthorities(List.of("ROLE_USER"));
          }
          user.setGuest(false);
          user.setLoggedIn(true);
          user.loggedInTimestamp();
          String fingerprint = authentication.getName();
          user.addFingerprint(fingerprint, true);
          return userRepository.save(user)
              .map(this::getJwtAuthResponse);
        });
  }

  private Mono<User> logoutUsersWithFingerprint(String fingerprint) {
    return userRepository.findByUserFingerprintAndLogged(fingerprint)
        .map(u -> {
          u.loggedOutTimestamp();
          u.setLoggedIn(false);
          return u;
        })
        .flatMap(userRepository::save);
  }

  public Mono<JwtAuthResponse> logout(String fingerprint, Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
        .flatMap(u -> logoutInternal(fingerprint, u))
        .map(this::getJwtAuthResponse);
  }

  private Mono<User> logoutInternal(String fingerprint, User user) {
    user.loggedOutTimestamp();
    user.setLoggedIn(false);
    user.findFingerprint(fingerprint).ifPresent(uf -> uf.setLoggedIn(false));
    return userRepository.save(user);
  }

  public Mono<User> findById(ObjectId userId) {
    return userRepository.findById(userId);
  }

  private JwtAuthResponse getJwtAuthResponse(User userDetails) {
    String token = jwtService.generateToken(userDetails.getId().toString(), userDetails.getUsername(), userDetails.getAuthorities());
    return new JwtAuthResponse(token, userDetails.getUsername());
  }

  public Mono<User> saveUser(ObjectId userId, User user) {
    return userRepository.findById(userId)
        .map(serverUser -> {
          serverUser.setFirstName(user.getFirstName());
          serverUser.setLastName(user.getLastName());
          serverUser.setTheme(user.getTheme());
          return serverUser;
        })
        .flatMap(userRepository::save);
  }
}
