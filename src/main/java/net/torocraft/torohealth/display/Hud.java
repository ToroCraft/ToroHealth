package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.AnchorPoint;

public class Hud extends Screen {
  private static final Identifier BACKGROUND_TEXTURE =
      new Identifier(ToroHealth.MODID + ":textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private Config config = new Config();
  private int age;

  public Hud() {
    super(Text.literal("ToroHealth HUD"));
    this.client = MinecraftClient.getInstance();
    barDisplay = new BarDisplay(MinecraftClient.getInstance());
  }

  public void draw(DrawContext drawContext, Config config) {
    if (this.client.getDebugHud().shouldShowDebugHud()) {
      return;
    }
    this.config = config;
    if (this.config == null) {
      this.config = new Config();
    }
    float x = determineX();
    float y = determineY();
    draw(drawContext, x, y, config.hud.scale);
  }

  private float determineX() {
    float x = config.hud.x;
    AnchorPoint anchor = config.hud.anchorPoint;
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
    float y = config.hud.y;
    AnchorPoint anchor = config.hud.anchorPoint;
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

    if (entity == null && age > config.hud.hideDelay) {
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

  private void draw(DrawContext drawContext, float x, float y, float scale) {
    if (entity == null) {
      return;
    }

    if (config.hud.onlyWhenHurt && entity.getHealth() >= entity.getMaxHealth()) {
      return;
    }

    MatrixStack matrix = drawContext.getMatrices();
    matrix.push();
    matrix.scale(scale, scale, scale);
    matrix.translate(x - 10, y - 10, 0);
    if (config.hud.showSkin) {
      this.drawSkin(drawContext);
    }
    matrix.translate(10, 10, 0);
    if (config.hud.showEntity) {
      entityDisplay.draw(matrix, scale);
    }
    matrix.translate(44, 0, 0);
    if (config.hud.showBar) {
      barDisplay.draw(drawContext, entity);
    }
    matrix.pop();
  }

  private void drawSkin(DrawContext drawContext) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    int w = 160, h = 60;
    drawContext.drawTexture(BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, w, h, w, h);
  }
}
