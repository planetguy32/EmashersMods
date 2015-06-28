package emasher.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.tileentities.TileAdapterBase;
import io.netty.buffer.ByteBuf;

public class AdapterSideMessage implements IMessage {
	public byte[] msg;
	TileAdapterBase p;
	byte side;

	public AdapterSideMessage() {
	}

	public AdapterSideMessage( TileAdapterBase p, byte side ) {
		this.p = p;
		this.side = side;
	}

	@Override
	public void fromBytes( ByteBuf buf ) {
		msg = new byte[buf.capacity()];
		buf.readBytes( msg );
	}

	@Override
	public void toBytes( ByteBuf buf ) {
		buf.capacity( 19 );

		byte[] out = new byte[19];

		out[0] = 4;
		NetworkUtilities.toByte( out, p.xCoord, 1 );
		NetworkUtilities.toByte( out, p.yCoord, 5 );
		NetworkUtilities.toByte( out, p.zCoord, 9 );

		NetworkUtilities.toByte( out, p.getWorldObj().provider.dimensionId, 13 );
		if( p.outputs[side] ) out[17] = 1;
		else out[17] = 0;
		out[18] = ( byte ) side;

		buf.writeBytes( out );
		msg = out;
	}

	public static class Handler implements IMessageHandler<AdapterSideMessage, IMessage> {
		@Override
		public IMessage onMessage( AdapterSideMessage message, MessageContext ctx ) {
			Handlers.onAdapterSideMessage( message, ctx );
			return null;
		}
	}
}
