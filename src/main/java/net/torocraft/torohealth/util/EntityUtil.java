package net.torocraft.torohealth.util;

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
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
    if (entity instanceof MobEntity) {
      return Relation.FOE;
    } else if (entity instanceof SlimeEntity) {
      return Relation.FOE;
    } else if (entity instanceof GhastEntity) {
      return Relation.FOE;
    } else if (entity instanceof AnimalEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof SquidEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof AmbientCreatureEntity) {
      return Relation.FRIEND;
    } else {
      return Relation.UNKNOWN;
    }
  }
}
