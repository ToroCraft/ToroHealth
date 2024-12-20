package net.torocraft.torohealth.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

  // @Shadow @Final private BufferBuilderStorage bufferBuilders;

  @Inject(method = "renderEntity", at = @At(value = "RETURN"))
  private void renderEntity(Entity entity, double x, double y, double z, float g,
      MatrixStack matrix, VertexConsumerProvider v, CallbackInfo info) {
    if (entity instanceof LivingEntity) {
      HealthBarRenderer.prepareRenderInWorld((LivingEntity) entity);
    }
  }

  /* Replaced by WorldRenderEvents.LAST event listener registered in ToroHealth.java
  @Inject(method = "render", at = @At(value = "RETURN"))
  private void render(MatrixStack matrices, float tickDelta, long limitTime,
      boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
      LightmapTextureManager lightmapTextureManager, Matrix4f matrix, CallbackInfo info) {
    HealthBarRenderer.renderInWorld(matrices, this.bufferBuilders.getEntityVertexConsumers(), camera);
    ParticleRenderer.renderParticles(matrices, this.bufferBuilders.getEntityVertexConsumers(), camera);
  } */

}
