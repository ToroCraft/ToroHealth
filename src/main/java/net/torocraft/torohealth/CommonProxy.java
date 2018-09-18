package net.torocraft.torohealth;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torohealth.network.MessageEntityStatsRequest;
import net.torocraft.torohealth.network.MessageEntityStatsResponse;
import net.torocraft.torohealth.network.MessageLivingHurt;

public class CommonProxy {

  public void preInit(FMLPreInitializationEvent e) {

  }

  public void init(FMLInitializationEvent e) {
    int packetId = 0;
    MessageLivingHurt.init(packetId++);
    MessageEntityStatsRequest.init(packetId++);
    MessageEntityStatsResponse.init(packetId++);
  }

  public void postInit(FMLPostInitializationEvent e) {

  }

  public void displayDamageDealt(EntityLivingBase entity) {

  }

  public void displayDamageDealt(EntityLivingBase entity, float damage) {

  }

  public void setEntityInCrosshairs() {

  }

  public EntityPlayer getPlayer() {
    return null;
  }

}
