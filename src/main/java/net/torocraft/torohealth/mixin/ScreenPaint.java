package net.torocraft.torohealth.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.torocraft.torohealth.Handlers;
import net.torocraft.torohealth.display.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ScreenPaint {

  private Hud hud = new Hud();

  @Inject(method = "render", at = @At("RETURN"))
  private void render(float partial, CallbackInfo info) {
    try {
      Handlers.updateSelectedEntity(partial);
      hud.draw();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
