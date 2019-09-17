package net.torocraft.torohealth.util;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.torocraft.torohealth.ToroHealth;

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
      ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("ToroHealth Config");

      builder.setSavingRunnable(() -> {
          ConfigLoader.save();
      });

      ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

      ConfigCategory category = builder.getOrCreateCategory("config.torohealth.category.hud");

      category.addEntry(entryBuilder.startIntField("key.torohealth.config.", ToroHealth.CONFIG.hud.hideDelay)
          .setSaveConsumer(val -> {
            ToroHealth.CONFIG.hud.hideDelay = val;
            //ConfigLoader.save();
          })
          .setDefaultValue(DEFAULTS.hud.hideDelay)
          .build()
      );

      return builder.build();
    };
  }
}