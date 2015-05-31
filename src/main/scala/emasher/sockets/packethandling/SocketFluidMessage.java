package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.TileSocket;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fluids.FluidStack;

public class SocketFluidMessage implements IMessage {
	public byte[] msg;
	byte tank;
	TileSocket ts;

	public SocketFluidMessage() {
	}

	public SocketFluidMessage( TileSocket ts, byte tank ) {
		this.ts = ts;
		this.tank = tank;
	}

	@Override
	public void fromBytes( ByteBuf buf ) {
		msg = new byte[buf.capacity()];
		buf.readBytes( msg );
	}

	@Override
	public void toBytes( ByteBuf buf ) {
		buf.capacity( 27 );

		FluidStack l = ts.tanks[tank].getFluid();

		int id = -1;
		int meta = 0;
		int amnt = 0;

		byte[] out = new byte[27];
		out[0] = 2;

		if( l != null ) {
			id = l.getFluidID();
			meta = 0;
			amnt = l.amount;
		} else {
			id = -1;
			meta = 0;
			amnt = 0;
		}

		NetworkUtilities.toByte( out, ts.xCoord, 1 );
		NetworkUtilities.toByte( out, ts.yCoord, 5 );
		NetworkUtilities.toByte( out, ts.zCoord, 9 );
		NetworkUtilities.toByte( out, id, 13 );
		NetworkUtilities.toByte( out, meta, 17 );
		NetworkUtilities.toByte( out, amnt, 22 );
		out[26] = ( byte ) tank;

		buf.writeBytes( out );
		msg = out;
	}

	public static class Handler implements IMessageHandler<SocketFluidMessage, IMessage> {
		@Override
		public IMessage onMessage( SocketFluidMessage message, MessageContext ctx ) {
			Handlers.onSocketFluidMessage( message, ctx );

			return null;
		}
	}
}
