package net.torocraft.torohealthmod.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.torohealthmod.ToroHealthMod;
import net.torocraft.torohealthmod.config.ConfigurationHandler;

public class GuiEntityStatus extends Gui {

	private Minecraft mc;
	private EntityLivingBase entity;
	private int age = 0;
	private boolean showHealthBar = false;

	private ScaledResolution viewport;
	private final int PADDING_FROM_EDGE = 2;

	String displayPosition;
	int screenX = PADDING_FROM_EDGE, screenY = PADDING_FROM_EDGE;
	int displayHeight;
	int displayWidth;

	/*
	 * Entity drawing
	 */
	private int entityRenderWidth;
	private final int entityRenderHeightUnit = 20;
	private final int entityRenderX = 20;
	private Entity leashedToEntity;

	/*
	 * for hearts drawing
	 */
	private int entityHealth = 0;
	private int lastEntityHealth = 0;
	private long lastSystemTime = 0L;
	private int updateCounter;
	private long healthUpdateCounter = 0L;
	private Random rand = new Random();

	public GuiEntityStatus() {
		this(Minecraft.getMinecraft());
	}

	public GuiEntityStatus(Minecraft mc) {
		super();
		this.mc = mc;
	}

	public void setEntity(EntityLivingBase entityToTrack) {
		showHealthBar();
		age = 0;
		if (entity != null && entity.getUniqueID().equals(entityToTrack.getUniqueID())) {
			return;
		}
		entity = entityToTrack;
	}

	@SubscribeEvent
	public void drawHealthBar(RenderGameOverlayEvent event) {
		if (!showHealthBar) {
			return;
		}
		String entityStatusDisplay = ConfigurationHandler.entityStatusDisplay;
		age++;
		if (age > ConfigurationHandler.hideDelay || entityStatusDisplay.equals("OFF")) {
			hideHealthBar();
		}

		if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
			return;
		}

		boolean showEntityModel = ConfigurationHandler.showEntityModel;
		if (showEntityModel) {
			entityRenderWidth = 40;
		} else {
			entityRenderWidth = 0;
		}

		viewport = new ScaledResolution(mc);
		displayPosition = ConfigurationHandler.statusDisplayPosition;

		if (isUnsupportedDisplayType(entityStatusDisplay)) {
			entityStatusDisplay = "HEARTS";
		}

		if (entityStatusDisplay.equals("NUMERIC")) {
			drawNumericDisplayStyle();
		} else if (entityStatusDisplay.equals("HEARTS")) {
			drawHeartsDisplay();
		}

		if (showEntityModel) {
			try {
				drawEntityOnScreen();
			} catch (Throwable ignore) {
			}
		}
	}

	public void drawEntityOnScreen() {

		int sw = viewport.getScaledWidth(), sh = viewport.getScaledHeight();

		int entityRenderHeight = 20;

		double h = entityRenderHeight / entity.height;

		if (displayPosition.contains("TOP")) {
			screenY = entityRenderHeight + 5;
		}

		if (displayPosition.contains("BOTTOM")) {
			screenY = sh - displayHeight + entityRenderHeight;
		}

		if (displayPosition.contains("LEFT")) {
			screenX = entityRenderX;
		}

		if (displayPosition.contains("RIGHT")) {
			screenX = sw - entityRenderWidth + 10;
		}

		if (displayPosition.contains("CENTER")) {
			screenX = ((sw - entityRenderWidth - displayWidth) / 2);
		}

		if (displayPosition.equals("CUSTOM")) {
			screenX = ConfigurationHandler.statusDisplayX + (entityRenderWidth / 2);
			screenY = ConfigurationHandler.statusDisplayY + entityRenderHeight + 10;
		}

		int scale = MathHelper.ceiling_double_int(h);

		if (entity instanceof EntityLiving) {
			if (((EntityLiving) entity).getLeashed()) {
				leashedToEntity = ((EntityLiving) entity).getLeashedToEntity();
				((EntityLiving) entity).setLeashedToEntity(null, false);
			}
		}

		float prevYawOffset = entity.renderYawOffset;
		float prevYaw = entity.rotationYaw;
		float prevPitch = entity.rotationPitch;
		float prevYawHead = entity.rotationYawHead;
		float prevPrevYahHead = entity.prevRotationYawHead;
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) screenX, (float) screenY, 50.0F);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-100.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(0.0f, 1.0F, 0.0F, 0.0F);
		entity.renderYawOffset = 0.0f;
		entity.rotationYaw = 0.0f;
		entity.rotationPitch = 0.0f;
		entity.rotationYawHead = 0.0f;
		entity.prevRotationYawHead = 0.0f;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		entity.renderYawOffset = prevYawOffset;
		entity.rotationYaw = prevYaw;
		entity.rotationPitch = prevPitch;
		entity.rotationYawHead = prevYawHead;
		entity.prevRotationYawHead = prevPrevYahHead;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

		if (entity instanceof EntityLiving && leashedToEntity != null) {
			((EntityLiving) entity).setLeashedToEntity(leashedToEntity, false);
			leashedToEntity = null;
		}
	}

	private boolean isUnsupportedDisplayType(String entityStatusDisplay) {
		return !entityStatusDisplay.equals("HEARTS") && !entityStatusDisplay.equals("NUMERIC");
	}

	private void drawNumericDisplayStyle() {
		ResourceLocation spriteLoc = new ResourceLocation(ToroHealthMod.MODID, "textures/gui/entityStatus.png");
		mc.renderEngine.bindTexture(spriteLoc);

		/*
		 * defines positions of each element from the top left position of
		 * status display
		 */
		int bgX = 0, bgY = 0, healthBarX = 2, healthBarY = 16, nameX = 50, nameY = 4, healthX = 50, healthY = 20;

		displayWidth = 100;
		displayHeight = 34;

		adjustForDisplayPositionSetting();

		Gui.drawModalRectWithCustomSizedTexture(screenX + bgX, screenY + bgY, 0.0f, 0.0f, displayWidth, displayHeight, 200.0f, 200.0f);

		Gui.drawModalRectWithCustomSizedTexture(screenX + healthBarX, screenY + healthBarY, 0.0f, 150.0f, 96, 16, 200.0f, 200.0f);

		int currentHealthWidth = (int) Math.ceil(96 * (entity.getHealth() / entity.getMaxHealth()));
		Gui.drawModalRectWithCustomSizedTexture(screenX + healthBarX, screenY + healthBarY, 0.0f, 100.0f, currentHealthWidth, 16, 200.0f, 200.0f);

		String name = getDisplayName();

		drawCenteredString(mc.fontRendererObj, name, screenX + nameX, screenY + nameY, 0xFFFFFF);
		drawCenteredString(mc.fontRendererObj, (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth(), screenX + healthX, screenY + healthY, 0xFFFFFF);
	}

	private void drawHeartsDisplay() {
		screenX = PADDING_FROM_EDGE;
		screenY = PADDING_FROM_EDGE;
		displayHeight = 74;
		displayWidth = 84;

		adjustForDisplayPositionSetting();

		drawName();
		drawHearts();
		drawArmor();
	}

	private void drawName() {
		String name = getDisplayName();
		drawString(mc.fontRendererObj, name, screenX, screenY, 0xFFFFFF);
		screenY += 10;
	}

	private int drawHearts() {
		mc.renderEngine.bindTexture(icons);
		int currentHealth = MathHelper.ceiling_float_int(entity.getHealth());
		entityHealth = currentHealth;
		int absorptionAmount = MathHelper.ceiling_float_int(entity.getAbsorptionAmount());
		int remainingAbsorption = absorptionAmount;

		float maxHealth = entity.getMaxHealth();

		int numRowsOfHearts = MathHelper.ceiling_float_int((maxHealth + (float) absorptionAmount) / 2.0F / 10.0F);
		int j2 = Math.max(10 - (numRowsOfHearts - 2), 3);

		for (int currentHeartBeingDrawn = MathHelper.ceiling_float_int((maxHealth + (float) absorptionAmount) / 2.0F) - 1; currentHeartBeingDrawn >= 0; --currentHeartBeingDrawn) {
			int texturePosX = 16;
			int flashingHeartOffset = 0;

			int rowsOfHearts = MathHelper.ceiling_float_int((float) (currentHeartBeingDrawn + 1) / 10.0F) - 1;
			int heartToDrawX = screenX + currentHeartBeingDrawn % 10 * 8;
			int heartToDrawY = screenY + rowsOfHearts * j2;

			int hardcoreModeOffset = 0;

			if (entity.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
				hardcoreModeOffset = 5;
			}

			this.drawTexturedModalRect(heartToDrawX, heartToDrawY, 16 + flashingHeartOffset * 9, 9 * hardcoreModeOffset, 9, 9);

			if (remainingAbsorption > 0) {
				if (remainingAbsorption == absorptionAmount && absorptionAmount % 2 == 1) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 153, 9 * hardcoreModeOffset, 9, 9);
					--remainingAbsorption;
				} else {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 144, 9 * hardcoreModeOffset, 9, 9);
					remainingAbsorption -= 2;
				}
			} else {
				if (currentHeartBeingDrawn * 2 + 1 < currentHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 36, 9 * hardcoreModeOffset, 9, 9);
				}

				if (currentHeartBeingDrawn * 2 + 1 == currentHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 45, 9 * hardcoreModeOffset, 9, 9);
				}
			}
		}

		screenY += (numRowsOfHearts - 1) * j2 + 10;

		return remainingAbsorption;
	}

	private void drawArmor() {
		mc.renderEngine.bindTexture(icons);

		int armor = entity.getTotalArmorValue();

		for (int i = 0; i < 10; ++i) {
			if (armor > 0) {
				int armorIconX = screenX + i * 8;

				/*
				 * determines whether armor is full, half, or empty icon
				 */
				if (i * 2 + 1 < armor) {
					this.drawTexturedModalRect(armorIconX, screenY, 34, 9, 9, 9);
				}

				if (i * 2 + 1 == armor) {
					this.drawTexturedModalRect(armorIconX, screenY, 25, 9, 9, 9);
				}

				if (i * 2 + 1 > armor) {
					this.drawTexturedModalRect(armorIconX, screenY, 16, 9, 9, 9);
				}
			}
		}

		screenY += 10;
	}

	private void adjustForDisplayPositionSetting() {
		int sh = viewport.getScaledHeight();
		int sw = viewport.getScaledWidth();

		if (displayPosition.equals("CUSTOM")) {
			screenX = ConfigurationHandler.statusDisplayX + entityRenderWidth;
			screenY = ConfigurationHandler.statusDisplayY;
			return;
		}

		if (displayPosition.contains("TOP")) {
			screenY = PADDING_FROM_EDGE;
		}

		if (displayPosition.contains("BOTTOM")) {
			screenY = sh - displayHeight - PADDING_FROM_EDGE;
		}

		if (displayPosition.contains("LEFT")) {
			screenX = entityRenderWidth + PADDING_FROM_EDGE;
		}

		if (displayPosition.contains("RIGHT")) {
			screenX = sw - displayWidth - PADDING_FROM_EDGE - entityRenderWidth - 10;
		}

		if (displayPosition.contains("CENTER")) {
			screenX = (sw - displayWidth) / 2;
		}
	}

	private boolean isHealthUpdateOver() {
		return Minecraft.getSystemTime() - lastSystemTime > 1000L;
	}

	private boolean isBeingHealed(int currentHealth) {
		return currentHealth > entityHealth && entity.hurtResistantTime > 0;
	}

	private boolean isBeingDamaged(int currentHealth) {
		return currentHealth < entityHealth && entity.hurtResistantTime > 0;
	}

	private String getDisplayName() {
		String name = entity.getDisplayName().getFormattedText();
		return name;
	}

	private void showHealthBar() {
		showHealthBar = true;
	}

	private void hideHealthBar() {
		showHealthBar = false;
	}
}
