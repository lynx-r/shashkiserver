/*
 * © Copyright
 *
 * JsonUtils.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flipkart.zjsonpatch.JsonDiff;
import com.flipkart.zjsonpatch.JsonPatch;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JsonUtils {
  @NotNull
  public static final ObjectMapper mapper;

  static {
    mapper = configureObjectMapper();
  }

  @NotNull
  private static ObjectMapper configureObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    SimpleModule module = new SimpleModule();

    mapper.registerModule(module);

    return mapper;
  }

  public static String dataToJson(Object data) {
    try {
      return mapper.writeValueAsString(data);
    } catch (IOException e) {
      throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON.\n" + e.getMessage());
    }
  }

  public static String dataToJsonPretty(Object data) {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    } catch (IOException e) {
      throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON.\n" + e.getMessage());
    }
  }

  public static <T> T jsonToData(String json, @NotNull Class<T> clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException("IOException while mapping json " + json + ".\n" + e.getMessage());
    }
  }

  public static <T> T jsonToDataTypeRef(String json, @NotNull TypeReference<T> typeRef) {
    try {
      return mapper.readValue(json, typeRef);
    } catch (IOException e) {
      throw new RuntimeException("IOException while mapping json " + json + ".\n" + e.getMessage());
    }
  }

  public static <T> JsonNode dataToJsonNode(T value) {
    return mapper.valueToTree(value);
  }

  public static <T> T jsonNodeToData(JsonNode jsonNode, Class<T> clazz) {
    try {
      return mapper.treeToValue(jsonNode, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("IOException while converting tree to value " + jsonNode + ".\n" + e.getMessage());
    }
  }

  public static <T> JsonNode asJsonDiff(T source, T target) {
    return JsonDiff.asJson(dataToJsonNode(source), dataToJsonNode(target));
  }

  public static <T> JsonNode asJsonDiff(JsonNode source, T target) {
    return JsonDiff.asJson(source, dataToJsonNode(target));
  }

  public static <T> JsonNode asJsonDiff(T source, JsonNode target) {
    return JsonDiff.asJson(dataToJsonNode(source), target);
  }

  public static <T> JsonNode applyPatch(JsonNode patch, T data) {
    return JsonPatch.apply(patch, dataToJsonNode(data));
  }

  public static <T> T applyPatch(JsonNode patch, T data, Class<T> clazz) {
    return jsonNodeToData(JsonPatch.apply(patch, dataToJsonNode(data)), clazz);
  }
}
