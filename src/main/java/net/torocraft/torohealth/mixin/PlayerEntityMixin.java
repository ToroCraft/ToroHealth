package net.torocraft.torohealth.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SwordItem;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.BarState;
import net.torocraft.torohealth.util.Config.Mode;
import net.torocraft.torohealth.util.HoldingWeaponUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

  @Inject(method = "tick()V", at = @At("HEAD"))
  private void render(CallbackInfo info) {
    BarState.updateState();
    HoldingWeaponUpdater.update();
    ToroHealth.HUD.tick();
  }

}
