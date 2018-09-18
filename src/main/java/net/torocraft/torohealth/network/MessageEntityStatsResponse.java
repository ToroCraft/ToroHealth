package net.torocraft.torohealth.network;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torohealth.ToroHealth;

public class MessageEntityStatsResponse implements IMessage {

  private static final String POTIONS_KEY = "p";

  @SideOnly(Side.CLIENT)
  public static Collection<PotionEffect> POTIONS;

  private int id;
  private Collection<PotionEffect> potions;

  public static void init(int packetId) {
    ToroHealth.NETWORK.registerMessage(Handler.class, MessageEntityStatsResponse.class, packetId, Side.CLIENT);
  }

  public MessageEntityStatsResponse() {

  }

  public MessageEntityStatsResponse(int id, Collection<PotionEffect> potions) {
    this.id = id;
    this.potions = potions;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    id = buf.readInt();
    readPotions(buf);
  }

  private void readPotions(ByteBuf buf) {
    potions = new ArrayList<>();
    NBTTagList potionNbtList = ByteBufUtils.readTag(buf).getTagList(POTIONS_KEY, 10);
    for (int i = 0; i < potionNbtList.tagCount(); i++) {
      potions.add(PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound) potionNbtList.get(i)));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(id);
    writePotions(buf);
  }

  private void writePotions(ByteBuf buf) {
    NBTTagList potionNbtList = new NBTTagList();
    for (PotionEffect potion : potions) {
      NBTTagCompound c = new NBTTagCompound();
      potion.writeCustomPotionEffectToNBT(c);
      potionNbtList.appendTag(c);
    }
    NBTTagCompound c = new NBTTagCompound();
    c.setTag(POTIONS_KEY, potionNbtList);
    ByteBufUtils.writeTag(buf, c);
  }

  public static class Handler implements IMessageHandler<MessageEntityStatsResponse, IMessage> {

    @Override
    public IMessage onMessage(final MessageEntityStatsResponse message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> work(message));
      return null;
    }

    public static void work(MessageEntityStatsResponse message) {
      POTIONS = message.potions;
      for (PotionEffect p : message.potions) {
        System.out.println("POTION ON CLIENT: " + p.getEffectName() + " " + p.getDuration());
      }

    }
  }
}
