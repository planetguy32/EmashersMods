package emasher.sockets.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import emasher.sockets.CommonProxy;
import emasher.sockets.TileSocket;
import emasher.sockets.pipes.TilePipeBase;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers() 
	{
		/*TileEntityRenderer.instance.specialRendererMap.put(TileSocket.class, SocketRenderer.instance);
		TileEntityRenderer.instance.specialRendererMap.put(TilePipeBase.class, PipeRenderer.instance);
		SocketRenderer.instance.setTileEntityRenderer(TileEntityRenderer.instance);
		PipeRenderer.instance.setTileEntityRenderer(TileEntityRenderer.instance);*/
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileSocket.class,  SocketRenderer.instance);
		ClientRegistry.bindTileEntitySpecialRenderer(TilePipeBase.class, PipeRenderer.instance);
	}
}
