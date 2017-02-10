package net.torocraft.torohealthmod.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.ResourceLocation;
import net.torocraft.torohealthmod.ToroHealthMod;

public class BarDisplay implements ToroHealthDisplay {

	private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation(ToroHealthMod.MODID, "textures/gui/bars.png");

	private static final int BAR_WIDTH = 92;

	private final Minecraft mc;
	private final Gui gui;
	private int y;
	private int barX;
	private int barY;

	private EntityLivingBase entity;

	public BarDisplay(Minecraft mc, Gui gui) {
		this.mc = mc;
		this.gui = gui;
	}

	@Override
	public void setPosition(int x, int y) {
		this.y = y;
		barX = x + 2;
		barY = y + 12;
	}

	@Override
	public void draw() {
		if (entity == null) {
			return;
		}
		renderBossHealth();
	}

	public void renderBossHealth() {
		String name = entity.getDisplayName().getFormattedText();
		String health = (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
		renderHealthBar();
		mc.fontRendererObj.drawStringWithShadow(name, barX, y + 2, 16777215);
		mc.fontRendererObj.drawStringWithShadow(health, barX, y + 20, 16777215);
	}

	public static enum Color {
		PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE;
	}

	private void renderHealthBar() {
		Color color = determineColor();
		float percent = entity.getHealth() / entity.getMaxHealth();
		gui.drawTexturedModalRect(barX, barY, 0, color.ordinal() * 5 * 2, BAR_WIDTH, 5);
		int healthWidth = (int) (percent * BAR_WIDTH);
		if (healthWidth > 0) {
			gui.drawTexturedModalRect(barX, barY, 0, color.ordinal() * 5 * 2 + 5, healthWidth, 5);
		}
	}

	private Color determineColor() {
		if (entity instanceof EntityMob) {
			return Color.RED;
		} else if (entity instanceof EntitySlime) {
			return Color.RED;
		} else if (entity instanceof EntityGhast) {
			return Color.RED;
		} else if (entity instanceof EntityAnimal) {
			return Color.GREEN;
		} else if (entity instanceof EntityAmbientCreature) {
			return Color.GREEN;
		} else {
			return Color.WHITE;
		}
	}

	@Override
	public void setEntity(EntityLivingBase entity) {
		this.entity = entity;
	}

}