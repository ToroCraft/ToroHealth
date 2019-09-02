package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.stream.Stream;

public class EntityDisplay extends Screen implements IDisplay {

    private static final int RENDER_HEIGHT = 30;
    private static final int RENDER_WIDTH = 18;
    private static final int PADDING = 2;
    private static final int WIDTH = 40;
    private static final int HEIGHT = WIDTH;

    private int y;

    private float glX;
    private float glY;

    private LivingEntity entity;
    private Entity leashedToEntity;
    private float prevYawOffset;
    private float prevYaw;
    private float prevPitch;
    private float prevYawHead;
    private float prevPrevYahHead;
    private int scale = 1;

    public EntityDisplay() {
        super(new LiteralText("Entity Display"));
    }

    @Override
    public void setPosition(int x, int y) {
        this.y = y;
        glX = (float) x + WIDTH / 2;
        updateScale();
    }

    @Override
    public void setEntity(LivingEntity entity) {
        this.entity = entity;
        updateScale();
    }

    @Override
    public void draw() {
        try {
           // pushEntityLeasedTo();
//            pushEntityRotations();
//            glDraw();
//            popEntityRotations();
          //  popEntityLeasedTo();
           if (entity != null) {
               drawEntity(50, 50, 30, -40, -20, entity);
           }
        } catch (Throwable ignore) {
        }
    }

    private void updateScale() {
        if (entity == null) {
            glY = (float) y + HEIGHT - PADDING;
            return;
        }

        int scaleY = MathHelper.ceil(RENDER_HEIGHT / entity.getHeight());
        int scaleX = MathHelper.ceil(RENDER_WIDTH / entity.getWidth());
        scale = Math.min(scaleX, scaleY);
        glY = (float) y + (int)(((double)HEIGHT / 2) + ((double)RENDER_HEIGHT / 2));
        if (entity instanceof GhastEntity) {
            glY -= 10;
        }
    }

    private void glDraw() {

        GlStateManager.enableColorMaterial();

        GlStateManager.pushMatrix();

        GlStateManager.translatef(glX, glY, 50.0F);
        GlStateManager.scalef((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-100.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(0.0f, 1.0F, 0.0F, 0.0F);

        GuiLighting.enableForItems();

        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRenderDispatcher rm = MinecraftClient.getInstance().getEntityRenderManager(); //  .getRenderManager();
        rm.cameraPitch = 180.0F;
        rm.setRenderShadows(false);
        rm.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rm.setRenderShadows(true);

        GlStateManager.popMatrix();
        GuiLighting.disable();
        GlStateManager.disableRescaleNormal();

        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture(); //  disableTexture2D();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }

    public static void drawEntity(int x, int y, int scale, float yaw, float pitch, LivingEntity entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y, 50.0F);
        GlStateManager.scalef((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float float_3 = entity.field_6283;
        float float_4 = entity.yaw;
        float float_5 = entity.pitch;
        float float_6 = entity.prevHeadYaw;
        float float_7 = entity.headYaw;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        GuiLighting.enable();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-((float)Math.atan((double)(pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.field_6283 = yaw; //yaw; //(float)Math.atan((double)(yaw / 40.0F)) * 20.0F;
        entity.yaw = -4; // yay head this is //(float)Math.atan((double)(yaw / 40.0F)) * 40.0F;
        entity.pitch = pitch; //-((float)Math.atan((double)(pitch / 40.0F))) * 20.0F;
        entity.headYaw = entity.yaw;
        entity.prevHeadYaw = entity.yaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRenderDispatcher entityRenderDispatcher_1 = MinecraftClient.getInstance().getEntityRenderManager();
        entityRenderDispatcher_1.method_3945(180.0F);
        entityRenderDispatcher_1.setRenderShadows(false);
        entityRenderDispatcher_1.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        entityRenderDispatcher_1.setRenderShadows(true);
        entity.field_6283 = float_3;
        entity.yaw = float_4;
        entity.pitch = float_5;
        entity.prevHeadYaw = float_6;
        entity.headYaw = float_7;
        GlStateManager.popMatrix();
        GuiLighting.disable();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }
//
//    private void popEntityLeasedTo() {
//        if (entity instanceof EntityLiving && leashedToEntity != null) {
//            ((EntityLiving) entity).setLeashHolder(leashedToEntity, false);
//            leashedToEntity = null;
//        }
//    }
//
//    private void pushEntityLeasedTo() {
//        if (entity instanceof EntityLiving) {
//            if (((EntityLiving) entity).getLeashed()) {
//                leashedToEntity = ((EntityLiving) entity).getLeashHolder();
//                ((EntityLiving) entity).setLeashHolder(null, false);
//            }
//        }
//    }
//
    private void popEntityRotations() {
        entity.field_6283 = prevYawOffset;
        entity.yaw = prevYaw;
        entity.pitch = prevPitch;
        entity.headYaw = prevYawHead;
        entity.prevHeadYaw = prevPrevYahHead;
    }

    private void pushEntityRotations() {
        prevYawOffset = entity.field_6283;
        prevYaw = entity.yaw;
        prevPitch = entity.pitch;
        prevYawHead = entity.headYaw;
        prevPrevYahHead = entity.prevHeadYaw;
        entity.field_6283 = 0.0f;
        entity.yaw = 0.0f;
        entity.pitch = 0.0f;
        entity.headYaw = 0.0f;
        entity.prevHeadYaw = 0.0f;
    }
}