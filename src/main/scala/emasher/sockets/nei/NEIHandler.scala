package emasher.sockets.nei

import codechicken.nei.api.{API, IConfigureNEI}
import codechicken.nei.recipe.TemplateRecipeHandler
import emasher.sockets.SocketsMod

class NEIHandler extends IConfigureNEI {

  override def loadConfig(): Unit = {
    if( SocketsMod.enableGrinder ) NEIHandler.registerHandler( new GrinderRecipeHandler )
    if( SocketsMod.enableMultiSmelter ) NEIHandler.registerHandler( new MultiSmelterRecipeHandler )
    NEIHandler.registerHandler( new SpinningWheelRecipeHandler )
    if( SocketsMod.enableKiln ) NEIHandler.registerHandler( new KilnRecipeHandler )
    if( SocketsMod.enableCentrifuge ) NEIHandler.registerHandler( new CentrifugeRecipeHandler )
    NEIHandler.registerHandler( new RefineryRecipeHandler )
    NEIHandler.registerHandler( new MixerRecipeHandler )
    NEIHandler.registerHandler( new PhotobioreactorRecipeHandler )
  }

  override def getName: String = "eng_toolbox"

  override def getVersion: String = "1.2.1.0"
}

object NEIHandler {
  private def registerHandler( handler: TemplateRecipeHandler ): Unit = {
    API.registerRecipeHandler( handler )
    API.registerUsageHandler( handler )
  }
}