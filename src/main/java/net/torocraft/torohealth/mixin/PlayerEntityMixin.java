package net.torocraft.torohealth.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.BarState;
import net.torocraft.torohealth.util.HoldingWeaponUpdater;
import net.torocraft.torohealth.util.RayTrace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

  @Inject(method = "tick()V", at = @At("HEAD"))
  private void render(CallbackInfo info) {
    ToroHealth.HUD.setEntity(RayTrace.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
    BarState.tick();
    HoldingWeaponUpdater.update();
    ToroHealth.HUD.tick();
  }

}
