
package net.torocraft.torohealth;

        import net.minecraft.client.Camera;
        import net.minecraft.client.Minecraft;
        import net.minecraft.client.model.EntityModel;
        import net.minecraft.client.renderer.MultiBufferSource;
        import net.minecraft.world.entity.LivingEntity;
        import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
        import net.minecraftforge.client.event.RenderLevelStageEvent;
        import net.minecraftforge.client.event.RenderLivingEvent;
        import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
        import net.minecraftforge.common.MinecraftForge;
        import net.minecraftforge.event.TickEvent.PlayerTickEvent;
        import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
        import net.torocraft.torohealth.bars.BarStates;
        import net.torocraft.torohealth.bars.HealthBarRenderer;
        import net.torocraft.torohealth.bars.ParticleRenderer;
        import net.torocraft.torohealth.util.HoldingWeaponUpdater;

        import java.lang.annotation.ElementType;

public class ClientEventHandler {

  public static void init() {
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::playerTick);
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::entityRender);
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::renderParticles);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandler::registerOverlays);
  }

  private static void registerOverlays(final RegisterGuiOverlaysEvent event) {
    event.registerAbove(VanillaGuiOverlay.POTION_ICONS.id(), "torohealth_hud", ToroHealthClient.HUD::draw);
  }

  private static Minecraft minecraft = Minecraft.getInstance();

  private static void entityRender(
          RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<?>> event) {
    HealthBarRenderer.prepareRenderInWorld(event.getEntity());
  }

    private static void renderParticles(RenderLevelStageEvent event) {
      if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        MultiBufferSource bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();
        ParticleRenderer.renderParticles(event.getPoseStack(), camera, bufferSource);
        HealthBarRenderer.renderInWorld(event.getPartialTick(), event.getPoseStack(), bufferSource, camera);
      }
    }

  private static void playerTick(PlayerTickEvent event) {
    if (!event.player.level().isClientSide) {
      return;
    }
    ToroHealthClient.HUD.setEntity(
        ToroHealthClient.RAYTRACE.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
    BarStates.tick();
    HoldingWeaponUpdater.update();
    ToroHealthClient.HUD.tick();
  }
}
