package net.torocraft.torohealth.display;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.torocraft.torohealth.bars.HealthBarRenderer;

public class BarDisplay {

  private static final ResourceLocation ICON_TEXTURES =
      new ResourceLocation("textures/gui/icons.png");
  private final Minecraft mc;
  private final AbstractGui gui;

  public BarDisplay(Minecraft mc, AbstractGui gui) {
    this.mc = mc;
    this.gui = gui;
  }

  private String getEntityName(LivingEntity entity) {
    return entity.getDisplayName().getString();
  }

  public void drawStringWithShadow(MatrixStack matrices, FontRenderer textRenderer, String text,
      int x, int y, int color) {
    gui.drawString(matrices, textRenderer, text, x, y, color);
  }

  public int drawWithShadow(MatrixStack matrices, String text, float x, float y, int color) {
    return mc.fontRenderer.drawString(matrices, text, x, y, color);
  }

  public void draw(MatrixStack matrix, LivingEntity entity) {
    int xOffset = 0;

    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    HealthBarRenderer.render(matrix, entity, 63, 14, 130, false);
    String name = getEntityName(entity);
    String health = (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth();
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    drawStringWithShadow(matrix, mc.fontRenderer, name, xOffset, (int) 2, 16777215);

    drawWithShadow(matrix, name, xOffset, 2, 16777215);
    xOffset += mc.fontRenderer.getStringWidth(name) + 5;

    renderHeartIcon(matrix, xOffset, (int) 1);
    xOffset += 10;

    drawWithShadow(matrix, health, xOffset, 2, 0xe0e0e0);
    xOffset += mc.fontRenderer.getStringWidth(health) + 5;


    int armor = entity.getTotalArmorValue();

    if (armor > 0) {
      renderArmorIcon(matrix, xOffset, (int) 1);
      xOffset += 10;
      drawWithShadow(matrix, armor + "", xOffset, 2, 0xe0e0e0);
    }
  }

  private void renderArmorIcon(MatrixStack matrix, int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.blit(matrix, x, y, 34, 9, 9, 9);
  }

  private void renderHeartIcon(MatrixStack matrix, int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.blit(matrix, x, y, 16 + 36, 0, 9, 9);
  }
}
