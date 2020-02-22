package net.torocraft.torohealth.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.torocraft.torohealth.ToroHealth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererMixin {
  @Shadow
  public Camera camera;

  @Inject(method = "render", at = @At("RETURN"))
  private void postRender(Entity entity, double x, double y, double z, float float_1, float float_2, MatrixStack m, VertexConsumerProvider v, int i, CallbackInfo info) {
    ToroHealth.IN_WORLD_BARS.addEntity(camera, entity, x, y, z);
  }
}
