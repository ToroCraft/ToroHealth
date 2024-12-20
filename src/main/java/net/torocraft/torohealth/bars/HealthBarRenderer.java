package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.InWorld;
import net.torocraft.torohealth.config.Config.Mode;
import net.torocraft.torohealth.util.EntityUtil;
import net.torocraft.torohealth.util.EntityUtil.Relation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class HealthBarRenderer {

  private static final Identifier GUI_BARS_TEXTURES = Identifier.of(ToroHealth.MODID + ":textures/gui/bars.png");
  private static final int DARK_GRAY = 0x808080;
  private static final float FULL_SIZE = 40;

  private static InWorld getConfig() {
    return ToroHealth.CONFIG.inWorld;
  }

  private static final List<LivingEntity> renderedEntities = new ArrayList<>();

  public static void prepareRenderInWorld(LivingEntity entity) {
    MinecraftClient client = MinecraftClient.getInstance();

    if (!EntityUtil.showHealthBar(entity, client)) {
      return;
    }

    if (entity.distanceTo(client.getCameraEntity()) > ToroHealth.CONFIG.inWorld.distance) {
      return;
    }

    BarStates.getState(entity);

    if (Mode.WHEN_HOLDING_WEAPON.equals(getConfig().mode) && !ToroHealth.IS_HOLDING_WEAPON) {
      return;
    }

    if (Mode.NONE.equals(getConfig().mode)) {
      return;
    }

    if (ToroHealth.CONFIG.inWorld.onlyWhenLookingAt && ToroHealth.HUD.getEntity() != entity) {
      return;
    }

    if (ToroHealth.CONFIG.inWorld.onlyWhenHurt && entity.getHealth() >= entity.getMaxHealth()) {
      return;
    }

    renderedEntities.add(entity);

  }

  public static void renderInWorld(MatrixStack matrix,
                                   VertexConsumerProvider vertexConsumerProvider, Camera camera) {

    MinecraftClient client = MinecraftClient.getInstance();

    if (camera == null) {
      camera = client.getEntityRenderDispatcher().camera;
    }

    if (camera == null) {
      renderedEntities.clear();
      return;
    }

    if (renderedEntities.isEmpty()) {
      return;
    }

    RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);

    for (LivingEntity entity : renderedEntities) {
      float scaleToGui = 0.025f;
      boolean sneaking = entity.isInSneakingPose();
      float height = entity.getHeight() + 0.6F - (sneaking ? 0.25F : 0.0F);

      float tickDelta = client.getRenderTickCounter().getTickDelta(true);
      double x = MathHelper.lerp((double) tickDelta, entity.prevX, entity.getX());
      double y = MathHelper.lerp((double) tickDelta, entity.prevY, entity.getY());
      double z = MathHelper.lerp((double) tickDelta, entity.prevZ, entity.getZ());

      Vec3d camPos = camera.getPos();
      double camX = camPos.x;
      double camY = camPos.y;
      double camZ = camPos.z;

      matrix.push();
      matrix.translate(x - camX, (y + height) - camY, z - camZ);
      matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
      matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

      render(matrix, vertexConsumerProvider, entity, 0, 0, FULL_SIZE, true);

      matrix.pop();
    }

    RenderSystem.disableBlend();

    renderedEntities.clear();
  }

  public static void render(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider,
                            LivingEntity entity, double x, double y,
                            float width, boolean inWorld) {

    Relation relation = EntityUtil.determineRelation(entity);

    int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor
        : ToroHealth.CONFIG.bar.foeColor;
    int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary
        : ToroHealth.CONFIG.bar.foeColorSecondary;

    BarState state = BarStates.getState(entity);

    float percent = Math.min(1, Math.min(state.health, entity.getMaxHealth()) / entity.getMaxHealth());
    float percent2 = Math.min(state.previousHealthDisplay, entity.getMaxHealth()) / entity.getMaxHealth();
    int zOffset = 0;

    Matrix4f m4f = matrix.peek().getPositionMatrix(); //  .getModel();
    drawBar(m4f, x, y, width, 1, DARK_GRAY, zOffset++, inWorld);
    drawBar(m4f, x, y, width, percent2, color2, zOffset++, inWorld);
    drawBar(m4f, x, y, width, percent, color, zOffset, inWorld);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    if (!inWorld) {
      if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.CUMULATIVE)) {
        drawDamageNumber(matrix, vertexConsumerProvider, state.lastDmgCumulative, x, y, width);
      } else if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.LAST)) {
        drawDamageNumber(matrix, vertexConsumerProvider, state.lastDmg, x, y, width);
      }
    }
  }

  public static void drawDamageNumber(MatrixStack matrix,
                                      VertexConsumerProvider vertexConsumerProvider,
                                      int dmg, double x, double y, float width) {
    int i = Math.abs(Math.round(dmg));
    if (i == 0) {
      return;
    }
    String s = Integer.toString(i);
    MinecraftClient minecraft = MinecraftClient.getInstance();
    int sw = minecraft.textRenderer.getWidth(s);
    int color = dmg < 0 ? ToroHealth.CONFIG.particle.healColor : ToroHealth.CONFIG.particle.damageColor;
    minecraft.textRenderer.draw(
            s, (float) (x + (width / 2) - sw), (float) y + 5, color, false,
            matrix.peek().getPositionMatrix(), vertexConsumerProvider,
            TextRenderer.TextLayerType.NORMAL, 0, 15728880);
  }

  private static void drawBar(Matrix4f matrix4f, double x, double y, float width, float percent,
      int color, int zOffset, boolean inWorld) {
    float c = 0.00390625F;
    int u = 0;
    int v = 6 * 5 * 2 + 5;
    int uw = MathHelper.ceil(92 * percent);
    int vh = 5;

    double size = percent * width;
    double h = inWorld ? 4 : 6;

    float r = (color >> 16 & 255) / 255.0F;
    float g = (color >> 8 & 255) / 255.0F;
    float b = (color & 255) / 255.0F;

    RenderSystem.setShaderColor(r, g, b, 1);
    RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
    RenderSystem.setShaderTexture(0, GUI_BARS_TEXTURES);
    RenderSystem.enableBlend();

    float half = width / 2;

    float zOffsetAmount = inWorld ? -0.1F : 0.1F;

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
    buffer.vertex(matrix4f, (float) (-half + x), (float) y, zOffset * zOffsetAmount)
        .texture(u * c, v * c);
    buffer.vertex(matrix4f, (float) (-half + x), (float) (h + y), zOffset * zOffsetAmount)
        .texture(u * c, (v + vh) * c);
    buffer.vertex(matrix4f, (float) (-half + size + x), (float) (h + y), zOffset * zOffsetAmount)
        .texture((u + uw) * c, (v + vh) * c);
    buffer.vertex(matrix4f, (float) (-half + size + x), (float) y, zOffset * zOffsetAmount)
        .texture(((u + uw) * c), v * c);
    BufferRenderer.drawWithGlobalProgram(buffer.end());
  }
}
