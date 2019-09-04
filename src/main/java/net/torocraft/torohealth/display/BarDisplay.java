package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.ToroHealth;

public class BarDisplay extends Screen implements Displayable {

  private static final Identifier ICON_TEXTURES = new Identifier("textures/gui/icons.png");

  private static final int BAR_WIDTH = 92;

  private final MinecraftClient mc;
  private final DrawableHelper gui;
  private int y;
  //private int barX;
  //private int barY;

  public BarDisplay(MinecraftClient mc, DrawableHelper gui) {
    super(new LiteralText("Health Bar"));
    this.mc = mc;
    this.gui = gui;
  }

  public String getEntityName() {
    return ToroHealth.selectedEntity.getDisplayName().asFormattedString();
  }

  @Override
  public void draw(float x, float y, float scale) {
    LivingEntity entity = ToroHealth.selectedEntity;

    int xOffset = (int) x;

    String name = getEntityName();
    String health = (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getHealthMaximum();

    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    HealthBars.drawEntityHealthBarInGui(gui, entity, xOffset, (int) y + 14);

    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    gui.drawString(mc.textRenderer, name, xOffset, (int) y + 2, 16777215);

    mc.textRenderer.drawWithShadow(name, xOffset, y + 2, 16777215);
    xOffset += mc.textRenderer.getStringWidth(name) + 5;

    renderHeartIcon(xOffset, (int) y + 1);
    xOffset += 10;

    mc.textRenderer.drawWithShadow(health, xOffset, y + 2, 0xe0e0e0);
    xOffset += mc.textRenderer.getStringWidth(health) + 5;

    renderArmorIcon(xOffset, (int) y + 1);
    xOffset += 10;

    mc.textRenderer.drawWithShadow(entity.getArmor() + "", xOffset, y + 2, 0xe0e0e0);
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
