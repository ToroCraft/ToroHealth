package net.torocraft.torohealth.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.torocraft.torohealth.bars.BarState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTick {

  @Inject(method = "tick()V", at = @At("HEAD"))
  private void render(CallbackInfo info) {
    try {
      BarState.updateState();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
