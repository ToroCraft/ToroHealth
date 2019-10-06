package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.Config;
import net.torocraft.torohealth.util.EntityUtil;
import net.torocraft.torohealth.util.EntityUtil.Relation;

import java.util.Iterator;

import static org.lwjgl.glfw.GLFWGammaRamp.RED;

public class HealthBarRenderer {

  private static final Identifier GUI_BARS_TEXTURES = new Identifier(ToroHealth.MODID + ":textures/gui/bars.png");
  private static final int DARK_GRAY = 0x808080FF;
  private static final float FULL_SIZE = 40;

  public static void renderTrackedEntity(float cameraYaw, float cameraPitch) {
    for (Iterator<EntityTracker.TrackedEntity> i = EntityTracker.INSTANCE.iterator(); i.hasNext(); ) {
      EntityTracker.TrackedEntity t = i.next();
      renderInWorld(t.entity, t.x, t.y, t.z, cameraYaw, cameraPitch);
    }
  }

  public static void renderInWorld(LivingEntity entity, double x, double y, double z, float cameraYaw, float cameraPitch) {
    float scaleToGui = 0.025f;
    boolean sneaking = entity.isInSneakingPose();
    float height = entity.getHeight() + 0.5F - (sneaking ? 0.25F : 0.0F);

    GlStateManager.pushMatrix();
    GlStateManager.translatef((float) x, (float) y + height, (float) z);
    //GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(-cameraYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(cameraPitch, 1.0F, 0.0F, 0.0F);
    GlStateManager.scalef(-scaleToGui, -scaleToGui, scaleToGui);
    GlStateManager.disableLighting();

    GlStateManager.enableBlend();
    GlStateManager.disableAlphaTest();
    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.shadeModel(7425);

    render(entity, 0, 0, FULL_SIZE, true);

    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlphaTest();

    GlStateManager.popMatrix();
  }

  public static void render(LivingEntity entity, double x, double y, float width, boolean inWorld) {

    Relation relation = EntityUtil.determineRelation(entity);

    int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor : ToroHealth.CONFIG.bar.foeColor;
    int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary : ToroHealth.CONFIG.bar.foeColorSecondary;

    BarState state = BarState.getState(entity);

    float percent = entity.getHealth() / entity.getHealthMaximum();
    float percent2 = state.previousHealthDisplay / entity.getHealthMaximum();
    int zOffset = 0;

    drawBar(x, y, width, 1, DARK_GRAY, zOffset++, inWorld);
    drawBar(x, y, width, percent2, color2, zOffset++, inWorld);
    drawBar(x, y, width, percent, color, zOffset, inWorld);

    if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.CUMULATIVE)) {
      drawDamageNumber(state.previousHealth - entity.getHealth(), entity, x, y, width);
    } else if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.LAST)) {
      drawDamageNumber(state.lastDmg, entity, x, y, width);
    }
  }

  private static void drawDamageNumber(float dmg, LivingEntity entity, double x, double y, float width) {
    int i = Math.round(dmg);
    if (i < 1) {
      return;
    }
    String s = Integer.toString(i);
    int sw = MinecraftClient.getInstance().textRenderer.getStringWidth(s);
    MinecraftClient.getInstance().textRenderer.draw(s, (int) (x + (width / 2) - sw), (int) y + 5, 0xd00000);
  }

  private static void drawBar(double x, double y, float width, float percent, int color, int zOffset, boolean inWorld) {
    float c = 0.00390625f;
    int u = 0;
    int v = 6 * 5 * 2 + 5;
    int uw = MathHelper.ceil(92 * percent);
    int vh = 5;

    double size = percent * width;
    double h = inWorld ? 4 : 6;

    float r = (color >> 24 & 255) / 255.0F;
    float g = (color >> 16 & 255) / 255.0F;
    float b = (color >> 8 & 255) / 255.0F;
    float a = (color & 255) / 255.0F;

    MinecraftClient.getInstance().getTextureManager().bindTexture(GUI_BARS_TEXTURES);
    GlStateManager.color4f(r, g, b, a);

    double half = width / 2;

    float zOffsetAmount = inWorld ? -0.1f : 0.1f;

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBufferBuilder();
    buffer.begin(7, VertexFormats.POSITION_UV);
    buffer.vertex(-half + x, y, zOffset * zOffsetAmount).texture(u * c, v * c).next();
    buffer.vertex(-half + x, h + y, zOffset * zOffsetAmount).texture(u * c, (v + vh) * c).next();
    buffer.vertex(-half + size + x, h + y, zOffset * zOffsetAmount).texture((u + uw) * c, (v + vh) * c).next();
    buffer.vertex(-half + size + x, y, zOffset * zOffsetAmount).texture(((u + uw) * c), v * c).next();
    tessellator.draw();
  }
}
