package net.torocraft.damageindicatorsmod.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.damageindicatorsmod.DamageIndicatorsMod;

public class EventHooks {

	@SubscribeEvent
	public void displayDamage(LivingHurtEvent event) {
		float amount = event.getAmount();
		DamageSource source = event.getSource();
		EntityLivingBase entity = event.getEntityLiving();
		
		if (amount <= 0) {
			return;
		}
		if (!source.isUnblockable()) {
			amount = CombatRules.getDamageAfterAbsorb(amount, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		}
		amount = applyPotionDamageCalculations(entity, source, amount);
        amount = Math.max(amount - entity.getAbsorptionAmount(), 0.0F);
        
        amount = Math.round(amount);
		
        DamageIndicatorsMod.proxy.displayDamageDealt(entity, (int)amount);
	}
	
	private float applyPotionDamageCalculations(EntityLivingBase entity, DamageSource source, float damage) {
		 if (source.isDamageAbsolute()) {
			 return damage;
		 } else {
			 if (entity.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.outOfWorld) {
				 int i = (entity.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
				 int j = 25 - i;
				 float f = damage * (float)j;
				 damage = f / 25.0F;
			 }
			 if (damage <= 0.0F) {
				 return 0.0F;
			 } else {
				 int k = EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), source);

				 if (k > 0) {
					 damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
				 }
				 return damage;
			 }
		 }
	 }
	
}
