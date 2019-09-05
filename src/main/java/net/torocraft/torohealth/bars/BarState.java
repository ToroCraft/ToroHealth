package net.torocraft.torohealth.bars;

import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

class BarState {

  private static final Map<Integer, BarState> states = new HashMap<>();

  public float showOnHurtDelay;
  public float previousHealth;
  public float previousHealthDisplay;
  public float previousHealthDelay;
  public float lastDmg;
  public float lastHealth;
  public float lastDmgDelay;

  public static BarState getState(Entity entity) {
    int id = entity.getEntityId();
    BarState state = states.get(id);
    if (state == null) {
      state = new BarState();
      states.put(id, state);
    }
    return state;
  }
}
