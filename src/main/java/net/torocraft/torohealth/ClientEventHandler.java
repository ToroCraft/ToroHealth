
package net.torocraft.torohealth;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.torocraft.torohealth.bars.BarStates;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.util.HoldingWeaponUpdater;

@EventBusSubscriber(modid = ToroHealth.MODID, bus = EventBusSubscriber.Bus.FORGE,
    value = Dist.CLIENT)
public class ClientEventHandler {
  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void entityRender(
      RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<?>> event) {
    HealthBarRenderer.renderInWorld(event.getMatrixStack(), event.getEntity());
  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void playerTick(PlayerTickEvent event) {
    if (!event.player.world.isRemote) {
      return;
    }
    ToroHealth.HUD
        .setEntity(ToroHealth.RAYTRACE.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
    BarStates.tick();
    HoldingWeaponUpdater.update();
    ToroHealth.HUD.tick();
  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void hudRender(RenderGameOverlayEvent.Post event) {
    if (event.getType().equals(ElementType.BOSSHEALTH)) {
      ToroHealth.HUD.draw(event.getMatrixStack());
    }
  }

}
