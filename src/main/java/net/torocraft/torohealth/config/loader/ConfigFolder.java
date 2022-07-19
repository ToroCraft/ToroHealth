package net.torocraft.torohealth.config.loader;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class ConfigFolder {
	public static File get() {
		return FMLPaths.CONFIGDIR.get().toFile();
	}
}
