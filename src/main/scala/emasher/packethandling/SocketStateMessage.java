package emasher.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.api.SideConfig;
import emasher.tileentities.TileSocket;
import io.netty.buffer.ByteBuf;

public class SocketStateMessage implements IMessage {

	public byte[] msg;
	byte side;
	TileSocket ts;

	public SocketStateMessage() {
	}

	public SocketStateMessage( TileSocket ts, byte side ) {
		this.ts = ts;
		this.side = side;
	}

	@Override
	public void fromBytes( ByteBuf buf ) {
		msg = new byte[buf.capacity()];
		buf.readBytes( msg );
	}

	@Override
	public void toBytes( ByteBuf buf ) {
		buf.capacity( 36 );
		SideConfig c = ts.configs[side];

		byte[] out = new byte[36];

		out[0] = 0;
		out[2] = ( byte ) c.tank;
		out[3] = ( byte ) c.inventory;
		out[4] = ( byte ) ts.rsIndicatorIndex( side );
		out[5] = ( byte ) ts.latchIndicatorIndex( side );

		NetworkUtilities.toByte( out, c.meta, 6 );
		NetworkUtilities.toByte( out, ts.xCoord, 10 );
		NetworkUtilities.toByte( out, ts.yCoord, 14 );
		NetworkUtilities.toByte( out, ts.zCoord, 18 );
		NetworkUtilities.toByte( out, ts.facID[side], 27 );
		NetworkUtilities.toByte( out, ts.facMeta[side], 31 );

		out[1] = ( byte ) side;
		NetworkUtilities.toByte( out, ts.sides[side], 22 );
		out[26] = NetworkUtilities.boolToByte( ts.sideLocked[side] );

		buf.writeBytes( out );
		msg = out;
	}

	public static class Handler implements IMessageHandler<SocketStateMessage, IMessage> {
		@Override
		public IMessage onMessage( SocketStateMessage message, MessageContext ctx ) {
			Handlers.onSocketStateMessage( message, ctx );

			return null;
		}
	}
}
