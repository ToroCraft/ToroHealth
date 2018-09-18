package net.torocraft.torohealth.display;

import net.minecraft.entity.EntityLivingBase;

public interface ToroHealthDisplay {

  void setEntity(EntityLivingBase entity);

  void setPosition(int x, int y);

  void draw();
}