package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.torocraft.torohealth.ToroHealth;

public class Hud extends Screen {
  private static final Identifier BACKGROUND_TEXTURE = new Identifier(ToroHealth.MODID, "textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity previousEntity;
  private BarDisplay barDisplay;

  public Hud() {
    super(new LiteralText("ToroHealth HUD"));
    barDisplay = new BarDisplay(MinecraftClient.getInstance(), this);
  }

  public void draw() {
    draw(ToroHealth.CONFIG.hud.x, ToroHealth.CONFIG.hud.y, ToroHealth.CONFIG.hud.scale);
  }

  private void draw(float x, float y, float scale) {
    x = 10;
    y = 10;
    scale = 1f;

    if (ToroHealth.selectedEntity != previousEntity) {
      entityDisplay.setEntity(ToroHealth.selectedEntity);
      previousEntity = ToroHealth.selectedEntity;
    }
    if (ToroHealth.selectedEntity == null) {
      return;
    }

    GlStateManager.pushMatrix();
    GlStateManager.scalef(scale, scale, scale);
    GlStateManager.translatef(x, y, 0);

    drawSkin();
    entityDisplay.draw();

    GlStateManager.pushMatrix();
    GlStateManager.translatef(54, 10, 0);
    barDisplay.draw();
    GlStateManager.popMatrix();



    GlStateManager.popMatrix();
  }

  private void drawSkin() {
    MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    int w = 160;
    int h = 60;

    blit(0, 0, 0.0f, 0.0f, w, h, w, h);
  }

}
