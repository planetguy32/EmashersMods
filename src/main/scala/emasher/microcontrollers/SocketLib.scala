package emasher.microcontrollers

import emasher.EngineersToolbox
import emasher.packethandling.SocketStateMessage
import emasher.tileentities.TileSocket
import net.minecraftforge.common.util.ForgeDirection
import org.mod.luaj.vm2.lib.{OneArgFunction, TwoArgFunction}
import org.mod.luaj.vm2.{LuaUserdata, LuaValue}

class SocketLib extends TwoArgFunction {
  implicit var tileEntity: Option[TileSocket] = None

  val getModuleIdInstance = new getModuleId
  val setInventoryInstance = new setInventory
  val setTankInstance = new setTank

  override def call( modName: LuaValue, env: LuaValue ): LuaValue = {
    val library = LuaValue.tableOf

    library.set( "getModuleId", getModuleIdInstance )
    library.set( "setInventory", setInventoryInstance )
    library.set( "setTank", setTankInstance )

    env.set( "SocketLib", library )
    val socketValue = env.get( "socketObject" )
    socketValue match {
      case u: LuaUserdata =>
        u.userdata() match {
          case s: TileSocket =>
            tileEntity = Option( s )
          case _ =>
        }
      case _ =>
    }
    library
  }

  class getModuleId extends OneArgFunction {
    override def call( side: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide ) =>
        LuaValue.valueOf( t.sides( theSide.ordinal() ) )
      }
    }
  }

  class setInventory extends TwoArgFunction {
    override def call( side: LuaValue, inventory: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide ) =>
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
      }
    }
  }

  class setTank extends TwoArgFunction {
    override def call( side: LuaValue, tank: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide ) =>
        val theTank = tank.checkint
        if( theSide != ForgeDirection.UNKNOWN && t.getSide( theSide ).hasTankIndicator ) {
          if( theTank >= 0 && theTank < 3 ) {
            t.getConfigForSide( theSide ).tank = theTank
            t.getSide( theSide ).indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
            EngineersToolbox.network.sendToDimension( new SocketStateMessage( t, theSide.ordinal( ).toByte ),
              t.getWorldObj.provider.dimensionId )
          }
        }
        LuaValue.NIL
      }
    }
  }

  object SidedRun {
    def apply( side: LuaValue )( impl: ( TileSocket, ForgeDirection ) => LuaValue )( implicit te: Option[TileSocket] ): LuaValue = {
      tileEntity match {
        case Some( t: TileSocket ) =>
          val theSide = ForgeDirection.getOrientation( side.checkint )
          impl( t, theSide )
        case _ => LuaValue.NIL
      }
    }
  }
}
