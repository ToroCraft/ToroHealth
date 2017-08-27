package net.torocraft.torohealthmod.events;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
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
		if (event.getType() != ElementType.CHAT) {
			return;
		}
		ToroHealthMod.proxy.setEntityInCrosshairs();
	}
	
}
