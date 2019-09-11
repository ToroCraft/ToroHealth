package net.torocraft.torohealth.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.display.Hud;
import net.torocraft.torohealth.util.RayTrace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
  @Inject(method = "render", at = @At("RETURN"))
  private void render(float partial, CallbackInfo info) {
    ToroHealth.HUD.draw();
  }
}

//@Mixin(InGameHud.class)
//public class InGameHudMixin {
//  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(I)V"))
//  private void render(float partial, CallbackInfo info) {
//    ToroHealth.HUD.draw();
//  }
//}

//this.chatHud.render(this.ticks);

//@Mixin(GameRenderer.class)
//public class InGameHudMixin {
//  @Inject(method = "render", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;draw(F)V")), at = @At(value = "INVOKE", ordinal = 0))
//  private void renderOverlay(float var1, long nanoTime, boolean var4, CallbackInfo callbackInfo) {
//    ToroHealth.HUD.draw();
//  }
//}