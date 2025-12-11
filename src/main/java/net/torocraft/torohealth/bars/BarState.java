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


    private BarState(Integer id, float health){
        this.entityID = id;
        this.health = health;
        this.previousHealthDisplay = health;
        this.lastDmg = 0;
        this.lastDmgCumulative = 0;
        this.lastHealth = health;
        this.lastDmgDelay = 0;
        this.animationSpeed = 0;
    }

    public static BarState create(Integer id){
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.world != null;
        Entity entity = client.world.getEntityById(id);
        if (entity instanceof LivingEntity living) {
            float currentHealth = Math.min(living.getHealth(), living.getMaxHealth());
            return new BarState(id, currentHealth);
        }
        return  null;
    }


    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.world != null;
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
            assert client.world != null;
            LivingEntity entity = (LivingEntity) client.world.getEntityById(entityID);
            if (entity != null) {
                BarStates.PARTICLES.add(new BarParticle(entity, lastDmg));
            }
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
