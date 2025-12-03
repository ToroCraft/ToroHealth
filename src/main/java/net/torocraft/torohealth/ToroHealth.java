package net.torocraft.torohealth;

import java.util.Random;

import net.fabricmc.api.ModInitializer;
import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.util.RayTrace;

public class ToroHealth implements ModInitializer {

  public static final String MODID = "torohealth";


  public static ModConfig CONFIG;
  public static Hud HUD = new Hud();
  public static RayTrace RAYTRACE = new RayTrace();
  public static boolean IS_HOLDING_WEAPON = false;
  public static Random RAND = new Random();


  @Override
  public void onInitialize() {
      ModConfig.init();
      CONFIG = ModConfig.INSTANCE;
  }
}
