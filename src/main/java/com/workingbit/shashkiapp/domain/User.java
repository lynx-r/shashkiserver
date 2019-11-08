/*
 * © Copyright
 *
 * User.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User extends BaseDomain implements UserDetails {

  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private String middleName;
  private EnumRank rank;
  private String theme;

  @JsonIgnore
  private String fingerprint;
  @Builder.Default()
  private List<UserFingerprint> userFingerprints = new ArrayList<>();
  @JsonIgnore
  @Builder.Default()
  private boolean active = true;

  /**
   * True - means user is not temporary, False - means system user
   */
  @JsonIgnore
  @Builder.Default()
  private boolean guest = true;

  /**
   * true - user in system, false - user logged out
   */
  private boolean loggedIn;

  @JsonIgnore
  @Builder.Default()
  private List<LocalDateTime> loginTime = new ArrayList<>();
  @JsonIgnore
  @Builder.Default()
  private List<LocalDateTime> logoutTime = new ArrayList<>();

  @Builder.Default()
  private List<String> userAuthorities = new ArrayList<>();

  @Builder.Default()
  private Integer score = 0;

  @JsonCreator
  public User(@JsonProperty("email") String email, @JsonProperty("password") String password,
              @JsonProperty("authorities") List<String> userAuthorities) {
    this.email = email;
    this.password = password;
    this.userAuthorities = Objects.requireNonNullElseGet(userAuthorities, ArrayList::new);
    loginTime = new ArrayList<>();
    logoutTime = new ArrayList<>();
    userFingerprints = new ArrayList<>();
  }

  public String getFullName() {
    return firstName + (StringUtils.isEmpty(middleName) ? "" : " " + middleName) + " " + lastName;
  }

  public void setFullName(String ignore) {
  }

  public void loggedInTimestamp() {
    loginTime.add(LocalDateTime.now());
  }

  public void loggedOutTimestamp() {
    logoutTime.add(LocalDateTime.now());
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return email;
  }

  public void setUsername(String email) {
    this.email = email;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(userAuthorities.toArray(new String[0]));
  }

  public void addAuthority(String authority) {
    userAuthorities.add(authority);
  }

  public void retainAuthorities(List<String> authorities) {
    this.userAuthorities.retainAll(authorities);
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return active;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return active;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return active;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return active;
  }

  public User addFingerprint(String fingerprint, boolean loggedIn) {
    var contains = userFingerprints.stream().filter(uf -> uf.getFingerprint().equals(fingerprint)).findFirst();
    if (contains.isPresent()) {
      contains.get().setLoggedIn(loggedIn);
      return this;
    }
    userFingerprints.add(new UserFingerprint(fingerprint, loggedIn));
    return this;
  }

  public User addFingerprints(List<UserFingerprint> userFingerprints) {
    this.userFingerprints.addAll(userFingerprints);
    return this;
  }

  public Optional<UserFingerprint> findFingerprint(String fingerprint) {
    return userFingerprints.stream().filter(uf -> uf.getFingerprint().equals(fingerprint)).findFirst();
  }
}
