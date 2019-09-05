package net.torocraft.torohealth.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.torocraft.torohealth.bars.EntityTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

  @Inject(method = "renderEntities", at = @At(value = "HEAD"))
  public void renderEntitiesPre(CallbackInfo info) {
    EntityTracker.INSTANCE.reset();
  }

  @Inject(method = "renderEntities", at = @At(value = "RETURN"))
  public void renderEntitiesPost(CallbackInfo info) {
    System.out.println("done rendering entities, found " + EntityTracker.INSTANCE.size());
  }

}
