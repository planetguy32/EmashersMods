package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.api.SideConfig;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SocketStateMessage implements IMessage {

    byte side;
    TileSocket ts;

    public byte[] msg;

    public SocketStateMessage()
    {
    }

    public SocketStateMessage(TileSocket ts, byte side)
    {
        this.ts = ts;
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
        buf.capacity(36);
        SideConfig c = ts.configs[side];

        byte[] out = new byte[36];

        out[0] = 0;
        out[2] = (byte)c.tank;
        out[3] = (byte)c.inventory;
        out[4] = (byte)ts.rsIndicatorIndex(side);
        out[5] = (byte)ts.latchIndicatorIndex(side);

        NetworkUtilities.toByte(out, c.meta, 6);
        NetworkUtilities.toByte(out, ts.xCoord, 10);
        NetworkUtilities.toByte(out, ts.yCoord, 14);
        NetworkUtilities.toByte(out, ts.zCoord, 18);
        NetworkUtilities.toByte(out, ts.facID[side], 27);
        NetworkUtilities.toByte(out, ts.facMeta[side], 31);

        out[1] = (byte)side;
        NetworkUtilities.toByte(out, ts.sides[side], 22);
        out[26] = NetworkUtilities.boolToByte(ts.sideLocked[side]);

        buf.writeBytes(out);
        msg = out;
    }

    public static class Handler implements IMessageHandler<SocketStateMessage, IMessage>
    {
        @Override
        public IMessage onMessage(SocketStateMessage message, MessageContext ctx)
        {
            //if(message.msg.length == 0) return null;
            World world = Minecraft.getMinecraft().theWorld;

            int x = NetworkUtilities.toInteger(message.msg, 10);
            int y = NetworkUtilities.toInteger(message.msg, 14);
            int z = NetworkUtilities.toInteger(message.msg, 18);
            int side = message.msg[1];

            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                SideConfig c = ts.configs[side];

                c.meta = NetworkUtilities.toInteger(message.msg, 6);

                ts.sides[side] = NetworkUtilities.toInteger(message.msg, 22);
                c.tank = (int)message.msg[2];
                c.inventory = (int)message.msg[3];
                c.rsControl[0] = (message.msg[4] & 1) != 0;
                c.rsControl[1] = (message.msg[4] & 2) != 0;
                c.rsControl[2] = (message.msg[4] & 4) != 0;
                c.rsLatch[0] = (message.msg[5] & 1) != 0;
                c.rsLatch[1] = (message.msg[5] & 2) != 0;
                c.rsLatch[2] = (message.msg[5] & 4) != 0;
                ts.sides[side] = NetworkUtilities.toInteger(message.msg, 22);
                ts.sideLocked[side] = NetworkUtilities.byteToBool(message.msg[26]);
                ts.facID[side] = NetworkUtilities.toInteger(message.msg, 27);
                ts.facMeta[side] = NetworkUtilities.toInteger(message.msg, 31);

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, SocketsMod.socket);
            }

            return null;
        }
    }
}
