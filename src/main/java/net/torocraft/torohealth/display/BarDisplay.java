package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
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
    return entity.getDisplayName().asFormattedString();
  }

  public void draw(LivingEntity entity) {
    int xOffset = 0;

    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    HealthBarRenderer.render(entity, 63, 14, 130, false);
    String name = getEntityName(entity);
    String health = (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getHealthMaximum();
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    gui.drawString(mc.textRenderer, name, xOffset, (int) 2, 16777215);

    mc.textRenderer.drawWithShadow(name, xOffset, 2, 16777215);
    xOffset += mc.textRenderer.getStringWidth(name) + 5;

    renderHeartIcon(xOffset, (int) 1);
    xOffset += 10;

    mc.textRenderer.drawWithShadow(health, xOffset, 2, 0xe0e0e0);
    xOffset += mc.textRenderer.getStringWidth(health) + 5;

    renderArmorIcon(xOffset, (int) 1);
    xOffset += 10;

    mc.textRenderer.drawWithShadow(entity.getArmor() + "", xOffset, 2, 0xe0e0e0);
  }

  private void renderArmorIcon(int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.blit(x, y, 34, 9, 9, 9);
  }

  private void renderHeartIcon(int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.blit(x, y, 16 + 36, 0, 9, 9);
  }
}
