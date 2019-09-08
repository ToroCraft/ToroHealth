package net.torocraft.torohealth.bars;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.torocraft.torohealth.ToroHealth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BarState {

  private static final Map<Integer, BarState> states = new HashMap<>();

  public float previousHealth;
  public float previousHealthDisplay;
  public float previousHealthDelay;
  public float lastDmg;
  public float lastHealth;
  public float lastDmgDelay;
  private float animationSpeed = 0;

  private static final float HEALTH_INDICATOR_DELAY = 40;

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
    if (ToroHealth.selectedEntity != null){
      updateState(ToroHealth.selectedEntity);
    }
    for (Iterator<EntityTracker.TrackedEntity> i = EntityTracker.INSTANCE.iterator(); i.hasNext(); ) {
      EntityTracker.TrackedEntity t = i.next();
      updateState(t.entity);
    }
    if (tickCount % 200 == 0) {
      cleanCache();
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

    if (state.animationSpeed == 0) {
      state.animationSpeed = entity.getHealth() / 125;
    }

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
      state.animationSpeed = (state.previousHealthDisplay - entity.getHealth()) / 40;
    } else if (state.previousHealthDelay < 1 && state.previousHealthDisplay > entity.getHealth()) {
      state.previousHealthDisplay -= state.animationSpeed;
    } else {
      state.previousHealthDisplay = entity.getHealth();
      state.previousHealth = entity.getHealth();
      state.previousHealthDelay = HEALTH_INDICATOR_DELAY;
    }
  }
}
