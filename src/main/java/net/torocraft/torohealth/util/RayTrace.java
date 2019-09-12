package net.torocraft.torohealth.util;

import java.util.function.Predicate;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.RayTraceContext.FluidHandling;
import net.minecraft.world.World;

public class RayTrace {
  private static Predicate<Entity> isVisible = entity -> !entity.isSpectator() && entity.collides();

  public static LivingEntity getEntityInCrosshair (float partialTicks, double reachDistance) {
    MinecraftClient client = MinecraftClient.getInstance();

    if (client == null) {
      return null;
    }

    Entity viewer = client.getCameraEntity();

    if (viewer == null) {
      return null;
    }

    Vec3d position = viewer.getCameraPosVec(partialTicks);
    Vec3d look = viewer.getRotationVec(1.0F);
    Vec3d max = position.add(look.x * reachDistance, look.y * reachDistance, look.z * reachDistance);
    Box searchBox = viewer.getBoundingBox().stretch(look.multiply(reachDistance)).expand(1.0D, 1.0D, 1.0D);

    EntityHitResult result = ProjectileUtil.rayTrace(viewer, position, max, searchBox, isVisible, reachDistance * reachDistance);

    if (result == null || result.getEntity() == null) {
      return null;
    }

    if (result.getEntity() instanceof LivingEntity) {
      LivingEntity target = (LivingEntity) result.getEntity();
      HitResult blockHit = rayTraceBlock(target.world, client.player, reachDistance, FluidHandling.NONE);

      if (!blockHit.getType().equals(Type.MISS)) {
        double blockDistance = blockHit.getPos().distanceTo(position);
        if (blockDistance > target.distanceTo(client.player)) {
          return target;
        }
      } else {
        return target;
      }
    }

    return null;
  }

  protected static HitResult rayTraceBlock(World world, PlayerEntity player, double distance, RayTraceContext.FluidHandling fluidHandling) {
    float pitch = player.pitch;
    float yaw = player.yaw;
    Vec3d fromPos = player.getCameraPosVec(1.0F);
    float float_3 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float float_4 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    float float_5 = -MathHelper.cos(-pitch * 0.017453292F);
    float xComponent = float_4 * float_5;
    float yComponent = MathHelper.sin(-pitch * 0.017453292F);
    float zComponent = float_3 * float_5;
    Vec3d toPos = fromPos.add((double)xComponent * distance, (double)yComponent * distance, (double)zComponent * distance);
    return world.rayTrace(new RayTraceContext(fromPos, toPos, RayTraceContext.ShapeType.OUTLINE, fluidHandling, player));
  }

}