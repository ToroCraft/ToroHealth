package net.torocraft.damageindicatorsmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.damageindicatorsmod.events.EventHooks;


@Mod (modid = DamageIndicatorsMod.MODID, name = DamageIndicatorsMod.MODNAME, version = DamageIndicatorsMod.VERSION)
public class DamageIndicatorsMod {

	
	public static final String MODID = "damageindicatorsmod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "DamageIndicatorsMod";
	
	@SidedProxy(clientSide="net.torocraft.damageindicatorsmod.ClientProxy", serverSide="net.torocraft.damageindicatorsmod.ServerProxy")
	public static CommonProxy proxy;
	
	@Instance(value = DamageIndicatorsMod.MODID)
	public static DamageIndicatorsMod instance;
	
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
	    MinecraftForge.EVENT_BUS.register(new EventHooks());
	}
	
	
	
	
}
