package net.torocraft.torohealth;

import net.torocraft.torohealth.util.Raytrace;

public class Handlers {

  private static final double DISTANCE = 50;

  public static void updateSelectedEntity(float partial) {
    ToroHealth.selectedEntity = Raytrace.getEntityInCrosshair(partial, DISTANCE);
  }

}