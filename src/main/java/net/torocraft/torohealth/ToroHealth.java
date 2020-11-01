package net.torocraft.torohealth;

import net.minecraftforge.fml.common.Mod;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.loader.ConfigLoader;
import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.util.RayTrace;

@Mod(ToroHealth.MODID)
public class ToroHealth {
  public static final String MODID = "torohealth";

  public static Config CONFIG = new Config();
  public static Hud HUD = new Hud();
  public static RayTrace RAYTRACE = new RayTrace();
  public static boolean IS_HOLDING_WEAPON = false;


  private static ConfigLoader<Config> CONFIG_LOADER = new ConfigLoader<>(new Config(),
      ToroHealth.MODID + ".json", config -> ToroHealth.CONFIG = config);

  public ToroHealth() {
    CONFIG_LOADER.load();
  }

}
