package emasher.nei

import codechicken.nei.api.{API, IConfigureNEI}
import codechicken.nei.recipe.TemplateRecipeHandler

class NEIHandler extends IConfigureNEI {

  override def loadConfig(): Unit = {
    if( emasher.util.Config.enableGrinder ) NEIHandler.registerHandler( new GrinderRecipeHandler )
    if( emasher.util.Config.enableMultiSmelter ) NEIHandler.registerHandler( new MultiSmelterRecipeHandler )
    NEIHandler.registerHandler( new SpinningWheelRecipeHandler )
    if( emasher.util.Config.enableKiln ) NEIHandler.registerHandler( new KilnRecipeHandler )
    if( emasher.util.Config.enableCentrifuge ) NEIHandler.registerHandler( new CentrifugeRecipeHandler )
    NEIHandler.registerHandler( new RefineryRecipeHandler )
    NEIHandler.registerHandler( new MixerRecipeHandler )
    NEIHandler.registerHandler( new PhotobioreactorRecipeHandler )
  }

  override def getName: String = "eng_toolbox"

  override def getVersion: String = "1.2.2.0"
}

object NEIHandler {
  private def registerHandler( handler: TemplateRecipeHandler ): Unit = {
    API.registerRecipeHandler( handler )
    API.registerUsageHandler( handler )
  }
}