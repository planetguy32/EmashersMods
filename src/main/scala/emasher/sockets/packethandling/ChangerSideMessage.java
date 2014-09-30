package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.pipes.TileDirectionChanger;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ChangerSideMessage implements IMessage
{
    TileDirectionChanger p;

    public byte[] msg;

    byte side;

    public ChangerSideMessage()
    {
    }

    public ChangerSideMessage(TileDirectionChanger p, byte side)
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

        out[0] = 5;
        NetworkUtilities.toByte(out, p.xCoord, 1);
        NetworkUtilities.toByte(out, p.yCoord, 5);
        NetworkUtilities.toByte(out, p.zCoord, 9);
        NetworkUtilities.toByte(out, p.getWorldObj().provider.dimensionId, 13);
        out[17] = (byte)p.directions[side].ordinal();
        out[18] = (byte)side;

        buf.writeBytes(out);
        msg = out;
    }

    public static class Handler implements IMessageHandler<ChangerSideMessage, IMessage>
    {
        @Override
        public IMessage onMessage(ChangerSideMessage message, MessageContext ctx)
        {
            World world = Minecraft.getMinecraft().theWorld;

            int x = NetworkUtilities.toInteger(message.msg, 1);
            int y = NetworkUtilities.toInteger(message.msg, 5);
            int z = NetworkUtilities.toInteger(message.msg, 9);
            int id = NetworkUtilities.toInteger(message.msg, 13);
            ForgeDirection dir = ForgeDirection.getOrientation(message.msg[17]);
            int side = (int)message.msg[18];

            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileDirectionChanger)
            {
                TileDirectionChanger t = (TileDirectionChanger)te;

                t.directions[side] = dir;

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
            }

            return null;
        }
    }
}
