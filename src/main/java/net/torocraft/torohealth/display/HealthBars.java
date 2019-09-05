package net.torocraft.torohealth.display;

public class HealthBars {
//
//
//
//
//  private static boolean holdingWeapon = false;
//  private static long tickCount = 0;
//
//  public static class HealthBarGuiConf {
//
//  }
//
//  //onRenderWorldLast
//  public static void tick(float partial) {
//
//    if (!barsAreCurrentlyDisabled()) {
//      MinecraftClient mc = MinecraftClient.getInstance();
//      Entity viewer = mc.getCameraEntity();
//      double diameter = HealthBarGuiConf.distance * 2;
//      BlockPos pos = new BlockPos(viewer); //.subtract(new Vec3i(HealthBarGuiConf.distance, HealthBarGuiConf.distance, HealthBarGuiConf.distance));
//      Box box = new Box(pos);
//      box = box.expand(diameter);
//      List<LivingEntity> entities = mc.world.getEntities(LivingEntity.class, box);
//      //System.out.println("entities " + entities.size() + "  x" + box.minX  + "-x" + box.maxX+ "  z" + box.minZ  + "-z" + box.maxZ);
//      entities.forEach(e -> HealthBars.drawEntityHealthBarInWorld(e, partial));
//    }
//  }
//
//  //@SubscribeEvent ClientTickEvent cleanup
//  public static void tick() {
//    ClientWorld world = MinecraftClient.getInstance().world;
//
//    if (world == null) {
//      return;
//    }
//
//    if (tickCount % 500 == 0) {
//      states.entrySet().removeIf(e -> stateExpired(world, e.getKey(), e.getValue()));
//    }
//
//    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HOLDING_WEAPON) && tickCount % 10 == 0) {
//      updateEquipment();
//    }
//
//    tickCount++;
//  }
//
//  public static boolean barsAreCurrentlyDisabled() {
//    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.ALWAYS)) {
//      return false;
//    }
//    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.NONE)) {
//      return true;
//    }
//    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HOLDING_WEAPON)) {
//      return !holdingWeapon;
//    }
//    return false;
//  }
//
//
//  public static void updateEquipment() {
//    PlayerEntity player = MinecraftClient.getInstance().player;
//    if (player == null) {
//      holdingWeapon = false;
//      return;
//    }
//    ItemStack item = player.getMainHandStack(); //  .getHeldItem(Hand.MAIN_HAND);
//    ItemStack item2 = player.getOffHandStack(); // .getHeldItem(EnumHand.OFF_HAND);
//    holdingWeapon = isWeapon(item) || isWeapon(item2);
//  }
//
//  private static boolean isWeapon(ItemStack item) {
//    return item.getItem() instanceof SwordItem ||
//        item.getItem() instanceof BowItem ||
//        item.getItem() instanceof PotionItem;
//        // isInWeaponWhiteList(item);
//  }
//
//  // private static boolean isInWeaponWhiteList(ItemStack item) {
//  //   String itemName = item.getItem().getUnlocalizedName();
//  //   return ArrayUtils.contains(HealthBarGuiConf.additionalWeaponItems, itemName);
//  // }
//
//  private static boolean stateExpired(World world, int id, BarState state) {
//    if (state == null) {
//      return true;
//    }
//
//    Entity entity = world.getEntityById(id);
//
//    if (entity == null) {
//      return true;
//    }
//
//    if (!world.isBlockLoaded(new BlockPos(entity))) {
//      return true;
//    }
//
//    return !entity.isAlive();  // .isDead;
//  }
//
//
//
//  public static void drawEntityHealthBarInWorld(LivingEntity entity, float partialTicks) {
//    if (!EntityUtil.whiteListedEntity(entity) || entity == MinecraftClient.getInstance().player) {
//      return;
//    }
//    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HURT) && entity.getHealth() >= entity.getHealthMaximum()) {
//      return;
//    }
//    //System.out.println("draw!");
//    double x = entity.prevX + ((entity.x - entity.prevX) * partialTicks);
//    double y = entity.prevY + ((entity.y - entity.prevY) * partialTicks);
//    double z = entity.prevZ + ((entity.z - entity.prevZ) * partialTicks);
//    HealthBarRenderer.drawEntityHealthBar(entity, x, y, z);
//  }
//
//  public static void drawEntityHealthBarInGui(LivingEntity entity, int x, int y) {
//    HealthBarRenderer.drawEntityHealthBar(entity, x, y, 0);
//  }


}
