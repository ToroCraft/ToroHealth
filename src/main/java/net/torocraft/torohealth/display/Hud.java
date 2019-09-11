package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.ToroHealth;

public class Hud extends Screen {
  private static final Identifier BACKGROUND_TEXTURE = new Identifier(ToroHealth.MODID, "textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private int age;

  public Hud() {
    super(new LiteralText("ToroHealth HUD"));
    barDisplay = new BarDisplay(MinecraftClient.getInstance(), this);
  }

  public void draw() {
    draw(ToroHealth.CONFIG.hud.x, ToroHealth.CONFIG.hud.y, ToroHealth.CONFIG.hud.scale);
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

    GlStateManager.pushMatrix();
    GlStateManager.scalef(scale, scale, scale);
    GlStateManager.translatef(x - 10, y - 10, 0);

    drawSkin();
    entityDisplay.draw();
    GlStateManager.translatef(44, 0, 0);
    barDisplay.draw(entity);

    GlStateManager.popMatrix();
  }

  private void drawSkin() {
    MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    int w = 160;
    int h = 60;

    blit(0, 0, 0.0f, 0.0f, w, h, w, h);
    GlStateManager.translatef(10, 10, 0);
  }

}
