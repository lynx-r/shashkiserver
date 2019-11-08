/*
 * © Copyright
 *
 * ArrayNodeWriteConverter.java is part of shashkiserver.
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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.BasicDBList;
import com.workingbit.shashkiapp.util.JsonUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class ArrayNodeWriteConverter implements Converter<ArrayNode, BasicDBList> {

  @Override
  public BasicDBList convert(ArrayNode source) {
    return JsonUtils.jsonToData(source.toString(), BasicDBList.class);
  }
}
