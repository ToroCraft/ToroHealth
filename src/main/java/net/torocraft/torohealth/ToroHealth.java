package net.torocraft.torohealth;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.util.Config;
import net.torocraft.torohealth.util.ConfigLoader;
import net.torocraft.torohealth.util.InWorldEntityBars;
import net.torocraft.torohealth.util.RayTrace;

public class ToroHealth implements ModInitializer {

  public static final String MODID = "torohealth";

  public static Config CONFIG;
  public static Hud HUD = new Hud();
  public static RayTrace RAYTRACE = new RayTrace();
  public static boolean IS_HOLDING_WEAPON = false;
  public static InWorldEntityBars IN_WORLD_BARS = new InWorldEntityBars();

  @Override
  public void onInitialize() {
    ConfigLoader.load();
  }
}
