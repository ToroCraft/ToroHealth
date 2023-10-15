package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.InWorld;
import net.torocraft.torohealth.config.Config.Mode;
import net.torocraft.torohealth.util.EntityUtil;
import net.torocraft.torohealth.util.EntityUtil.Relation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class HealthBarRenderer {

  private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation(
      ToroHealth.MODID + ":textures/gui/bars.png");
  private static final int DARK_GRAY = 0x808080;
  private static final float FULL_SIZE = 40;

  private static InWorld getConfig() {
    return ToroHealth.CONFIG.inWorld;
  }

  private static final List<LivingEntity> renderedEntities = new ArrayList<>();

  public static void prepareRenderInWorld(LivingEntity entity) {
    Minecraft client = Minecraft.getInstance();

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

  public static void renderInWorld(float partialTick, PoseStack matrix, Camera camera) {

    Minecraft client = Minecraft.getInstance();

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

    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);

    for (LivingEntity entity : renderedEntities) {
      float scaleToGui = 0.025f;
      boolean sneaking = entity.isCrouching();
      float height = entity.getBbHeight() + 0.6F - (sneaking ? 0.25F : 0.0F);

      double x = Mth.lerp((double) partialTick, entity.xo, entity.getX());
      double y = Mth.lerp((double) partialTick, entity.yo, entity.getY());
      double z = Mth.lerp((double) partialTick, entity.zo, entity.getZ());

      Vec3 camPos = camera.getPosition();
      double camX = camPos.x();
      double camY = camPos.y();
      double camZ = camPos.z();

      matrix.pushPose();
      matrix.translate(x - camX, (y + height) - camY, z - camZ);
      matrix.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
      matrix.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
      matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

      render(matrix, entity, 0, 0, FULL_SIZE, true);

      matrix.popPose();
    }

    RenderSystem.disableBlend();

    renderedEntities.clear();
  }

  public static void render(PoseStack matrix, LivingEntity entity, double x, double y,
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

    Matrix4f m4f = matrix.last().pose();
    drawBar(m4f, x, y, width, 1, DARK_GRAY, zOffset++, inWorld);
    drawBar(m4f, x, y, width, percent2, color2, zOffset++, inWorld);
    drawBar(m4f, x, y, width, percent, color, zOffset, inWorld);

    if (!inWorld) {
      if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.CUMULATIVE)) {
        drawDamageNumber(matrix, state.lastDmgCumulative, x, y, width);
      } else if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.LAST)) {
        drawDamageNumber(matrix, state.lastDmg, x, y, width);
      }
    }
  }

  public static void drawDamageNumber(PoseStack matrix, int dmg, double x, double y,
      float width) {
    int i = Math.abs(Math.round(dmg));
    if (i == 0) {
      return;
    }
    String s = Integer.toString(i);
    Minecraft minecraft = Minecraft.getInstance();
    int sw = minecraft.font.width(s);
    int color = dmg < 0 ? ToroHealth.CONFIG.particle.healColor : ToroHealth.CONFIG.particle.damageColor;
//    minecraft.font.draw(matrix, s, (int) (x + (width / 2) - sw), (int) y + 5, color);
    // FIXME
  }

  private static void drawBar(Matrix4f matrix4f, double x, double y, float width, float percent,
                              int color, int zOffset, boolean inWorld) {
    float c = 0.00390625F;
    int u = 0;
    int v = 6 * 5 * 2 + 5;
    int uw = Mth.ceil(92 * percent);
    int vh = 5;

    double size = percent * width;
    double h = inWorld ? 4 : 6;

    float r = (color >> 16 & 255) / 255.0F;
    float g = (color >> 8 & 255) / 255.0F;
    float b = (color & 255) / 255.0F;

    RenderSystem.setShaderColor(r, g, b, 1);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, GUI_BARS_TEXTURES);
    RenderSystem.enableBlend();

    float half = width / 2;

    float zOffsetAmount = inWorld ? -0.1F : 0.1F;

    Tesselator tessellator = Tesselator.getInstance();
    BufferBuilder buffer = tessellator.getBuilder();
    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

    buffer.vertex(matrix4f, (float) (-half + x), (float) y, zOffset * zOffsetAmount)
        .uv(u * c, v * c).endVertex();
    buffer.vertex(matrix4f, (float) (-half + x), (float) (h + y), zOffset * zOffsetAmount)
        .uv(u * c, (v + vh) * c).endVertex();
    buffer.vertex(matrix4f, (float) (-half + size + x), (float) (h + y), zOffset * zOffsetAmount)
        .uv((u + uw) * c, (v + vh) * c).endVertex();
    buffer.vertex(matrix4f, (float) (-half + size + x), (float) y, zOffset * zOffsetAmount)
        .uv(((u + uw) * c), v * c).endVertex();
    tessellator.end();
  }
}
