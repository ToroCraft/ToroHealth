package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

public class ParticleRenderer {

  public static void renderParticles(MatrixStack matrix) {
    for (BarParticle p : BarStates.PARTICLES) {
      renderParticle(matrix, p);
    }
  }

  private static void renderParticle(MatrixStack matrix, BarParticle particle) {
    float scaleToGui = 0.025f;

    Minecraft client = Minecraft.getInstance();
    float tickDelta = client.getRenderPartialTicks();

    double x = MathHelper.lerp((double) tickDelta, particle.xPrev, particle.x);
    double y = MathHelper.lerp((double) tickDelta, particle.yPrev, particle.y);
    double z = MathHelper.lerp((double) tickDelta, particle.zPrev, particle.z);

    ActiveRenderInfo camera = client.gameRenderer.getActiveRenderInfo();
    Vector3d camPos = camera.getProjectedView();
    double camX = camPos.x;
    double camY = camPos.y;
    double camZ = camPos.z;

    matrix.push();
    matrix.translate(x - camX, y - camY, z - camZ);
    matrix.rotate(Vector3f.YP.rotationDegrees(-camera.getYaw()));
    matrix.rotate(Vector3f.XP.rotationDegrees(camera.getPitch()));
    matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

    RenderSystem.disableLighting();
    RenderSystem.enableDepthTest();
    RenderSystem.disableAlphaTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);
    RenderSystem.shadeModel(7425);

    HealthBarRenderer.drawDamageNumber(matrix, particle.damage, 0, 0, 10);

    RenderSystem.shadeModel(7424);
    RenderSystem.disableBlend();
    RenderSystem.enableAlphaTest();

    matrix.pop();
  }
}
