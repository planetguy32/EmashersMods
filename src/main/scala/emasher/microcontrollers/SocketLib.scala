package emasher.microcontrollers

import emasher.EngineersToolbox
import emasher.api.{SocketModule, SideConfig}
import emasher.modules.{ModRummager, ModTrack, ModElevator}
import emasher.packethandling.SocketStateMessage
import emasher.tileentities.TileSocket
import net.minecraftforge.common.util.ForgeDirection
import org.mod.luaj.vm2.lib.{ZeroArgFunction, ThreeArgFunction, OneArgFunction, TwoArgFunction}
import org.mod.luaj.vm2.{Globals, LuaUserdata, LuaValue}

class SocketLib {
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

  val getCircuitValueInstance = new getCircuitValue
  val getLatchValueInstance = new getLatchValue
  val setCircuitValueInstance = new setCircuitValue
  val setLatchValueInstance = new setLatchValue
  val toggleCircuitValueInstance = new toggleCircuitValue
  val toggleLatchValueInstance = new toggleLatchValue

  val getInventoryAmountInstance = new getInventoryAmount
  val getInventoryItemInstance = new getInventoryItem
  val getInventoryMetaInstance = new getInventoryMeta

  val getTankAmountInstance = new getTankAmount
  val getTankFluidInstance = new getTankFluid
  val getTankCapacityInstance = new getTankCapacity

  val getStoredEnergyInstance = new getStoredEnergy
  val getEnergyCapacityInstance = new getEnergyCapacity

  val sendGenericSignalInstance = new sendGenericSignal
  val isSolidBlockOnSideInstance = new isSolidBlockOnSide
  val setElevatorDirectionInstance = new setElevatorDirection
  val toggleElevatorDirectionInstance = new toggleElevatorDirection
  val setTrackDirectionInstance = new setTrackDirection

  val moveItemInstance = new moveItem
  val extractWithRummagerInstance = new extractWithRummager
  val insertWithRummagerInstance = new insertWithRummager
  val moveFluidInstance = new moveFluid

  def install( library: Globals ): Unit = {
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

    library.set( "getCircuitValue", getCircuitValueInstance )
    library.set( "getLatchValue", getLatchValueInstance )
    library.set( "setCircuitValue", setCircuitValueInstance )
    library.set( "setLatchValue", setLatchValueInstance )
    library.set( "toggleCircuitValue", toggleCircuitValueInstance )
    library.set( "toggleLatchValue", toggleLatchValueInstance )

    library.set( "getInventoryAmount", getInventoryAmountInstance )
    library.set( "getInventoryItem", getInventoryItemInstance )
    library.set( "getInventoryMeta", getInventoryMetaInstance )

    library.set( "getTankAmount", getTankAmountInstance )
    library.set( "getTankFluid", getTankFluidInstance )
    library.set( "getTankCapacity", getTankCapacityInstance )

    library.set( "getStoredEnergy", getStoredEnergyInstance )
    library.set( "getEnergyCapacity", getEnergyCapacityInstance )

    library.set( "sendGenericSignal", sendGenericSignalInstance )
    library.set( "isSolidBlockOnSide", isSolidBlockOnSideInstance )
    library.set( "setElevatorDirection", setElevatorDirectionInstance )
    library.set( "toggleElevatorDirection", toggleElevatorDirectionInstance )
    library.set( "setTrackDirection", setTrackDirectionInstance )

    library.set( "moveItem", moveItemInstance )
    library.set( "extractWithRummager", extractWithRummagerInstance )
    library.set( "insertWithRummager", insertWithRummagerInstance )
    library.set( "moveFluid", moveFluidInstance )

    val socketValue = library.get( "socketObject" )
    socketValue match {
      case u: LuaUserdata =>
        u.userdata() match {
          case s: TileSocket =>
            tileEntity = Option( s )
          case _ =>
        }
      case _ =>
    }
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
    override def call: LuaValue = {
      Run { ( t ) =>
        LuaValue.valueOf( t.tanks(0).getCapacity )
      }
    }
  }

  class getStoredEnergy extends ZeroArgFunction {
    override def call: LuaValue = {
      Run { ( t ) =>
        LuaValue.valueOf( t.capacitor.getEnergyStored )
      }
    }
  }

  class getEnergyCapacity extends ZeroArgFunction {
    override def call: LuaValue = {
      Run { ( t ) =>
        LuaValue.valueOf( t.capacitor.getMaxEnergyStored )
      }
    }
  }

  class sendGenericSignal extends OneArgFunction {
    override def call( side: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        theModule.onGenericRemoteSignal( t, theConfig, theSide )
        LuaValue.NIL
      }
    }
  }

  class isSolidBlockOnSide extends OneArgFunction {
    override def call( side: LuaValue ): LuaValue = {
      SidedRun( side ) { ( t, theSide, theConfig, theModule ) =>
        val xo = t.xCoord + theSide.offsetX
        val yo = t.yCoord + theSide.offsetY
        val zo = t.zCoord + theSide.offsetZ
        val block = t.getWorldObj.getBlock( xo, yo, zo )
        LuaValue.valueOf( block.isAir( t.getWorldObj, xo, yo, zo ) || block.isReplaceable( t.getWorldObj, xo, yo, zo ) )
      }
    }
  }

  class setElevatorDirection extends OneArgFunction {
    override def call( direction: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theDirection = direction.checkint
        for( i <- 2 to 5 ) {
          val theModule = t.getSide( ForgeDirection.getOrientation( i ) )
          if( theModule.isInstanceOf[ModElevator] ) {
            if( theDirection == 0 ) {
              t.configs( i ).rsLatch( 0 ) = false
            } else {
              t.configs( i ).rsLatch( 0 ) = true
            }
            updateClientSide( ForgeDirection.getOrientation( i ) )
          }
        }
        LuaValue.NIL
      }
    }
  }

  class toggleElevatorDirection extends ZeroArgFunction {
    override def call: LuaValue = {
      Run { ( t ) =>
        for( i <- 2 to 5 ) {
          val theModule = t.getSide( ForgeDirection.getOrientation( i ) )
          if( theModule.isInstanceOf[ModElevator] ) {
            t.configs( i ).rsLatch( 0 ) = ! t.configs( i ).rsLatch( 0 )
            updateClientSide( ForgeDirection.getOrientation( i ) )
          }
        }
        LuaValue.NIL
      }
    }
  }

  class setTrackDirection extends OneArgFunction {
    override def call( direction: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theDirection = direction.checkint
        val theModule = t.getSide( ForgeDirection.getOrientation( 0 ) )
        if( theModule.isInstanceOf[ModTrack] ) {
          t.configs( 0 ).meta = theDirection % 4
          updateClientSide( ForgeDirection.DOWN )
        }
        LuaValue.NIL
      }
    }
  }

  class moveItem extends ThreeArgFunction {
    override def call( source: LuaValue, destination: LuaValue, amount: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theSource = source.checkint
        val theDestination = destination.checkint
        val theAmount = amount.checkint

        if( theSource < 3 && theSource >= 0 && theDestination < 3 && theDestination >= 0 && theSource != theDestination && theAmount > 0 ) {
          val sourceStack = t.getStackInInventorySlot( theSource )
          if( sourceStack != null ) {
            val sourceCopy = sourceStack.copy

            val maxAmount = Math.min( sourceCopy.stackSize, theAmount )
            sourceCopy.stackSize = maxAmount
            val amountAdded = t.addItemInternal( sourceCopy, true, theDestination )

            t.extractItemInternal( true, theSource, amountAdded )
          }
        }
        LuaValue.NIL
      }
    }
  }

  class extractWithRummager extends ThreeArgFunction {
    override def call( itemName: LuaValue, destination: LuaValue, amount: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theItemName = itemName.checkjstring
        val theDestination = destination.checkint
        val theAmount = amount.checkint

        if( theAmount > 0 && theAmount < 64 && theDestination >= 0 && theDestination < 3 ) {
          val side = t.getRummagerSide
          if( side != ForgeDirection.UNKNOWN ) {
            val m = t.getSide( side )
            m.asInstanceOf[ModRummager].extractItemToInventory( t, t.getConfigForSide( side ), side, theItemName, theAmount, theDestination )
          }
        }

        LuaValue.NIL
      }
    }
  }

  class insertWithRummager extends TwoArgFunction {
    override def call( source: LuaValue, amount: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theSource = source.checkint
        val theAmount = amount.checkint

        if( theAmount > 0 && theAmount < 64 && theSource >= 0 && theSource < 3 ) {
          val side = t.getRummagerSide
          if( side != ForgeDirection.UNKNOWN ) {
            val m = t.getSide( side )
            m.asInstanceOf[ModRummager].pushItemFromInventory( t, t.getConfigForSide( side ), side, theSource, theAmount )
          }
        }

        LuaValue.NIL
      }
    }
  }

  class moveFluid extends ThreeArgFunction {
    override def call( source: LuaValue, destination: LuaValue, amount: LuaValue ): LuaValue = {
      Run { ( t ) =>
        val theSource = source.checkint
        val theDestination = destination.checkint
        val theAmount = amount.checkint

        if( theSource < 3 && theSource >= 0 && theDestination < 3 && theDestination >= 0 && theSource != theDestination && theAmount > 0 ) {
          val sourceStack = t.getFluidInTank( theSource )
          if( sourceStack != null ) {
            val sourceCopy = sourceStack.copy

            val maxAmount = Math.min( sourceCopy.amount, theAmount )
            sourceCopy.amount = maxAmount
            val amountAdded = t.fillInternal( theDestination, sourceCopy, true )

            t.drainInternal( theSource, amountAdded, true )
          }
        }
        LuaValue.NIL
      }
    }
  }



  object SidedRun {
    def apply( side: LuaValue )( impl: ( TileSocket, ForgeDirection, SideConfig, SocketModule ) => LuaValue )
             ( implicit te: Option[TileSocket] ): LuaValue = {
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
