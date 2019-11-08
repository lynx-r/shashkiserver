/*
 * © Copyright
 *
 * SpringWebFluxConfig.java is part of shashkiserver.
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.workingbit.shashkiapp.repo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Configuration
public class SpringWebFluxConfig {

  private final ApplicationClientsProperties applicationClients;

  public SpringWebFluxConfig(ApplicationClientsProperties applicationClients) {
    this.applicationClients = applicationClients;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * It will be used by default for Basic Auth
   *
   * @return
   */
  @Bean
  @Primary
  public MapReactiveUserDetailsService userDetailsRepositoryInMemory() {
    List<UserDetails> users = applicationClients.getClients()
        .stream()
        .map(applicationClient ->
            User.builder()
                .username(applicationClient.getUsername())
                .password(passwordEncoder().encode(applicationClient.getPassword()))
                .roles(applicationClient.getRoles()).build())
        .collect(toList());
    return new MapReactiveUserDetailsService(users);
  }

  @Bean
  public ReactiveUserDetailsService userDetailsRepository(UserRepository users) {
    return (email) -> users.findByEmail(email).cast(UserDetails.class);
  }

  @Bean
  @Primary
  ObjectMapper objectMapper() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.featuresToEnable(
        DeserializationFeature.READ_ENUMS_USING_TO_STRING,
        SerializationFeature.WRITE_ENUMS_USING_TO_STRING
    );

    builder.serializerByType(ObjectId.class, new ToStringSerializer());
    builder.deserializerByType(ObjectId.class, new JsonDeserializer() {
      @Override
      public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
          Map oid = p.readValueAs(Map.class);
          return new ObjectId(
              (Integer) oid.get("timestamp"),
              (Integer) oid.get("machineIdentifier"),
              ((Integer) oid.get("processIdentifier")).shortValue(),
              (Integer) oid.get("counter"));
        } catch (IOException e) {
          return new ObjectId(p.getText());
        }
      }
    });
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return builder.build();
  }

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener() {
    return new ValidatingMongoEventListener(validator());
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    return new LocalValidatorFactoryBean();
  }

}
