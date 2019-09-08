package net.torocraft.torohealth.util;

import java.util.function.Predicate;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

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
      return (LivingEntity) result.getEntity();
    }

    return null;
  }
}