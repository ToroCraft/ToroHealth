package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.torocraft.torohealth.Handlers;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerTick {

  @Inject(method = "tick()V", at = @At("HEAD"))
  private void render(CallbackInfo info) {
    try {
      //Handlers.updateSelectedEntity();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
