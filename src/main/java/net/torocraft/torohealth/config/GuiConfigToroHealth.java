package net.torocraft.torohealth.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.torocraft.torohealth.ToroHealth;

public class GuiConfigToroHealth extends GuiConfig {

  public GuiConfigToroHealth(GuiScreen parent) {
    super(parent, new ConfigElement(ConfigurationHandler.config.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(),
        ToroHealth.MODID,
        false,
        false,
        "ToroHealth");
    titleLine2 = ConfigurationHandler.config.getConfigFile().getAbsolutePath();
  }
}
