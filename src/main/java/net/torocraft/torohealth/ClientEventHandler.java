
package net.torocraft.torohealth;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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

  private static void entityRender(
      RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<?>> event) {
    HealthBarRenderer.renderInWorld(event.getMatrixStack(), event.getEntity());
  }

  private static void renderParticles(RenderWorldLastEvent event) {
    ParticleRenderer.renderParticles(event.getMatrixStack());
  }

  private static void playerTick(PlayerTickEvent event) {
    if (!event.player.world.isRemote) {
      return;
    }
    ToroHealthClient.HUD.setEntity(
        ToroHealthClient.RAYTRACE.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
    BarStates.tick();
    HoldingWeaponUpdater.update();
    ToroHealthClient.HUD.tick();
  }

  private static void hudRender(RenderGameOverlayEvent.Post event) {
    if (event.getType().equals(ElementType.BOSSHEALTH)) {
      ToroHealthClient.HUD.draw(event.getMatrixStack(), ToroHealth.CONFIG);
    }
  }

}
