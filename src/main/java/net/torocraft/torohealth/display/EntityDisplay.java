package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;

public class EntityDisplay extends Screen implements IDisplay {

  private static final int RENDER_HEIGHT = 30;
  private static final int RENDER_WIDTH = 18;
  private static final int PADDING = 2;
  private static final int WIDTH = 40;
  private static final int HEIGHT = WIDTH;

  private LivingEntity entity;
  private int scale = 1;
  private float x;
  private float y;

  public EntityDisplay() {
    super(new LiteralText("Entity Display"));
  }

  @Override
  public void setPosition(int x, int y) {
    this.y = y;
    this.x = x + ((float)WIDTH) / 2;
    updateScale();
  }

  @Override
  public void setEntity(LivingEntity entity) {
    this.entity = entity;
    updateScale();
  }

  @Override
  public void draw() {
    if (entity != null) {
      drawEntity(x, y, scale, -40, -20, entity);
    }
  }

  private void updateScale() {
    if (entity == null) {
      y = (float) y + HEIGHT - PADDING;
      return;
    }

    int scaleY = MathHelper.ceil(RENDER_HEIGHT / entity.getHeight());
    int scaleX = MathHelper.ceil(RENDER_WIDTH / entity.getWidth());
    scale = Math.min(scaleX, scaleY);
    y = (float) y + (int) (((double) HEIGHT / 2) + ((double) RENDER_HEIGHT / 2));
    if (entity instanceof GhastEntity) {
      y -= 10;
    }
  }

  private static void drawEntity(float x, float y, int scale, float yaw, float pitch, LivingEntity entity) {
    GlStateManager.enableColorMaterial();
    GlStateManager.pushMatrix();
    GlStateManager.translatef(x, y, 50.0F);
    GlStateManager.scalef((float) (-scale), (float) scale, (float) scale);
    GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
    float float_3 = entity.field_6283;
    float float_4 = entity.yaw;
    float float_5 = entity.pitch;
    float float_6 = entity.prevHeadYaw;
    float float_7 = entity.headYaw;
    GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
    GuiLighting.enable();
    GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(-((float) Math.atan(pitch / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
    entity.field_6283 = yaw;
    entity.yaw = -4;
    entity.pitch = pitch;
    entity.headYaw = entity.yaw;
    entity.prevHeadYaw = entity.yaw;
    GlStateManager.translatef(0.0F, 0.0F, 0.0F);
    EntityRenderDispatcher entityRenderDispatcher_1 = MinecraftClient.getInstance().getEntityRenderManager();
    entityRenderDispatcher_1.method_3945(180.0F);
    entityRenderDispatcher_1.setRenderShadows(false);
    entityRenderDispatcher_1.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
    entityRenderDispatcher_1.setRenderShadows(true);
    entity.field_6283 = float_3;
    entity.yaw = float_4;
    entity.pitch = float_5;
    entity.prevHeadYaw = float_6;
    entity.headYaw = float_7;
    GlStateManager.popMatrix();
    GuiLighting.disable();
    GlStateManager.disableRescaleNormal();
    GlStateManager.activeTexture(GLX.GL_TEXTURE1);
    GlStateManager.disableTexture();
    GlStateManager.activeTexture(GLX.GL_TEXTURE0);
  }
}