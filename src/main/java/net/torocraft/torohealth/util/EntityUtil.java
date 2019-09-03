package net.torocraft.torohealth.util;

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import org.apache.commons.lang3.ArrayUtils;


public class EntityUtil {

  public static class HealthBarGuiConf {
    public static String[] entityBlacklist = {};
  }

  public static boolean whiteListedEntity(Entity entity) {
    return !ArrayUtils.contains(HealthBarGuiConf.entityBlacklist, getEntityStringId(entity));
  }

  public static String getEntityStringId(Entity entity) {
    try {
      //EntityRendererRegistry
      return EntityRegistry.getEntry(entity.getClass()).getRegistryName().toString();
    } catch (Exception e) {
      return "unknown:unknown";
    }
  }
}
