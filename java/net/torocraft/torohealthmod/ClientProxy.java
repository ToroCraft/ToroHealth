package net.torocraft.torohealthmod;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torohealthmod.config.ConfigurationHandler;
import net.torocraft.torohealthmod.gui.GuiEntityStatus;
import net.torocraft.torohealthmod.render.DamageParticle;

public class ClientProxy extends CommonProxy {
	
	GuiEntityStatus entityStatusGUI;

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		entityStatusGUI = new GuiEntityStatus();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		MinecraftForge.EVENT_BUS.register(entityStatusGUI);
	}

	@Override	
	public void displayDamageDealt(EntityLivingBase entity) {

		if (!entity.worldObj.isRemote) {
			return;
		}
		
		if (!ConfigurationHandler.showDamageParticles) {
			return;
		}
		
		int currentHealth = (int) Math.ceil(entity.getHealth());

		if (entity.getEntityData().hasKey("health")) {
			int entityHealth = ((NBTTagInt) entity.getEntityData().getTag("health")).getInt();

			if (entityHealth != currentHealth) {
				displayParticle(entity, (int) entityHealth - currentHealth);
			}
		}

		entity.getEntityData().setTag("health", new NBTTagInt(currentHealth));
	}

	private void displayParticle(Entity entity, int damage) {
		if (damage == 0) {
			return;
		}
		World world = entity.worldObj;
		double motionX = world.rand.nextGaussian() * 0.02;
		double motionY = 0.5f;
		double motionZ = world.rand.nextGaussian() * 0.02;
		Particle damageIndicator = new DamageParticle(damage, world, entity.posX, entity.posY + entity.height, entity.posZ, motionX, motionY, motionZ);
		Minecraft.getMinecraft().effectRenderer.addEffect(damageIndicator);
	}
	
	@Override
	public void setEntityInCrosshairs() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = player.worldObj;
		
		EntityLivingBase entity = findEntityLookedAt(player, world);
		if (entity != null) {
			entityStatusGUI.setEntity(entity);
		}
	}
	
	private EntityLivingBase findEntityLookedAt(EntityPlayer player, World world) {
		RayTraceResult rtr;
		BlockPos pos;
		BlockPos prevPos = null;
		for (double d = 0.0; d < 50.0; d++) {
			rtr = player.rayTrace(d, 1.0f);
			
			if (rtr == null) {
				continue;
			}
			
			pos = new BlockPos(rtr.hitVec);
			
			if (prevPos != null && isSolidBlock(pos, prevPos)) {
				return null;
			}
			
			prevPos = pos;
			
			AxisAlignedBB scan = new AxisAlignedBB(pos);
			
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, scan);
			
			for (Entity entity : entities) {
				if (entity instanceof EntityLivingBase) {
					return (EntityLivingBase)entity;
				}
			}
		}
		
		return null;
	}

	private boolean isSolidBlock(BlockPos pos, BlockPos prevPos) {
		return prevPos.compareTo(pos) == 0;
	}

}