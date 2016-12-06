package net.torocraft.torohealthmod;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
	private Minecraft mc = Minecraft.getMinecraft();
	private Entity pointedEntity;

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
		RayTraceResult r = getMouseOver(1.0f);
		if (r != null && RayTraceResult.Type.ENTITY.equals(r.typeOfHit)) {
			if (r.entityHit instanceof EntityLiving) {
				entityStatusGUI.setEntity((EntityLiving) r.entityHit);
			}
		}
	}

	public RayTraceResult getMouseOver(float partialTicks) {
		RayTraceResult objectMouseOver = null;
		Entity observer = this.mc.getRenderViewEntity();

		if (observer == null || this.mc.theWorld == null) {
			return objectMouseOver;
		}

		this.mc.pointedEntity = null;
		double reachDistance = 50;
		objectMouseOver = observer.rayTrace(reachDistance, partialTicks);
		Vec3d observerPositionEyes = observer.getPositionEyes(partialTicks);

		double distance = reachDistance;

		if (objectMouseOver != null) {
			distance = objectMouseOver.hitVec.distanceTo(observerPositionEyes);
		}

		Vec3d lookVector = observer.getLook(partialTicks);
		Vec3d lookVectorFromEyePosition = observerPositionEyes.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance);
		this.pointedEntity = null;
		Vec3d hitVector = null;
		float f = 1.0F;
		List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(observer,
				observer.getEntityBoundingBox().addCoord(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance).expand(1.0D, 1.0D, 1.0D), EntitySelectors.NOT_SPECTATING);
		double d2 = distance;

		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = (Entity) list.get(j);
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz((double) entity1.getCollisionBorderSize());
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(observerPositionEyes, lookVectorFromEyePosition);

			if (axisalignedbb.isVecInside(observerPositionEyes)) {
				if (d2 >= 0.0D) {
					this.pointedEntity = entity1;
					hitVector = raytraceresult == null ? observerPositionEyes : raytraceresult.hitVec;
					d2 = 0.0D;
				}
			} else if (raytraceresult != null) {
				double d3 = observerPositionEyes.distanceTo(raytraceresult.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity1.getLowestRidingEntity() == observer.getLowestRidingEntity() && !observer.canRiderInteract()) {
						if (d2 == 0.0D) {
							this.pointedEntity = entity1;
							hitVector = raytraceresult.hitVec;
						}
					} else {
						this.pointedEntity = entity1;
						hitVector = raytraceresult.hitVec;
						d2 = d3;
					}
				}
			}
		}

		objectMouseOver = new RayTraceResult(this.pointedEntity, hitVector);

		if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
			this.mc.pointedEntity = this.pointedEntity;
		}

		return objectMouseOver;
	}

	private boolean isSolidBlock(BlockPos pos, BlockPos prevPos) {
		return prevPos.compareTo(pos) == 0;
	}

}