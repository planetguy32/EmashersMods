package emasher.sockets.nei

import codechicken.nei.api.{API, IConfigureNEI}
import codechicken.nei.recipe.TemplateRecipeHandler

class NEIHandler extends IConfigureNEI {

  override def loadConfig(): Unit = {
    NEIHandler.registerHandler( new GrinderRecipeHandler )
    NEIHandler.registerHandler( new MultiSmelterRecipeHandler )
  }

  override def getName: String = "eng_toolbox"

  override def getVersion: String = "1.2.0.7"
}

object NEIHandler {
  private def registerHandler( handler: TemplateRecipeHandler ): Unit = {
    API.registerRecipeHandler( handler )
    API.registerUsageHandler( handler )
  }
}