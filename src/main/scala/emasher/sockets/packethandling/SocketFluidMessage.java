package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class SocketFluidMessage implements IMessage
{
    byte tank;
    TileSocket ts;

    public byte[] msg;

    public SocketFluidMessage()
    {
    }

    public SocketFluidMessage(TileSocket ts, byte tank)
    {
        this.ts = ts;
        this.tank = tank;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        msg = new byte[buf.capacity()];
        buf.readBytes(msg);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.capacity(27);

        FluidStack l = ts.tanks[tank].getFluid();

        int id = -1;
        int meta = 0;
        int amnt = 0;

        byte[] out = new byte[27];
        out[0] = 2;

        if(l != null)
        {
            id = l.fluidID;
            meta = 0;
            amnt = l.amount;
        }

        NetworkUtilities.toByte(out, ts.xCoord, 1);
        NetworkUtilities.toByte(out, ts.yCoord, 5);
        NetworkUtilities.toByte(out, ts.zCoord, 9);
        NetworkUtilities.toByte(out, id, 13);
        NetworkUtilities.toByte(out, meta, 17);
        NetworkUtilities.toByte(out, amnt, 22);
        out[26] = (byte)tank;

        buf.writeBytes(out);
        msg = out;
    }

    public static class Handler implements IMessageHandler<SocketFluidMessage, IMessage>
    {
        @Override
        public IMessage onMessage(SocketFluidMessage message, MessageContext ctx)
        {
            //if(message.msg.length == 0) return null;
            World world = Minecraft.getMinecraft().theWorld;

            int x = NetworkUtilities.toInteger(message.msg, 1);
            int y = NetworkUtilities.toInteger(message.msg, 5);
            int z = NetworkUtilities.toInteger(message.msg, 9);
            int id = NetworkUtilities.toInteger(message.msg, 13);
            int meta = NetworkUtilities.toInteger(message.msg, 17);
            int amnt = NetworkUtilities.toInteger(message.msg, 22);
            int tank = (int)message.msg[26];

            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                ts.tanks[tank].setFluid(new FluidStack(id, amnt));

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, SocketsMod.socket);
            }

            return null;
        }
    }
}
