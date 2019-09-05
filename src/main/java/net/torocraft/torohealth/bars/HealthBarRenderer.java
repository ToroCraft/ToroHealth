package net.torocraft.torohealth.bars;

public class HealthBarRenderer {
  // was called from mixin
  //renderBar(entity_1, double_1, double_2, double_3, 50);





//
//    protected void renderBar(Entity entity,  double x, double y, double z, int distance) {
//        if (!(entity instanceof LivingEntity)) {
//           return;
//        }
//        double squaredDistanceTo = entity.squaredDistanceTo(this.renderManager.camera.getPos());
//
//        if (squaredDistanceTo > (double)(distance * distance)) {
//            return;
//        }
//
//        float cameraYaw = this.renderManager.cameraYaw;
//        float cameraPitch = this.renderManager.cameraPitch;
//        boolean sneaking = entity.isInSneakingPose();
//        float height = entity.getHeight() + 0.5F - (sneaking ? 0.25F : 0.0F);
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translatef((float) x, (float) y + height, (float) z);
//        GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(-cameraYaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(cameraPitch, 1.0F, 0.0F, 0.0F);
//        GlStateManager.scalef(-0.025F, -0.025F, 0.025F);
//        GlStateManager.disableLighting();
//
//
//        HealthBars.drawEntityHealthBar((LivingEntity) entity, x, y, z);
//
//        GlStateManager.popMatrix();
//    }

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
}
