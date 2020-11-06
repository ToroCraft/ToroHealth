package net.torocraft.torohealth;

import java.util.Random;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.loader.ConfigLoader;
import org.apache.commons.lang3.tuple.Pair;

@Mod(ToroHealth.MODID)
public class ToroHealth {
  public static final String MODID = "torohealth";

  public static Config CONFIG = new Config();
  public static Random RAND = new Random();

  private static ConfigLoader<Config> CONFIG_LOADER = new ConfigLoader<>(new Config(),
      ToroHealth.MODID + ".json", config -> ToroHealth.CONFIG = config);

  public ToroHealth() {
    // Make sure the mod being absent on the other network side does not cause the client to display
    // the server as incompatible
    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
        () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

    CONFIG_LOADER.load();

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ToroHealthClient::init);
  }

}
