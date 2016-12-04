package net.torocraft.torohealthmod;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
			if (r.entityHit instanceof EntityLivingBase) {
				entityStatusGUI.setEntity((EntityLivingBase) r.entityHit);
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
				observer.getEntityBoundingBox().addCoord(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance).expand(1.0D, 1.0D, 1.0D),
				Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
					public boolean apply(@Nullable Entity p_apply_1_) {
						return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
					}
				}));
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

	public RayTraceResult rayTraceEntities(World world, Vec3d fromVector, Vec3d toVector) {
		boolean stopOnLiquid = false;
		boolean ignoreBlockWithoutBoundingBox = false;
		boolean returnLastUncollidableBlock = false;

		if (Double.isNaN(fromVector.xCoord) || Double.isNaN(fromVector.yCoord) || Double.isNaN(fromVector.zCoord)) {
			return null;
		}

		if (Double.isNaN(toVector.xCoord) || Double.isNaN(toVector.yCoord) || Double.isNaN(toVector.zCoord)) {
			return null;
		}

		int i = MathHelper.floor_double(toVector.xCoord);
		int j = MathHelper.floor_double(toVector.yCoord);
		int k = MathHelper.floor_double(toVector.zCoord);
		int xPos = MathHelper.floor_double(fromVector.xCoord);
		int yPos = MathHelper.floor_double(fromVector.yCoord);
		int zPos = MathHelper.floor_double(fromVector.zCoord);

		BlockPos blockpos = new BlockPos(xPos, yPos, zPos);
		IBlockState iblockstate = world.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
			RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, fromVector, toVector);

			if (raytraceresult != null) {
				return raytraceresult;
			}
		}

		RayTraceResult raytraceresult2 = null;
		int maxDistance = 200;

		while (maxDistance-- >= 0) {
			if (Double.isNaN(fromVector.xCoord) || Double.isNaN(fromVector.yCoord) || Double.isNaN(fromVector.zCoord)) {
				return null;
			}

			if (xPos == i && yPos == j && zPos == k) {
				return returnLastUncollidableBlock ? raytraceresult2 : null;
			}

			boolean flag2 = true;
			boolean flag = true;
			boolean flag1 = true;
			double d0 = 999.0D;
			double d1 = 999.0D;
			double d2 = 999.0D;

			if (i > xPos) {
				d0 = (double) xPos + 1.0D;
			} else if (i < xPos) {
				d0 = (double) xPos + 0.0D;
			} else {
				flag2 = false;
			}

			if (j > yPos) {
				d1 = (double) yPos + 1.0D;
			} else if (j < yPos) {
				d1 = (double) yPos + 0.0D;
			} else {
				flag = false;
			}

			if (k > zPos) {
				d2 = (double) zPos + 1.0D;
			} else if (k < zPos) {
				d2 = (double) zPos + 0.0D;
			} else {
				flag1 = false;
			}

			double d3 = 999.0D;
			double d4 = 999.0D;
			double d5 = 999.0D;
			double d6 = toVector.xCoord - fromVector.xCoord;
			double d7 = toVector.yCoord - fromVector.yCoord;
			double d8 = toVector.zCoord - fromVector.zCoord;

			if (flag2) {
				d3 = (d0 - fromVector.xCoord) / d6;
			}

			if (flag) {
				d4 = (d1 - fromVector.yCoord) / d7;
			}

			if (flag1) {
				d5 = (d2 - fromVector.zCoord) / d8;
			}

			if (d3 == -0.0D) {
				d3 = -1.0E-4D;
			}

			if (d4 == -0.0D) {
				d4 = -1.0E-4D;
			}

			if (d5 == -0.0D) {
				d5 = -1.0E-4D;
			}

			EnumFacing enumfacing;

			if (d3 < d4 && d3 < d5) {
				enumfacing = i > xPos ? EnumFacing.WEST : EnumFacing.EAST;
				fromVector = new Vec3d(d0, fromVector.yCoord + d7 * d3, fromVector.zCoord + d8 * d3);
			} else if (d4 < d5) {
				enumfacing = j > yPos ? EnumFacing.DOWN : EnumFacing.UP;
				fromVector = new Vec3d(fromVector.xCoord + d6 * d4, d1, fromVector.zCoord + d8 * d4);
			} else {
				enumfacing = k > zPos ? EnumFacing.NORTH : EnumFacing.SOUTH;
				fromVector = new Vec3d(fromVector.xCoord + d6 * d5, fromVector.yCoord + d7 * d5, d2);
			}

			xPos = MathHelper.floor_double(fromVector.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
			yPos = MathHelper.floor_double(fromVector.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
			zPos = MathHelper.floor_double(fromVector.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
			blockpos = new BlockPos(xPos, yPos, zPos);
			IBlockState iblockstate1 = world.getBlockState(blockpos);
			Block block1 = iblockstate1.getBlock();

			if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) {
				if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
					RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, fromVector, toVector);

					if (raytraceresult1 != null) {
						return raytraceresult1;
					}
				} else {
					raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, fromVector, enumfacing, blockpos);
				}
			}
		}

		return returnLastUncollidableBlock ? raytraceresult2 : null;

	}

	private EntityLivingBase old(BlockPos prevPos, BlockPos pos, EntityPlayer player, World world) {

		if (prevPos != null && isSolidBlock(pos, prevPos)) {
			return null;
		}

		prevPos = pos;

		AxisAlignedBB scan = new AxisAlignedBB(pos);

		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, scan);

		for (Entity entity : entities) {
			if (entity instanceof EntityLivingBase) {
				return (EntityLivingBase) entity;
			}
		}
		return null;
	}

	private boolean isSolidBlock(BlockPos pos, BlockPos prevPos) {
		return prevPos.compareTo(pos) == 0;
	}

}