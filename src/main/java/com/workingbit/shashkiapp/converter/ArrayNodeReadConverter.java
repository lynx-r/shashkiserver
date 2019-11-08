/*
 * © Copyright
 *
 * ArrayNodeReadConverter.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.workingbit.shashkiapp.util.JsonUtils;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@ReadingConverter
public class ArrayNodeReadConverter implements Converter<ArrayList, ArrayNode> {

  @Override
  public ArrayNode convert(ArrayList source) {
    var arrayNode = JsonNodeFactory.instance.arrayNode();
    for (Object o : source) {
      String json = ((Document) o).toJson();
      JsonNode jsonNode = JsonUtils.jsonToData(json, JsonNode.class);
      arrayNode.add(jsonNode);
    }
    return arrayNode;
  }
}
