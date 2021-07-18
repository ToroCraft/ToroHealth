package net.torocraft.torohealth.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.torocraft.torohealth.ToroHealth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

  @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
  private void renderStatusEffectOverlay(MatrixStack matrixStack, CallbackInfo info) {
    ToroHealth.HUD.draw(matrixStack, ToroHealth.CONFIG);
  }

}
