package emasher.microcontrollers

import emasher.EngineersToolbox
import emasher.packethandling.SocketStateMessage
import emasher.tileentities.TileSocket
import net.minecraftforge.common.util.ForgeDirection
import org.mod.luaj.vm2.LuaValue
import org.mod.luaj.vm2.lib.{OneArgFunction, TwoArgFunction}

class SocketLib extends TwoArgFunction {
  var tileEntity: Option[TileSocket] = None
  var getModuleIdInstance = new getModuleId
  var setInventoryInstance = new setInventory

  def init( entity: TileSocket ): SocketLib = {
    tileEntity = Option( entity )
    this
  }

  override def call( modName: LuaValue, env: LuaValue ): LuaValue = {
    val library = LuaValue.tableOf
    library.set( "getModuleId", getModuleIdInstance )
    library.set( "setInventory", setInventoryInstance )
    env.set( "socket", library )
    library
  }

  class getModuleId extends OneArgFunction {
    override def call( side: LuaValue ): LuaValue = {
      tileEntity match {
        case Some( t: TileSocket ) => LuaValue.valueOf( t.sides( side.checkint ) )
        case _ => LuaValue.valueOf( 0 )
      }
    }
  }

  class setInventory extends TwoArgFunction {
    override def call( side: LuaValue, inventory: LuaValue ): LuaValue = {
      tileEntity match {
        case Some( t: TileSocket ) =>
          val theSide = ForgeDirection.getOrientation( side.checkint )
          val theInventory = inventory.checkint
          if( theSide != ForgeDirection.UNKNOWN && t.getSide( theSide ).hasInventoryIndicator ) {
            if( theInventory >= 0 && theInventory < 3 ) {
              t.getConfigForSide( theSide ).inventory = theInventory
              t.getSide( theSide ).indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
              EngineersToolbox.network.sendToDimension( new SocketStateMessage( t, theSide.ordinal().toByte ),
                t.getWorldObj.provider.dimensionId )
            }
          }
          LuaValue.NIL
        case _ => LuaValue.NIL
      }
    }
  }
}
