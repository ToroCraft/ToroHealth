package net.torocraft.torohealth.hud;

import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;

public class Hud extends DrawableHelper {
    private static final Identifier BAR_TEX = new Identifier("textures/gui/bars.png");
    private final MinecraftClient client;
    //private ClientBossBar bar = new ClientBossBar(bossBarS2CPacket_1);

   public Hud(MinecraftClient minecraftClient_1) {
      this.client = minecraftClient_1;
   }

    public void render() {
       int width = this.client.window.getScaledWidth();
       int height = 12;


       GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
       this.client.getTextureManager().bindTexture(BAR_TEX);
       // this.renderBossBar(center, height, bar);
       String title = "Test Test Test";
       int int_5 = this.client.textRenderer.getStringWidth(title);
       int int_6 = width / 2 - int_5 / 2;
       int int_7 = height - 9;
       this.client.textRenderer.drawWithShadow(title, (float)int_6, (float)int_7, 16777215);
       //this.client.textRenderer.getClass();
       height += 10 + 9;
       // if (height >= this.client.window.getScaledHeight() / 3) {
       //     break;
       // }


    }


}
