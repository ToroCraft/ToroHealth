package net.torocraft.torohealth.util;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.EntityTracker;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.util.Config.InWorld;
import net.torocraft.torohealth.util.Config.Mode;

public class InWorldEntityBars {

  private float distanceSq = 0;

  private static InWorld getConfig() {
    return ToroHealth.CONFIG.inWorld;
  }

  public void start() {
    EntityTracker.INSTANCE.reset();
    if (Mode.NONE.equals(getConfig().mode)) return;
    distanceSq = (float) Math.pow(ToroHealth.CONFIG.inWorld.distance, 2);
  }

  public void addEntity(EntityRenderDispatcher renderManager, Entity entity, double x, double y, double z) {
    if (Mode.NONE.equals(getConfig().mode)) return;

    if (Mode.WHEN_HOLDING_WEAPON.equals(getConfig().mode) && !ToroHealth.IS_HOLDING_WEAPON) {
      return;
    }

    double entityDistSq = entity.squaredDistanceTo(renderManager.camera.getPos());
    if (entityDistSq <= distanceSq) {
      EntityTracker.INSTANCE.add(entity, x, y, z);
    }
  }

  public void render(EntityRenderDispatcher entityRenderDispatcher) {
    if (Mode.NONE.equals(getConfig().mode)) return;

    float cameraYaw = entityRenderDispatcher.cameraYaw;
    float cameraPitch = entityRenderDispatcher.cameraPitch;
    HealthBarRenderer.renderTrackedEntity(cameraYaw, cameraPitch);
  }

}
