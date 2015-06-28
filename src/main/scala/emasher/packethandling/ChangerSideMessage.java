package emasher.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.tileentities.TileDirectionChanger;
import io.netty.buffer.ByteBuf;

public class ChangerSideMessage implements IMessage {
	public byte[] msg;
	TileDirectionChanger p;
	byte side;

	public ChangerSideMessage() {
	}

	public ChangerSideMessage( TileDirectionChanger p, byte side ) {
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

		out[0] = 5;
		NetworkUtilities.toByte( out, p.xCoord, 1 );
		NetworkUtilities.toByte( out, p.yCoord, 5 );
		NetworkUtilities.toByte( out, p.zCoord, 9 );
		NetworkUtilities.toByte( out, p.getWorldObj().provider.dimensionId, 13 );
		out[17] = ( byte ) p.directions[side].ordinal();
		out[18] = ( byte ) side;

		buf.writeBytes( out );
		msg = out;
	}

	public static class Handler implements IMessageHandler<ChangerSideMessage, IMessage> {
		@Override
		public IMessage onMessage( ChangerSideMessage message, MessageContext ctx ) {
			Handlers.onChangeSideMessage( message, ctx );

			return null;
		}
	}
}
