package emasher.sockets;

import cpw.mods.fml.relauncher.Side;
import emasher.sockets.packethandling.RequestInfoFromServerMessage;

public class CommonProxy
{
	public void registerRenderers() 
	{
		// Nothing here as this is the server side proxy
	}

    public void registerMessages()
    {
        SocketsMod.network.registerMessage(RequestInfoFromServerMessage.Handler.class, RequestInfoFromServerMessage.class, 6, Side.SERVER);
    }
}
