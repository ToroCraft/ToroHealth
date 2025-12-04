package net.torocraft.torohealth.bars;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.MinecraftClient;
import net.torocraft.torohealth.ToroHealth;

public class BarState {

    public final Integer entityID;

    public float health;
    public float previousHealthDisplay;
    public float previousHealthDelay;
    public int lastDmg;
    public int lastDmgCumulative;
    public float lastHealth;
    public float lastDmgDelay;
    private float animationSpeed;

    private static final float HEALTH_INDICATOR_DELAY = 10;



    public BarState(Integer id) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.world != null ? client.world.getEntityById(id) : null;
        if (entity instanceof LivingEntity living) {
            this.entityID = id;
            health = Math.min(living.getHealth(), living.getMaxHealth());
            previousHealthDisplay = health;
            lastDmg = 0;
            lastDmgCumulative = 0;
            lastHealth = health;
            lastDmgDelay = 0;
            animationSpeed = 0;
        } else {
            this.entityID = null; // will be ignored
        }
    }

    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        LivingEntity entity = (LivingEntity) client.world.getEntityById(entityID);

        if (entity != null){
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
        lastDmg = MathHelper.ceil(lastHealth) - MathHelper.ceil(health);
        lastDmgCumulative += lastDmg;

        lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
        lastHealth = health;
        if (ToroHealth.CONFIG.particle.show) {
            MinecraftClient client = MinecraftClient.getInstance();
            LivingEntity entity = (LivingEntity) client.world.getEntityById(entityID);
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
            previousHealthDelay = HEALTH_INDICATOR_DELAY;
        }
    }
}
