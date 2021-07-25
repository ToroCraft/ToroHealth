package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

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

  public void draw(MatrixStack matrix, float scale) {
    if (entity != null) {
      drawEntity(matrix, (int) xOffset, (int) yOffset, entityScale, -80, -20, entity, scale);
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

  /**
   * copied from InventoryScreen.drawEntity() to expose the matrixStack
   */
  public static void drawEntity(MatrixStack matrixStack2, int x, int y, int size, float mouseX,
      float mouseY, LivingEntity entity, float scale) {
    float f = (float) Math.atan((double) (mouseX / 40.0F));
    float g = (float) Math.atan((double) (mouseY / 40.0F));
    MatrixStack matrixStack = RenderSystem.getModelViewStack();
    matrixStack.push();
    matrixStack.translate((double) x * scale, (double) y * scale, 1050.0D * scale);
    matrixStack.scale(1.0F, 1.0F, -1.0F);
    RenderSystem.applyModelViewMatrix();
    matrixStack2.push();
    matrixStack2.translate(0.0D, 0.0D, 1000.0D);
    matrixStack2.scale((float) size, (float) size, (float) size);
    Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
    Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0F);
    quaternion.hamiltonProduct(quaternion2);
    matrixStack2.multiply(quaternion);
    float h = entity.bodyYaw;
    float i = entity.getYaw();
    float j = entity.getPitch();
    float k = entity.prevHeadYaw;
    float l = entity.headYaw;
    entity.bodyYaw = 180.0F + f * 20.0F;
    entity.setYaw(180.0F + f * 40.0F);
    entity.setPitch(-g * 20.0F);
    entity.headYaw = entity.getYaw();
    entity.prevHeadYaw = entity.getYaw();
    DiffuseLighting.method_34742();
    EntityRenderDispatcher entityRenderDispatcher =
        MinecraftClient.getInstance().getEntityRenderDispatcher();
    quaternion2.conjugate();
    entityRenderDispatcher.setRotation(quaternion2);
    entityRenderDispatcher.setRenderShadows(false);
    VertexConsumerProvider.Immediate immediate =
        MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
    RenderSystem.runAsFancy(() -> {
      entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack2, immediate,
          15728880);
    });
    immediate.draw();
    entityRenderDispatcher.setRenderShadows(true);
    entity.bodyYaw = h;
    entity.setYaw(i);
    entity.setPitch(j);
    entity.prevHeadYaw = k;
    entity.headYaw = l;
    matrixStack.pop();
    matrixStack2.pop();
    RenderSystem.applyModelViewMatrix();
    DiffuseLighting.enableGuiDepthLighting();
  }

}
