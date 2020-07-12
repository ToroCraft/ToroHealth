package net.torocraft.torohealth.util;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.torocraft.torohealth.ToroHealth;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ToroHealth.MODID, bus = Bus.MOD)
public class ConfigLoader {
  /*
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public static void load(File file) {
    if (!file.exists()) {
      ToroHealth.CONFIG = new Config();
      save(file);
    } else {
      read(file);
      save(file);
    }
  }

  private static Config read(File file) {
    try (FileReader reader = new FileReader(file)) {
      return GSON.fromJson(reader, Config.class);
    } catch (Exception e) {
      e.printStackTrace();
      return new Config();
    }
  }

  public static void save(File file) {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(GSON.toJson(ToroHealth.CONFIG));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  */

  public static class Common {
    
    public final IntValue crafting_width;
    public final IntValue crafting_height;
    
    public Common(ForgeConfigSpec.Builder builder) {
      builder.comment("Tutorial Mod Configurations")
           .push("tutorial");
      
      crafting_width = builder
          .comment("This sets the crafting width of the game. If a value is higher declared by a different mod, this becomes obsolete.")
          .translation("tutorial.configgui.crafting_width")
          .worldRestart()
          .defineInRange("crafting_width", 3, 3, 5);
      
      crafting_height = builder
          .comment("This sets the crafting height of the game. If a value is higher declared by a different mod, this becomes obsolete.")
          .translation("tutorial.configgui.crafting_height")
          .worldRestart()
          .defineInRange("crafting_height", 3, 3, 5);
      
      builder.pop();
    }
  }

  public static final ForgeConfigSpec COMMON_SPEC;
  public static final Common COMMON;

  static {
    final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
    COMMON_SPEC = specPair.getRight();
    COMMON = specPair.getLeft();
    System.out.println("building");
  }
  
  @SubscribeEvent
  public static void onLoad(final ModConfig.Loading event) {
    System.out.println("onload");
  }
  
  @SubscribeEvent
  public static void onFileChange(final ModConfig.Reloading event) {
    System.out.println("onFileChanged");
  }

}
