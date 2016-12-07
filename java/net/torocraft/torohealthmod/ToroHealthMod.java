package net.torocraft.torohealthmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torohealthmod.config.ConfigurationHandler;
import net.torocraft.torohealthmod.events.Events;

@Mod(modid = ToroHealthMod.MODID, name = ToroHealthMod.MODNAME, version = ToroHealthMod.VERSION, clientSideOnly = true, guiFactory = "net.torocraft."
		+ ToroHealthMod.MODID + ".gui.GuiFactoryToroHealth")
public class ToroHealthMod {

	public static final String MODID = "torohealthmod";
	public static final String VERSION = "1.8.9-1";
	public static final String MODNAME = "ToroHealthMod";

	@SidedProxy(clientSide = "net.torocraft.torohealthmod.ClientProxy", serverSide = "net.torocraft.torohealthmod.ServerProxy")
	public static CommonProxy proxy;

	@Instance(value = ToroHealthMod.MODID)
	public static ToroHealthMod instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);

		ConfigurationHandler.init(e.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
		MinecraftForge.EVENT_BUS.register(new Events());
	}

}
