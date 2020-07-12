package net.torocraft.torohealth;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.util.Config;
import net.torocraft.torohealth.util.ConfigLoader;
import net.torocraft.torohealth.util.RayTrace;

@Mod(ToroHealth.MODID)
public class ToroHealth {
  public static final String MODID = "torohealth";
  
  public static Config CONFIG;
  public static Hud HUD = new Hud();
  public static RayTrace RAYTRACE = new RayTrace();
  public static boolean IS_HOLDING_WEAPON = false;

  public ToroHealth() {
    ModLoadingContext.get().registerConfig(Type.COMMON, ConfigLoader.COMMON_SPEC, ToroHealth.MODID + ".toml");
  }

}
