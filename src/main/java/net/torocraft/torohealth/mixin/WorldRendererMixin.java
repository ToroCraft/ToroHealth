package net.torocraft.torohealth.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.bars.EntityTracker;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

  @Shadow
  private EntityRenderDispatcher entityRenderDispatcher;

  @Inject(method = "renderEntities", at = @At(value = "HEAD"))
  public void renderEntitiesPre(CallbackInfo info) {
    EntityTracker.INSTANCE.reset();
  }

  @Inject(method = "renderEntities", at = @At(value = "RETURN"))
  public void renderEntitiesPost(CallbackInfo info) {
    float cameraYaw = entityRenderDispatcher.cameraYaw;
    float cameraPitch = entityRenderDispatcher.cameraPitch;
    HealthBarRenderer.renderTrackedEntity(cameraYaw, cameraPitch);
  }

}
