package net.torocraft.torohealth.util;

import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

public class RayTrace implements IBlockReader {
  private static Predicate<Entity> isVisible =
      entity -> !entity.isSpectator() && entity.canBeCollidedWith();
  private static Minecraft minecraft = Minecraft.getInstance();

  @Override
  public TileEntity getTileEntity(BlockPos pos) {
    return minecraft.world.getTileEntity(pos);
  }

  @Override
  public BlockState getBlockState(BlockPos pos) {
    return minecraft.world.getBlockState(pos);
  }

  @Override
  public FluidState getFluidState(BlockPos pos) {
    return minecraft.world.getFluidState(pos);
  }

  public LivingEntity getEntityInCrosshair(float partialTicks, double reachDistance) {
    Minecraft client = Minecraft.getInstance();
    Entity viewer = client.getRenderViewEntity();

    if (viewer == null) {
      return null;
    }

    Vector3d position = viewer.getEyePosition(partialTicks);
    Vector3d look = viewer.getLook(1.0F);
    Vector3d max =
        position.add(look.x * reachDistance, look.y * reachDistance, look.z * reachDistance);
    AxisAlignedBB searchBox =
        viewer.getBoundingBox().expand(look.scale(reachDistance)).expand(1.0D, 1.0D, 1.0D);

    EntityRayTraceResult result = ProjectileHelper.rayTraceEntities(viewer, position, max,
        searchBox, isVisible, reachDistance * reachDistance);

    if (result == null || result.getEntity() == null) {
      return null;
    }

    if (result.getEntity() instanceof LivingEntity) {
      LivingEntity target = (LivingEntity) result.getEntity();

      BlockRayTraceResult blockHit = rayTraceBlocks(
          setupRayTraceContext(client.player, reachDistance, RayTraceContext.FluidMode.NONE));

      if (!blockHit.getType().equals(RayTraceResult.Type.MISS)) {
        double blockDistance = blockHit.getHitVec().distanceTo(position);
        if (blockDistance > target.getDistance(client.player)) {
          return target;
        }
      } else {
        return target;
      }
    }

    return null;
  }

  private RayTraceContext setupRayTraceContext(PlayerEntity player, double distance,
      RayTraceContext.FluidMode fluidHandling) {
    float pitch = player.rotationPitch;
    float yaw = player.rotationYaw;
    Vector3d fromPos = player.getEyePosition(1.0F);
    float float_3 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float float_4 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    float float_5 = -MathHelper.cos(-pitch * 0.017453292F);
    float xComponent = float_4 * float_5;
    float yComponent = MathHelper.sin(-pitch * 0.017453292F);
    float zComponent = float_3 * float_5;
    Vector3d toPos = fromPos.add((double) xComponent * distance, (double) yComponent * distance,
        (double) zComponent * distance);
    return new RayTraceContext(fromPos, toPos, RayTraceContext.BlockMode.OUTLINE, fluidHandling,
        player);
  }

  @Override
  public BlockRayTraceResult rayTraceBlocks(RayTraceContext context) {
    return IBlockReader.doRayTrace(context, (c, pos) -> {
      BlockState block = this.getBlockState(pos);
      if (!block.isSolid()) {
        return null;
      }
      VoxelShape blockShape = c.getBlockShape(block, this, pos);
      return this.rayTraceBlocks(c.getStartVec(), c.getEndVec(), pos, blockShape, block);

    }, (c) -> {
      Vector3d v = c.getStartVec().subtract(c.getEndVec());
      return BlockRayTraceResult.createMiss(c.getEndVec(),
          Direction.getFacingFromVector(v.x, v.y, v.z), new BlockPos(c.getEndVec()));
    });
  }
}
