package net.torocraft.torohealthmod.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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

	private final int TTL = 150;

	private ScaledResolution viewport;

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
		if (age > TTL || entityStatusDisplay.equals("OFF")) {
			hideHealthBar();
		}

		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE) {
			return;
		}

		viewport = new ScaledResolution(mc);

		if (isUnsupportedDisplayType(entityStatusDisplay)) {
			entityStatusDisplay = "HEARTS";
		}

		drawEntityOnScreen(200, 200, 30);

		if (entityStatusDisplay.equals("NUMERIC")) {
			drawNumericDisplayStyle();
		} else if (entityStatusDisplay.equals("HEARTS")) {
			drawHeartsDisplay();
		}
	}

	public void drawEntityOnScreen(int posX, int posY, int scale) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX, (float) posY, 50.0F);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = entity.renderYawOffset;
		float f1 = entity.rotationYaw;
		float f2 = entity.rotationPitch;
		float f3 = entity.prevRotationYawHead;
		float f4 = entity.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (50 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		entity.renderYawOffset = (float) Math.atan((double) (50 / 40.0F)) * 20.0F;
		entity.rotationYaw = (float) Math.atan((double) (50 / 40.0F)) * 40.0F;
		entity.rotationPitch = -((float) Math.atan((double) (50 / 40.0F))) * 20.0F;
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		entity.renderYawOffset = f;
		entity.rotationYaw = f1;
		entity.rotationPitch = f2;
		entity.prevRotationYawHead = f3;
		entity.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private boolean isUnsupportedDisplayType(String entityStatusDisplay) {
		return !entityStatusDisplay.equals("HEARTS") && !entityStatusDisplay.equals("NUMERIC");
	}

	private void drawNumericDisplayStyle() {
		ResourceLocation spriteLoc = new ResourceLocation(ToroHealthMod.MODID, "textures/gui/entityStatus.png");
		mc.renderEngine.bindTexture(spriteLoc);

		int bgX = 2, bgY = 2, healthBarX = 4, healthBarY = 18, healthBarWidth = 100, healthBarHeight = 34, nameX = 52,
				nameY = 6, healthX = 52, healthY = 22;

		String displayPosition = ConfigurationHandler.statusDisplayPosition;

		int sw = viewport.getScaledWidth(), sh = viewport.getScaledHeight();

		if (displayPosition.equals("TOP CENTER")) {
			bgX = (sw - healthBarWidth) / 2;
			healthBarX = bgX + 2;
			nameX = healthX = sw / 2;
		} else if (displayPosition.equals("TOP RIGHT")) {
			bgX = sw - healthBarWidth - 2;
			healthBarX = bgX + 2;
			nameX = healthX = sw - (healthBarWidth / 2);
		} else if (displayPosition.equals("BOTTOM LEFT") || displayPosition.equals("BOTTOM RIGHT")) {
			bgY = sh - healthBarHeight - 2;
			healthBarY = bgY + 16;
			nameY = bgY + 4;
			healthY = healthBarY + 4;
		}

		if (displayPosition.equals("BOTTOM RIGHT")) {
			bgX = sw - healthBarWidth - 2;
			healthBarX = bgX + 2;
			nameX = healthX = sw - (healthBarWidth / 2);
		}

		Gui.drawModalRectWithCustomSizedTexture(bgX, bgY, 0.0f, 0.0f, healthBarWidth, healthBarHeight, 200.0f, 200.0f);

		Gui.drawModalRectWithCustomSizedTexture(healthBarX, healthBarY, 0.0f, 150.0f, 96, 16, 200.0f, 200.0f);

		int currentHealthWidth = (int) Math.ceil(96 * (entity.getHealth() / entity.getMaxHealth()));
		Gui.drawModalRectWithCustomSizedTexture(healthBarX, healthBarY, 0.0f, 100.0f, currentHealthWidth, 16, 200.0f,
				200.0f);

		String name = getDisplayName();

		drawCenteredString(mc.fontRendererObj, name, nameX, nameY, 0xFFFFFF);
		drawCenteredString(mc.fontRendererObj, (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth(),
				healthX, healthY, 0xFFFFFF);
	}

	private void drawHeartsDisplay() {
		mc.renderEngine.bindTexture(ICONS);
		int currentHealth = MathHelper.ceiling_float_int(entity.getHealth());
		boolean isFlashFrame = this.healthUpdateCounter > (long) this.updateCounter
				&& (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;

		if (isBeingDamaged(currentHealth)) {
			lastSystemTime = Minecraft.getSystemTime();
			healthUpdateCounter = (long) (updateCounter + 20);
		} else if (isBeingHealed(currentHealth)) {
			lastSystemTime = Minecraft.getSystemTime();
			healthUpdateCounter = (long) (updateCounter + 10);
		}

		if (isHealthUpdateOver()) {
			entityHealth = currentHealth;
			lastEntityHealth = currentHealth;
			lastSystemTime = Minecraft.getSystemTime();
		}

		entityHealth = currentHealth;
		int preUpdateHealth = lastEntityHealth; // was j
		rand.setSeed((long) (updateCounter * 312871));

		int leftEdgeStatusBar = 2;
		int topEdgeStatusBar = 12;

		float maxHealth = entity.getMaxHealth();
		int absorptionAmount = MathHelper.ceiling_float_int(entity.getAbsorptionAmount());

		int numRowsOfHearts = MathHelper.ceiling_float_int((maxHealth + (float) absorptionAmount) / 2.0F / 10.0F);
		int j2 = Math.max(10 - (numRowsOfHearts - 2), 3);

		int topOfArmor = topEdgeStatusBar - (numRowsOfHearts - 1) * j2 - 10;
		int remainingAbsorption = absorptionAmount;
		int armor = entity.getTotalArmorValue();

		int regenCounter = -1;

		if (entity.isPotionActive(MobEffects.REGENERATION)) {
			regenCounter = this.updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
		}

		for (int i = 0; i < 10; ++i) {
			if (armor > 0) {
				int armorIconX = leftEdgeStatusBar + i * 8;

				/*
				 * determines whether armor is full, half, or empty icon
				 */
				if (i * 2 + 1 < armor) {
					this.drawTexturedModalRect(armorIconX, topOfArmor, 34, 9, 9, 9);
				}

				if (i * 2 + 1 == armor) {
					this.drawTexturedModalRect(armorIconX, topOfArmor, 25, 9, 9, 9);
				}

				if (i * 2 + 1 > armor) {
					this.drawTexturedModalRect(armorIconX, topOfArmor, 16, 9, 9, 9);
				}
			}
		}

		for (int currentHeartBeingDrawn = MathHelper.ceiling_float_int((maxHealth + (float) absorptionAmount) / 2.0F)
				- 1; currentHeartBeingDrawn >= 0; --currentHeartBeingDrawn) {
			int texturePosX = 16;

			if (entity.isPotionActive(MobEffects.POISON)) {
				texturePosX += 36;
			} else if (entity.isPotionActive(MobEffects.WITHER)) {
				texturePosX += 72;
			}

			int flashingHeartOffset = 0;

			if (isFlashFrame) {
				flashingHeartOffset = 1;
			}

			int rowsOfHearts = MathHelper.ceiling_float_int((float) (currentHeartBeingDrawn + 1) / 10.0F) - 1;
			int heartToDrawX = leftEdgeStatusBar + currentHeartBeingDrawn % 10 * 8;
			int heartToDrawY = topEdgeStatusBar + rowsOfHearts * j2;

			if (currentHealth <= 4) {
				heartToDrawY += this.rand.nextInt(2);
			}

			if (remainingAbsorption <= 0 && currentHeartBeingDrawn == regenCounter) {
				heartToDrawY -= 2;
			}

			int hardcoreModOffset = 0;

			if (entity.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
				hardcoreModOffset = 5;
			}

			this.drawTexturedModalRect(heartToDrawX, heartToDrawY, 16 + flashingHeartOffset * 9, 9 * hardcoreModOffset,
					9, 9);

			if (isFlashFrame) {
				if (currentHeartBeingDrawn * 2 + 1 < preUpdateHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 54, 9 * hardcoreModOffset, 9,
							9);
				}

				if (currentHeartBeingDrawn * 2 + 1 == preUpdateHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 63, 9 * hardcoreModOffset, 9,
							9);
				}
			}

			if (remainingAbsorption > 0) {
				if (remainingAbsorption == absorptionAmount && absorptionAmount % 2 == 1) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 153, 9 * hardcoreModOffset, 9,
							9);
					--remainingAbsorption;
				} else {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 144, 9 * hardcoreModOffset, 9,
							9);
					remainingAbsorption -= 2;
				}
			} else {
				if (currentHeartBeingDrawn * 2 + 1 < currentHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 36, 9 * hardcoreModOffset, 9,
							9);
				}

				if (currentHeartBeingDrawn * 2 + 1 == currentHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 45, 9 * hardcoreModOffset, 9,
							9);
				}
			}
		}

		String name = getDisplayName();

		drawString(mc.fontRendererObj, name, 2, 2, 0xFFFFFF);
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
		String name = entity.getName();
		if (entity.hasCustomName()) {
			return entity.getCustomNameTag();
		}
		if (entity instanceof EntityVillager) {
			int profId = ((EntityVillager) entity).getProfession();
			int careerId = ((EntityVillager) entity).getEntityData().getInteger("Career");

			switch (profId) {
			case 0:
				switch (careerId) {
				case 1:
					name = "Farmer";
					break;
				case 2:
					name = "Fisherman";
					break;
				case 3:
					name = "Shepherd";
					break;
				case 4:
					name = "Fletcher";
					break;
				default:
					name = "Farmer";
					break;
				}
				break;
			case 1:
				name = "Librarian";
				break;
			case 2:
				name = "Cleric";
				break;
			case 3:
				switch (careerId) {
				case 1:
					name = "Armorer";
					break;
				case 2:
					name = "Weapon Smith";
					break;
				case 3:
					name = "Tool Smith";
					break;
				default:
					name = "Blacksmith";
				}
				break;
			case 4:
				switch (careerId) {
				case 1:
					name = "Butcher";
					break;
				case 2:
					name = "Leatherworker";
					break;
				default:
					name = "Butcher";
					break;
				}
				break;
			default:
				name = "Villager";
				break;
			}
		}
		return name;
	}

	private void showHealthBar() {
		showHealthBar = true;
	}

	private void hideHealthBar() {
		showHealthBar = false;
	}
}
