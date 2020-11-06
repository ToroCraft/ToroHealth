package net.torocraft.torohealth.bars;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.torocraft.torohealth.ToroHealth;

public class BarParticle {
  public float damage;

  public double x = 0;
  public double y = 0;
  public double z = 0;
  public double xPrev = 0;
  public double yPrev = 0;
  public double zPrev = 0;

  public int age = 0;

  public double ax = 0.00;
  public double ay = -0.01;
  public double az = 0.00;

  public double vx = 0;
  public double vy = 0;
  public double vz = 0;

  public BarParticle(Entity entity, float damage) {
    Minecraft client = Minecraft.getInstance();
    Vector3d entityLocation = entity.getPositionVec().add(0, entity.getHeight() / 2, 0);
    Vector3d cameraLocation = client.gameRenderer.getActiveRenderInfo().getProjectedView();
    double offsetBy = entity.getWidth();
    Vector3d offset = cameraLocation.subtract(entityLocation).normalize().scale(offsetBy);
    Vector3d pos = entityLocation.add(offset);

    age = 0;
    this.damage = damage;

    vx = ToroHealth.RAND.nextGaussian() * 0.04;
    vy = 0.10 + (ToroHealth.RAND.nextGaussian() * 0.05);
    vz = ToroHealth.RAND.nextGaussian() * 0.04;

    x = pos.x;
    y = pos.y;
    z = pos.z;

    xPrev = x;
    yPrev = y;
    zPrev = z;
  }

  public void tick() {
    xPrev = x;
    yPrev = y;
    zPrev = z;
    age++;
    x += vx;
    y += vy;
    z += vz;
    vx += ax;
    vy += ay;
    vz += az;
  }

}
