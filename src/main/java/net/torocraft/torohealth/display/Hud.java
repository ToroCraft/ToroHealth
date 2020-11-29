package net.torocraft.torohealth.display;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config.AnchorPoint;

public class Hud extends Screen {
  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(ToroHealth.MODID + ":textures/gui/default_skin_basic.png");

  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private int age;

  public Hud() {
    super(new StringTextComponent("ToroHealth HUD"));
    this.minecraft = Minecraft.getInstance();
    barDisplay = new BarDisplay(Minecraft.getInstance(), this);
  }

  public void draw() {
    float x = determineX();
    float y = determineY();
    draw(x, y, ToroHealth.CONFIG.hud.scale);
  }

  private float determineX() {
    float x = ToroHealth.CONFIG.hud.x;
    AnchorPoint anchor = ToroHealth.CONFIG.hud.anchorPoint;
    float wScreen = minecraft.getMainWindow().getScaledWidth();

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
    float hScreen = minecraft.getMainWindow().getScaledHeight();

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

  private void draw(float x, float y, float scale) {
    if (entity == null) {
      return;
    }

    RenderSystem.pushMatrix();
    RenderSystem.scalef(scale, scale, scale);
    RenderSystem.translatef(x - 10, y - 10, 0);
    if (ToroHealth.CONFIG.hud.showSkin) {
      this.drawSkin();
    }
    RenderSystem.translatef(10, 10, 0);
    if (ToroHealth.CONFIG.hud.showEntity) {
      entityDisplay.draw();
    }
    RenderSystem.translatef(44, 0, 0);
    if (ToroHealth.CONFIG.hud.showBar) {
      barDisplay.draw(entity);
    }
    RenderSystem.popMatrix();
  }

  public void drawTexture(int x, int y, float u, float v, int width,
      int height, int textureWidth, int textureHeight) {
    blit(x, y, u, v, width, height, textureWidth, textureHeight);
  }

  private void drawSkin() {
    Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    int w = 160, h = 60;
    drawTexture(0, 0, 0.0f, 0.0f, w, h, w, h);
  }
}
