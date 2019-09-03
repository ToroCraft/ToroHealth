package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GlStateManager;

public class BarDisplay {

  private static final ResourceLocation ICON_TEXTURES = new ResourceLocation("textures/gui/icons.png");

  private static final int BAR_WIDTH = 92;

  private final Minecraft mc;
  private final Gui gui;
  private int y;
  private int barX;
  private int barY;

  public BarDisplay(Minecraft mc, Gui gui) {
    this.mc = mc;
    this.gui = gui;
  }

  @Override
  public void setPosition(int x, int y) {
    this.y = y;
    barX = x + 4;
    barY = y + 12;
  }

  @Override
  public void draw() {
    if (entity == null) {
      return;
    }
    render();
  }

  public void render() {
    String name = getEntityName();
    String health = (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth();

    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    HealthBars.drawEntityHealthBarInGui(gui, entity, barX, barY);

    mc.fontRenderer.drawStringWithShadow(name, barX, y + 2, 16777215);
    barX += mc.fontRenderer.getStringWidth(name) + 5;

    renderHeartIcon(barX, y + 1);
    barX += 10;

    mc.fontRenderer.drawStringWithShadow(health, barX, y + 2, 0xe0e0e0);
    barX += mc.fontRenderer.getStringWidth(health) + 5;

    renderArmorIcon(barX, y + 1);
    barX += 10;

    mc.fontRenderer.drawStringWithShadow(entity.getTotalArmorValue() + "", barX, y + 2, 0xe0e0e0);
  }

  private void renderArmorIcon(int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.drawTexturedModalRect(x, y, 34, 9, 9, 9);
  }

  private void renderHeartIcon(int x, int y) {
    mc.getTextureManager().bindTexture(ICON_TEXTURES);
    gui.drawTexturedModalRect(x, y, 16 + 36, 0, 9, 9);
  }
}
