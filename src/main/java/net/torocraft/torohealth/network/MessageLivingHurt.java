package net.torocraft.torohealth.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.torohealth.ToroHealth;

public class MessageLivingHurt implements IMessage {

  private int id;
  private float amount;

  public static void init(int packetId) {
    ToroHealth.NETWORK.registerMessage(Handler.class, MessageLivingHurt.class, packetId, Side.CLIENT);
  }

  public MessageLivingHurt() {

  }

  public MessageLivingHurt(int id, float amount) {
    this.id = id;
    this.amount = amount;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    id = buf.readInt();
    amount = buf.readFloat();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(id);
    buf.writeFloat(amount);
  }

  public static class Handler implements IMessageHandler<MessageLivingHurt, IMessage> {

    @Override
    public IMessage onMessage(final MessageLivingHurt message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> work(message));
      return null;
    }

    public static void work(MessageLivingHurt message) {
      EntityPlayer player = ToroHealth.PROXY.getPlayer();
      Entity e = player.getEntityWorld().getEntityByID(message.id);
      if (e instanceof EntityLivingBase) {
        ToroHealth.PROXY.displayDamageDealt((EntityLivingBase)e, message.amount);
      }
    }
  }
}
