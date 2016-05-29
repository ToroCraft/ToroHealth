package net.torocraft.damageindicatorsmod.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.damageindicatorsmod.DamageIndicatorsMod;

public class Events {

	@SubscribeEvent
	public void displayDamage(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		
		int currentHealth = (int)Math.ceil(entity.getHealth());
		
		if (entity.getEntityData().hasKey("health")) {
			int entityHealth = ((NBTTagInt)entity.getEntityData().getTag("health")).getInt();
			
			if (entityHealth != currentHealth && entity.worldObj.isRemote) {
				DamageIndicatorsMod.proxy.displayDamageDealt(entity, (int)entityHealth - currentHealth);
			}
		}
		
		entity.getEntityData().setTag("health", new NBTTagInt(currentHealth));
	}
	
}
