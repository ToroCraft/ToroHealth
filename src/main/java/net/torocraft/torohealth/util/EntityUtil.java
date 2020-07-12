package net.torocraft.torohealth.util;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;

public class EntityUtil {

  public enum Relation {
    FRIEND, FOE, UNKNOWN
  }

  public static Relation determineRelation(Entity entity) {
    if (entity instanceof MonsterEntity) {
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
    } else if (entity instanceof AgeableEntity) {
      return Relation.FRIEND;
    } else if (entity instanceof AbstractFishEntity) {
      return Relation.FRIEND;
    } else {
      return Relation.UNKNOWN;
    }
  }
}
