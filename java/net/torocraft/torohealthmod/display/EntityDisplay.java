package net.torocraft.torohealthmod.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityDisplay implements ToroHealthDisplay {

	private static final int RENDER_HEIGHT = 20;

	private int x = 50;
	private int y = 50;

	private EntityLivingBase entity;
	private Entity leashedToEntity;
	private float prevYawOffset;
	private float prevYaw;
	private float prevPitch;
	private float prevYawHead;
	private float prevPrevYahHead;
	private int scale = 1;

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setEntity(EntityLivingBase entity) {
		this.entity = entity;
		updateScale();
	}

	@Override
	public void draw() {
		try {
			pushEntityLeasedTo();
			pushEntityRotations();
			glDraw();
			popEntityRotations();
			popEntityLeasedTo();
		} catch (Throwable ignore) {

		}
	}

	private void updateScale() {
		scale = MathHelper.ceil(RENDER_HEIGHT / entity.height);
	}

	private void glDraw() {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y, 50.0F);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-100.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(0.0f, 1.0F, 0.0F, 0.0F);

		RenderHelper.enableStandardItemLighting();

		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);

		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private void popEntityLeasedTo() {
		if (entity instanceof EntityLiving && leashedToEntity != null) {
			((EntityLiving) entity).setLeashedToEntity(leashedToEntity, false);
			leashedToEntity = null;
		}
	}

	private void pushEntityLeasedTo() {
		if (entity instanceof EntityLiving) {
			if (((EntityLiving) entity).getLeashed()) {
				leashedToEntity = ((EntityLiving) entity).getLeashedToEntity();
				((EntityLiving) entity).setLeashedToEntity(null, false);
			}
		}
	}

	private void popEntityRotations() {
		entity.renderYawOffset = prevYawOffset;
		entity.rotationYaw = prevYaw;
		entity.rotationPitch = prevPitch;
		entity.rotationYawHead = prevYawHead;
		entity.prevRotationYawHead = prevPrevYahHead;
	}

	private void pushEntityRotations() {
		prevYawOffset = entity.renderYawOffset;
		prevYaw = entity.rotationYaw;
		prevPitch = entity.rotationPitch;
		prevYawHead = entity.rotationYawHead;
		prevPrevYahHead = entity.prevRotationYawHead;
		entity.renderYawOffset = 0.0f;
		entity.rotationYaw = 0.0f;
		entity.rotationPitch = 0.0f;
		entity.rotationYawHead = 0.0f;
		entity.prevRotationYawHead = 0.0f;
	}
}