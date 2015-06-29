package emasher.client

import cpw.mods.fml.client.registry.{ClientRegistry, RenderingRegistry}
import cpw.mods.fml.common.Loader
import cpw.mods.fml.relauncher.Side
import emasher.nei.NEIHandler
import emasher.{EngineersToolbox, CommonProxy}
import emasher.entities.EntitySmokeBomb
import emasher.packethandling._
import emasher.tileentities.{TileTempRS, TilePipeBase, TileSocket}
import net.minecraft.client.renderer.entity.RenderSnowball

class ClientProxy extends CommonProxy {
  override def registerRenderers(): Unit = {
    RenderingRegistry.registerEntityRenderingHandler( classOf[EntitySmokeBomb], new RenderSnowball( emasher.items.Items.smokeGrenade ) )
    ClientRegistry.bindTileEntitySpecialRenderer( classOf[TileSocket], SocketRenderer.instance )
    ClientRegistry.bindTileEntitySpecialRenderer( classOf[TilePipeBase], PipeRenderer.instance )
    ClientRegistry.bindTileEntitySpecialRenderer( classOf[TileTempRS], TempRSRenderer.instance )
  }

  override def registerMessages(): Unit = {
    EngineersToolbox.network.registerMessage( classOf[RequestInfoFromServerMessage.Handler], classOf[RequestInfoFromServerMessage], 6, Side.SERVER )

    EngineersToolbox.network.registerMessage( classOf[SocketStateMessage.Handler], classOf[SocketStateMessage], 0, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[SocketItemMessage.Handler], classOf[SocketItemMessage], 1, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[SocketFluidMessage.Handler], classOf[SocketFluidMessage], 2, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[PipeColourMessage.Handler], classOf[PipeColourMessage], 3, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[AdapterSideMessage.Handler], classOf[AdapterSideMessage], 4, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[ChangerSideMessage.Handler], classOf[ChangerSideMessage], 5, Side.CLIENT )
  }

  override def registerNEI(): Unit = {
    if (Loader.isModLoaded("NotEnoughItems")) {
      val handler: NEIHandler = new NEIHandler
      handler.loadConfig()
    }
  }
}
