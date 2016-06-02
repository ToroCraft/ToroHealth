package net.torocraft.torohealthmod.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.torocraft.torohealthmod.ToroHealthMod;

public class GuiConfigToroHealth extends GuiConfig {

	public GuiConfigToroHealth(GuiScreen parent) {
		super (parent, new ConfigElement(ToroHealthMod.config.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(),
				ToroHealthMod.MODID,
				false,
				false,
				"ToroHealth");
		titleLine2 = ToroHealthMod.config.getConfigFile().getAbsolutePath();
	}
}
