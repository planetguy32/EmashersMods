package emasher.sockets.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import emasher.sockets.CommonProxy;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import emasher.sockets.TileTempRS;
import emasher.sockets.packethandling.*;
import emasher.sockets.pipes.TilePipeBase;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer( TileSocket.class, SocketRenderer.instance );
		ClientRegistry.bindTileEntitySpecialRenderer( TilePipeBase.class, PipeRenderer.instance );
		ClientRegistry.bindTileEntitySpecialRenderer( TileTempRS.class, TempRSRenderer.instance() );
	}

	@Override
	public void registerMessages() {
		SocketsMod.network.registerMessage( RequestInfoFromServerMessage.Handler.class, RequestInfoFromServerMessage.class, 6, Side.SERVER );

		SocketsMod.network.registerMessage( SocketStateMessage.Handler.class, SocketStateMessage.class, 0, Side.CLIENT );
		SocketsMod.network.registerMessage( SocketItemMessage.Handler.class, SocketItemMessage.class, 1, Side.CLIENT );
		SocketsMod.network.registerMessage( SocketFluidMessage.Handler.class, SocketFluidMessage.class, 2, Side.CLIENT );
		SocketsMod.network.registerMessage( PipeColourMessage.Handler.class, PipeColourMessage.class, 3, Side.CLIENT );
		SocketsMod.network.registerMessage( AdapterSideMessage.Handler.class, AdapterSideMessage.class, 4, Side.CLIENT );
		SocketsMod.network.registerMessage( ChangerSideMessage.Handler.class, ChangerSideMessage.class, 5, Side.CLIENT );
	}
}
