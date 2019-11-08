/*
 * © Copyright
 *
 * UserRepository.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.repo;

import com.workingbit.shashkiapp.domain.User;
import com.workingbit.shashkiapp.domain.UserFingerprint;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {

  Mono<User> findByEmail(String email);

  Mono<User> findByEmailAndFingerprint(String email, String fingerprint);

  Mono<User> findByFingerprint(String fingerprint);

  Flux<User> findAllByFingerprint(String fingerprint);

  default Mono<User> findByUserFingerprintAndLogged(String fingerprint) {
    var fp = UserFingerprint.createLoggedIn(fingerprint);
    return findByUserFingerprintsIsContainingAndLoggedInTrue(fp);
  }

  Mono<User> findByUserFingerprintsIsContainingAndLoggedInTrue(UserFingerprint fingerprint);

  Mono<User> findByLoggedInTrue();

  Mono<User> findByFingerprintAndGuestFalse(String fingerprint);

  Mono<Boolean> existsByEmail(String email);

  Mono<Void> deleteByFingerprint(String fingerprint);

}
