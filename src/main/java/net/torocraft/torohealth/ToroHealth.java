package net.torocraft.torohealth;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.util.Config;
import net.torocraft.torohealth.util.ConfigLoader;

public class ToroHealth implements ModInitializer {

  public static LivingEntity selectedEntity;
  public static Config CONFIG;
  public static final String MODID = "torohealth";

  @Override
  public void onInitialize() {
    ConfigLoader.load();
  }
}
