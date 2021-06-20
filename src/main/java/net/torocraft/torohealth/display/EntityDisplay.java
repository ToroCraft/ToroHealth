package net.torocraft.torohealth.display;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.MathHelper;

public class EntityDisplay {

  private static final float RENDER_HEIGHT = 30;
  private static final float RENDER_WIDTH = 18;
  private static final float WIDTH = 40;
  private static final float HEIGHT = WIDTH;

  private LivingEntity entity;
  private int entityScale = 1;

  private float xOffset;
  private float yOffset;

  public void setEntity(LivingEntity entity) {
    this.entity = entity;
    updateScale();
  }

  public void draw(MatrixStack matrix) {
    if (entity != null) {
      InventoryScreen.drawEntity((int) xOffset + 4, (int) yOffset + 3, entityScale, -80, -20,
          entity);
    }
  }

  private void updateScale() {
    if (entity == null) {
      return;
    }

    int scaleY = MathHelper.ceil(RENDER_HEIGHT / entity.getHeight());
    int scaleX = MathHelper.ceil(RENDER_WIDTH / entity.getWidth());
    entityScale = Math.min(scaleX, scaleY);

    if (entity instanceof ChickenEntity) {
      entityScale *= 0.7;
    }

    if (entity instanceof VillagerEntity && entity.isSleeping()) {
      entityScale = entity.isBaby() ? 31 : 16;
    }

    xOffset = WIDTH / 2;

    yOffset = HEIGHT / 2 + RENDER_HEIGHT / 2;
    if (entity instanceof GhastEntity) {
      yOffset -= 10;
    }
  }

}
