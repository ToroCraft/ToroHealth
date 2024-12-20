package net.torocraft.torohealth.util;

import java.util.stream.StreamSupport;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SquidEntity;

public class EntityUtil {

  public enum Relation {
    FRIEND, FOE, UNKNOWN
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

  public static boolean showHealthBar(Entity entity, MinecraftClient client) {
    return entity instanceof LivingEntity living
            && !(entity instanceof ArmorStandEntity)
            && (!entity.isInvisibleTo(client.player)
                || entity.isGlowing()
                || entity.isOnFire()
                || entity instanceof CreeperEntity && ((CreeperEntity) entity).isCharged() // charged creeper
                || StreamSupport.stream(living.getEquippedItems().spliterator(), false).anyMatch(is -> !is.isEmpty()))
            && entity != client.player
            && !entity.isSpectator();
  }
}
