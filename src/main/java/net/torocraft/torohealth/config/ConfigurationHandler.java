package net.torocraft.torohealth.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.torohealth.ToroHealth;

public class ConfigurationHandler {

  public static Configuration config;

  public static boolean showEntityModel;
  public static boolean showDamageParticles;
  public static String entityStatusDisplay;
  public static String statusDisplayPosition;
  public static String skin;
  public static Integer statusDisplayX;
  public static Integer statusDisplayY;
  public static Integer damageColor;
  public static Integer healColor;
  public static int hideDelay;

  private static String[] acceptedColors = new String[]{"RED", "GREEN", "BLUE", "YELLOW", "ORANGE", "WHITE", "BLACK", "PURPLE"};

  public static void init(File configFile) {
    config = new Configuration(configFile);
    loadConfiguration();
  }

  public static void loadConfiguration() {
    try {
      skin = config.getString("Skin", Configuration.CATEGORY_CLIENT, "BASIC", "Background Skin Selection", new String[]{"NONE", "BASIC", "HEAVY"});
      showEntityModel = config.getBoolean("Show 3D Model of Entity", Configuration.CATEGORY_CLIENT, true, "Shows a 3D model of the entity being targeted");
      entityStatusDisplay = config.getString("Health Bar Display", Configuration.CATEGORY_CLIENT, "HEARTS", "Display Health Bars", new String[]{"HEARTS", "NUMERIC", "BAR", "OFF"});
      statusDisplayPosition = config
          .getString("Health Bar Position", Configuration.CATEGORY_CLIENT, "TOP LEFT", "Location of Health Bar", new String[]{"TOP LEFT", "TOP CENTER", "TOP RIGHT", "BOTTOM LEFT", "BOTTOM RIGHT"});
      statusDisplayX = config.getInt("Health Bar X", Configuration.CATEGORY_CLIENT, 0, -20000, 20000, "With CUSTOM position, sets X position of Health Bar");
      statusDisplayY = config.getInt("Health Bar Y", Configuration.CATEGORY_CLIENT, 0, -20000, 20000, "With CUSTOM position, sets Y position of Health Bar");
      hideDelay = config.getInt("Hide Delay", Configuration.CATEGORY_CLIENT, 400, 50, 5000, "Delays hiding the dialog for the given number of milliseconds");
      showDamageParticles = config.getBoolean("Show Damage Particles", Configuration.CATEGORY_CLIENT, true, "Show Damage Indicators");
      healColor = mapColor(config.getString("Heal Color", Configuration.CATEGORY_CLIENT, "GREEN", "Heal Text Color", acceptedColors));
      damageColor = mapColor(config.getString("Damage Color", Configuration.CATEGORY_CLIENT, "RED", "Damage Text Color", acceptedColors));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (config.hasChanged()) {
        config.save();
      }
    }
  }

  private static int mapColor(String color) {
    if (color.equals("RED")) {
      return 0xff0000;
    } else if (color.equals("GREEN")) {
      return 0x00ff00;
    } else if (color.equals("BLUE")) {
      return 0x0000ff;
    } else if (color.equals("YELLOW")) {
      return 0xffff00;
    } else if (color.equals("ORANGE")) {
      return 0xffa500;
    } else if (color.equals("BLACK")) {
      return 0x000000;
    } else if (color.equals("PURPLE")) {
      return 0x960096;
    } else {
      return 0xffffff;
    }
  }

  @SubscribeEvent
  public void onConfigChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equalsIgnoreCase(ToroHealth.MODID)) {
      loadConfiguration();
    }
  }

}
