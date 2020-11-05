package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config.AnchorPoint;

public class Hud extends Screen {
  private static final Identifier BACKGROUND_TEXTURE =
      new Identifier(ToroHealth.MODID + ":textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private int age;

  public Hud() {
    super(new LiteralText("ToroHealth HUD"));
    this.client = MinecraftClient.getInstance();
    barDisplay = new BarDisplay(MinecraftClient.getInstance(), this);
  }

  public void draw(MatrixStack matrix) {
    float x = determineX();
    float y = determineY();
    draw(matrix, x, y, ToroHealth.CONFIG.hud.scale);
  }

  private float determineX() {
    float x = ToroHealth.CONFIG.hud.x;
    AnchorPoint anchor = ToroHealth.CONFIG.hud.anchorPoint;
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
    float y = ToroHealth.CONFIG.hud.y;
    AnchorPoint anchor = ToroHealth.CONFIG.hud.anchorPoint;
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

    if (entity == null && age > ToroHealth.CONFIG.hud.hideDelay) {
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

    matrix.push();
    matrix.scale(scale, scale, scale);
    matrix.translate(x - 10, y - 10, 0);
    if (ToroHealth.CONFIG.hud.showSkin) {
      this.drawSkin(matrix);
    }
    matrix.translate(10, 10, 0);
    if (ToroHealth.CONFIG.hud.showEntity) {
      entityDisplay.draw(matrix);
    }
    matrix.translate(44, 0, 0);
    if (ToroHealth.CONFIG.hud.showBar) {
      barDisplay.draw(matrix, entity);
    }
    matrix.pop();
  }

  private void drawSkin(MatrixStack matrix) {
    MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    int w = 160, h = 60;
    drawTexture(matrix, 0, 0, 0.0f, 0.0f, w, h, w, h);
  }
}
