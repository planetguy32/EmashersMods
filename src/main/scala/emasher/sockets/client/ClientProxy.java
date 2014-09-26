package emasher.sockets.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import emasher.sockets.TileTempRS;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import emasher.sockets.CommonProxy;
import emasher.sockets.TileSocket;
import emasher.sockets.pipes.TilePipeBase;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers() 
    {
		ClientRegistry.bindTileEntitySpecialRenderer(TileSocket.class,  SocketRenderer.instance);
		ClientRegistry.bindTileEntitySpecialRenderer(TilePipeBase.class, PipeRenderer.instance);
        ClientRegistry.bindTileEntitySpecialRenderer(TileTempRS.class, TempRSRenderer.instance());
	}
}
