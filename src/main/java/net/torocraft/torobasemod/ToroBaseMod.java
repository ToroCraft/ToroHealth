package net.torocraft.torobasemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod (modid = ToroBaseMod.MODID, name = ToroBaseMod.MODNAME, version = ToroBaseMod.VERSION)
public class ToroBaseMod {

	
	public static final String MODID = "torobasemod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "ToroBaseMod";
	
	@SidedProxy(clientSide="net.torocraft.torobasemod.ClientProxy", serverSide="net.torocraft.torobasemod.ServerProxy")
	public static CommonProxy proxy;
	
	@Instance(value = ToroBaseMod.MODID)
	public static ToroBaseMod instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	    proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
	    proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	    proxy.postInit(e);
	}
	
	
	
	
}
