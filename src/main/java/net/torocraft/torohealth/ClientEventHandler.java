
package net.torocraft.torohealth;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.torocraft.torohealth.bars.BarStates;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.bars.ParticleRenderer;
import net.torocraft.torohealth.util.HoldingWeaponUpdater;

public class ClientEventHandler {

  public static void init() {
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::playerTick);
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::entityRender);
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::renderParticles);
    MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::hudRender);
  }

  private static Minecraft minecraft = Minecraft.getInstance();

  private static void entityRender(
      RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<?>> event) {
    HealthBarRenderer.prepareRenderInWorld(event.getEntity());
  }

  private static void renderParticles(RenderLevelLastEvent event) {
    Camera camera = minecraft.gameRenderer.getMainCamera();
    ParticleRenderer.renderParticles(event.getPoseStack(), camera);
    HealthBarRenderer.renderInWorld(event.getPartialTick(), event.getPoseStack(), camera);
  }

  private static void playerTick(PlayerTickEvent event) {
    if (!event.player.level.isClientSide) {
      return;
    }
    ToroHealthClient.HUD.setEntity(
        ToroHealthClient.RAYTRACE.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
    BarStates.tick();
    HoldingWeaponUpdater.update();
    ToroHealthClient.HUD.tick();
  }

  private static void hudRender(RenderGameOverlayEvent.Post event) {
    if (event.getType().equals(ElementType.ALL)) {
      ToroHealthClient.HUD.draw(event.getMatrixStack(), ToroHealth.CONFIG);
    }
  }

}
