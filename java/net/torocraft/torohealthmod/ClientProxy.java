package net.torocraft.torohealthmod;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
		DamageParticle damageIndicator = new DamageParticle(damage, world, entity.posX, entity.posY + entity.height, entity.posZ, motionX, motionY, motionZ);
		Minecraft.getMinecraft().effectRenderer.addEffect(damageIndicator);

	}

	@Override
	public void setEntityInCrosshairs() {
		MovingObjectPosition r = getMouseOver(1.0f);
		if (r != null && MovingObjectPosition.MovingObjectType.ENTITY.equals(r.typeOfHit)) {
			if (r.entityHit instanceof EntityLivingBase) {
				entityStatusGUI.setEntity((EntityLivingBase) r.entityHit);
			}
		}
	}

	public MovingObjectPosition getMouseOver(float partialTicks) {
		Entity observer = this.mc.getRenderViewEntity();

		MovingObjectPosition objectMouseOver = null;

		if (observer == null || this.mc.theWorld == null) {
			return null;
		}

		this.mc.pointedEntity = null;
		double reachDistance = 50d;
		objectMouseOver = observer.rayTrace(reachDistance, partialTicks);
		double d1 = reachDistance;
		Vec3 vec3 = observer.getPositionEyes(partialTicks);
		boolean outOfReach = false;
		int i = 3;

		if (objectMouseOver != null) {
			d1 = objectMouseOver.hitVec.distanceTo(vec3);
		}

		Vec3 vec31 = observer.getLook(partialTicks);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * reachDistance, vec31.yCoord * reachDistance, vec31.zCoord * reachDistance);
		this.pointedEntity = null;
		Vec3 vec33 = null;
		float f = 1.0F;
		List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(observer,
				observer.getEntityBoundingBox().addCoord(vec31.xCoord * reachDistance, vec31.yCoord * reachDistance, vec31.zCoord * reachDistance).expand((double) f, (double) f, (double) f), EntitySelectors.NOT_SPECTATING);
		double d2 = d1;

		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = (Entity) list.get(j);
			float f1 = entity1.getCollisionBorderSize();
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
			MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

			if (axisalignedbb.isVecInside(vec3)) {
				if (d2 >= 0.0D) {
					this.pointedEntity = entity1;
					vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
					d2 = 0.0D;
				}
			} else if (movingobjectposition != null) {
				double d3 = vec3.distanceTo(movingobjectposition.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity1 == observer.ridingEntity && !observer.canRiderInteract()) {
						if (d2 == 0.0D) {
							this.pointedEntity = entity1;
							vec33 = movingobjectposition.hitVec;
						}
					} else {
						this.pointedEntity = entity1;
						vec33 = movingobjectposition.hitVec;
						d2 = d3;
					}
				}
			}
		}

		if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
			objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);

			if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
				this.mc.pointedEntity = this.pointedEntity;
			}
		}

		return objectMouseOver;
	}

	private boolean isSolidBlock(BlockPos pos, BlockPos prevPos) {
		return prevPos.compareTo(pos) == 0;
	}

}