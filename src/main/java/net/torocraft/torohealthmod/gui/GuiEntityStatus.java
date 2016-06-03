package net.torocraft.torohealthmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
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
		String entityStatusDisplay = ConfigurationHandler.entityStatusDisplay;
		age++;
		if (age > TTL || entityStatusDisplay.equals("OFF")) {
			hideHealthBar();
		}
		
		if(event.isCancelable() || event.getType() != ElementType.EXPERIENCE) {
	    	return;
	    }
		ResourceLocation spriteLoc = new ResourceLocation(ToroHealthMod.MODID, "textures/gui/entityStatus.png");
    	this.mc.renderEngine.bindTexture(spriteLoc);
    	if (entityStatusDisplay.equals("STANDARD")) {
    		drawStandardDisplayStyle();
    	} else if (entityStatusDisplay.equals("COMPACT")) {
    		drawCompactDisplayStyle();
    	}
	}

	private void drawStandardDisplayStyle() {
		Gui.drawModalRectWithCustomSizedTexture(2, 2, 0.0f, 0.0f, 150, 40, 200.0f, 200.0f);
		
		Gui.drawModalRectWithCustomSizedTexture(4, 24, 0.0f, 150.0f, 146, 16, 200.0f, 200.0f);
		
		int currentHealthWidth = (int)Math.ceil(146 * (entity.getHealth() / entity.getMaxHealth()));
		Gui.drawModalRectWithCustomSizedTexture(4, 24, 0.0f, 100.0f, currentHealthWidth, 16, 200.0f, 200.0f);

		String name = getDisplayName();
		
		drawCenteredString(mc.fontRendererObj, name, 77, 8, 0xFFFFFF);
		drawCenteredString(mc.fontRendererObj, (int)Math.ceil(entity.getHealth()) + "/" + (int)entity.getMaxHealth(), 77, 28, 0xFFFFFF);
	}

	private void drawCompactDisplayStyle() {
		Gui.drawModalRectWithCustomSizedTexture(2, 14, 0.0f, 150.0f, 100, 16, 200.0f, 200.0f);
		
		int currentHealthWidth = (int)Math.ceil(100 * (entity.getHealth() / entity.getMaxHealth()));
		Gui.drawModalRectWithCustomSizedTexture(2, 14, 0.0f, 100.0f, currentHealthWidth, 16, 200.0f, 200.0f);

		String name = getDisplayName();
		
		drawString(mc.fontRendererObj, name, 2, 2, 0xFFFFFF);
		drawCenteredString(mc.fontRendererObj, (int)Math.ceil(entity.getHealth()) + "/" + (int)entity.getMaxHealth(), 52, 18, 0xFFFFFF);
	}

	private String getDisplayName() {
		String name = entity.getName();
		if (entity instanceof EntityVillager) {
			int profId = ((EntityVillager)entity).getProfession();
			int careerId = ((EntityVillager)entity).getEntityData().getInteger("Career");
			
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
