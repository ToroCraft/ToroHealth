package net.torocraft.torohealthmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torohealthmod.events.Events;

@Mod(modid = ToroHealthMod.MODID, name = ToroHealthMod.MODNAME, version = ToroHealthMod.VERSION, clientSideOnly = true,
	guiFactory = "net.torocraft." + ToroHealthMod.MODID + ".gui.GuiFactoryToroHealth")
public class ToroHealthMod {

	public static final String MODID = "torohealthmod";
	public static final String VERSION = "1.9.4-2";
	public static final String MODNAME = "ToroHealthMod";
	
	public static Configuration config; 

	@SidedProxy(clientSide = "net.torocraft.torohealthmod.ClientProxy", serverSide = "net.torocraft.torohealthmod.ServerProxy")
	public static CommonProxy proxy;

	@Instance(value = ToroHealthMod.MODID)
	public static ToroHealthMod instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
		
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();
		
		config.getBoolean("showDamageParticles", Configuration.CATEGORY_CLIENT, true, "Show Damage Indicators");
		config.getString("entityStatusDisplay", Configuration.CATEGORY_CLIENT, "STANDARD", "Display Health Bars", new String[]{"STANDARD", "COMPACT", "OFF"});
		config.getInt("damageColor", Configuration.CATEGORY_CLIENT, 0xff0000, 0x000000, 0xffffff, "Damage Text Color");
		config.getInt("healColor", Configuration.CATEGORY_CLIENT, 0x00ff00, 0x000000, 0xffffff, "Heal Text Color");
		
		config.save();
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
