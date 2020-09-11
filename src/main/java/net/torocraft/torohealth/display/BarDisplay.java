package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.bars.HealthBarRenderer;

public class BarDisplay {

  private static final Identifier ICON_TEXTURES = new Identifier("textures/gui/icons.png");
  private final MinecraftClient mc;
  private final DrawableHelper gui;

  public BarDisplay(MinecraftClient mc, DrawableHelper gui) {
    this.mc = mc;
    this.gui = gui;
  }

  private String getEntityName(LivingEntity entity) {
    return entity.getDisplayName().getString();
  }

  public void draw(MatrixStack matrix, LivingEntity entity) {
    int xOffset = 0;

    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    HealthBarRenderer.render(matrix, entity, 63, 14, 130, false);
    String name = getEntityName(entity);
    String health = (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth();
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    DrawableHelper.drawStringWithShadow(matrix, mc.textRenderer, name, xOffset, (int) 2, 16777215);

    mc.textRenderer.drawWithShadow(matrix, name, xOffset, 2, 16777215);
    xOffset += mc.textRenderer.getWidth(name) + 5;

    renderHeartIcon(matrix, xOffset, (int) 1);
    xOffset += 10;

    mc.textRenderer.drawWithShadow(matrix, health, xOffset, 2, 0xe0e0e0);
    xOffset += mc.textRenderer.getWidth(health) + 5;


    int armor = entity.getArmor();

    if (armor > 0) {
      renderArmorIcon(matrix, xOffset, (int) 1);
      xOffset += 10;
      mc.textRenderer.drawWithShadow(matrix, entity.getArmor() + "", xOffset, 2, 0xe0e0e0);
    }
  }

  private void renderArmorIcon(MatrixStack matrix, int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.drawTexture(matrix, x, y, 34, 9, 9, 9);
  }

  private void renderHeartIcon(MatrixStack matrix, int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.drawTexture(matrix, x, y, 16 + 36, 0, 9, 9);
  }
}
