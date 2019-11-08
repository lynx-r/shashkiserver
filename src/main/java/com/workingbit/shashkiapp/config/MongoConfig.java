/*
 * © Copyright
 *
 * MongoConfig.java is part of shashkiserver.
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

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.workingbit.shashkiapp.converter.ArrayNodeReadConverter;
import com.workingbit.shashkiapp.converter.ArrayNodeWriteConverter;
import com.workingbit.shashkiapp.converter.ObjectNodeReadConverter;
import com.workingbit.shashkiapp.converter.ObjectNodeWriteConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {
  @Override
  public MongoClient reactiveMongoClient() {
    return MongoClients.create();
  }

  @Override
  protected String getDatabaseName() {
    return "shashki";
  }

  @Override
  public CustomConversions customConversions() {
    var converters = new ArrayList<>();
    converters.add(new ObjectNodeReadConverter());
    converters.add(new ObjectNodeWriteConverter());
    converters.add(new ArrayNodeReadConverter());
    converters.add(new ArrayNodeWriteConverter());
    return new MongoCustomConversions(converters);
  }
}
