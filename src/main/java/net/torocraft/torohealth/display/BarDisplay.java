package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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

    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    HealthBarRenderer.render(matrix, entity, 63, 14, 130, false);
    String name = getEntityName(entity);
    int healthMax = MathHelper.ceil(entity.getMaxHealth());
    int healthCur = Math.min(MathHelper.ceil(entity.getHealth()), healthMax);
    String healthText = healthCur + "/" + healthMax;
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    DrawableHelper.drawStringWithShadow(matrix, mc.textRenderer, name, xOffset, (int) 2, 16777215);

    mc.textRenderer.drawWithShadow(matrix, name, xOffset, 2, 16777215);
    xOffset += mc.textRenderer.getWidth(name) + 5;

    renderHeartIcon(matrix, xOffset, (int) 1);
    xOffset += 10;

    mc.textRenderer.drawWithShadow(matrix, healthText, xOffset, 2, 0xe0e0e0);
    xOffset += mc.textRenderer.getWidth(healthText) + 5;

    int armor = entity.getArmor();

    if (armor > 0) {
      renderArmorIcon(matrix, xOffset, (int) 1);
      xOffset += 10;
      mc.textRenderer.drawWithShadow(matrix, entity.getArmor() + "", xOffset, 2, 0xe0e0e0);
    }
  }

  private void renderArmorIcon(MatrixStack matrix, int x, int y) {
	RenderSystem.setShaderTexture(0, ICON_TEXTURES);
    gui.drawTexture(matrix, x, y, 34, 9, 9, 9);
  }

  private void renderHeartIcon(MatrixStack matrix, int x, int y) {
	RenderSystem.setShaderTexture(0, ICON_TEXTURES);
    gui.drawTexture(matrix, x, y, 16 + 36, 0, 9, 9);
  }
}
