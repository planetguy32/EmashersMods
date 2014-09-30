package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.pipes.TilePipeBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PipeColourMessage implements IMessage
{
    TilePipeBase p;

    public byte[] msg;

    public PipeColourMessage()
    {
    }

    public PipeColourMessage(TilePipeBase p)
    {
        this.p = p;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        msg = new byte[buf.capacity()];
        buf.readBytes(msg);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.capacity(18);

        byte[] out = new byte[18];

        out[0] = 3;
        NetworkUtilities.toByte(out, p.xCoord, 1);
        NetworkUtilities.toByte(out, p.yCoord, 5);
        NetworkUtilities.toByte(out, p.zCoord, 9);
        NetworkUtilities.toByte(out, p.getWorldObj().provider.dimensionId, 13);
        out[17] = (byte)p.colour;

        buf.writeBytes(out);
        msg = out;
    }

    public static class Handler implements IMessageHandler<PipeColourMessage, IMessage>
    {
        @Override
        public IMessage onMessage(PipeColourMessage message, MessageContext ctx)
        {
            //if(message.msg.length == 0) return null;
            World world = Minecraft.getMinecraft().theWorld;

            int x = NetworkUtilities.toInteger(message.msg, 1);
            int y = NetworkUtilities.toInteger(message.msg, 5);
            int z = NetworkUtilities.toInteger(message.msg, 9);
            int id = NetworkUtilities.toInteger(message.msg, 13);
            int colour = message.msg[17];

            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TilePipeBase)
            {
                TilePipeBase p = (TilePipeBase)te;

                p.colour = colour;

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
            }

            return null;
        }
    }
}
