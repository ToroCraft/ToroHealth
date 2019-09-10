package net.torocraft.torohealth.util;

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import org.apache.commons.lang3.ArrayUtils;


public class EntityUtil {

  public enum Relation {
    FRIEND, FOE, UNKNOWN
  }

  public static class HealthBarGuiConf {
    public static String[] entityBlacklist = {};
  }

  public static boolean whiteListedEntity(Entity entity) {
    return !ArrayUtils.contains(HealthBarGuiConf.entityBlacklist, getEntityStringId(entity));
  }

  public static String getEntityStringId(Entity entity) {
    try {
      //EntityRendererRegistry
      //return EntityRegistry.getEntityStringId(entity.getClass()).getRegistryName().toString();
      // return EntityRendererRegistry.INSTANCE.
      return entity.getEntityName();
    } catch (Exception e) {
      return "unknown:unknown";
    }
  }

  public static Relation determineRelation(Entity entity) {
    if (entity instanceof HostileEntity) {
      return Relation.FOE;
    } else if (entity instanceof SlimeEntity) {
      return Relation.FOE;
    } else if (entity instanceof GhastEntity) {
      return Relation.FOE;
    } else if (entity instanceof AnimalEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof SquidEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof AmbientEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof PassiveEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof FishEntity) {
      return Relation.FRIEND;
    } else {
      return Relation.UNKNOWN;
    }
  }
}
