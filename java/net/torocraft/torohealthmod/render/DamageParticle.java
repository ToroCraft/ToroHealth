package net.torocraft.torohealthmod.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torohealthmod.config.ConfigurationHandler;

@SideOnly(Side.CLIENT)
public class DamageParticle extends Particle {

	protected static final float GRAVITY = 0.5F;
	protected static final float SIZE = 3.0F;
	protected static final int LIFESPAN = 30;// 12;
	protected static final double BOUNCE_STRENGTH = 0.5F;

	protected String text;
	protected boolean shouldOnTop = false;
	protected boolean grow = true;
	protected float scale = 1.0F;
	private int damage;

	public DamageParticle(int damage, World world, double parX, double parY, double parZ, double parMotionX, double parMotionY, double parMotionZ) {
		super(world, parX, parY, parZ, parMotionX, parMotionY, parMotionZ);
		particleTextureJitterX = 0.0F;
		particleTextureJitterY = 0.0F;
		particleGravity = GRAVITY;
		particleScale = SIZE;
		particleMaxAge = LIFESPAN;
		height = 0.5f;
		width = 0.5f;
		canCollide = true;
		this.damage = damage;
		text = Integer.toString(Math.abs(damage));
	}

	protected DamageParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
		this(0, worldIn, posXIn, posYIn, posZIn, 0, 0, 0);
	}

	// public void renderParticle(VertexBuffer buffer, Entity entityIn, float
	// partialTicks, float rotationX, float rotationZ, float rotationYZ, float
	// rotationXY, float rotationXZ)

	// public void renderParticle(final VertexBuffer renderer, final Entity
	// entity, final float x, final float y, final float z, final float dX,
	// final float dY, final float dZ) {

	@Override
	public void onUpdate() {

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setExpired();
		}

		motionY -= 0.04D * (double) particleGravity;
		move(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (onGround) {
			System.out.println("on ground");
			// motionX *= 0.699999988079071D;
			// motionZ *= 0.699999988079071D;
			motionY = -motionY;
			setExpired();
		}
	}

	@Override

	public void renderParticle(VertexBuffer buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ,
			float rotationXY, float rotationXZ) {

		float rotationYaw = (-Minecraft.getMinecraft().player.rotationYaw);
		float rotationPitch = Minecraft.getMinecraft().player.rotationPitch;

		float locX = (float) (prevPosX + (posX - prevPosX) * (double) partialTicks - interpPosX);
		float locY = (float) (prevPosY + (posY - prevPosY) * (double) partialTicks - interpPosY);
		float locZ = (float) (prevPosZ + (posZ - prevPosZ) * (double) partialTicks - interpPosZ);

		// final float locX = ((float) (prevPosX + (posX - prevPosX) * motionX -
		// interpPosX));
		// final float locY = ((float) (prevPosY + (posY - prevPosY) * motionY -
		// interpPosY));
		// final float locZ = ((float) (prevPosZ + (posZ - prevPosZ) * motionZ -
		// interpPosZ));

		GL11.glPushMatrix();
		if (shouldOnTop) {
			GL11.glDepthFunc(519);
		} else {
			GL11.glDepthFunc(515);
		}
		GL11.glTranslatef(locX, locY, locZ);
		// GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
		GL11.glRotatef(rotationYaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(rotationPitch, 1.0F, 0.0F, 0.0F);

		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glScaled(particleScale * 0.008D, particleScale * 0.008D, particleScale * 0.008D);
		GL11.glScaled(scale, scale, scale);

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.003662109F);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(2896);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glEnable(3008);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		renderFont();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDepthFunc(515);

		GL11.glPopMatrix();
		if (grow) {
			particleScale *= 1.08F;
			if (particleScale > SIZE * 3.0D) {
				grow = false;
			}
		} else {
			particleScale *= 0.96F;
		}
	}

	public void move(double x, double y, double z) {
		double d0 = y;

		if (this.canCollide) {
			List<AxisAlignedBB> list = this.world.getCollisionBoxes((Entity) null, this.getBoundingBox().addCoord(x, y, z));

			for (AxisAlignedBB axisalignedbb : list) {
				y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
			}

			this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

			for (AxisAlignedBB axisalignedbb1 : list) {
				x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
			}

			this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

			for (AxisAlignedBB axisalignedbb2 : list) {
				z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
			}

			this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
		} else {
			this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		}

		this.resetPositionToBB();
		this.onGround = y != y && d0 < 0.0D;

		if (x != x) {
			this.motionX = 0.0D;
		}

		if (z != z) {
			this.motionZ = 0.0D;
		}
	}

	protected void resetPositionToBB() {
		System.out.println("reset");
		AxisAlignedBB axisalignedbb = this.getBoundingBox();
		double yPrev = posY; 
		
		posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
		posY = axisalignedbb.minY;
		posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
		
		if(yPrev != posY){
			System.out.println("revert");
			//motionY = -motionY;
		}
	}

	private void renderFont() {
		int color = ConfigurationHandler.damageColor;
		if (damage < 0) {
			color = ConfigurationHandler.healColor;
		}

		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		fontRenderer.drawStringWithShadow(text, -MathHelper.floor(fontRenderer.getStringWidth(text) / 2.0F) + 1,
				-MathHelper.floor(fontRenderer.FONT_HEIGHT / 2.0F) + 1, color);
	}

	public int getFXLayer() {
		return 3;
	}

}
