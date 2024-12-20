package net.torocraft.torohealth;

import java.util.Random;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.bars.ParticleRenderer;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.loader.ConfigLoader;
import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.mixin.WorldRendererAccessor;
import net.torocraft.torohealth.util.RayTrace;

public class ToroHealth implements ModInitializer {

  public static final String MODID = "torohealth";


  public static Config CONFIG = new Config();
  public static Hud HUD = new Hud();
  public static RayTrace RAYTRACE = new RayTrace();
  public static boolean IS_HOLDING_WEAPON = false;
  public static Random RAND = new Random();

  private static ConfigLoader<Config> CONFIG_LOADER = new ConfigLoader<>(new Config(),
      ToroHealth.MODID + ".json", config -> ToroHealth.CONFIG = config);

  @Override
  public void onInitialize() {
    CONFIG_LOADER.load();
    WorldRenderEvents.LAST.register((context) -> {
      var entityVertexConsumers = ((WorldRendererAccessor) context.worldRenderer())
              .getBufferBuilders()
              .getEntityVertexConsumers();
      HealthBarRenderer.renderInWorld(context.matrixStack(), entityVertexConsumers, context.camera());
      ParticleRenderer.renderParticles(context.matrixStack(), entityVertexConsumers, context.camera());
    });
  }
}
