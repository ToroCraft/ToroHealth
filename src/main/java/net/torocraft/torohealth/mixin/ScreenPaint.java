package net.torocraft.torohealth.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.Handlers;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.display.EntityDisplay;
import net.torocraft.torohealth.hud.Hud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ScreenPaint {

  private EntityDisplay display = new EntityDisplay();
  private LivingEntity prev;

  @Inject(method = "render", at = @At("RETURN"))
  private void render(float partial, CallbackInfo info) {
    try {
      Handlers.updateSelectedEntity(partial);

      if (ToroHealth.selectedEntity != prev) {
        display.setEntity(ToroHealth.selectedEntity);
        prev = ToroHealth.selectedEntity;
      }

      display.setPosition(0, 0);
      display.draw();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
