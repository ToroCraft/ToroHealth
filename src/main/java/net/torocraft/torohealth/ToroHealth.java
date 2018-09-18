package net.torocraft.torohealth;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torohealth.config.ConfigurationHandler;
import net.torocraft.torohealth.events.Events;

@Mod(modid = ToroHealth.MODID, name = ToroHealth.MODNAME, version = ToroHealth.VERSION, guiFactory = "net.torocraft.torohealth.gui.GuiFactoryToroHealth")

public class ToroHealth {

  public static final String MODID = "torohealthmod";
  public static final String VERSION = "1.12.2-11";
  public static final String MODNAME = "ToroHealth";

  @SidedProxy(clientSide = "net.torocraft.torohealth.ClientProxy", serverSide = "net.torocraft.torohealth.ServerProxy")
  public static CommonProxy PROXY;

  @Instance(value = ToroHealth.MODID)
  public static ToroHealth INSTANCE;

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    PROXY.preInit(e);

    ConfigurationHandler.init(e.getSuggestedConfigurationFile());
    MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    PROXY.init(e);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    PROXY.postInit(e);
    MinecraftForge.EVENT_BUS.register(new Events());
  }

}
