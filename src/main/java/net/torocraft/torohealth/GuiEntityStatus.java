package net.torocraft.torohealth;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.torocraft.torohealth.util.Config;

public class GuiEntityStatus {

  // private static final int PADDING_FROM_EDGE = 3;
  // // private static final ResourceLocation SKIN_BASIC = new ResourceLocation(ToroHealth.MODID, "textures/gui/default_skin_basic.png");
  // // private static final ResourceLocation SKIN_HEAVY = new ResourceLocation(ToroHealth.MODID, "textures/gui/default_skin_heavy.png");

  // private final MinecraftClient mc;
  // private final ToroHealthDisplay entityDisplay;
  // private final ToroHealthDisplay heartsDisplay;
  // private final ToroHealthDisplay numericDisplay;
  // private final ToroHealthDisplay barDisplay;
  // int screenX = PADDING_FROM_EDGE;
  // int screenY = PADDING_FROM_EDGE;
  // int displayHeight;
  // int displayWidth;
  // int x, y;
  // private LivingEntity entity;
  // private int age = 0;
  // private boolean showHealthBar = false;

  // public GuiEntityStatus() {
  //   this.mc = MinecraftClient.getInstance();
  //   // entityDisplay = new EntityDisplay(mc);
  //   // heartsDisplay = new HeartsDisplay(mc, this);
  //   // numericDisplay = new NumericDisplay(mc, this);
  //   // barDisplay = new BarDisplay(mc, this);

  //   entityDisplay.setPosition(50, 50);
  //   heartsDisplay.setPosition(25, 150);
  //   numericDisplay.setPosition(130, 150);
  //   barDisplay.setPosition(25, 200);
  // }

  // public void draw() {
  //   if (!showHealthBar) {
  //     return;
  //   }
  //   updateGuiAge();
  //   updatePositions();
  //   drawDisplay();
  //   draw();
  // }

  // // private void drawSkin() {
  // //   if (Config.skin.equals("NONE")) {
  // //     return;
  // //   }

  // //   if (Config.skin.equals("HEAVY")) {
  // //     mc.getTextureManager().bindTexture(SKIN_HEAVY);
  // //   } else {
  // //     mc.getTextureManager().bindTexture(SKIN_BASIC);
  // //   }

  // //   GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
  // //   Gui.drawModalRectWithCustomSizedTexture(screenX - 10, screenY - 10, 0.0f, 0.0f, 160, 60, 160, 60);
  // // }

  // private void updateGuiAge() {
  //   String entityStatusDisplay = Config.entityStatusDisplay;
  //   age = age + 15;
  //   if (age > Config.hideDelay || entityStatusDisplay.equals("OFF")) {
  //     hideHealthBar();
  //   }
  // }

  // private void updatePositions() {
  //   adjustForDisplayPositionSetting();

  //   x = screenX;
  //   y = screenY;

  //   if (Config.showEntityModel) {
  //     entityDisplay.setPosition(x, y);
  //     x += 40;
  //   }

  //   if (Config.statusDisplayPosition.contains("BOTTOM")) {
  //     y += 6;
  //   }

  //   numericDisplay.setPosition(x, y);
  //   barDisplay.setPosition(x, y);
  //   heartsDisplay.setPosition(x, y);
  // }

  // private void drawDisplay() {
  //   if (Config.showEntityModel) {
  //     entityDisplay.draw();
  //   }

  //   if ("NUMERIC".equals(Config.entityStatusDisplay)) {
  //     numericDisplay.draw();
  //   } else if ("BAR".equals(Config.entityStatusDisplay)) {
  //     barDisplay.draw();
  //   } else if ("HEARTS".equals(Config.entityStatusDisplay)) {
  //     heartsDisplay.draw();
  //   }
  // }

  // private void adjustForDisplayPositionSetting() {

  //   if (Config.showEntityModel) {
  //     displayHeight = 40;
  //     displayWidth = 140;
  //   } else {
  //     displayHeight = 32;
  //     displayWidth = 100;
  //   }

  //   //ScaledResolution viewport = new ScaledResolution(mc);
  //   String displayPosition = Config.statusDisplayPosition;

  //   int sh = viewport.getScaledHeight();
  //   int sw = viewport.getScaledWidth();

  //   if (displayPosition.contains("TOP") || displayPosition.equals("CUSTOM")) {
  //     screenY = PADDING_FROM_EDGE;
  //   }

  //   if (displayPosition.contains("BOTTOM")) {
  //     screenY = sh - displayHeight - PADDING_FROM_EDGE;
  //   }

  //   if (displayPosition.contains("LEFT") || displayPosition.equals("CUSTOM")) {
  //     screenX = PADDING_FROM_EDGE;
  //   }

  //   if (displayPosition.contains("RIGHT")) {
  //     screenX = sw - displayWidth - PADDING_FROM_EDGE;
  //   }

  //   if (displayPosition.contains("CENTER")) {
  //     screenX = (sw - displayWidth) / 2;
  //   }

  //   screenX += Config.statusDisplayX;
  //   screenY += Config.statusDisplayY;
  // }

  // private void showHealthBar() {
  //   showHealthBar = true;
  // }

  // private void hideHealthBar() {
  //   showHealthBar = false;
  // }

  // // public void setEntity(EntityLivingBase entityToTrack) {
  // //   showHealthBar();
  // //   age = 0;
  // //   if (entity != null && entity.getUniqueID().equals(entityToTrack.getUniqueID())) {
  // //     return;
  // //   }
  // //   entity = entityToTrack;
  // //   entityDisplay.setEntity(entity);
  // //   heartsDisplay.setEntity(entity);
  // //   numericDisplay.setEntity(entity);
  // //   barDisplay.setEntity(entity);
  // // }
}
