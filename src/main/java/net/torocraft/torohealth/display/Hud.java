package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.ModConfig.AnchorPoint;

public class Hud extends Screen {
  private static final Identifier BACKGROUND_TEXTURE =
      new Identifier(ToroHealth.MODID + ":textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private int age;

  public Hud() {
    super(Text.literal("ToroHealth HUD"));
    this.client = MinecraftClient.getInstance();
    barDisplay = new BarDisplay(MinecraftClient.getInstance(), this);
  }

  public void draw(MatrixStack matrix) {
    if (this.client.options.debugEnabled) {
      return;
    }
    float x = determineX();
    float y = determineY();
    draw(matrix, x, y, ToroHealth.CONFIG.hudOptions.hudScale);
  }

  private float determineX() {
    float x = ToroHealth.CONFIG.hudOptions.hudXPosition;
    AnchorPoint anchor = ToroHealth.CONFIG.hudOptions.anchorPoint;
    float wScreen = client.getWindow().getScaledWidth();

    switch (anchor) {
      case BOTTOM_CENTER:
      case TOP_CENTER:
        return (wScreen / 2) + x;
      case BOTTOM_RIGHT:
      case TOP_RIGHT:
        return (wScreen) + x;
      default:
        return x;
    }
  }

  private float determineY() {
    float y = ToroHealth.CONFIG.hudOptions.hudYPosition;
    AnchorPoint anchor = ToroHealth.CONFIG.hudOptions.anchorPoint;
    float hScreen = client.getWindow().getScaledHeight();

    switch (anchor) {
      case BOTTOM_CENTER:
      case BOTTOM_LEFT:
      case BOTTOM_RIGHT:
        return y + hScreen;
      default:
        return y;
    }
  }


  public void tick() {
    age++;
  }

  public void setEntity(LivingEntity entity) {
    if (entity != null) {
      age = 0;
    }

    if (entity == null && age > ToroHealth.CONFIG.hudOptions.hudHideDelay) {
      setEntityWork(null);
    }

    if (entity != null && entity != this.entity) {
      setEntityWork(entity);
    }
  }

  private void setEntityWork(LivingEntity entity) {
    this.entity = entity;
    entityDisplay.setEntity(entity);
  }

  public LivingEntity getEntity() {
    return entity;
  }

  private void draw(MatrixStack matrix, float x, float y, float scale) {
    if (entity == null) {
      return;
    }
    
    if (ToroHealth.CONFIG.hudOptions.onlyWhenHurt && entity.getHealth() >= entity.getMaxHealth()) {
      return;
    }

    matrix.push();
    matrix.scale(scale, scale, scale);
    matrix.translate(x - 10, y - 10, 0);
    if (ToroHealth.CONFIG.hudOptions.showSkin) {
      this.drawSkin(matrix);
    }
    matrix.translate(10, 10, 0);
    if (ToroHealth.CONFIG.hudOptions.showEntity) {

      entityDisplay.draw(matrix, scale);
    }
    matrix.translate(44, 0, 0);
    if (ToroHealth.CONFIG.hudOptions.showBar) {
      barDisplay.draw(matrix, entity);
    }
    matrix.pop();
  }

  private void drawSkin(MatrixStack matrix) {
    RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    int w = 160, h = 60;
    drawTexture(matrix, 0, 0, 0.0f, 0.0f, w, h, w, h);
  }
}
