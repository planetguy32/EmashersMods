package emasher.microcontrollers

import emasher.api.SideConfig
import emasher.tileentities.TileSocket

object BlueGreenIndicatorState extends Enumeration {
  type BlueGreenIndicatorState = Value
  val ZERO, ONE, TWO, INVALID = Value
}
import BlueGreenIndicatorState._

case class RedPurpleIndicatorState(
  one: Boolean,
  two: Boolean,
  three: Boolean
) {

}

case class SideState(
  sideId: Int,
  sideMeta: Int,
  blueIndicator: BlueGreenIndicatorState,
  greenState: BlueGreenIndicatorState,
  redState: RedPurpleIndicatorState,
  purpleState: RedPurpleIndicatorState
) {

}

case class SocketState( sides: List[SideState] ) {

}

object SocketState {
  def generateFromTileEntity( te: TileSocket ): SocketState = {
    SocketState( te.configs.toList.zip( te.sides.toList ).map {
      case ( config: SideConfig, side: Int ) =>
        generateSideFromConfig( config, side )
    } )
  }

  private def generateSideFromConfig( config: SideConfig, side: Int ): SideState = {
    SideState( side, config.meta, intToBlueGreenState( config.tank ), intToBlueGreenState( config.inventory ),
      boolArrayToRedPurpleState( config.rsControl ), boolArrayToRedPurpleState( config.rsLatch ) )
  }

  private def intToBlueGreenState( state: Int ): BlueGreenIndicatorState = state match {
    case 0 => ZERO
    case 1 => ONE
    case 2 => TWO
    case _ => INVALID
  }

  private def boolArrayToRedPurpleState( state: Array[Boolean] ): RedPurpleIndicatorState = {
    RedPurpleIndicatorState( state( 0 ), state( 1 ), state( 2 ) )
  }
}


