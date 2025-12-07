package net.torocraft.torohealth.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;


public class RayTrace {
    // modified from minecraft.client.render.GameRender.updateTargetedEntity
    public LivingEntity getEntityInCrosshair(float tickDelta, float reachDistance) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity2 = client.getCameraEntity();
        if (entity2 == null) {
            return null;
        }
        if (client.world == null) {
            return null;
        }
        HitResult crosshairTarget = this.raycastEntity(entity2, reachDistance, tickDelta, false);
        Vec3d vec3d = entity2.getCameraPosVec(tickDelta);
        double e = reachDistance;

        e *= e;
        if (crosshairTarget != null) {
            e = crosshairTarget.getPos().squaredDistanceTo(vec3d);
        }
        Vec3d vec3d2 = entity2.getRotationVec(1.0f);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * reachDistance, vec3d2.y * reachDistance, vec3d2.z * reachDistance);
        Box box = entity2.getBoundingBox().stretch(vec3d2.multiply(reachDistance)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity2, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.collides(), e);
        if (entityHitResult != null) {
            Entity entity22 = entityHitResult.getEntity();
            Vec3d vec3d4 = entityHitResult.getPos();
            double g = vec3d.squaredDistanceTo(vec3d4);
            if (g < e || crosshairTarget == null) {
                if (entity22 instanceof LivingEntity) {
                    return (LivingEntity) entity22;
                }
            }
        }
        return null;
    }

    // modified from net.minecraft.entity.Entity.raycast, adding ignore opaque blocks feature
    private BlockHitResult raycastEntity(Entity entity, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = entity.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = entity.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return raycastBlockView(entity.world, new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, entity));
    }



    private BlockHitResult raycastBlockView(World world, RaycastContext context) {
        return BlockView.raycast(context.getStart(), context.getEnd(), context, (c, pos) -> {
            BlockState block = world.getBlockState(pos);
            if (!block.isOpaque()) {
                return null;
            }
            VoxelShape blockShape = c.getBlockShape(block, world, pos);
            return world.raycastBlock(c.getStart(), c.getEnd(), pos, blockShape, block);
        }, (c) -> {
            Vec3d v = c.getStart().subtract(c.getEnd());
            return BlockHitResult.createMissed(c.getEnd(), Direction.getFacing(v.x, v.y, v.z), new BlockPos(c.getEnd()));
        });
    }
}