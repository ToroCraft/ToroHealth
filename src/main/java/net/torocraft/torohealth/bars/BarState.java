package net.torocraft.torohealth.bars;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.ToroHealth;

public class BarState {

  public final LivingEntity entity;

  public float health;
  public float previousHealth;
  public float previousHealthDisplay;
  public float previousHealthDelay;
  public int lastDmg;
  public int lastDmgCumulative;
  public float lastHealth;
  public float lastDmgDelay;
  private float animationSpeed = 0;

  private static final float HEALTH_INDICATOR_DELAY = 10;

  public BarState(LivingEntity entity) {
    this.entity = entity;
  }

  public void tick() {
    health = Math.min(entity.getHealth(), entity.getMaxHealth());
    incrementTimers();

    if (lastHealth < 0.1) {
      reset();

    } else if (lastHealth != health) {
      handleHealthChange();

    } else if (lastDmgDelay == 0.0F) {
      reset();
    }

    updateAnimations();
  }

  private void reset() {
    lastHealth = health;
    lastDmg = 0;
    lastDmgCumulative = 0;
  }

  private void incrementTimers() {
    if (this.lastDmgDelay > 0) {
      this.lastDmgDelay--;
    }
    if (this.previousHealthDelay > 0) {
      this.previousHealthDelay--;
    }
  }

  private void handleHealthChange() {
    lastDmg = Mth.ceil(lastHealth) - Mth.ceil(health);
    lastDmgCumulative += lastDmg;

    lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
    lastHealth = health;
    if (ToroHealth.CONFIG.particle.show) {
      BarStates.PARTICLES.add(new BarParticle(entity, lastDmg));
    }
  }

  private void updateAnimations() {
    if (previousHealthDelay > 0) {
      float diff = previousHealthDisplay - health;
      if (diff > 0) {
        animationSpeed = diff / 10f;
      }
    } else if (previousHealthDelay < 1 && previousHealthDisplay > health) {
      previousHealthDisplay -= animationSpeed;
    } else {
      previousHealthDisplay = health;
      previousHealth = health;
      previousHealthDelay = HEALTH_INDICATOR_DELAY;
    }
  }

}
