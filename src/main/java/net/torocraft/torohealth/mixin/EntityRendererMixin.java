package net.torocraft.torohealth.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.torocraft.torohealth.bars.EntityTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

  @Shadow
  protected EntityRenderDispatcher renderManager;

  private static final double RANGE_SQUARED = 30 * 30;

  @Inject(method = "postRender", at = @At("RETURN"))
  private void renderLabel(Entity entity, double double_1, double double_2, double double_3, float float_1, float float_2, CallbackInfo info) {
    double distSq = entity.squaredDistanceTo(renderManager.camera.getPos());
    if (distSq <= RANGE_SQUARED) {
      EntityTracker.INSTANCE.add(entity);
    }
  }

}
