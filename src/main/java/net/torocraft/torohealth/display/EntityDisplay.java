package net.torocraft.torohealth.display;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.npc.Villager;

public class EntityDisplay {
	private static final float RENDER_HEIGHT = 30;
	private static final float RENDER_WIDTH = 18;
	private static final float WIDTH = 40;
	private static final float HEIGHT = WIDTH;

	private LivingEntity entity;
	private int entityScale = 1;

	private float xOffset;
	private float yOffset;

	public void setEntity(LivingEntity entity) {
		this.entity = entity;

		updateScale();
	}

	public void draw(PoseStack matrix, float scale) {
		if (entity != null) {
			try {
				drawEntity(matrix, (int)xOffset, (int)yOffset, entityScale, -80, -20, entity, scale);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateScale() {
		if (entity == null) {
			return;
		}

		int scaleY = Mth.ceil(RENDER_HEIGHT / entity.getBbHeight());
		int scaleX = Mth.ceil(RENDER_WIDTH / entity.getBbHeight());
		entityScale = Math.min(scaleX, scaleY);

		if (entity instanceof Chicken) {
			entityScale *= 0.7;
		}

		if (entity instanceof Villager && entity.isSleeping()) {
			entityScale = entity.isBaby() ? 31 : 16;
		}

		xOffset = WIDTH / 2;

		yOffset = HEIGHT / 2 + RENDER_HEIGHT / 2;

		if (entity instanceof Ghast) {
			yOffset -= 10;
		}
	}

	/**
	 * copied from InventoryScreen.drawEntity() to expose the matrixStack
	 */
	public static void drawEntity(PoseStack matrixStack2, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, float scale) {
		float f = (float)Math.atan(mouseX / 40.0F);
		float g = (float)Math.atan(mouseY / 40.0F);
		PoseStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.pushPose();
		matrixStack.translate((double)x * scale, (double)y * scale, 1050.0D * scale);
		matrixStack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		matrixStack2.pushPose();
		matrixStack2.translate(0.0D, 0.0D, 1000.0D);
		matrixStack2.scale((float)size, (float)size, (float)size);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion2 = Vector3f.XP.rotationDegrees(g * 20.0F);
		quaternion.mul(quaternion2);
		matrixStack2.mulPose(quaternion);
		float h = entity.yBodyRot; // bodyYaw;
		float i = entity.getYRot(); // getYaw();
		float j = entity.getXRot(); // getPitch();
		float k = entity.yHeadRotO; // prevHeadYaw;
		float l = entity.yHeadRot; // headYaw;
		entity.yBodyRot = 180.0F + f * 20.0F;
		entity.setYRot(180.0F + f * 40.0F);
		entity.setXRot(-g * 20.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();
		Lighting.setupForEntityInInventory();
		EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion2.conj();
		entityrenderdispatcher.overrideCameraOrientation(quaternion2);
		entityrenderdispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();

		RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack2, immediate, 15728880));

		immediate.endBatch();
		entityrenderdispatcher.setRenderShadow(true);
		entity.yBodyRot = h;
		entity.setYRot(i);
		entity.setXRot(j);
		entity.yHeadRotO = k;
		entity.yHeadRot = l;
		matrixStack.popPose();
		matrixStack2.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}
}
