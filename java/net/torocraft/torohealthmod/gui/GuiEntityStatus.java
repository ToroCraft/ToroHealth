package net.torocraft.torohealthmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.torohealthmod.config.ConfigurationHandler;
import net.torocraft.torohealthmod.display.EntityDisplay;
import net.torocraft.torohealthmod.display.HeartsDisplay;
import net.torocraft.torohealthmod.display.NumericDisplay;
import net.torocraft.torohealthmod.display.ToroHealthDisplay;

public class GuiEntityStatus extends Gui {

	private static final int PADDING_FROM_EDGE = 2;

	private final Minecraft mc;
	private final ToroHealthDisplay entityDisplay;
	private final ToroHealthDisplay heartsDisplay;
	private final ToroHealthDisplay numericDisplay;

	private EntityLivingBase entity;
	private int age = 0;
	private boolean showHealthBar = false;

	int screenX = PADDING_FROM_EDGE;
	int screenY = PADDING_FROM_EDGE;

	int displayHeight;
	int displayWidth;

	public GuiEntityStatus() {
		this(Minecraft.getMinecraft());
	}

	public GuiEntityStatus(Minecraft mc) {
		this.mc = mc;
		entityDisplay = new EntityDisplay(mc, this);
		heartsDisplay = new HeartsDisplay(mc, this);
		numericDisplay = new NumericDisplay(mc, this);

		entityDisplay.setPosition(50, 50);

		heartsDisplay.setPosition(100, 150);
		numericDisplay.setPosition(210, 150);
	}

	@SubscribeEvent
	public void drawHealthBar(RenderGameOverlayEvent.Pre event) {
		if (!showHealthBar || event.getType() != ElementType.CHAT) {
			return;
		}
		updateGuiAge();
		draw();
	}

	private void updateGuiAge() {
		String entityStatusDisplay = ConfigurationHandler.entityStatusDisplay;
		age = age + 15;
		if (age > ConfigurationHandler.hideDelay || entityStatusDisplay.equals("OFF")) {
			hideHealthBar();
		}
	}

	private void draw() {

		adjustForDisplayPositionSetting();

		if (ConfigurationHandler.showEntityModel) {
			entityDisplay.draw();
		}

		// if("NUMERIC".equals(ConfigurationHandler.entityStatusDisplay)){
		numericDisplay.draw();
		// }else{
		heartsDisplay.draw();
		// }
	}

	private void adjustForDisplayPositionSetting() {
		ScaledResolution viewport = new ScaledResolution(mc);
		int entityRenderWidth = 0;

		String displayPosition = ConfigurationHandler.statusDisplayPosition;

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

	private void showHealthBar() {
		showHealthBar = true;
	}

	private void hideHealthBar() {
		showHealthBar = false;
	}

	public void setEntity(EntityLivingBase entityToTrack) {
		showHealthBar();
		age = 0;
		if (entity != null && entity.getUniqueID().equals(entityToTrack.getUniqueID())) {
			return;
		}
		entity = entityToTrack;
		entityDisplay.setEntity(entity);
		heartsDisplay.setEntity(entity);
		numericDisplay.setEntity(entity);
	}
}
