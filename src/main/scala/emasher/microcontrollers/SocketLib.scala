package emasher.microcontrollers

import emasher.EngineersToolbox
import emasher.api.{SocketModule, SideConfig}
import emasher.packethandling.SocketStateMessage
import emasher.tileentities.TileSocket
import net.minecraftforge.common.util.ForgeDirection
import org.mod.luaj.vm2.lib.{ZeroArgFunction, ThreeArgFunction, OneArgFunction, TwoArgFunction}
import org.mod.luaj.vm2.{LuaUserdata, LuaValue}

class SocketLib extends TwoArgFunction {
  implicit var tileEntity: Option[TileSocket] = None

  val getModuleIdInstance = new getModuleId
  val setInventoryInstance = new setInventory
  val setTankInstance = new setTank
  val setCircuitInstance = new setCircuit
  val setLatchInstance = new setLatch
  val toggleCircuitInstance = new toggleCircuit
  val toggleLatchInstance = new toggleLatch
  val getInventoryInstance = new getInventory
  val getTankInstance = new getTank
  val getCircuitInstance = new getCircuit
  val getLatchInstance = new getLatch

  override def call( modName: LuaValue, env: LuaValue ): LuaValue = {
    val library = LuaValue.tableOf

    library.set( "getModuleId", getModuleIdInstance )

    library.set( "setInventory", setInventoryInstance )
    library.set( "setTank", setTankInstance )
    library.set( "setCircuit", setCircuitInstance )
    library.set( "setLatch", setLatchInstance )
    library.set( "toggleCircuit", toggleCircuitInstance )
    library.set( "toggleLatch", toggleLatchInstance )

    library.set( "getInventory", getInventoryInstance )
    library.set( "getTank", getTankInstance )
    library.set( "getCircuit", getCircuitInstance )
    library.set( "getLatch", getLatchInstance )

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
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        LuaValue.valueOf( theModule.moduleID )
      }
    }
  }

  class setInventory extends TwoArgFunction {
    override def call( side: LuaValue, inventory: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theInventory = inventory.checkint
        if( theModule.hasInventoryIndicator && theInventory >= -1 && theInventory < 3 ) {
          theConfig.inventory = theInventory
          theModule.indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
          updateClientSide( theSide )
        }
        LuaValue.NIL
      }
    }
  }

  class setTank extends TwoArgFunction {
    override def call( side: LuaValue, tank: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theTank = tank.checkint
        if( theModule.hasTankIndicator && theTank >= -1 && theTank < 3 ) {
          theConfig.tank = theTank
          theModule.indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
          updateClientSide( theSide )
        }
        LuaValue.NIL
      }
    }
  }

  class setCircuit extends ThreeArgFunction {
    override def call( side: LuaValue, circuit: LuaValue, state: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theCircuit = circuit.checkint
        val theState = state.checkboolean
        if( theCircuit >= 0 && theCircuit < 3 && theModule.hasRSIndicator ) {
          theConfig.rsControl( theCircuit ) = theState
          theModule.indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
          updateClientSide( theSide )
        }
        LuaValue.NIL
      }
    }
  }

  class setLatch extends ThreeArgFunction {
    override def call( side: LuaValue, latch: LuaValue, state: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theLatch = latch.checkint
        val theState = state.checkboolean
        if( theLatch >= 0 && theLatch < 3 && theModule.hasLatchIndicator ) {
          theConfig.rsLatch( theLatch ) = theState
          theModule.indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
          updateClientSide( theSide )
        }
        LuaValue.NIL
      }
    }
  }

  class toggleCircuit extends TwoArgFunction {
    override def call( side: LuaValue, circuit: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theCircuit = circuit.checkint
        if( theCircuit >= 0 && theCircuit < 3 && theModule.hasRSIndicator ) {
          theConfig.rsControl( theCircuit ) = ! theConfig.rsControl( theCircuit )
          theModule.indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
          updateClientSide( theSide )
        }
        LuaValue.NIL
      }
    }
  }

  class toggleLatch extends TwoArgFunction {
    override def call( side: LuaValue, latch: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theLatch = latch.checkint
        if( theLatch >= 0 && theLatch < 3 && theModule.hasLatchIndicator ) {
          theConfig.rsControl( theLatch ) = ! theConfig.rsControl( theLatch )
          theModule.indicatorUpdated( t, t.getConfigForSide( theSide ), theSide )
          updateClientSide( theSide )
        }
        LuaValue.NIL
      }
    }
  }

  class getInventory extends OneArgFunction {
    override def call( side: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule) =>
        if( theModule.hasInventoryIndicator ) {
          LuaValue.valueOf( theConfig.inventory )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getTank extends OneArgFunction {
    override def call( side: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule) =>
        if( theModule.hasTankIndicator ) {
          LuaValue.valueOf( theConfig.tank )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getCircuit extends TwoArgFunction {
    override def call( side: LuaValue, circuit: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theCircuit = circuit.checkint
        if( theCircuit >= 0 && theCircuit < 3 ) {
          LuaValue.valueOf( theConfig.rsControl( theCircuit ) )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getLatch extends TwoArgFunction {
    override def call( side: LuaValue, latch: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val theLatch = latch.checkint
        if( theLatch >= 0 && theLatch < 3 ) {
          LuaValue.valueOf( theConfig.rsLatch( theLatch ) )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getCircuitValue extends OneArgFunction {
    override def call( circuit: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theCircuit = circuit.checkint
        if( theCircuit >= 0 && theCircuit < 3) {
          LuaValue.valueOf( t.getRSControl( theCircuit ) )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getLatchValue extends OneArgFunction {
    override def call( latch: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theLatch = latch.checkint
        if( theLatch >= 0 && theLatch < 3) {
          LuaValue.valueOf( t.getRSLatch( theLatch ) )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class setCircuitValue extends TwoArgFunction {
    override def call( circuit: LuaValue, state: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theCircuit = circuit.checkint
        val theState = state.checkboolean
        if( theCircuit >= 0 && theCircuit < 3 ) {
          t.modifyRS( theCircuit, theState )
        }
        LuaValue.NIL
      }
    }
  }

  class setLatchValue extends TwoArgFunction {
    override def call( latch: LuaValue, state: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theLatch = latch.checkint
        val theState = state.checkboolean
        if( theLatch >= 0 && theLatch < 3 ) {
          t.modifyLatch( theLatch, theState )
        }
        LuaValue.NIL
      }
    }
  }

  class toggleCircuitValue extends OneArgFunction {
    override def call( circuit: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theCircuit = circuit.checkint
        if( theCircuit >= 0 && theCircuit < 3 ) {
          t.modifyRS( theCircuit, ! t.getRSControl( theCircuit ) )
        }
        LuaValue.NIL
      }
    }
  }

  class toggleLatchValue extends OneArgFunction {
    override def call( latch: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theLatch = latch.checkint
        if( theLatch >= 0 && theLatch < 3 ) {
          t.modifyLatch( theLatch, ! t.getRSLatch( theLatch ) )
        }
        LuaValue.NIL
      }
    }
  }

  class getInventoryAmount extends OneArgFunction {
    override def call( inventory: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theInventory = inventory.checkint
        if( theInventory >= 0 && theInventory < 3 && t.getStackInInventorySlot( theInventory ) != null ) {
          LuaValue.valueOf( t.getStackInInventorySlot( theInventory ).stackSize )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getInventoryItem extends OneArgFunction {
    override def call( inventory: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theInventory = inventory.checkint
        if( theInventory >= 0 && theInventory < 3 && t.getStackInInventorySlot( theInventory ) != null ) {
          LuaValue.valueOf( t.getStackInInventorySlot( theInventory ).getItem.getUnlocalizedName )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getInventoryMeta extends OneArgFunction {
    override def call( inventory: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theInventory = inventory.checkint
        if( theInventory >= 0 && theInventory < 3 && t.getStackInInventorySlot( theInventory ) != null ) {
          LuaValue.valueOf( t.getStackInInventorySlot( theInventory ).getItemDamage )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getTankAmount extends OneArgFunction {
    override def call( tank: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theTank = tank.checkint
        if( theTank >= 0 && theTank < 3 && t.getFluidInTank( theTank ) != null ) {
          LuaValue.valueOf( t.getFluidInTank( theTank ).amount )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getTankFluid extends OneArgFunction {
    override def call( tank: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theTank = tank.checkint
        if( theTank >= 0 && theTank < 3 && t.getFluidInTank( theTank ) != null ) {
          LuaValue.valueOf( t.getFluidInTank( theTank ).getUnlocalizedName )
        } else {
          LuaValue.NIL
        }
      }
    }
  }

  class getTankCapacity extends ZeroArgFunction {
    override def call(): LuaValue = {
      Run { ( t ) =>
        LuaValue.valueOf( t.tanks(0).getCapacity )
      }
    }
  }

  class getStoredEnergy extends ZeroArgFunction {
    override def call(): LuaValue = {
      Run { ( t ) =>
        LuaValue.valueOf( t.capacitor.getEnergyStored )
      }
    }
  }

  class getEnergyCapacity extends ZeroArgFunction {
    override def call(): LuaValue = {
      Run { ( t ) =>
        LuaValue.valueOf( t.capacitor.getMaxEnergyStored )
      }
    }
  }

  object SidedRun {
    def apply( side: LuaValue )( impl: ( TileSocket, ForgeDirection, SideConfig, SocketModule ) => LuaValue )( implicit te: Option[TileSocket] ): LuaValue = {
      tileEntity match {
        case Some( t: TileSocket ) =>
          val theSide = ForgeDirection.getOrientation( side.checkint )
          if( theSide != ForgeDirection.UNKNOWN ) {
            val theConfig = t.getConfigForSide( theSide )
            val theModule = t.getSide( theSide )
            impl( t, theSide, theConfig, theModule )
          } else {
            LuaValue.NIL
          }
        case _ => LuaValue.NIL
      }
    }
  }

  object Run {
    def apply( impl: ( TileSocket ) => LuaValue )( implicit te: Option[TileSocket] ): LuaValue = {
      tileEntity match {
        case Some( t: TileSocket ) =>
          impl( t )
        case _ => LuaValue.NIL
      }
    }
  }

  private def updateClientSide( side: ForgeDirection ): Unit = {
    tileEntity match {
      case Some( t: TileSocket ) =>
        EngineersToolbox.network.sendToDimension( new SocketStateMessage( t, side.ordinal.toByte ),
          t.getWorldObj.provider.dimensionId )
      case _ =>
    }

  }
}
