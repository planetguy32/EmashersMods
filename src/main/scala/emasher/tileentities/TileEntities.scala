package emasher.tileentities

import cpw.mods.fml.common.registry.GameRegistry

object TileEntities {
  def register(): Unit = {
    GameRegistry.registerTileEntity(classOf[TileStartPipe], "emasherstartpipe")
    GameRegistry.registerTileEntity(classOf[TileFluidPipe], "emasherfluidpipe")
    GameRegistry.registerTileEntity(classOf[TileEnergyPipe], "emasherenergypipe")
    GameRegistry.registerTileEntity(classOf[TileSocket], "modular_socket")
    GameRegistry.registerTileEntity(classOf[TileTempRS], "TempRS")
    GameRegistry.registerTileEntity(classOf[TilePipeBase], "emasherbasepipe")
    GameRegistry.registerTileEntity(classOf[TileMiniPortal], "emasherminiportal")
    GameRegistry.registerTileEntity(classOf[TileDirectionChanger], "emasherdirectionchanger")
    GameRegistry.registerTileEntity(classOf[TileFrame], "emasherframe")
    GameRegistry.registerTileEntity(classOf[TileShaleResource], "shaleResource")
    GameRegistry.registerTileEntity(classOf[TileGas], "gas")
    GameRegistry.registerTileEntity(classOf[TileDuct], "chimney")
    GameRegistry.registerTileEntity(classOf[TileDeflectorGen], "DeflectorGen")
  }
}
