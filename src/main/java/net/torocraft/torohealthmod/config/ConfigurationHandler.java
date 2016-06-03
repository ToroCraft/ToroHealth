package net.torocraft.torohealthmod.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.torohealthmod.ToroHealthMod;

public class ConfigurationHandler {

	public static Configuration config;
	
	public static boolean showDamageParticles;
	public static String entityStatusDisplay;
	public static Integer damageColor;
	public static Integer healColor;
	
	private static String[] acceptedColors = new String[]{"RED", "GREEN", "BLUE", "YELLOW", "ORANGE", "WHITE", "BLACK", "PURPLE"};
	
	public static void init(File configFile) {
		config = new Configuration(configFile);
		loadConfiguration();
	}
	
	public static void loadConfiguration() {		
		try {
			showDamageParticles = config.getBoolean("Show Damage Particles", Configuration.CATEGORY_CLIENT, true, "Show Damage Indicators");
			entityStatusDisplay = config.getString("Health Bar Display", Configuration.CATEGORY_CLIENT, "STANDARD", "Display Health Bars", new String[]{"STANDARD", "COMPACT", "OFF"});
			damageColor = mapColor(config.getString("Damage Color", Configuration.CATEGORY_CLIENT, "RED", "Damage Text Color", acceptedColors));
			healColor = mapColor(config.getString("Heal Color", Configuration.CATEGORY_CLIENT, "GREEN", "Heal Text Color", acceptedColors));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
	
	@SubscribeEvent
	public void onConfigChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(ToroHealthMod.MODID)) {
			loadConfiguration();
		}
	}
	
	private static int mapColor(String color) {
		if (color.equals("RED")) {
			return 0xff0000;
		} else if (color.equals("GREEN")) {
			return 0x00ff00;
		} else if (color.equals("BLUE")) {
			return 0x0000ff;
		} else if (color.equals("YELLOW")) {
			return 0xffff00;
		} else if (color.equals("ORANGE")) {
			return 0xffa500;
		} else if (color.equals("BLACK")) {
			return 0x000000;
		} else if (color.equals("PURPLE")) {
			return 0x960096;
		} else {
			return 0xffffff;
		}		
	}
	
}
