package net.torocraft.torohealth.bars;

import net.minecraft.entity.LivingEntity;

public class BarState {

  public final LivingEntity entity;

  public float previousHealth;
  public float previousHealthDisplay;
  public float previousHealthDelay;
  public float lastDmg;
  public float lastHealth;
  public float lastDmgDelay;
  private float animationSpeed = 0;

  private static final float HEALTH_INDICATOR_DELAY = 10;

  public BarState(LivingEntity entity) {
    this.entity = entity;
  }

  public void tick() {
    if (this.lastDmgDelay > 0) {
      this.lastDmgDelay--;
    }
    if (this.previousHealthDelay > 0) {
      this.previousHealthDelay--;
    }

    if (lastHealth < 0.1) {
      lastHealth = entity.getHealth();
      lastDmg = 0;
    } else if (lastHealth != entity.getHealth()) {
      lastDmg = lastHealth - entity.getHealth();
      lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
      lastHealth = entity.getHealth();
      BarStates.PARTICLES.add(new BarParticle(entity, lastDmg));
    } else if (lastDmgDelay == 0.0F) {
      lastHealth = entity.getHealth();
      lastDmg = 0;
    }

    if (previousHealthDelay > 0) {
      float diff = previousHealthDisplay - entity.getHealth();
      if (diff > 0) {
        animationSpeed = diff / 10f;
      }
    } else if (previousHealthDelay < 1 && previousHealthDisplay > entity.getHealth()) {
      previousHealthDisplay -= animationSpeed;
    } else {
      previousHealthDisplay = entity.getHealth();
      previousHealth = entity.getHealth();
      previousHealthDelay = HEALTH_INDICATOR_DELAY;
    }
  }
}
