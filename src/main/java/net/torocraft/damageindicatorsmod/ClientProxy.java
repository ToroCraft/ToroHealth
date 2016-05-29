package net.torocraft.damageindicatorsmod;

import java.util.List;

import net.minecraft.block.state.IBlockState;
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
import net.torocraft.damageindicatorsmod.gui.GuiEntityStatus;
import net.torocraft.damageindicatorsmod.render.DamageParticle;

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
		
		RayTraceResult rtr = player.rayTrace(200.0, 1.0f);
		
		BlockPos pos = new BlockPos(rtr.hitVec);
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isAir(state, world, pos)) {
			pos = pos.down();
		}
		
		AxisAlignedBB scan = new AxisAlignedBB(pos);
		
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, scan);
		
		for (Entity entity : entities) {
			if (entity instanceof EntityLivingBase) {
				entityStatusGUI.setEntity((EntityLivingBase)entity);
			}
		}
	}

}