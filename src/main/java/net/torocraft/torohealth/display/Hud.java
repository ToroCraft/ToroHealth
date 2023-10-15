package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.AnchorPoint;

import java.awt.*;

public class Hud extends Screen {
  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(ToroHealth.MODID + ":textures/gui/default_skin_basic.png");
  private EntityDisplay entityDisplay = new EntityDisplay();
  private LivingEntity entity;
  private BarDisplay barDisplay;
  private Config config = new Config();
  private int age;

  public Hud() {
    super(Component.literal("ToroHealth HUD"));
    this.minecraft = Minecraft.getInstance();
    barDisplay = new BarDisplay(Minecraft.getInstance(), this);
  }

  public void draw(ForgeGui gui, GuiGraphics poseStack, float partialTick, int width, int height) {
    if (this.minecraft.options.renderDebug) {
      return;
    }
    this.config = ToroHealth.CONFIG;
    if (this.config == null) {
      this.config = new Config();
    }
    float x = determineX();
    float y = determineY();
    // FIXME
    draw(poseStack, x, y, config.hud.scale);
  }

  private float determineX() {
    float x = config.hud.x;
    AnchorPoint anchor = config.hud.anchorPoint;
    float wScreen = minecraft.getWindow().getGuiScaledHeight();

    switch (anchor) {
      case BOTTOM_CENTER:
      case TOP_CENTER:
        return (wScreen / 2) + x;
      case BOTTOM_RIGHT:
      case TOP_RIGHT:
        return (wScreen) + x;
      default:
        return x;
    }
  }

  private float determineY() {
    float y = config.hud.y;
    AnchorPoint anchor = config.hud.anchorPoint;
    float hScreen = minecraft.getWindow().getGuiScaledHeight();

    switch (anchor) {
      case BOTTOM_CENTER:
      case BOTTOM_LEFT:
      case BOTTOM_RIGHT:
        return y + hScreen;
      default:
        return y;
    }
  }

  public void tick() {
    age++;
  }

  public void setEntity(LivingEntity entity) {
    if (entity != null) {
      age = 0;
    }

    if (entity == null && age > config.hud.hideDelay) {
      setEntityWork(null);
    }

    if (entity != null && entity != this.entity) {
      setEntityWork(entity);
    }
  }

  private void setEntityWork(LivingEntity entity) {
    this.entity = entity;
    entityDisplay.setEntity(entity);
  }

  public LivingEntity getEntity() {
    return entity;
  }

  private void draw(GuiGraphics guiGraphics, float x, float y, float scale) {
    if (entity == null) {
      return;
    }
    
    if (config.hud.onlyWhenHurt && entity.getHealth() >= entity.getMaxHealth()) {
      return;
    }

    PoseStack matrix = guiGraphics.pose();
    matrix.pushPose();
    matrix.scale(scale, scale, scale);
    matrix.translate(x - 10, y - 10, 0);
    if (config.hud.showSkin) {
      this.drawSkin(guiGraphics);
    }
    matrix.translate(10, 10, 0);
    if (config.hud.showEntity) {
      entityDisplay.draw(matrix, scale);
    }
    matrix.translate(44, 0, 0);
    if (config.hud.showBar) {
      barDisplay.draw(guiGraphics, entity);
    }
    matrix.popPose();
  }

  private void drawSkin(GuiGraphics guiGraphics) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    int w = 160, h = 60;
    guiGraphics.blit(BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, w, h, w, h);
  }
}
