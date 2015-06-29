package emasher

import cpw.mods.fml.relauncher.Side
import emasher.packethandling._

class CommonProxy {
  def registerMessages(): Unit = {
    EngineersToolbox.network.registerMessage( classOf[RequestInfoFromServerMessage.Handler], classOf[RequestInfoFromServerMessage], 6, Side.SERVER )
    EngineersToolbox.network.registerMessage( classOf[SocketStateMessage.Handler], classOf[SocketStateMessage], 0, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[SocketItemMessage.Handler], classOf[SocketItemMessage], 1, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[SocketFluidMessage.Handler], classOf[SocketFluidMessage], 2, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[PipeColourMessage.Handler], classOf[PipeColourMessage], 3, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[AdapterSideMessage.Handler], classOf[AdapterSideMessage], 4, Side.CLIENT )
    EngineersToolbox.network.registerMessage( classOf[ChangerSideMessage.Handler], classOf[ChangerSideMessage], 5, Side.CLIENT )
  }

  def registerRenderers(): Unit = {

  }

  def registerNEI(): Unit = {

  }
}
