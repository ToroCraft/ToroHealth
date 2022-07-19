package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.bars.HealthBarRenderer;

public class BarDisplay {
	private static final ResourceLocation ICON_TEXTURES = new ResourceLocation("textures/gui/icons.png");
	private final Minecraft mc;
	private final GuiComponent gui;

	public BarDisplay(Minecraft mc, GuiComponent gui) {
		this.mc = mc;
		this.gui = gui;
	}

	private String getEntityName(LivingEntity entity) {
		return entity.getDisplayName().getString();
	}

	public void draw(PoseStack matrix, LivingEntity entity) {
		int xOffset = 0;

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		RenderSystem.enableBlend();

		HealthBarRenderer.render(matrix, entity, 63, 14, 130, false);
		String name = getEntityName(entity);
		int healthMax = Mth.ceil(entity.getMaxHealth());
		int healthCur = Math.min(Mth.ceil(entity.getHealth()), healthMax);
		String healthText = healthCur + "/" + healthMax;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		GuiComponent.drawString(matrix, mc.font, name, xOffset, (int)2, 16777215);

		mc.font.drawShadow(matrix, name, xOffset, 2, 16777215);
		xOffset += mc.font.width(name) + 5;

		renderHeartIcon(matrix, xOffset, (int)1);
		xOffset += 10;

		mc.font.drawShadow(matrix, healthText, xOffset, 2, 0xe0e0e0);
		xOffset += mc.font.width(healthText) + 5;

		int armor = entity.getArmorValue();// getArmor();

		if (armor > 0) {
			renderArmorIcon(matrix, xOffset, (int)1);
			xOffset += 10;
			mc.font.drawShadow(matrix, entity.getArmorValue() + "", xOffset, 2, 0xe0e0e0);
		}
	}

	private void renderArmorIcon(PoseStack matrix, int x, int y) {
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		gui.blit(matrix, x, y, 34, 9, 9, 9);
	}

	private void renderHeartIcon(PoseStack matrix, int x, int y) {
		RenderSystem.setShaderTexture(0, ICON_TEXTURES);
		gui.blit(matrix, x, y, 16 + 36, 0, 9, 9);
	}
}
