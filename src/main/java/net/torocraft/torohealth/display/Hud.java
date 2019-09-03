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

  public Hud() {
    super(new LiteralText("ToroHealth HUD"));
  }

  public void draw() {
    draw(10, 10, 1f);
  }

  private void draw(float x, float y, float scale) {
    if (ToroHealth.selectedEntity != previousEntity) {
      entityDisplay.setEntity(ToroHealth.selectedEntity);
      previousEntity = ToroHealth.selectedEntity;
    }
    if (ToroHealth.selectedEntity == null) {
      return;
    }
    drawSkin(x, y, scale);
    entityDisplay.draw(x, y, scale);
    drawTitle(x, y, scale);
  }

  private void drawTitle(float x, float y, float scale) {
    int x1 = (int) ((48 * scale) + x);
    int y1 = (int) y + 2;
    drawString(MinecraftClient.getInstance().textRenderer, ToroHealth.selectedEntity.getName().asString(), x1, y1, 0xFFFFFF);
  }

  private void drawSkin(float x, float y, float scale) {
    MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    int x1 = (int) (x - (scale * 10));
    int y1 = (int) (y - (scale * 10));
    int w = (int) (scale * 160);
    int h = (int) (scale * 60);

    blit(x1, y1, 0.0f, 0.0f, w, h, w, h);
  }

}
