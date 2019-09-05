package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.EntityUtil;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

public class HealthBarRenderer {

  private static final Identifier GUI_BARS_TEXTURES = new Identifier(ToroHealth.MODID, "textures/gui/bars.png");

  private static final float HEALTH_INDICATOR_DELAY = 80;
  private static final float HEALTH_ANIMATION_SPEED = 0.08f;
  private static final float SCALE = 0.03f;

  private static final int GREEN = 0x00FF00FF;
  private static final int DARK_GREEN = 0x008000FF;
  private static final int YELLOW = 0xFFFF00FF;
  private static final int WHITE = 0xFFFFFFFF;
  private static final int RED = 0xFF0000FF;
  private static final int DARK_RED = 0x800000FF;
  private static final int DARK_GRAY = 0x808080FF;
  private static final float zLevel = 0;

  private static final float VERTICAL_MARGIN = 0.35f;
  private static final double FULL_SIZE = 40;
  private static final double HALF_SIZE = FULL_SIZE / 2;


  //    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HURT_TEMP) && gui == null && state.lastDmg == 0) {
  //      return;
  //    }

  // was called from mixin
  //renderBar(entity_1, double_1, double_2, double_3, 50);

  public static void renderTrackedEntity(float cameraYaw, float cameraPitch) {
    for (Iterator<EntityTracker.TrackedEntity> i = EntityTracker.INSTANCE.iterator(); i.hasNext(); ) {
      EntityTracker.TrackedEntity t = i.next();
      HealthBarRenderer.render(t.entity, t.x, t.y, t.z, cameraYaw, cameraPitch);
    }
  }

  public static void render(LivingEntity entity, double x, double y, double z, float cameraYaw, float cameraPitch) {
    BarState state = BarState.getState(entity);
    updateState(entity, state);
    boolean sneaking = entity.isInSneakingPose();
    float height = entity.getHeight() + 0.5F - (sneaking ? 0.25F : 0.0F);
    render(entity, state, x, y + height, z, cameraYaw, cameraPitch);
  }


  private static void render(LivingEntity entity, BarState state, double x, double y, double z, float cameraYaw, float cameraPitch) {
    int color = determineColor(entity);
    int color2 = color == RED ? DARK_RED : DARK_GREEN;

    float percent = entity.getHealth() / entity.getHealthMaximum();
    float percent2 = state.previousHealthDisplay / entity.getHealthMaximum();
    int zOffset = 0;
    //y += entity.getHeight();


    //boolean sneaking = entity.isInSneakingPose();
    //float height = entity.getHeight() + 0.5F - (sneaking ? 0.25F : 0.0F);

    GlStateManager.pushMatrix();
    GlStateManager.translatef((float) x, (float) y, (float) z);
    //GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(-cameraYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(cameraPitch, 1.0F, 0.0F, 0.0F);
    GlStateManager.scalef(-0.025F, -0.025F, 0.025F);
    GlStateManager.disableLighting();

    drawBar(x, y, z, 1, DARK_GRAY, zOffset++);
    drawBar(x, y, z, percent2, color2, zOffset++);
    drawBar(x, y, z, percent, color, zOffset);

    if (Conf.numberType.equals(Conf.NumberType.CUMULATIVE)) {
      //drawDamageNumber(state.previousHealth - entity.getHealth(), entity, gui, x, y, z, zOffset);
    } else if (Conf.numberType.equals(Conf.NumberType.LAST)) {
      //drawDamageNumber(state.lastDmg, entity, gui, x, y, z, zOffset);
    }

    GlStateManager.popMatrix();
  }

  private static void updateState(LivingEntity entity, BarState state) {
    if (state.lastHealth < 0.1) {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    } else if (state.lastHealth != entity.getHealth()) {
      state.lastDmg = state.lastHealth - entity.getHealth();
      state.lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
      state.lastHealth = entity.getHealth();
    } else if (state.lastDmgDelay > -1) {
      state.lastDmgDelay--;
    } else {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    }

    if (state.previousHealthDelay > 0) {
      state.previousHealthDelay--;
    } else if (state.previousHealthDelay < 1 && state.previousHealthDisplay > entity.getHealth()) {
      state.previousHealthDisplay -= HEALTH_ANIMATION_SPEED;
    } else {
      state.previousHealthDisplay = entity.getHealth();
      state.previousHealth = entity.getHealth();
      state.previousHealthDelay = HEALTH_INDICATOR_DELAY;
    }
  }


//
//    public static void renderFloatingText(TextRenderer textRenderer_1, String string_1, float x, float y, float z, int int_1, float yaw, float pitch, boolean boolean_1) {
//        GlStateManager.pushMatrix();
//        GlStateManager.translatef(x, y, z);
//        GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(pitch, 1.0F, 0.0F, 0.0F);
//        GlStateManager.scalef(-0.025F, -0.025F, 0.025F);
//        GlStateManager.disableLighting();
//        GlStateManager.depthMask(false);
//        if (!boolean_1) {
//            GlStateManager.disableDepthTest();
//        }
//
//        GlStateManager.enableBlend();
//        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//        int int_2 = textRenderer_1.getStringWidth(string_1) / 2;
//        GlStateManager.disableTexture();
//        Tessellator tessellator_1 = Tessellator.getInstance();
//        BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
//        bufferBuilder_1.begin(7, VertexFormats.POSITION_COLOR);
//        float float_6 = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
//        bufferBuilder_1.vertex((double)(-int_2 - 1), (double)(-1 + int_1), 0.0D).color(0.0F, 0.0F, 0.0F, float_6).next();
//        bufferBuilder_1.vertex((double)(-int_2 - 1), (double)(8 + int_1), 0.0D).color(0.0F, 0.0F, 0.0F, float_6).next();
//        bufferBuilder_1.vertex((double)(int_2 + 1), (double)(8 + int_1), 0.0D).color(0.0F, 0.0F, 0.0F, float_6).next();
//        bufferBuilder_1.vertex((double)(int_2 + 1), (double)(-1 + int_1), 0.0D).color(0.0F, 0.0F, 0.0F, float_6).next();
//        tessellator_1.draw();
//        GlStateManager.enableTexture();
//        if (!boolean_1) {
//            textRenderer_1.draw(string_1, (float)(-textRenderer_1.getStringWidth(string_1) / 2), (float)int_1, 553648127);
//            GlStateManager.enableDepthTest();
//        }
//
//        GlStateManager.depthMask(true);
//        textRenderer_1.draw(string_1, (float)(-textRenderer_1.getStringWidth(string_1) / 2), (float)int_1, boolean_1 ? 553648127 : -1);
//        GlStateManager.enableLighting();
//        GlStateManager.disableBlend();
//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.popMatrix();
//    }


  private static void drawBar(double x, double y, double z, float percent, int color, int zOffset) {
    float c = 0.00390625f;
    int u = 0;
    int v = 6 * 5 * 2 + 5;
    int uw = MathHelper.ceil(92 * percent);
    int vh = 5;

    double size = percent * FULL_SIZE;
    double h = 3;

    float r = (color >> 24 & 255) / 255.0F;
    float g = (color >> 16 & 255) / 255.0F;
    float b = (color >> 8 & 255) / 255.0F;
    float a = (color & 255) / 255.0F;

    MinecraftClient.getInstance().getTextureManager().bindTexture(GUI_BARS_TEXTURES);
    GlStateManager.color4f(r, g, b, 255);

    //GlStateManager.alphaFunc(0,1);
    //GlStateManager.alphaFunc(516, 0.1F);

   // DrawableHelper.blit((int) x, (int) y, 0, (float) u, (float) v, uw, vh, 256, 256 + zOffset);

    ///EntityRenderDispatcher renderManager = MinecraftClient.getInstance().getEntityRenderManager();
    //boolean lighting = setupGlStateForInWorldRender(x, y, z, zOffset, renderManager);

    //GlStateManager.enableTexture();
    GlStateManager.enableBlend();
    GlStateManager.disableAlphaTest();
    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.shadeModel(7425);





    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBufferBuilder();
    buffer.begin(7, VertexFormats.POSITION_UV);
    buffer.vertex(-HALF_SIZE, 0, zOffset * -0.01f).texture(u * c, v * c).next();
    buffer.vertex(-HALF_SIZE, h, zOffset * -0.01f).texture(u * c, (v + vh) * c).next();
    buffer.vertex(-HALF_SIZE + size, h, zOffset * -0.01f).texture((u + uw) * c, (v + vh) * c).next();
    buffer.vertex(-HALF_SIZE + size, 0, zOffset * -0.01f).texture(((u + uw) * c), v * c).next();
    tessellator.draw();


    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlphaTest();
   // GlStateManager.enableTexture();

    //restoreGlState(lighting);
  }


  private static boolean setupGlStateForInWorldRender(double x, double y, double z, int zOffset, EntityRenderDispatcher renderManager) {
    double relX = x - renderManager.camera.getPos().x; //.viewerPosX;
    double relY = y - renderManager.camera.getPos().y; //.viewerPosY + VERTICAL_MARGIN;
    double relZ = z - renderManager.camera.getPos().z; //.viewerPosZ;

    GlStateManager.pushMatrix();
    GlStateManager.translated(relX, relY, relZ);

    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(-renderManager.cameraPitch, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(renderManager.cameraYaw, 1.0F, 0.0F, 0.0F);
    GlStateManager.translatef(0, 0, -zOffset * 0.001f);

    GlStateManager.scalef(-SCALE, -SCALE, SCALE);
    boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    return lighting;
  }

  private static void restoreGlState(boolean lighting) {
    GlStateManager.disableBlend();
    GlStateManager.enableDepthTest();//  enableDepth();
    GlStateManager.depthMask(true);
    if (lighting) {
      GlStateManager.enableLighting();
    }
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
  }

  private static int determineColor(LivingEntity entity) {
    if (EntityUtil.determineRelation(entity) == EntityUtil.Relation.FRIEND) {
      return GREEN;
    } else {
      return RED;
    }
  }


  private static void drawDamageNumber(float dmg, LivingEntity entity, DrawableHelper gui, double x, double y, double z, int zOffset) {

    int i = Math.round(dmg);

    if (i < 1) {
      return;
    }

    String s = Integer.toString(i);
    int sw = MinecraftClient.getInstance().textRenderer.getStringWidth(s);

    if (gui != null) {
      MinecraftClient.getInstance().textRenderer.draw(s, ((int) x + 92) - sw, (int) y + 6, 0xd00000);
    }
  }
}
