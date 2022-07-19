package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;
import org.lwjgl.opengl.GL11;

public class ParticleRenderer {

  public static void renderParticles(PoseStack matrix, Camera camera) {
    for (BarParticle p : BarStates.PARTICLES) {
      renderParticle(matrix, p, camera);
    }
  }

  private static void renderParticle(PoseStack matrix, BarParticle particle, Camera camera) {
    double distanceSquared = camera.getPosition().distanceToSqr(particle.x, particle.y, particle.z);
    if (distanceSquared > ToroHealth.CONFIG.particle.distanceSquared) {
      return;
    }

    float scaleToGui = 0.025f;

    Minecraft client = Minecraft.getInstance();
    float tickDelta = client.getDeltaFrameTime();

    double x = Mth.lerp((double) tickDelta, particle.xPrev, particle.x);
    double y = Mth.lerp((double) tickDelta, particle.yPrev, particle.y);
    double z = Mth.lerp((double) tickDelta, particle.zPrev, particle.z);

    Vec3 camPos = camera.getPosition();
    double camX = camPos.x;
    double camY = camPos.y;
    double camZ = camPos.z;

    matrix.pushPose();
    matrix.translate(x - camX, y - camY, z - camZ);
    matrix.mulPose(Vector3f.YP.rotationDegrees(-camera.getYRot()));
    matrix.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
    matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);

    HealthBarRenderer.drawDamageNumber(matrix, particle.damage, 0, 0, 10);

    RenderSystem.disableBlend();

    matrix.popPose();
  }
}
