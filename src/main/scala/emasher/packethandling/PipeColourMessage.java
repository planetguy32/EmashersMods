package emasher.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.tileentities.TilePipeBase;
import io.netty.buffer.ByteBuf;

public class PipeColourMessage implements IMessage {
	public byte[] msg;
	TilePipeBase p;

	public PipeColourMessage() {
	}

	public PipeColourMessage( TilePipeBase p ) {
		this.p = p;
	}

	@Override
	public void fromBytes( ByteBuf buf ) {
		msg = new byte[buf.capacity()];
		buf.readBytes( msg );
	}

	@Override
	public void toBytes( ByteBuf buf ) {
		buf.capacity( 18 );

		byte[] out = new byte[18];

		out[0] = 3;
		NetworkUtilities.toByte( out, p.xCoord, 1 );
		NetworkUtilities.toByte( out, p.yCoord, 5 );
		NetworkUtilities.toByte( out, p.zCoord, 9 );
		NetworkUtilities.toByte( out, p.getWorldObj().provider.dimensionId, 13 );
		out[17] = ( byte ) p.colour;

		buf.writeBytes( out );
		msg = out;
	}

	public static class Handler implements IMessageHandler<PipeColourMessage, IMessage> {
		@Override
		public IMessage onMessage( PipeColourMessage message, MessageContext ctx ) {
			Handlers.onPipeColourMessage( message, ctx );
			return null;
		}
	}
}
