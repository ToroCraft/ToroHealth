package net.torocraft.damageindicatorsmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.damageindicatorsmod.DamageIndicatorsMod;

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
		
		if(event.isCancelable() || event.getType() != ElementType.EXPERIENCE) {
	    	return;
	    }
		ResourceLocation spriteLoc = new ResourceLocation(DamageIndicatorsMod.MODID, "textures/gui/entityStatus.png");
    	this.mc.renderEngine.bindTexture(spriteLoc);
		Gui.drawModalRectWithCustomSizedTexture(2, 2, 0.0f, 0.0f, 200, 50, 200.0f, 200.0f);
		
		Gui.drawModalRectWithCustomSizedTexture(4, 29, 0.0f, 150.0f, 196, 21, 200.0f, 200.0f);
		
		int currentHealthWidth = (int)Math.ceil(196 * (entity.getHealth() / entity.getMaxHealth()));
		Gui.drawModalRectWithCustomSizedTexture(4, 29, 0.0f, 100.0f, currentHealthWidth, 21, 200.0f, 200.0f);

		drawCenteredString(mc.fontRendererObj, entity.getName(), 102, 7, 0xFFFFFF);
		drawCenteredString(mc.fontRendererObj, (int)Math.ceil(entity.getHealth()) + "/" + (int)entity.getMaxHealth(), 102, 40, 0xFFFFFF);
	}
	
	private void showHealthBar() {
		showHealthBar = true;
	}
	
	private void hideHealthBar() {
		showHealthBar = false;
	}
}
