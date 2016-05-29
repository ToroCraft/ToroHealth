package net.torocraft.damageindicatorsmod.events;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.damageindicatorsmod.DamageIndicatorsMod;

public class Events {

	@SubscribeEvent
	public void displayDamage(LivingUpdateEvent event) {
		DamageIndicatorsMod.proxy.displayDamageDealt(event.getEntityLiving());
	}

	@SubscribeEvent
	public void displayEntityStatus(RenderGameOverlayEvent.Pre event) {
		DamageIndicatorsMod.proxy.setEntityInCrosshairs();
	}
	
}
