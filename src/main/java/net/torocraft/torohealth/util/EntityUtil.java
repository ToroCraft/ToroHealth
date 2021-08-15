package net.torocraft.torohealth.util;

import java.util.stream.StreamSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;

public class EntityUtil {

  public enum Relation {
    FRIEND, FOE, UNKNOWN
  }

  public static Relation determineRelation(Entity entity) {
    if (entity instanceof Monster) {
      return Relation.FOE;
    } else if (entity instanceof Slime) {
      return Relation.FOE;
    } else if (entity instanceof Ghast) {
      return Relation.FOE;
    } else if (entity instanceof Animal) {
      return Relation.FRIEND;
    } else if (entity instanceof Squid) {
      return Relation.FRIEND;
    } else if (entity instanceof AmbientCreature) {
      return Relation.FRIEND;
    } else if (entity instanceof AgeableMob) {
      return Relation.FRIEND;
    } else if (entity instanceof AbstractFish) {
      return Relation.FRIEND;
    } else {
      return Relation.UNKNOWN;
    }
  }

  public static boolean showHealthBar(Entity entity, Minecraft client) {
    return entity instanceof LivingEntity && !(entity instanceof ArmorStand)
        && (!entity.isInvisibleTo(client.player) || entity.isCurrentlyGlowing() || entity.isOnFire()
            || entity instanceof Creeper && ((Creeper) entity).isPowered()

            || StreamSupport.stream(entity.getAllSlots().spliterator(), false)
                .anyMatch(is -> !is.isEmpty()))
        && entity != client.player && !entity.isSpectator();
  }
}
