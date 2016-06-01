package net.torocraft.torohealthmod.events;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.torohealthmod.ToroHealthMod;

public class Events {

	@SubscribeEvent
	public void displayDamage(LivingUpdateEvent event) {
		ToroHealthMod.proxy.displayDamageDealt(event.getEntityLiving());
	}

	@SubscribeEvent
	public void displayEntityStatus(RenderGameOverlayEvent.Pre event) {
		ToroHealthMod.proxy.setEntityInCrosshairs();
	}
	
}
