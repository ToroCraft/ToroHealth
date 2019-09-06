package net.torocraft.torohealth.bars;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BarState {

  private static final Map<Integer, BarState> states = new HashMap<>();

  public float showOnHurtDelay;
  public float previousHealth;
  public float previousHealthDisplay;
  public float previousHealthDelay;
  public float lastDmg;
  public float lastHealth;
  public float lastDmgDelay;

  private static final float HEALTH_INDICATOR_DELAY = 80;
  private static final float HEALTH_ANIMATION_SPEED = 0.08f;

  public static BarState getState(Entity entity) {
    int id = entity.getEntityId();
    BarState state = states.get(id);
    if (state == null) {
      state = new BarState();
      states.put(id, state);
    }
    return state;
  }

  private static int tickCount = 0;

  public static void updateState() {
    for (Iterator<EntityTracker.TrackedEntity> i = EntityTracker.INSTANCE.iterator(); i.hasNext(); ) {
      EntityTracker.TrackedEntity t = i.next();
      updateState(t.entity);
    }

    if (tickCount % 100 == 0) {
      cleanCache();
      System.out.println("Entity State Cache [" + states.size() + "]  Tracked Entities [" + EntityTracker.INSTANCE.size() + "]");
    }

    tickCount++;
  }

  private static void cleanCache() {
    states.entrySet().removeIf(BarState::stateExpired);
  }

  private static boolean stateExpired(Map.Entry<Integer, BarState> entry) {
    if (entry.getValue() == null) {
      return true;
    }

    World world = MinecraftClient.getInstance().world;
    Entity entity = world.getEntityById(entry.getKey());

    if (!((entity instanceof LivingEntity))) {
      return true;
    }

    if (!world.isBlockLoaded(entity.getBlockPos())) {
      return true;
    }

    return !entity.isAlive();
  }

  private static void updateState(LivingEntity entity) {
    BarState state = BarState.getState(entity);
    if (state.lastHealth < 0.1) {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    } else if (state.lastHealth != entity.getHealth()) {
      state.lastDmg = state.lastHealth - entity.getHealth();
      state.lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
      state.lastHealth = entity.getHealth();
    } else if (state.lastDmgDelay > -1) {
      state.lastDmgDelay--;
    } else {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    }

    if (state.previousHealthDelay > 0) {
      state.previousHealthDelay--;
    } else if (state.previousHealthDelay < 1 && state.previousHealthDisplay > entity.getHealth()) {
      state.previousHealthDisplay -= HEALTH_ANIMATION_SPEED;
    } else {
      state.previousHealthDisplay = entity.getHealth();
      state.previousHealth = entity.getHealth();
      state.previousHealthDelay = HEALTH_INDICATOR_DELAY;
    }
  }
}
