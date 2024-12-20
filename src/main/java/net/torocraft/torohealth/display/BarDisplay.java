package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealth.bars.HealthBarRenderer;

public class BarDisplay {

  private static final Identifier ARMOR_TEXTURE = Identifier.of("hud/armor_full");
  private static final Identifier HEART_TEXTURE = Identifier.of("hud/heart/full");
  private final MinecraftClient mc;

  public BarDisplay(MinecraftClient mc) {
    this.mc = mc;
  }

  private String getEntityName(LivingEntity entity) {
    return entity.getDisplayName().getString();
  }

  public void draw(DrawContext drawContext, LivingEntity entity) {
    int xOffset = 0;

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
    RenderSystem.enableBlend();

    drawContext.draw(vertexConsumerProvider -> {
      HealthBarRenderer.render(drawContext.getMatrices(), vertexConsumerProvider, entity, 63, 14, 130, false);
    });
    String name = getEntityName(entity);
    int healthMax = MathHelper.ceil(entity.getMaxHealth());
    int healthCur = Math.min(MathHelper.ceil(entity.getHealth()), healthMax);
    String healthText = healthCur + "/" + healthMax;
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    drawContext.drawTextWithShadow(mc.textRenderer, name, xOffset, 2, 16777215);

    drawContext.drawTextWithShadow(mc.textRenderer, name, xOffset, 2, 16777215);
    xOffset += mc.textRenderer.getWidth(name) + 5;

    renderHeartIcon(drawContext, xOffset, (int) 1);
    xOffset += 10;

    drawContext.drawTextWithShadow(mc.textRenderer, healthText, xOffset, 2, 0xe0e0e0);
    xOffset += mc.textRenderer.getWidth(healthText) + 5;

    int armor = entity.getArmor();

    if (armor > 0) {
      renderArmorIcon(drawContext, xOffset, (int) 1);
      xOffset += 10;
      drawContext.drawTextWithShadow(mc.textRenderer, entity.getArmor() + "", xOffset, 2, 0xe0e0e0);
    }
  }

  private void renderArmorIcon(DrawContext drawContext, int x, int y) {
    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_TEXTURE, x, y, 9, 9);
  }

  private void renderHeartIcon(DrawContext drawContext, int x, int y) {
    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, HEART_TEXTURE, x, y, 9, 9);
  }
}
