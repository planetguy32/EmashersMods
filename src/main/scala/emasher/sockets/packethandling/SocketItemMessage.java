package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SocketItemMessage implements IMessage
{
    byte inventory;
    TileSocket ts;

    public byte[] msg;

    public SocketItemMessage()
    {
    }

    public SocketItemMessage(TileSocket ts, byte inventory)
    {
        this.ts = ts;
        this.inventory = inventory;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        msg = new byte[buf.capacity()];
        buf.readBytes(msg);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ItemStack s = ts.inventory.getStackInSlot(inventory);
        byte[] NBTData = new byte[]{};
        try
        {
            if(s != null && s.getTagCompound() != null) NBTData = CompressedStreamTools.compress(s.getTagCompound());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        buf.capacity(27 + NBTData.length);


        byte[] out = new byte[27 + NBTData.length];

        out[0] = 1;

        if(s != null)
        {
            NetworkUtilities.toByte(out, Item.getIdFromItem(s.getItem()), 1);
            NetworkUtilities.toByte(out, s.getItemDamage(), 5);
            NetworkUtilities.toByte(out, s.stackSize, 23);
        }
        else
        {
            NetworkUtilities.toByte(out, -1, 1);
        }

        NetworkUtilities.toByte(out, ts.xCoord, 9);
        NetworkUtilities.toByte(out, ts.yCoord, 13);
        NetworkUtilities.toByte(out, ts.zCoord, 17);
        out[22] = (byte)inventory;

        for(int i = 0; i < NBTData.length; i++)
        {
            out[27 + i] = NBTData[i];
        }

        buf.writeBytes(out);
        msg = out;
    }

    public static class Handler implements IMessageHandler<SocketItemMessage, IMessage>
    {
        @Override
        public IMessage onMessage(SocketItemMessage message, MessageContext ctx)
        {
            //if(message.msg.length == 0) return null;
            World world = Minecraft.getMinecraft().theWorld;

            int x = NetworkUtilities.toInteger(message.msg, 9);
            int y = NetworkUtilities.toInteger(message.msg, 13);
            int z = NetworkUtilities.toInteger(message.msg, 17);
            int id = NetworkUtilities.toInteger(message.msg, 1);
            int damage = NetworkUtilities.toInteger(message.msg, 5);
            int inventory = message.msg[22];
            int size = NetworkUtilities.toInteger(message.msg, 23);

            ItemStack s = null;

            if(id != -1) {
                s = new ItemStack(Item.getItemById(id), size, damage);
                if (message.msg.length > 27) {
                    NBTTagCompound NBTData = null;
                    byte[] NBTArray = new byte[message.msg.length - 27];

                    for (int i = 0; i < message.msg.length - 27; i++) {
                        NBTArray[i] = message.msg[i + 27];
                    }

                    try {
                        NBTData = CompressedStreamTools.func_152457_a(NBTArray, NBTSizeTracker.field_152451_a);
                        if (NBTData != null) s.setTagCompound(NBTData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                if(id != -1) ts.inventory.setInventorySlotContents(inventory, new ItemStack(Item.getItemById(id), size, damage));
                else ts.inventory.setInventorySlotContents(inventory, null);

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, SocketsMod.socket);
            }

            return null;
        }
    }
}
