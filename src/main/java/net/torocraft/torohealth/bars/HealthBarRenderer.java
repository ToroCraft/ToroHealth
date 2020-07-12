package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.Config;
import net.torocraft.torohealth.util.Config.InWorld;
import net.torocraft.torohealth.util.Config.Mode;
import net.torocraft.torohealth.util.EntityUtil;
import net.torocraft.torohealth.util.EntityUtil.Relation;
// import net.torocraft.torohealth.util.EntityUtil;
// import net.torocraft.torohealth.util.EntityUtil.Relation;
import org.lwjgl.opengl.GL11;

public class HealthBarRenderer {

  private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation(ToroHealth.MODID + ":textures/gui/bars.png");
  private static final int DARK_GRAY = 0x808080FF;
  private static final float FULL_SIZE = 40;

  private static InWorld getConfig() {
    return ToroHealth.CONFIG.inWorld;
  }

  public static void renderInWorld(MatrixStack matrix, LivingEntity entity) {
    
    if (Mode.NONE.equals(getConfig().mode)) return;

    if (Mode.WHEN_HOLDING_WEAPON.equals(getConfig().mode) && !ToroHealth.IS_HOLDING_WEAPON) {
      return;
    }

    Quaternion camera = Minecraft.getInstance().getRenderManager().getCameraOrientation();

    float scaleToGui = 0.025f;
    //boolean sneaking = entity.isInSneakingPose();
    //float height = entity.getHeight() + 0.5F - (sneaking ? 0.25F : 0.0F);
    
    // double x = entity.getX();
    // double y = entity.getY();
    // double z = entity.getZ();
    
    // Vec3d camPos = camera.getPos();
    // double camX = camPos.x;
    // double camY = camPos.y;
    // double camZ = camPos.z;
    
    float f = entity.getHeight() + 0.5F;

    matrix.push();
    // matrixStackIn.translate(0.0D, (double)f, 0.0D);
    // matrixStackIn.rotate(this.renderManager.getCameraOrientation());
    // matrixStackIn.scale(-0.025F, -0.025F, 0.025F);

    // matrix.translate(x - camX, (y + height) - camY, z - camZ);
    // matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
    // matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
    // matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

    matrix.translate(0.0D, (double)f, 0.0D);
    matrix.rotate(camera);
    matrix.scale(-0.025F, -0.025F, 0.025F);
    
    RenderSystem.disableLighting();
    RenderSystem.enableDepthTest();
    RenderSystem.disableAlphaTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    RenderSystem.shadeModel(7425);

    render(matrix, entity, 0, 0, FULL_SIZE, true);

    RenderSystem.shadeModel(7424);
    RenderSystem.disableBlend();
    RenderSystem.enableAlphaTest();
    
    matrix.pop();
  }

  public static void render(MatrixStack matrix, LivingEntity entity, double x, double y, float width, boolean inWorld) {
    Relation relation = EntityUtil.determineRelation(entity);

    int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor : ToroHealth.CONFIG.bar.foeColor;
    int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary : ToroHealth.CONFIG.bar.foeColorSecondary;

    BarState state = BarStates.getState(entity);

    float percent = entity.getHealth() / entity.getMaxHealth();
    float percent2 = state.previousHealthDisplay / entity.getMaxHealth();
    int zOffset = 0;

    Matrix4f m4f = matrix.getLast().getMatrix();
    drawBar(m4f, x, y, width, 1, DARK_GRAY, zOffset++, inWorld);
    drawBar(m4f, x, y, width, percent2, color2, zOffset++, inWorld);
    drawBar(m4f, x, y, width, percent, color, zOffset, inWorld);

    if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.CUMULATIVE)) {
      drawDamageNumber(matrix, entity, state.previousHealth - entity.getHealth(), x, y, width);
    } else if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.LAST)) {
      drawDamageNumber(matrix, entity, state.lastDmg, x, y, width);
    }
  }

  private static void drawDamageNumber(MatrixStack matrix, LivingEntity entity, float dmg, double x, double y, float width) {
    int i = Math.round(dmg);
    if (i < 1) {
      return;
    }
    String s = Integer.toString(i);
    Minecraft minecraft = Minecraft.getInstance();
    int sw = minecraft.fontRenderer.getStringWidth(s);
    minecraft.fontRenderer.func_238421_b_(matrix, s, (int) (x + (width / 2) - sw), (int) y + 5, 0xd00000);
  }

  private static void drawBar(Matrix4f matrix4f, double x, double y, float width, float percent, int color, int zOffset, boolean inWorld) {
    float c = 0.00390625F;
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

    Minecraft.getInstance().getTextureManager().bindTexture(GUI_BARS_TEXTURES);
    RenderSystem.color4f(r, g, b, a);

    float half = width / 2;

    float zOffsetAmount = inWorld ? -0.1F : 0.1F;

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    buffer.pos(matrix4f, (float) (-half + x), (float) y, zOffset * zOffsetAmount).tex(u * c, v * c).endVertex();
    buffer.pos(matrix4f, (float) (-half + x), (float) (h + y), zOffset * zOffsetAmount).tex(u * c, (v + vh) * c).endVertex();
    buffer.pos(matrix4f, (float) (-half + size + x), (float) (h + y), zOffset * zOffsetAmount).tex((u + uw) * c, (v + vh) * c).endVertex();
    buffer.pos(matrix4f, (float) (-half + size + x), (float) y, zOffset * zOffsetAmount).tex(((u + uw) * c), v * c).endVertex();
    tessellator.draw();
  }
}
