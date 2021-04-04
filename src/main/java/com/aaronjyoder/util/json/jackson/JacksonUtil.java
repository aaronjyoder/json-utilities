package com.aaronjyoder.util.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class JacksonUtil {

  private static JsonMapper.Builder jsonAdapterBuilder = JsonMapper.builder().addModule(new JavaTimeModule());

  private JacksonUtil() {
  }

  public static void registerIfBaseType(final Class<?>... baseClasses) {
    var builder = BasicPolymorphicTypeValidator.builder();
    for (Class<?> baseClass : baseClasses) {
      builder.allowIfBaseType(baseClass);
    }
    jsonAdapterBuilder = jsonAdapterBuilder.activateDefaultTypingAsProperty(builder.build(), DefaultTyping.NON_FINAL, "type");
  }

  public static void registerIfSubType(final Class<?>... subClasses) {
    var builder = BasicPolymorphicTypeValidator.builder();
    for (Class<?> subClass : subClasses) {
      builder.allowIfSubType(subClass);
    }
    jsonAdapterBuilder = jsonAdapterBuilder.activateDefaultTypingAsProperty(builder.build(), DefaultTyping.NON_FINAL, "type");
  }

  // Read

  public static <T> T read(String file, Class<T> clazz) {
    File fileToRead = new File(file);
    if (fileToRead.exists()) {
      try {
        return jsonAdapterBuilder.build().readValue(fileToRead, clazz);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static <T> T read(String file, Type type) {
    File fileToRead = new File(file);
    if (fileToRead.exists()) {
      try {
        return jsonAdapterBuilder.build().readValue(fileToRead, jsonAdapterBuilder.build().constructType(type));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  // Write

  public static <T> void write(String file, Class<T> clazz, T object) {
    try {
      Writer writer = new FileWriter(file, StandardCharsets.UTF_8);
      writer.write(jsonAdapterBuilder.build().writerWithDefaultPrettyPrinter().writeValueAsString(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static <T> void write(String file, Type type, T object) {
    try {
      Writer writer = new FileWriter(file, StandardCharsets.UTF_8);
      writer.write(jsonAdapterBuilder.build().writerWithDefaultPrettyPrinter().writeValueAsString(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Write with basic directory creation

  public static <T> void write(String directory, String fileName, Class<T> clazz, T object) {
    try {
      Files.createDirectories(Paths.get(directory));
      Writer writer = new FileWriter(directory + fileName, StandardCharsets.UTF_8);
      writer.write(jsonAdapterBuilder.build().writerWithDefaultPrettyPrinter().writeValueAsString(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static <T> void write(String directory, String fileName, Type type, T object) {
    try {
      Files.createDirectories(Paths.get(directory));
      Writer writer = new FileWriter(directory + fileName, StandardCharsets.UTF_8);
      writer.write(jsonAdapterBuilder.build().writerWithDefaultPrettyPrinter().writeValueAsString(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}