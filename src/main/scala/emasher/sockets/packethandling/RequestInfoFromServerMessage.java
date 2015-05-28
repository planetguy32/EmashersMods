package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.sockets.TileSocket;
import emasher.sockets.pipes.TileAdapterBase;
import emasher.sockets.pipes.TileDirectionChanger;
import emasher.sockets.pipes.TilePipeBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RequestInfoFromServerMessage implements IMessage {
	byte[] msg;
	TileEntity te;
	byte tag;
	byte id;

	public RequestInfoFromServerMessage() {
	}

	public RequestInfoFromServerMessage( TileEntity te, byte tag, byte id ) {
		this.te = te;
		this.tag = tag;
		this.id = id;
	}

	@Override
	public void fromBytes( ByteBuf buf ) {
		msg = new byte[buf.capacity()];
		buf.readBytes( msg );
	}

	@Override
	public void toBytes( ByteBuf buf ) {
		byte[] out = new byte[18];

		out[0] = id;
		NetworkUtilities.toByte( out, te.xCoord, 1 );
		NetworkUtilities.toByte( out, te.yCoord, 5 );
		NetworkUtilities.toByte( out, te.zCoord, 9 );
		NetworkUtilities.toByte( out, te.getWorldObj().provider.dimensionId, 13 );
		out[17] = tag;

		buf.capacity( 18 );
		buf.writeBytes( out );
		msg = out;
	}

	public static class Handler implements IMessageHandler<RequestInfoFromServerMessage, IMessage> {
		@Override
		public IMessage onMessage( RequestInfoFromServerMessage message, MessageContext ctx ) {
			World world = ctx.getServerHandler().playerEntity.worldObj;

			byte id = message.msg[0];
			int x = NetworkUtilities.toInteger( message.msg, 1 );
			int y = NetworkUtilities.toInteger( message.msg, 5 );
			int z = NetworkUtilities.toInteger( message.msg, 9 );
			TileEntity te = world.getTileEntity( x, y, z );

			int dimID = NetworkUtilities.toInteger( message.msg, 13 );
			byte tag = message.msg[17];

			switch(id) {
				case 0:
					if( te != null && te instanceof TileSocket ) {
						TileSocket ts = ( TileSocket ) te;

						return new SocketStateMessage( ts, tag );
					}
					break;
				case 1:
					if( te != null && te instanceof TileSocket ) {
						TileSocket ts = ( TileSocket ) te;

						return new SocketItemMessage( ts, tag );
					}
					break;
				case 2:
					if( te != null && te instanceof TileSocket ) {
						TileSocket ts = ( TileSocket ) te;

						return new SocketFluidMessage( ts, tag );
					}
					break;
				case 3:
					if( te != null && te instanceof TilePipeBase ) {
						TilePipeBase ts = ( TilePipeBase ) te;

						return new PipeColourMessage( ts );
					}
					break;
				case 4:
					if( te != null && te instanceof TileAdapterBase ) {
						TileAdapterBase ts = ( TileAdapterBase ) te;

						return new AdapterSideMessage( ts, tag );
					}
					break;
				case 5:
					if( te != null && te instanceof TileDirectionChanger ) {
						TileDirectionChanger ts = ( TileDirectionChanger ) te;

						return new ChangerSideMessage( ts, tag );
					}
					break;
				default:
					break;
			}

			return null;
		}
	}
}
