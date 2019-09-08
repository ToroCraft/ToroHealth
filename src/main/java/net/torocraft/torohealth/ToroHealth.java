package net.torocraft.torohealth;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.util.Config;
import net.torocraft.torohealth.util.ConfigLoader;
import net.torocraft.torohealth.util.InWorldEntityBars;

public class ToroHealth implements ModInitializer {

  public static final String MODID = "torohealth";

  public static LivingEntity selectedEntity;
  public static Config CONFIG;
  public static boolean IS_HOLDING_WEAPON = false;
  public static InWorldEntityBars IN_WORLD_BARS = new InWorldEntityBars();

  @Override
  public void onInitialize() {
    ConfigLoader.load();
  }
}
