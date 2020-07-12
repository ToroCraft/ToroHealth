package net.torocraft.torohealth.util;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.torocraft.torohealth.ToroHealth;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ToroHealth.MODID, bus = Bus.MOD)
public class ConfigLoader {

  public static final ForgeConfigSpec COMMON_SPEC;

  static {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
    COMMON_SPEC = specPair.getRight();
    ToroHealth.CONFIG = specPair.getLeft();
  }
  
  @SubscribeEvent
  public static void onLoad(final ModConfig.Loading event) {
  }
  
  @SubscribeEvent
  public static void onFileChange(final ModConfig.Reloading event) {
  }

}
