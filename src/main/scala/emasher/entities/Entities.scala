package emasher.entities

import cpw.mods.fml.common.registry.EntityRegistry
import emasher.EngineersToolbox

object Entities {
  def register(): Unit = {
    EntityRegistry.registerModEntity(classOf[EntitySmokeBomb], "smokeGrenade", 1, EngineersToolbox, 80, 3, true)
  }
}
