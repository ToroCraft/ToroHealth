package net.torocraft.torohealth.display;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;

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
      drawEntity(matrix, (int)xOffset, (int)yOffset, entityScale, -40, -20, entity);
    }
  }

  private void updateScale() {
    if (entity == null) {
      return;
    }

    int scaleY = MathHelper.ceil(RENDER_HEIGHT / entity.getHeight());
    int scaleX = MathHelper.ceil(RENDER_WIDTH / entity.getWidth());
    entityScale = Math.min(scaleX, scaleY);

    xOffset = WIDTH / 2;

    yOffset = HEIGHT / 2 + RENDER_HEIGHT / 2;
    if (entity instanceof GhastEntity) {
      yOffset -= 10;
    }
  }

  public static void drawEntity(MatrixStack matrixStack, int x, int y, int scale, float yaw, float pitch, LivingEntity entity) {
    float h = (float)Math.atan((double)(yaw / 40.0F));
    float l = (float)Math.atan((double)(pitch / 40.0F));
    matrixStack.push();
    matrixStack.translate((float)x, (float)y, 1050.0F);
    matrixStack.scale(1.0F, 1.0F, -1.0F);
    matrixStack.push();
    matrixStack.translate(0.0D, 0.0D, 1000.0D);
    matrixStack.scale((float)scale, (float)scale, (float)scale);
    Quaternion quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
    Quaternion quaternion2 = Vector3f.POSITIVE_X.getDegreesQuaternion(l * 20.0F);
    quaternion.hamiltonProduct(quaternion2);
    matrixStack.multiply(quaternion);
    float m = entity.bodyYaw;
    float n = entity.yaw;
    float o = entity.pitch;
    float p = entity.prevHeadYaw;
    float q = entity.headYaw;
    entity.bodyYaw = 180.0F + h * 20.0F;
    entity.yaw = 180.0F + h * 40.0F;
    entity.pitch = -l * 20.0F;
    entity.headYaw = entity.yaw;
    entity.prevHeadYaw = entity.yaw;
    EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderManager();
    quaternion2.conjugate();
    entityRenderDispatcher.setRotation(quaternion2);
    entityRenderDispatcher.setRenderShadows(false);
    VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
    entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, immediate, 15728880);
    immediate.draw();
    matrixStack.pop();
    matrixStack.pop();
    entityRenderDispatcher.setRenderShadows(true);
    entity.bodyYaw = m;
    entity.yaw = n;
    entity.pitch = o;
    entity.prevHeadYaw = p;
    entity.headYaw = q;
 }
}