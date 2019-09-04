package net.torocraft.torohealth.display;

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.EntityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

public class HealthBars {

  private static final Identifier GUI_BARS_TEXTURES = new Identifier(ToroHealth.MODID, "textures/gui/bars.png");

  private static final float HEALTH_INDICATOR_DELAY = 80;
  private static final float HEALTH_ANIMATION_SPEED = 0.08f;

  private static final int GREEN = 0x00FF00FF;
  private static final int DARK_GREEN = 0x008000FF;
  private static final int YELLOW = 0xFFFF00FF;
  private static final int WHITE = 0xFFFFFFFF;
  private static final int RED = 0xFF0000FF;
  private static final int DARK_RED = 0x800000FF;
  private static final int DARK_GRAY = 0x808080FF;
  private static final float zLevel = 0;

  private static final float VERTICAL_MARGIN = 0.35f;
  private static final double FULL_SIZE = 40;
  private static final double HALF_SIZE = FULL_SIZE / 2;
  private static final float SCALE = 0.03f;
  private static final Map<Integer, State> states = new HashMap<>();

  private static boolean holdingWeapon = false;
  private static long tickCount = 0;

  public static class HealthBarGuiConf {
    public enum Mode {NONE, WHEN_HOLDING_WEAPON, ALWAYS, WHEN_HURT, WHEN_HURT_TEMP}
    public enum NumberType {NONE, LAST, CUMULATIVE}

    public static Mode showBarsAboveEntities = Mode.ALWAYS;
    public static float distance = 60f;
    public static NumberType numberType = NumberType.LAST;
  }

  //onRenderWorldLast
  public static void tick(float partial) {

    if (!barsAreCurrentlyDisabled()) {
      MinecraftClient mc = MinecraftClient.getInstance();
      Entity viewer = mc.getCameraEntity();
      double diameter = HealthBarGuiConf.distance * 2;
      BlockPos pos = new BlockPos(viewer); //.subtract(new Vec3i(HealthBarGuiConf.distance, HealthBarGuiConf.distance, HealthBarGuiConf.distance));
      Box box = new Box(pos);
      box = box.expand(diameter);
      List<LivingEntity> entities = mc.world.getEntities(LivingEntity.class, box);
      //System.out.println("entities " + entities.size() + "  x" + box.minX  + "-x" + box.maxX+ "  z" + box.minZ  + "-z" + box.maxZ);
      entities.forEach(e -> HealthBars.drawEntityHealthBarInWorld(e, partial));
    }
  }

  //@SubscribeEvent ClientTickEvent cleanup
  public static void tick() {
    ClientWorld world = MinecraftClient.getInstance().world;

    if (world == null) {
      return;
    }

    if (tickCount % 500 == 0) {
      states.entrySet().removeIf(e -> stateExpired(world, e.getKey(), e.getValue()));
    }

    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HOLDING_WEAPON) && tickCount % 10 == 0) {
      updateEquipment();
    }

    tickCount++;
  }

  public static boolean barsAreCurrentlyDisabled() {
    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.ALWAYS)) {
      return false;
    }
    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.NONE)) {
      return true;
    }
    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HOLDING_WEAPON)) {
      return !holdingWeapon;
    }
    return false;
  }


  private static class State {

    private float showOnHurtDelay;

    private float previousHealth;
    private float previousHealthDisplay;
    private float previousHealthDelay;

    private float lastDmg;
    private float lastHealth;
    private float lastDmgDelay;
  }




  public static void updateEquipment() {
    PlayerEntity player = MinecraftClient.getInstance().player;
    if (player == null) {
      holdingWeapon = false;
      return;
    }
    ItemStack item = player.getMainHandStack(); //  .getHeldItem(Hand.MAIN_HAND);
    ItemStack item2 = player.getOffHandStack(); // .getHeldItem(EnumHand.OFF_HAND);
    holdingWeapon = isWeapon(item) || isWeapon(item2);
  }

  private static boolean isWeapon(ItemStack item) {
    return item.getItem() instanceof SwordItem ||
        item.getItem() instanceof BowItem ||
        item.getItem() instanceof PotionItem;
        // isInWeaponWhiteList(item);
  }

  // private static boolean isInWeaponWhiteList(ItemStack item) {
  //   String itemName = item.getItem().getUnlocalizedName();
  //   return ArrayUtils.contains(HealthBarGuiConf.additionalWeaponItems, itemName);
  // }

  private static boolean stateExpired(World world, int id, State state) {
    if (state == null) {
      return true;
    }

    Entity entity = world.getEntityById(id);

    if (entity == null) {
      return true;
    }

    if (!world.isBlockLoaded(new BlockPos(entity))) {
      return true;
    }

    return !entity.isAlive();  // .isDead;
  }

  private static State getState(Entity entity) {
    int id = entity.getEntityId();
    State state = states.get(id);
    if (state == null) {
      state = new State();
      states.put(id, state);
    }
    return state;
  }

  public static void drawEntityHealthBarInWorld(LivingEntity entity, float partialTicks) {
    if (!EntityUtil.whiteListedEntity(entity) || entity == MinecraftClient.getInstance().player) {
      return;
    }
    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HURT) && entity.getHealth() >= entity.getHealthMaximum()) {
      return;
    }
    //System.out.println("draw!");
    double x = entity.prevX + ((entity.x - entity.prevX) * partialTicks);
    double y = entity.prevY + ((entity.y - entity.prevY) * partialTicks);
    double z = entity.prevZ + ((entity.z - entity.prevZ) * partialTicks);
    drawEntityHealthBar(entity, x, y, z);
  }

  public static void drawEntityHealthBarInGui(LivingEntity entity, int x, int y) {
    drawEntityHealthBar(entity, x, y, 0);
  }

  public static void drawEntityHealthBar(LivingEntity entity, double x, double y, double z) {
    State state = getState(entity);

    if (state.lastHealth < 0.1) {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    } else if (state.lastHealth != entity.getHealth()) {
      state.lastDmg = state.lastHealth - entity.getHealth();
      state.lastDmgDelay = HEALTH_INDICATOR_DELAY * 2;
      state.lastHealth = entity.getHealth();
    } else if (state.lastDmgDelay > -1) {
      state.lastDmgDelay--;
    } else {
      state.lastHealth = entity.getHealth();
      state.lastDmg = 0;
    }

    if (state.previousHealthDelay > 0) {
      state.previousHealthDelay--;
    } else if (state.previousHealthDelay < 1 && state.previousHealthDisplay > entity.getHealth()) {
      state.previousHealthDisplay -= HEALTH_ANIMATION_SPEED;
    } else {
      state.previousHealthDisplay = entity.getHealth();
      state.previousHealth = entity.getHealth();
      state.previousHealthDelay = HEALTH_INDICATOR_DELAY;
    }

//    if (HealthBarGuiConf.showBarsAboveEntities.equals(HealthBarGuiConf.Mode.WHEN_HURT_TEMP) && gui == null && state.lastDmg == 0) {
//      return;
//    }

    int color = determineColor(entity);
    int color2 = color == RED ? DARK_RED : DARK_GREEN;

    float percent = entity.getHealth() / entity.getHealthMaximum();
    float percent2 = state.previousHealthDisplay / entity.getHealthMaximum();
    int zOffset = 0;
    y += entity.getHeight();

    drawBar(x, y, z, 1, DARK_GRAY, zOffset++);
    drawBar(x, y, z, percent2, color2, zOffset++);
    drawBar(x, y, z, percent, color, zOffset++);
    if (HealthBarGuiConf.numberType.equals(HealthBarGuiConf.NumberType.CUMULATIVE)) {
      //drawDamageNumber(state.previousHealth - entity.getHealth(), entity, gui, x, y, z, zOffset);
    } else if (HealthBarGuiConf.numberType.equals(HealthBarGuiConf.NumberType.LAST)) {
      //drawDamageNumber(state.lastDmg, entity, gui, x, y, z, zOffset);
    }
  }

  private static void drawDamageNumber(float dmg, LivingEntity entity, DrawableHelper gui, double x, double y, double z, int zOffset) {

    int i = Math.round(dmg);

    if (i < 1) {
      return;
    }

    String s = Integer.toString(i);
    int sw = MinecraftClient.getInstance().textRenderer.getStringWidth(s);

    if (gui != null) {
      MinecraftClient.getInstance().textRenderer.draw(s, ((int) x + 92) - sw, (int) y + 6, 0xd00000);
    }
  }

  private static void drawBar(double x, double y, double z, float percent, int color, int zOffset) {
    float c = 0.00390625f;
    int u = 0;
    int v = 6 * 5 * 2 + 5;
    int uw = MathHelper.ceil(92 * percent);
    int vh = 5;

    double size = percent * FULL_SIZE;
    double h = 3;

    float r = (color >> 24 & 255) / 255.0F;
    float g = (color >> 16 & 255) / 255.0F;
    float b = (color >> 8 & 255) / 255.0F;
    float a = (color & 255) / 255.0F;

    MinecraftClient.getInstance().getTextureManager().bindTexture(GUI_BARS_TEXTURES);
    GlStateManager.color4f(r, g, b, a);

    DrawableHelper.blit((int)x, (int)y, 0, (float)u, (float)v, uw, vh, 256, 256 + zOffset);

    ///EntityRenderDispatcher renderManager = MinecraftClient.getInstance().getEntityRenderManager();
    //boolean lighting = setupGlStateForInWorldRender(x, y, z, zOffset, renderManager);

//    Tessellator tessellator = Tessellator.getInstance();
//    BufferBuilder buffer = tessellator.getBufferBuilder();
//    buffer.begin(7, VertexFormats.POSITION_UV);
//    buffer.vertex(-HALF_SIZE, 0, 0.0D).texture(u * c, v * c).next();
//    buffer.vertex(-HALF_SIZE, h, 0.0D).texture(u * c, (v + vh) * c).next();
//    buffer.vertex(-HALF_SIZE + size, h, 0.0D).texture((u + uw) * c, (v + vh) * c).next();
//    buffer.vertex(-HALF_SIZE + size, 0, 0.0D).texture(((u + uw) * c), v * c).next();
//    tessellator.draw();

    //restoreGlState(lighting);
  }


  private static boolean setupGlStateForInWorldRender(double x, double y, double z, int zOffset, EntityRenderDispatcher renderManager) {
    double relX = x - renderManager.camera.getPos().x; //.viewerPosX;
    double relY = y - renderManager.camera.getPos().y; //.viewerPosY + VERTICAL_MARGIN;
    double relZ = z - renderManager.camera.getPos().z; //.viewerPosZ;

    GlStateManager.pushMatrix();
    GlStateManager.translated(relX, relY, relZ);

    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(-renderManager.cameraPitch, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(renderManager.cameraYaw, 1.0F, 0.0F, 0.0F);
    GlStateManager.translatef(0, 0, -zOffset * 0.001f);

    GlStateManager.scalef(-SCALE, -SCALE, SCALE);
    boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    return lighting;
  }

  private static void restoreGlState(boolean lighting) {
    GlStateManager.disableBlend();
    GlStateManager.enableDepthTest();//  enableDepth();
    GlStateManager.depthMask(true);
    if (lighting) {
      GlStateManager.enableLighting();
    }
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
  }

  private static int determineColor(LivingEntity entity) {
    if (EntityUtil.determineRelation(entity) == EntityUtil.Relation.FRIEND) {
      return GREEN;
    } else {
      return RED;
    }
  }
}
