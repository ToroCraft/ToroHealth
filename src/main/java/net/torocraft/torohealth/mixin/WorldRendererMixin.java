package net.torocraft.torohealth.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.torocraft.torohealth.ToroHealth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

  @Shadow
  private EntityRenderDispatcher entityRenderDispatcher;

  @Inject(method = "renderEntities", at = @At(value = "HEAD"))
  public void renderEntitiesPre(CallbackInfo info) {
    ToroHealth.IN_WORLD_BARS.start();
  }

  @Inject(method = "renderEntities", at = @At(value = "RETURN"))
  public void renderEntitiesPost(CallbackInfo info) {
    ToroHealth.IN_WORLD_BARS.render(entityRenderDispatcher);
  }

}
