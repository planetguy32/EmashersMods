package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.pipes.TileAdapterBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AdapterSideMessage implements IMessage
{
    TileAdapterBase p;

    public byte[] msg;

    byte side;

    public AdapterSideMessage()
    {
    }

    public AdapterSideMessage(TileAdapterBase p, byte side)
    {
        this.p = p;
        this.side = side;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        msg = new byte[buf.capacity()];
        buf.readBytes(msg);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.capacity(19);

        byte[] out = new byte[19];

        out[0] = 4;
        NetworkUtilities.toByte(out, p.xCoord, 1);
        NetworkUtilities.toByte(out, p.yCoord, 5);
        NetworkUtilities.toByte(out, p.zCoord, 9);
        NetworkUtilities.toByte(out, p.getWorldObj().provider.dimensionId, 13);
        if(p.outputs[side]) out[17] = 1;
        else out[17] = 0;
        out[18] = (byte)side;

        buf.writeBytes(out);
        msg = out;
    }

    public static class Handler implements IMessageHandler<AdapterSideMessage, IMessage>
    {
        @Override
        public IMessage onMessage(AdapterSideMessage message, MessageContext ctx)
        {
            //if(message.msg.length == 0) return null;
            World world = Minecraft.getMinecraft().theWorld;

            int x = NetworkUtilities.toInteger(message.msg, 1);
            int y = NetworkUtilities.toInteger(message.msg, 5);
            int z = NetworkUtilities.toInteger(message.msg, 9);
            int id = NetworkUtilities.toInteger(message.msg, 13);
            boolean output = false;
            if(message.msg[17] != 0) output = true;
            int side = (int)message.msg[18];

            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileAdapterBase)
            {
                TileAdapterBase t = (TileAdapterBase)te;

                t.outputs[side] = output;

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
            }

            return null;
        }
    }
}
