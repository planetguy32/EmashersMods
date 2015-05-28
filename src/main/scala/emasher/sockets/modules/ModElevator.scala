package emasher.sockets.modules

import emasher.api.{SideConfig, SocketModule, SocketTileAccess}
import emasher.sockets.pipes.TileDirectionChanger
import emasher.sockets.{Coords, SocketsMod, UtilScala}
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict.ShapedOreRecipe

class ModElevator( id: Int ) extends SocketModule( id, "sockets:elevatorUp", "sockets:elevatorDown" ) {

  override def getLocalizedName: String = "Elevator"

  override def addRecipe( ): Unit = {
    CraftingManager.getInstance( ).getRecipeList.asInstanceOf[ java.util.List[ Object ] ]
      .add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 2, moduleID ), "gmg",
      Character.valueOf( 'g' ), Items.ghast_tear,
      Character.valueOf( 'm' ), new ItemStack( SocketsMod.blankSide ) ) )
  }

  override def getCurrentTexture( config: SideConfig ): Int = {
    if( config.rsLatch( 0 ) ) {
      1
    } else {
      0
    }
  }

  override def getToolTip( l: java.util.List[ Object ] ): Unit = {
    l.add( "Makes the socket move up or down when activated" )
    l.add( "on an internal redstone pulse" )
    l.add( "Also allows a track to move a socket through the air" )
  }

  override def getIndicatorKey( l: java.util.List[ Object ] ): Unit = {
    l.add( SocketsMod.PREF_RED + "Activate" )
    l.add( SocketsMod.PREF_WHITE + "Change Mode" )
  }

  override def hasRSIndicator: Boolean = true

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ): Boolean = {
    side != ForgeDirection.UP && side != ForgeDirection.DOWN
  }

  override def onGenericRemoteSignal( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): Unit = {
    config.rsLatch( 0 ) = !config.rsLatch( 0 )
    ts.sendClientSideState( side.ordinal( ) )
  }

  override def onRSInterfaceChange( config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean ): Unit = {
    if( !on ) return
    if( !config.rsControl( index ) ) return

    for( i <- 0 to 5 ) {
      val d = ForgeDirection.getOrientation( i )
      val m = ts.getSide( d )
      val c = ts.getConfigForSide( d )
      if( m.isInstanceOf[ ModAccelerometer ] && c.rsControl( index ) ) return
    }

    val dir = config.rsLatch( 0 ) match {
      case true => ForgeDirection.DOWN
      case false => ForgeDirection.UP
    }

    val world = ts.getWorldObj
    val x = ts.xCoord
    val y = ts.yCoord
    val z = ts.zCoord

    val done = UtilScala.moveGroup( world, Coords( x, y, z ), dir )

    if( done ) {
      ts.dead = true

      world.playSoundEffect( ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat( ) * 0.25F + 0.6F )
      world.playSoundEffect( ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.in", 0.5F, world.rand.nextFloat( ) * 0.25F + 0.6F )
    }

  }

  override def onAdjChange( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ) {
    onSocketPlaced( config, ts, side )
  }

  override def onSocketPlaced( config: SideConfig, ts: SocketTileAccess, side: ForgeDirection ): Unit = {
    for( i <- 2 to 5 ) {
      val d = ForgeDirection.getOrientation( i )
      val xo = ts.xCoord + d.offsetX
      val zo = ts.zCoord + d.offsetZ

      val t = ts.getWorldObj( ).getTileEntity( xo, ts.yCoord, zo )
      if( t != null && t.isInstanceOf[ TileDirectionChanger ] ) {
        val td = t.asInstanceOf[ TileDirectionChanger ]
        td.directions( d.getOpposite.ordinal( ) ) match {
          case ForgeDirection.UP => config.rsLatch( 0 ) = false
          case ForgeDirection.DOWN => config.rsLatch( 0 ) = true
          case _ =>
        }
      }
    }
  }
}
