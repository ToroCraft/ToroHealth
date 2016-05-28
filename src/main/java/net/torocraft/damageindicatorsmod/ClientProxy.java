package net.torocraft.damageindicatorsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.damageindicatorsmod.render.DamageParticle;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void displayDamageDealt(Entity entity, int damage) {
    	World world = entity.worldObj;
    	double motionX = world.rand.nextGaussian() * 0.02;
    	double motionY = world.rand.nextGaussian() * 0.02;
    	double motionZ = world.rand.nextGaussian() * 0.02;
    	Particle damageIndicator = new DamageParticle(
    				damage,
    				world,
    				entity.posX,
    				entity.posY + entity.height,
    				entity.posZ,
    				motionX,
    				motionY,
    				motionZ
    			);
    	Minecraft.getMinecraft().effectRenderer.addEffect(damageIndicator);
    }
    
}