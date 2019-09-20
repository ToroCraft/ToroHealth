package net.torocraft.torohealth.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.FabricLoader;
import net.torocraft.torohealth.ToroHealth;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigLoader {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final String FILENAME = ToroHealth.MODID + ".json";
  private static File file;

  public static void load() {
    File file = getFile();
    if (!file.exists()) {
      ToroHealth.CONFIG = new Config();
      save();
    } else {
      read();
      save();
    }
  }

  private static void read() {
    ToroHealth.CONFIG = get();
  }

  public static void save() {
    File file = getFile();
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(GSON.toJson(ToroHealth.CONFIG));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void set(Config config) {
    ToroHealth.CONFIG = config;
    save();
  }

  public static Config get() {
    File file = getFile();
    try (FileReader reader = new FileReader(file)) {
      return GSON.fromJson(reader, Config.class);
    } catch (Exception e) {
      e.printStackTrace();
      return new Config();
    }
  }

  private static File getFile() {
    if (file == null) {
     file = new File(FabricLoader.INSTANCE.getConfigDirectory(), FILENAME);
    }
    return file;
  }

}
