package emasher.sockets;

import cpw.mods.fml.relauncher.Side;
import emasher.sockets.packethandling.*;

public class CommonProxy {
	public void registerRenderers() {
		// Nothing here as this is the server side proxy
	}

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
