package net.torocraft.torohealth.events;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.torohealth.ToroHealth;

public class Events {

  @SubscribeEvent
  public void displayDamage(LivingUpdateEvent event) {
    ToroHealth.PROXY.displayDamageDealt(event.getEntityLiving());
  }

  @SubscribeEvent
  public void displayEntityStatus(RenderGameOverlayEvent.Pre event) {
    if (event.getType() != ElementType.CHAT) {
      return;
    }
    ToroHealth.PROXY.setEntityInCrosshairs();
  }

}
