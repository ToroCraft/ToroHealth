package net.torocraft.torohealth.display;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

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

  public void draw() {
    if (entity != null) {
      drawEntity((int)xOffset, (int)yOffset, entityScale, -40, -20, entity);
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

  public static void drawEntity(int x, int y, int scale, float yaw, float pitch, LivingEntity entity) {
    float h = (float)Math.atan((double)(yaw / 40.0F));
    float l = (float)Math.atan((double)(pitch / 40.0F));
    RenderSystem.pushMatrix();

    RenderSystem.translatef((float)x, (float)y, 1050.0F);
    RenderSystem.scalef(1.0F, 1.0F, -1.0F);

    MatrixStack matrixStack = new MatrixStack();

    matrixStack.translate(0.0D, 0.0D, 1000.0D);
    matrixStack.scale((float)scale, (float)scale, (float)scale);
    Quaternion quaternion =  Vector3f.ZP.rotationDegrees(180.0F);
    Quaternion quaternion2 = Vector3f.XP.rotationDegrees(l * 20.0F);
    quaternion.multiply(quaternion2);
    matrixStack.rotate(quaternion);
    float m = entity.renderYawOffset;  //bodyYaw;
    float n = entity.rotationYaw;  //yaw;
    float o = entity.rotationPitch;  //pitch;
    float p = entity.prevRotationYawHead;  //prevHeadYaw;
    float q = entity.rotationYawHead;  //headYaw;
    entity.renderYawOffset = 180.0F + h * 20.0F;
    entity.rotationYaw = 180.0F + h * 40.0F;
    entity.rotationPitch = -l * 20.0F;
    entity.rotationYawHead = entity.rotationYaw;
    entity.prevRotationYawHead = entity.rotationYaw;
    EntityRendererManager entityRenderDispatcher = Minecraft.getInstance().getRenderManager();
    quaternion2.conjugate();
    entityRenderDispatcher.setCameraOrientation(quaternion2);
    entityRenderDispatcher.setRenderShadow(false);
    //VertexConsumerProvider.Immediate immediate = Minecraft.getInstance().getBufferBuilders().getEntityVertexConsumers();

    IRenderTypeBuffer.Impl immediate = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

    entityRenderDispatcher.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, immediate, 15728880);
    immediate.finish();
    RenderSystem.popMatrix();
    entityRenderDispatcher.setRenderShadow(true);
    entity.renderYawOffset = m;
    entity.rotationYaw = n;
    entity.rotationPitch = o;
    entity.prevRotationYawHead = p;
    entity.rotationYawHead = q;
 }
}