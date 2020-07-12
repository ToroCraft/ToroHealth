package net.torocraft.torohealth.display;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.torocraft.torohealth.ToroHealth;

public class Hud extends Screen {
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ToroHealth.MODID + ":textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private int age;

  public Hud() {
    super(new StringTextComponent("ToroHealth HUD"));
    barDisplay = new BarDisplay(Minecraft.getInstance(), this);
  }

  public void draw(MatrixStack matrix) {
    draw(matrix, ToroHealth.CONFIG.hud.x.get().floatValue(), ToroHealth.CONFIG.hud.y.get().floatValue(), ToroHealth.CONFIG.hud.scale.get().floatValue());
  }

  public void tick() {
    age++;
  }

  public void setEntity(LivingEntity entity) {
    if (entity != null) {
      age = 0;
    }

    if (entity == null && age > ToroHealth.CONFIG.hud.hideDelay.get()) {
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
    this.drawSkin(matrix);
    matrix.translate(10, 10, 0);
    entityDisplay.draw(matrix);
    matrix.translate(44, 0, 0);
    barDisplay.draw(matrix, entity);
    matrix.pop();
  }

  public void drawTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
    func_238463_a_(matrices, x, y, u, v, width, height, textureWidth, textureHeight);
  }

  private void drawSkin(MatrixStack matrix) {
    Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    int w = 160, h = 60;
    drawTexture(matrix, 0, 0, 0.0f, 0.0f, w, h, w, h);
  }
}
