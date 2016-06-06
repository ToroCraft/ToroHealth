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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
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
	private final int PADDING_FROM_EDGE = 2;

	String displayPosition;
	int screenX = PADDING_FROM_EDGE, screenY = PADDING_FROM_EDGE;
	int displayHeight;
	int displayWidth;

	/*
	 * Entity drawing
	 */
	private EntityLivingBase tempEntity;
	private final int entityRenderWidth = 40;
	private final int entityRenderHeightUnit = 20;
	private final int entityRenderX = 20;

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
		displayPosition = ConfigurationHandler.statusDisplayPosition;

		if (isUnsupportedDisplayType(entityStatusDisplay)) {
			entityStatusDisplay = "HEARTS";
		}

		if (entityStatusDisplay.equals("NUMERIC")) {
			drawNumericDisplayStyle();
		} else if (entityStatusDisplay.equals("HEARTS")) {
			drawHeartsDisplay();
		}

		drawEntityOnScreen();
	}

	private void cloneEntity() {
		createNewEntityObject();
		copyEntityData();
	}

	protected void createNewEntityObject() {
		if (entity == null) {
			tempEntity = null;
			return;
		}
		try {
			tempEntity = ((EntityLivingBase) entity.getClass().getConstructor(new Class[] { World.class })
					.newInstance(new Object[] { mc.theWorld }));
		} catch (Exception e) {
			tempEntity = null;
		}
	}

	protected void copyEntityData() {
		if (tempEntity == null || entity == null) {
			return;
		}
		NBTTagCompound nbttagcompound = entity.writeToNBT(new NBTTagCompound());
		nbttagcompound.removeTag("Dimension");
		tempEntity.readFromNBT(nbttagcompound);
	}

	public void drawEntityOnScreen() {

		cloneEntity();

		if (tempEntity == null) {
			return;
		}

		float heightMultiplier = (float) Math.ceil(tempEntity.height);
		heightMultiplier = Math.max(heightMultiplier, 2.0f);

		int sw = viewport.getScaledWidth(), sh = viewport.getScaledHeight();

		int entityRenderHeight = MathHelper.ceiling_float_int(entityRenderHeightUnit * heightMultiplier);

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

		int scale = 20;
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) screenX, (float) screenY, 50.0F);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-100.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(0.0f, 1.0F, 0.0F, 0.0F);
		tempEntity.renderYawOffset = 0.0f;
		tempEntity.rotationYaw = 0.0f;
		tempEntity.rotationPitch = 0.0f;
		tempEntity.rotationYawHead = 0.0f;
		tempEntity.prevRotationYawHead = 0.0f;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(tempEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
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

		/*
		 * defines positions of each element from the top left position of
		 * status display
		 */
		int bgX = 0, bgY = 0, healthBarX = 2, healthBarY = 16, nameX = 50, nameY = 4, healthX = 50, healthY = 20;

		displayWidth = 100;
		displayHeight = 34;

		adjustForDisplayPositionSetting();

		Gui.drawModalRectWithCustomSizedTexture(screenX + bgX, screenY + bgY, 0.0f, 0.0f, displayWidth, displayHeight,
				200.0f, 200.0f);

		Gui.drawModalRectWithCustomSizedTexture(screenX + healthBarX, screenY + healthBarY, 0.0f, 150.0f, 96, 16,
				200.0f, 200.0f);

		int currentHealthWidth = (int) Math.ceil(96 * (entity.getHealth() / entity.getMaxHealth()));
		Gui.drawModalRectWithCustomSizedTexture(screenX + healthBarX, screenY + healthBarY, 0.0f, 100.0f,
				currentHealthWidth, 16, 200.0f, 200.0f);

		String name = getDisplayName();

		drawCenteredString(mc.fontRendererObj, name, screenX + nameX, screenY + nameY, 0xFFFFFF);
		drawCenteredString(mc.fontRendererObj, (int) Math.ceil(entity.getHealth()) + "/" + (int) entity.getMaxHealth(),
				screenX + healthX, screenY + healthY, 0xFFFFFF);
	}

	private void drawHeartsDisplay() {
		mc.renderEngine.bindTexture(ICONS);
		int currentHealth = MathHelper.ceiling_float_int(entity.getHealth());

		/*
		 * boolean isFlashFrame = this.healthUpdateCounter > (long)
		 * this.updateCounter && (this.healthUpdateCounter - (long)
		 * this.updateCounter) / 3L % 2L == 1L;
		 * 
		 * if (isBeingDamaged(currentHealth)) { lastSystemTime =
		 * Minecraft.getSystemTime(); healthUpdateCounter = (long)
		 * (updateCounter + 20); } else if (isBeingHealed(currentHealth)) {
		 * lastSystemTime = Minecraft.getSystemTime(); healthUpdateCounter =
		 * (long) (updateCounter + 10); }
		 * 
		 * if (isHealthUpdateOver()) { entityHealth = currentHealth;
		 * lastEntityHealth = currentHealth; lastSystemTime =
		 * Minecraft.getSystemTime(); }
		 */

		screenX = PADDING_FROM_EDGE;
		screenY = PADDING_FROM_EDGE;
		displayHeight = 74;
		displayWidth = 84;

		adjustForDisplayPositionSetting();

		entityHealth = currentHealth;
		int preUpdateHealth = lastEntityHealth; // was j
		rand.setSeed((long) (updateCounter * 312871));

		int leftEdgeStatusBar = 0;
		int topEdgeStatusBar = 10;

		float maxHealth = entity.getMaxHealth();
		int absorptionAmount = MathHelper.ceiling_float_int(entity.getAbsorptionAmount());

		int numRowsOfHearts = MathHelper.ceiling_float_int((maxHealth + (float) absorptionAmount) / 2.0F / 10.0F);
		int j2 = Math.max(10 - (numRowsOfHearts - 2), 3);

		int topOfArmor = screenY + topEdgeStatusBar + (numRowsOfHearts - 1) * j2 + 10;
		int remainingAbsorption = absorptionAmount;
		int armor = entity.getTotalArmorValue();

		int regenCounter = -1;

		if (entity.isPotionActive(MobEffects.REGENERATION)) {
			regenCounter = this.updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
		}

		for (int i = 0; i < 10; ++i) {
			if (armor > 0) {
				int armorIconX = screenX + leftEdgeStatusBar + i * 8;

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

			/*
			 * is buggy; removing for now
			 * 
			 * if (entity.isPotionActive(MobEffects.POISON)) { texturePosX +=
			 * 36; } else if (entity.isPotionActive(MobEffects.WITHER)) {
			 * texturePosX += 72; }
			 */

			int flashingHeartOffset = 0;

			/*
			 * if (isFlashFrame) { flashingHeartOffset = 1; }
			 */

			int rowsOfHearts = MathHelper.ceiling_float_int((float) (currentHeartBeingDrawn + 1) / 10.0F) - 1;
			int heartToDrawX = screenX + leftEdgeStatusBar + currentHeartBeingDrawn % 10 * 8;
			int heartToDrawY = screenY + topEdgeStatusBar + rowsOfHearts * j2;

			/*
			 * if (currentHealth <= 4) { heartToDrawY += this.rand.nextInt(2); }
			 * 
			 * if (remainingAbsorption <= 0 && currentHeartBeingDrawn ==
			 * regenCounter) { heartToDrawY -= 2; }
			 */

			int hardcoreModeOffset = 0;

			if (entity.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
				hardcoreModeOffset = 5;
			}

			this.drawTexturedModalRect(heartToDrawX, heartToDrawY, 16 + flashingHeartOffset * 9, 9 * hardcoreModeOffset,
					9, 9);

			/*
			 * if (isFlashFrame) { if (currentHeartBeingDrawn * 2 + 1 <
			 * preUpdateHealth) { this.drawTexturedModalRect(heartToDrawX,
			 * heartToDrawY, texturePosX + 54, 9 * hardcoreModOffset, 9, 9); }
			 * 
			 * if (currentHeartBeingDrawn * 2 + 1 == preUpdateHealth) {
			 * this.drawTexturedModalRect(heartToDrawX, heartToDrawY,
			 * texturePosX + 63, 9 * hardcoreModOffset, 9, 9); } }
			 */

			if (remainingAbsorption > 0) {
				if (remainingAbsorption == absorptionAmount && absorptionAmount % 2 == 1) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 153, 9 * hardcoreModeOffset, 9,
							9);
					--remainingAbsorption;
				} else {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 144, 9 * hardcoreModeOffset, 9,
							9);
					remainingAbsorption -= 2;
				}
			} else {
				if (currentHeartBeingDrawn * 2 + 1 < currentHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 36, 9 * hardcoreModeOffset, 9,
							9);
				}

				if (currentHeartBeingDrawn * 2 + 1 == currentHealth) {
					this.drawTexturedModalRect(heartToDrawX, heartToDrawY, texturePosX + 45, 9 * hardcoreModeOffset, 9,
							9);
				}
			}
		}

		String name = getDisplayName();

		drawString(mc.fontRendererObj, name, screenX, screenY, 0xFFFFFF);
	}

	private void adjustForDisplayPositionSetting() {
		int sh = viewport.getScaledHeight();
		int sw = viewport.getScaledWidth();

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
