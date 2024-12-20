package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.torocraft.torohealth.ToroHealth;
import org.lwjgl.opengl.GL11;

public class ParticleRenderer {

  public static void renderParticles(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, Camera camera) {
    for (BarParticle p : BarStates.PARTICLES) {
      renderParticle(matrix, vertexConsumerProvider, p, camera);
    }
  }

  private static void renderParticle(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, BarParticle particle, Camera camera) {
    double distanceSquared = camera.getPos().squaredDistanceTo(particle.x, particle.y, particle.z);
    if (distanceSquared > ToroHealth.CONFIG.particle.distanceSquared) {
      return;
    }

    float scaleToGui = 0.025f;

    MinecraftClient client = MinecraftClient.getInstance();
    float tickDelta = client.getRenderTickCounter().getTickDelta(true);

    double x = MathHelper.lerp((double) tickDelta, particle.xPrev, particle.x);
    double y = MathHelper.lerp((double) tickDelta, particle.yPrev, particle.y);
    double z = MathHelper.lerp((double) tickDelta, particle.zPrev, particle.z);

    Vec3d camPos = camera.getPos();
    double camX = camPos.x;
    double camY = camPos.y;
    double camZ = camPos.z;

    matrix.push();
    matrix.translate(x - camX, y - camY, z - camZ);
    matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
    matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
    matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

    RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);

    HealthBarRenderer.drawDamageNumber(matrix, vertexConsumerProvider, particle.damage, 0, 0, 10);

    RenderSystem.disableBlend();

    matrix.pop();
  }
}
