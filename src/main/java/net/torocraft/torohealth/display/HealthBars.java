package net.torocraft.torohealth.display;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.torocraft.torohealth.ToroHealth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public static class HealthBarsConf {
    public enum Mode {NONE, WHEN_HOLDING_WEAPON, ALWAYS, WHEN_HURT, WHEN_HURT_TEMP}

    public static Mode showBarsAboveEntities = Mode.ALWAYS;
    public static float distance = 60f;
  }

  public static void tick(float partial) {

    if (!barsAreCurrentlyDisabled()) {
      MinecraftClient mc = MinecraftClient.getInstance();
      Entity viewer = mc.getCameraEntity();
      double diameter = HealthBarsConf.distance * 2;
      BlockPos pos = new BlockPos(viewer).subtract(new Vec3i(HealthBarsConf.distance, HealthBarsConf.distance, HealthBarsConf.distance));
      Box box = new Box(pos);
      List<LivingEntity> entities = viewer.world.getEntities(LivingEntity.class, box);
      entities.forEach(e -> HealthBars.drawEntityHealthBarInWorld(e, partial));
    }
  }

  public static boolean barsAreCurrentlyDisabled() {
    if (HealthBarsConf.showBarsAboveEntities.equals(HealthBarsConf.Mode.ALWAYS)) {
      return false;
    }
    if (HealthBarsConf.showBarsAboveEntities.equals(HealthBarsConf.Mode.NONE)) {
      return true;
    }
    if (HealthBarsConf.showBarsAboveEntities.equals(HealthBarsConf.Mode.WHEN_HOLDING_WEAPON)) {
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


  @SubscribeEvent
  public static void cleanup(ClientTickEvent event) {
    WorldClient world = Minecraft.getMinecraft().world;

    if (world == null) {
      return;
    }

    if (tickCount % 500 == 0) {
      states.entrySet().removeIf(e -> stateExpired(world, e.getKey(), e.getValue()));
    }

    if (HealthBarsConf.showBarsAboveEntities.equals(HealthBarsConf.Mode.WHEN_HOLDING_WEAPON) && tickCount % 10 == 0) {
      updateEquipment();
    }

    tickCount++;
  }

  public static void updateEquipment() {
    EntityPlayer player = Minecraft.getMinecraft().player;
    if (player == null) {
      holdingWeapon = false;
      return;
    }
    ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
    ItemStack item2 = player.getHeldItem(EnumHand.OFF_HAND);
    holdingWeapon = isWeapon(item) || isWeapon(item2);
  }

  private static boolean isWeapon(ItemStack item) {
    return item.getItem() instanceof ItemSword ||
        item.getItem() instanceof ItemBow ||
        item.getItem() instanceof ItemPotion ||
        isInWeaponWhiteList(item);
  }

  private static boolean isInWeaponWhiteList(ItemStack item) {
    String itemName = item.getItem().getUnlocalizedName();
    return ArrayUtils.contains(HealthBarsConf.additionalWeaponItems, itemName);
  }

  private static boolean stateExpired(World world, int id, State state) {
    if (state == null) {
      return true;
    }

    Entity entity = world.getEntityByID(id);

    if (entity == null) {
      return true;
    }

    if (!world.isBlockLoaded(new BlockPos(entity))) {
      return true;
    }

    return entity.isDead;
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
    if (!EntityUtil.whiteListedEntity(entity) || entity == Minecraft.getMinecraft().player) {
      return;
    }
    if (HealthBarsConf.showBarsAboveEntities.equals(HealthBarsConf.Mode.WHEN_HURT) && entity.getHealth() >= entity.getMaxHealth()) {
      return;
    }
    double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
    double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
    double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);
    drawEntityHealthBar(entity, x, y, z, null);
  }

  public static void drawEntityHealthBarInGui(Gui gui, EntityLivingBase entity, int x, int y) {
    drawEntityHealthBar(entity, x, y, 0, gui);
  }

  private static void drawEntityHealthBar(EntityLivingBase entity, double x, double y, double z, Gui gui) {
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

    if (HealthBarsConf.showBarsAboveEntities.equals(HealthBarsConf.Mode.WHEN_HURT_TEMP) && gui == null && state.lastDmg == 0) {
      return;
    }

    int color = determineColor(entity);
    int color2 = color == RED ? DARK_RED : DARK_GREEN;

    float percent = entity.getHealth() / entity.getMaxHealth();
    float percent2 = state.previousHealthDisplay / entity.getMaxHealth();
    int zOffset = 0;
    y += entity.height;

    drawBar(gui, x, y, z, 1, DARK_GRAY, zOffset++);
    drawBar(gui, x, y, z, percent2, color2, zOffset++);
    drawBar(gui, x, y, z, percent, color, zOffset++);
    if (HealthBarGuiConf.numberType.equals(HealthBarsConf.NumberType.CUMULATIVE)) {
      drawDamageNumber(state.previousHealth - entity.getHealth(), entity, gui, x, y, z, zOffset);
    } else if (HealthBarGuiConf.numberType.equals(HealthBarsConf.NumberType.LAST)) {
      drawDamageNumber(state.lastDmg, entity, gui, x, y, z, zOffset);
    }
  }

  private static void drawDamageNumber(float dmg, EntityLivingBase entity, Gui gui, double x, double y, double z, int zOffset) {

    int i = Math.round(dmg);

    if (i < 1) {
      return;
    }

    String s = Integer.toString(i);
    int sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(s);

    if (gui != null) {
      Minecraft.getMinecraft().fontRenderer.drawString(s, ((int) x + 92) - sw, (int) y + 6, 0xd00000);
    }
  }

  private static void drawBar(Gui gui, double x, double y, double z, float percent, int color, int zOffset) {
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

    Minecraft.getMinecraft().renderEngine.bindTexture(GUI_BARS_TEXTURES);
    GlStateManager.color(r, g, b, a);

    if (gui != null) {
      gui.drawTexturedModalRect((int) x, (int) y, u, v, uw, vh);
      return;
    }

    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    boolean lighting = setupGlStateForInWorldRender(x, y, z, zOffset, renderManager);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    buffer.pos(-HALF_SIZE, 0, 0.0D).tex(u * c, v * c).endVertex();
    buffer.pos(-HALF_SIZE, h, 0.0D).tex(u * c, (v + vh) * c).endVertex();
    buffer.pos(-HALF_SIZE + size, h, 0.0D).tex((u + uw) * c, (v + vh) * c).endVertex();
    buffer.pos(-HALF_SIZE + size, 0, 0.0D).tex(((u + uw) * c), v * c).endVertex();
    tessellator.draw();

    restoreGlState(lighting);
  }


  private static boolean setupGlStateForInWorldRender(double x, double y, double z, int zOffset, RenderManager renderManager) {
    double relX = x - renderManager.viewerPosX;
    double relY = y - renderManager.viewerPosY + VERTICAL_MARGIN;
    double relZ = z - renderManager.viewerPosZ;

    GlStateManager.pushMatrix();
    GlStateManager.translate(relX, relY, relZ);

    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0, 0, -zOffset * 0.001);

    GlStateManager.scale(-SCALE, -SCALE, SCALE);
    boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    return lighting;
  }

  private static void restoreGlState(boolean lighting) {
    GlStateManager.disableBlend();
    GlStateManager.enableDepth();
    GlStateManager.depthMask(true);
    if (lighting) {
      GlStateManager.enableLighting();
    }
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
  }

  private static int determineColor(EntityLivingBase entity) {
    switch (AbstractEntityDisplay.determineRelation(entity)) {
      case FOE:
        return RED;
      case FRIEND:
        return GREEN;
      default:
        return RED;
    }
  }
}
