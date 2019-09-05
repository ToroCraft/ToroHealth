package net.torocraft.torohealth.bars;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.Iterator;

public class EntityTracker {
  private final static int MAX_BARS = 200;
  private TrackedEntity[] buffer = new TrackedEntity[MAX_BARS];
  private TrackedEntity[] emptyBuffer = new TrackedEntity[MAX_BARS];
  private int pointer = 0;
  private TrackedEntity e;

  public static EntityTracker INSTANCE = new EntityTracker();

  public static class TrackedEntity {
    public LivingEntity entity;
    public double x;
    public double y;
    public double z;
  }

  public void add(Entity entity, double x, double y, double z) {
    if (!(entity instanceof LivingEntity) || pointer >= MAX_BARS) {
      return;
    }

    if (buffer[pointer] == null) {
      buffer[pointer] = new TrackedEntity();
    }

    e = buffer[pointer];

    e.entity = (LivingEntity) entity;
    e.x = x;
    e.y = y;
    e.z = z;

    pointer++;
  }

  public int size() {
    return pointer;
  }

  public Iterator<TrackedEntity> iterator() {
    return new Iterator<TrackedEntity>() {

      private int index = 0;

      @Override
      public boolean hasNext() {
        return index < pointer;
      }

      @Override
      public TrackedEntity next() {
        return buffer[index++];
      }
    };
  }

  public void reset() {
    pointer = 0;
  }
}
