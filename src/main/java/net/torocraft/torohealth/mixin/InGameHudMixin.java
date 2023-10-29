package net.torocraft.torohealth.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.torocraft.torohealth.ToroHealth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

  @Inject(method = "render", at = @At("RETURN"))
  private void render(DrawContext drawContext, float partial, CallbackInfo info) {
    ToroHealth.HUD.draw(drawContext, ToroHealth.CONFIG);
  }

}
