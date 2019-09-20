package net.torocraft.torohealth.util;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.Config.Hud;
import net.torocraft.torohealth.util.Config.Mode;
import net.torocraft.torohealth.util.Config.NumberType;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModMenuEntry implements ModMenuApi {

  @Override
  public String getModId() {
    return ToroHealth.MODID;
  }

  private static final Config DEFAULTS = new Config();

  @Override
  public Function<Screen, ? extends Screen> getConfigScreenFactory() {
    return (screen) -> {
      ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("config.title");

      ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
      Config newConfig = ConfigLoader.get();

      builder.setSavingRunnable(() -> ConfigLoader.set(newConfig));

      ConfigCategory hudCategory = builder.getOrCreateCategory("config.category.hud");

      hudCategory.addEntry(entryBuilder
          .startIntField("config.hud.hideDelay", newConfig.hud.hideDelay)
          .setSaveConsumer(val -> newConfig.hud.hideDelay = val)
          .setDefaultValue(DEFAULTS.hud.hideDelay)
          .build());

      hudCategory.addEntry(entryBuilder
          .startFloatField("config.hud.x", newConfig.hud.x)
          .setSaveConsumer(val -> newConfig.hud.x = val)
          .setDefaultValue(DEFAULTS.hud.x)
          .build());

      hudCategory.addEntry(entryBuilder
          .startFloatField("config.hud.y", newConfig.hud.y)
          .setSaveConsumer(val -> newConfig.hud.y = val)
          .setDefaultValue(DEFAULTS.hud.y)
          .build());

      hudCategory.addEntry(entryBuilder
          .startFloatField("config.hud.scale", newConfig.hud.scale)
          .setSaveConsumer(val -> newConfig.hud.scale = val)
          .setDefaultValue(DEFAULTS.hud.scale)
          .build());

      ConfigCategory barCategory = builder.getOrCreateCategory("config.torohealth.category.bar");

      barCategory.addEntry(entryBuilder
          .startEnumSelector("config.bar.damageNumberType", NumberType.class, newConfig.bar.damageNumberType)
          .setSaveConsumer(val -> newConfig.bar.damageNumberType = val)
          .setDefaultValue(DEFAULTS.bar.damageNumberType)
          .build());

      barCategory.addEntry(entryBuilder
          .startIntSlider("config.bar.friendColor", newConfig.bar.friendColor, 0, 255)
          .setSaveConsumer(val -> newConfig.bar.foeColor = val)
          .setDefaultValue(DEFAULTS.bar.friendColor)
          .build());



      ConfigCategory inWorldCategory = builder.getOrCreateCategory("config.torohealth.category.inWorld");

      inWorldCategory.addEntry(entryBuilder
          .startEnumSelector("config.inWorld.mode", Mode.class, newConfig.inWorld.mode)
          .setSaveConsumer(val -> newConfig.inWorld.mode = val)
          .setDefaultValue(DEFAULTS.inWorld.mode)
          .build());

      inWorldCategory.addEntry(entryBuilder
          .startFloatField("config.inWorld.distance", newConfig.inWorld.distance)
          .setSaveConsumer(val -> newConfig.inWorld.distance = val)
          .setDefaultValue(DEFAULTS.inWorld.distance)
          .build());

      return builder.build();
    };
  }

  /*
      float r = (color >> 24 & 255) / 255.0F;
    float g = (color >> 16 & 255) / 255.0F;
    float b = (color >> 8 & 255) / 255.0F;
    float a = (color & 255) / 255.0F;
   */

  private static int getRed(int color) {
    return color >> 24 & 255;
  }

  private static int setRed(int color, int value) {
    int mask = 0xff000000;
    color = color & mask;
    return color | value << 24;
  }

}