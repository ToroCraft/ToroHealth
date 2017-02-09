package net.torocraft.torohealthmod.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.torocraft.torohealthmod.ToroHealthMod;
import net.torocraft.torohealthmod.gui.GuiEntityStatus;

public class HeartsDisplay implements ToroHealthDisplay {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(ToroHealthMod.MODID, "textures/gui/entityStatus.png");
	private static final int WIDTH = 100;
	private static final int HEIGHT = 34;
	
	private final Minecraft mc;
	private final Gui gui;
	private int x, originX = 100;
	private int y, originY = 100;
	private EntityLivingBase entity;
	
	public HeartsDisplay(Minecraft mc, Gui gui) {
		this.mc = mc;
		this.gui = gui;
	}

	@Override
	public void setPosition(int x, int y) {
		originX = x;
		originY = y;
		resetToOrigin();
	}

	@Override
	public void draw() {
		if (entity == null) {
			return;
		}
		
		
		resetToOrigin();
		
		
		
		mc.renderEngine.bindTexture(TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(x, y , 0.0f, 0.0f, WIDTH, HEIGHT, 200.0f, 200.0f);
		
		x += 2;
		y += 2;
		
		drawName();
		drawHearts();
		drawArmor();
	}
	
	@Override
	public void setEntity(EntityLivingBase entity) {
		this.entity = entity;
	}

	private void resetToOrigin() {
		x = originX;
		y = originY;
	}

	private void drawName() {
		gui.drawString(mc.fontRendererObj, entity.getDisplayName().getFormattedText(), x, y, 0xFFFFFF);
		y += 10;
	}

	private int drawHearts() {
		mc.renderEngine.bindTexture(GuiEntityStatus.ICONS);
		int currentHealth = MathHelper.ceil(entity.getHealth());

		int absorptionAmount = MathHelper.ceil(entity.getAbsorptionAmount());
		int remainingAbsorption = absorptionAmount;

		float maxHealth = entity.getMaxHealth();

		int numRowsOfHearts = MathHelper.ceil((maxHealth + (float) absorptionAmount) / 2.0F / 10.0F);
		int j2 = Math.max(10 - (numRowsOfHearts - 2), 3);

		for (int currentHeartBeingDrawn = MathHelper.ceil((maxHealth + (float) absorptionAmount) / 2.0F)
				- 1; currentHeartBeingDrawn >= 0; --currentHeartBeingDrawn) {
			int texturePosX = 16;
			int flashingHeartOffset = 0;

			int rowsOfHearts = MathHelper.ceil((float) (currentHeartBeingDrawn + 1) / 10.0F) - 1;
			int heartToDrawX = x + currentHeartBeingDrawn % 10 * 8;
			int heartToDrawY = y + rowsOfHearts * j2;

			int hardcoreModeOffset = 0;

			if (entity.world.getWorldInfo().isHardcoreModeEnabled()) {
				hardcoreModeOffset = 5;
			}

			gui.drawTexturedModalRect(heartToDrawX, heartToDrawY, 16 + flashingHeartOffset * 9, 9 * hardcoreModeOffset, 9, 9);

			if (remainingAbsorption > 0) {
				if (remainingAbsorption == absorptionAmount && absorptionAmount % 2 == 1) {
					gui.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 153, 9 * hardcoreModeOffset, 9, 9);
					--remainingAbsorption;
				} else {
					gui.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 144, 9 * hardcoreModeOffset, 9, 9);
					remainingAbsorption -= 2;
				}
			} else {
				if (currentHeartBeingDrawn * 2 + 1 < currentHealth) {
					gui.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 36, 9 * hardcoreModeOffset, 9, 9);
				}

				if (currentHeartBeingDrawn * 2 + 1 == currentHealth) {
					gui.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 45, 9 * hardcoreModeOffset, 9, 9);
				}
			}
		}

		y += (numRowsOfHearts - 1) * j2 + 10;

		return remainingAbsorption;
	}

	private void drawArmor() {
		mc.renderEngine.bindTexture(GuiEntityStatus.ICONS);

		int armor = entity.getTotalArmorValue();

		for (int i = 0; i < 10; ++i) {
			if (armor > 0) {
				int armorIconX = x + i * 8;

				/*
				 * determines whether armor is full, half, or empty icon
				 */
				if (i * 2 + 1 < armor) {
					gui.drawTexturedModalRect(armorIconX, y, 34, 9, 9, 9);
				}

				if (i * 2 + 1 == armor) {
					gui.drawTexturedModalRect(armorIconX, y, 25, 9, 9, 9);
				}

				if (i * 2 + 1 > armor) {
					gui.drawTexturedModalRect(armorIconX, y, 16, 9, 9, 9);
				}
			}
		}

		y += 10;
	}

}