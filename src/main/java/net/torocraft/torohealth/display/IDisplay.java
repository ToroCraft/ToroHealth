package net.torocraft.torohealth.display;

import net.minecraft.entity.LivingEntity;

public interface IDisplay {

    void setEntity(LivingEntity entity);

    void setPosition(int x, int y);

    void draw();
}
