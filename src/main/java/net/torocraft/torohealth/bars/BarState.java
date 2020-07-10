package net.torocraft.torohealth.bars;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.ToroHealth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class BarState {

  private static final Map<Integer, BarState> states = new HashMap<>();

  public float previousHealth;
  public float previousHealthDisplay;
  public float previousHealthDelay;
  public float lastDmg;
  public float lastHealth;
  public float lastDmgDelay;
  public double animationShift = 0.0;
  private float animationSpeed = 0;

  private static final float HEALTH_INDICATOR_DELAY = 20;
  private static final Random RANDOM = new Random();
  
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

  public static void tick() {
    LivingEntity selectedEntity = ToroHealth.HUD.getEntity();
    if (selectedEntity != null) {
      tick(selectedEntity);
    }
    for (Iterator<EntityTracker.TrackedEntity> i = EntityTracker.INSTANCE.iterator(); i.hasNext(); ) {
      EntityTracker.TrackedEntity t = i.next();
      tick(t.entity);
    }
    states.forEach((id, state) -> state.doTick());
    if (tickCount % 200 == 0) {
      cleanCache();
    }
    tickCount++;
  }
  
  private void doTick() {
	if (this.lastDmgDelay > 0) {
	  this.lastDmgDelay--;
	}
	if (this.previousHealthDelay > 0) {
	  this.previousHealthDelay--;
	}
  }

  private static void cleanCache() {
    states.entrySet().removeIf(BarState::stateExpired);
  }

  private static boolean stateExpired(Map.Entry<Integer, BarState> entry) {
    if (entry.getValue() == null) {
      return true;
    }
    
    MinecraftClient minecraft = MinecraftClient.getInstance();
    Entity entity = minecraft.world.getEntityById(entry.getKey());

    if (!(entity instanceof LivingEntity)) {
      return true;
    }

    if (!minecraft.world.isChunkLoaded(entity.getBlockPos())) {
      return true;
    }

    return !entity.isAlive();
  }

  private static void tick(LivingEntity entity) {
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
      state.animationShift = 30 * RANDOM.nextDouble() - 15;
      state.lastHealth = entity.getHealth();
    } else if (state.lastDmgDelay == 0.0F) {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    }

    if (state.previousHealthDelay > 0) {
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
