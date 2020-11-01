package net.torocraft.torohealth.bars;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class BarStates {

  private static final Map<Integer, BarState> STATES = new HashMap<>();
  private static int tickCount = 0;

  public static BarState getState(LivingEntity entity) {
    int id = entity.getEntityId();
    BarState state = STATES.get(id);
    if (state == null) {
      state = new BarState(entity);
      STATES.put(id, state);
    }
    return state;
  }

  public static void tick() {
    for (BarState state : STATES.values()) {
      state.tick();
    }

    if (tickCount % 200 == 0) {
      cleanCache();
    }
    tickCount++;
  }

  private static void cleanCache() {
    STATES.entrySet().removeIf(BarStates::stateExpired);
  }

  public static BlockPos getEntityPos(Entity e) {
    return e.func_233580_cy_();
  }

  private static boolean stateExpired(Map.Entry<Integer, BarState> entry) {
    if (entry.getValue() == null) {
      return true;
    }

    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.world.getEntityByID(entry.getKey());

    if (!(entity instanceof LivingEntity)) {
      return true;
    }

    if (entity.getDistanceSq(minecraft.player) > (100 * 100)) {
      return true;
    }

    return !entity.isAlive();
  }

}
