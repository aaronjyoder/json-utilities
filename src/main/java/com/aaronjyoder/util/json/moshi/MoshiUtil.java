package com.aaronjyoder.util.json.moshi;

import com.aaronjyoder.util.json.adapters.ColorAdapter;
import com.aaronjyoder.util.json.adapters.InstantAdapter;
import com.aaronjyoder.util.json.adapters.PointAdapter;
import com.aaronjyoder.util.json.adapters.RuntimeTypeAdapterFactory;
import com.aaronjyoder.util.json.adapters.UUIDAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import dev.zacsweers.moshix.records.RecordsJsonAdapterFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class MoshiUtil {

  private MoshiUtil() {
  }

  private static final Moshi.Builder jsonAdapterBuilder = new Moshi.Builder()
      .add(new RecordsJsonAdapterFactory())
      .add(new InstantAdapter())
      .add(new UUIDAdapter())
      .add(new PointAdapter())
      .add(new ColorAdapter());

  public static void register(RuntimeTypeAdapterFactory<?>... factories) {
    for (RuntimeTypeAdapterFactory<?> factory : factories) {
      jsonAdapterBuilder.add(factory);
    }
  }

  private static Moshi jsonAdapter(RuntimeTypeAdapterFactory<?>... factories) {
    Moshi.Builder builder = new Moshi.Builder()
        .add(new RecordsJsonAdapterFactory())
        .add(new InstantAdapter())
        .add(new UUIDAdapter())
        .add(new PointAdapter())
        .add(new ColorAdapter());

    for (RuntimeTypeAdapterFactory<?> factory : factories) {
      builder.add(factory);
    }

    return builder.build();
  }

  @Deprecated
  private static String fileToString(File file) throws IOException {
    if (file.exists()) {
      return Files.readString(file.toPath());
    }
    return "{}";
  }

  // Read

  @Nullable
  public static <T> T read(@Nonnull Path path, @Nonnull Class<T> type) throws IOException {
    if (Files.isRegularFile(path) && Files.isReadable(path)) {
      JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
      return jsonAdapter.fromJson(Files.readString(path));
    }
    return null;
  }

  @Nullable
  public static <T> T read(@Nonnull Path path, @Nonnull Type type) throws IOException {
    if (Files.isRegularFile(path) && Files.isReadable(path)) {
      JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
      return jsonAdapter.fromJson(Files.readString(path));
    }
    return null;
  }

  @Deprecated
  public static <T> T read(String file, Class<T> type) {
    File fileToRead = new File(file);
    if (fileToRead.exists()) {
      try {
        JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
        return jsonAdapter.fromJson(fileToString(fileToRead));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Deprecated
  public static <T> T read(String file, Type type) {
    File fileToRead = new File(file);
    if (fileToRead.exists()) {
      try {
        JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
        return jsonAdapter.fromJson(fileToString(fileToRead));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  // Write

  public static <T> void write(@Nonnull Path path, @Nonnull Class<T> type, @Nonnull T object) throws IOException {
    Files.createDirectories(path.getParent());
    Files.writeString(path, jsonAdapter().adapter(type).indent("  ").toJson(object), StandardCharsets.UTF_8);
  }

  public static <T> void write(@Nonnull Path path, @Nonnull Type type, @Nonnull T object) throws IOException {
    Files.createDirectories(path.getParent());
    Files.writeString(path, jsonAdapter().adapter(type).indent("  ").toJson(object), StandardCharsets.UTF_8);
  }

  @Deprecated
  public static <T> void write(String file, Class<T> type, T object) {
    try {
      Writer writer = new FileWriter(file, StandardCharsets.UTF_8);
      writer.write(jsonAdapter().adapter(type).indent("  ").toJson(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Deprecated
  public static <T> void write(String file, Type type, T object) {
    try {
      Writer writer = new FileWriter(file, StandardCharsets.UTF_8);
      writer.write(jsonAdapter().adapter(type).indent("  ").toJson(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Write with basic directory creation

  @Deprecated
  public static <T> void write(String directory, String fileName, Class<T> type, T object) {
    try {
      Files.createDirectories(Paths.get(directory));
      Writer writer = new FileWriter(directory + fileName, StandardCharsets.UTF_8);
      writer.write(jsonAdapter().adapter(type).indent("  ").toJson(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Deprecated
  public static <T> void write(String directory, String fileName, Type type, T object) {
    try {
      Files.createDirectories(Paths.get(directory));
      Writer writer = new FileWriter(directory + fileName, StandardCharsets.UTF_8);
      writer.write(jsonAdapter().adapter(type).indent("  ").toJson(object));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
