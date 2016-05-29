package net.torocraft.damageindicatorsmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEntityStatus extends Gui {

	private Minecraft mc;
	private EntityLivingBase entity;
	private int age = 0;
	private boolean showHealthBar = false;
	
	private final int TTL = 1500;
	
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
	public void drawHealthBar(RenderGameOverlayEvent.Post event) {
		if (!showHealthBar) {
			return;
		}
		age++;
		if (age > TTL) {
			hideHealthBar();
		}
		
		drawString(mc.fontRendererObj, entity.getName() + " " + (int)Math.ceil(entity.getHealth()) + "/" + (int)entity.getMaxHealth(), 2, 2, 0xFFFFFF);
	}
	
	private void showHealthBar() {
		showHealthBar = true;
	}
	
	private void hideHealthBar() {
		showHealthBar = false;
	}
}
