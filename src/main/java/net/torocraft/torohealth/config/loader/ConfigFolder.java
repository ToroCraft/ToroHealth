package net.torocraft.torohealth.config.loader;

import java.io.File;
import net.fabricmc.loader.api.FabricLoader;

public class ConfigFolder {

  public static File get() {
    return FabricLoader.getInstance().getConfigDir().toFile();
  }

}
