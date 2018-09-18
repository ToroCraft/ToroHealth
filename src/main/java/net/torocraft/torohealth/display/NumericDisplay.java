package net.torocraft.torohealth.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.torocraft.torohealth.ToroHealth;

public class NumericDisplay implements ToroHealthDisplay {

  private static final ResourceLocation TEXTURE = new ResourceLocation(ToroHealth.MODID, "textures/gui/entityStatus.png");
  private static final int WIDTH = 100;
  private static final int HEIGHT = 34;

  private final Minecraft mc;
  private final Gui gui;
  private int x = 220;
  private int y = 100;
  private EntityLivingBase entity;

  public NumericDisplay(Minecraft mc, Gui gui) {
    this.mc = mc;
    this.gui = gui;
  }

  @Override
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;

  }

  @Override
  public void draw() {
    if (entity == null) {
      return;
    }

    mc.renderEngine.bindTexture(TEXTURE);

    /*
     * defines positions of each element from the top left position of
     * status display
     */
    int bgX = 0, bgY = 0, healthBarX = 2, healthBarY = 16, nameX = 50, nameY = 4, healthX = 50, healthY = 20;

    Gui.drawModalRectWithCustomSizedTexture(x + bgX, y + bgY, 0.0f, 0.0f, WIDTH, HEIGHT, 200.0f, 200.0f);

    Gui.drawModalRectWithCustomSizedTexture(x + healthBarX, y + healthBarY, 0.0f, 150.0f, 96, 16, 200.0f, 200.0f);

    int currentHealthWidth = (int) Math.ceil(96 * (entity.getHealth() / entity.getMaxHealth()));
    Gui.drawModalRectWithCustomSizedTexture(x + healthBarX, y + healthBarY, 0.0f, 100.0f, currentHealthWidth, 16, 200.0f, 200.0f);

    String name = getEntityName();

    gui.drawCenteredString(mc.fontRenderer, name, x + nameX, y + nameY, 0xFFFFFF);
    gui.drawCenteredString(mc.fontRenderer, (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth(), x + healthX, y + healthY,
        0xFFFFFF);
  }

  @Override
  public void setEntity(EntityLivingBase entity) {
    this.entity = entity;
  }

  public String getEntityName() {
    if (entity == null) {
      return "";
    }
    return entity.getDisplayName().getFormattedText();
  }

}