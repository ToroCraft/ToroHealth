package net.torocraft.torohealth.bars;

import net.minecraft.entity.Entity;

public class EntityTracker {
  private final static int MAX_BARS = 200;
  private Entity[] buffer = new Entity[MAX_BARS];
  private Entity[] emptyBuffer = new Entity[MAX_BARS];
  private int pointer = 0;

  public static EntityTracker INSTANCE = new EntityTracker();

  public void add(Entity entity) {
    if (pointer < MAX_BARS) {
      buffer[pointer++] = entity;
    }
  }

  public int size() {
    return pointer;
  }

  public Entity[] get() {
    return buffer;
  }

  public void reset() {
    System.arraycopy(emptyBuffer, 0, buffer, 0, emptyBuffer.length);
    pointer = 0;
  }
}
