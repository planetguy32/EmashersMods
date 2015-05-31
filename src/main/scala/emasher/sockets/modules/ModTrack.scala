package emasher.sockets.modules

import emasher.api.{SideConfig, SocketModule, SocketTileAccess}
import emasher.core.EmasherCore
import emasher.sockets.pipes.TileDirectionChanger
import emasher.sockets.{Coords, SocketsMod, UtilScala}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict.ShapedOreRecipe

class ModTrack( id: Int ) extends SocketModule( id, "sockets:trackUp", "sockets:trackLeft", "sockets:trackDown", "sockets:trackRight" ) {

  override def getLocalizedName: String = "Track"

  override def addRecipe( ): Unit = {
    CraftingManager.getInstance( ).getRecipeList.asInstanceOf[ java.util.List[ Object ] ]
      .add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 2, moduleID ), "pmp", "iii",
      Character.valueOf( 'i' ), new ItemStack( EmasherCore.gem, 1, 0 ),
      Character.valueOf( 'p' ), Blocks.piston,
      Character.valueOf( 'm' ), new ItemStack( SocketsMod.blankSide ) ) )
  }

  override def getCurrentTexture( config: SideConfig ): Int = {
    config.meta
  }

  override def getToolTip( l: java.util.List[ Object ] ): Unit = {
    l.add( "Moves a socket horizontally in the set direction" )
    l.add( "on an internal redstone pulse" )
  }

  override def getIndicatorKey( l: java.util.List[ Object ] ): Unit = {
    l.add( SocketsMod.PREF_RED + "Activate" )
    l.add( SocketsMod.PREF_WHITE + "Change Direction" )
  }

  override def hasRSIndicator: Boolean = true

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ): Boolean = {
    side == ForgeDirection.DOWN
  }

  override def onGenericRemoteSignal( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): Unit = {
    config.meta += 1
    if( config.meta == 4 ) config.meta = 0
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

    val nx = config.meta match {
      case 0 => ts.xCoord
      case 1 => ts.xCoord - 1
      case 2 => ts.xCoord
      case 3 => ts.xCoord + 1
    }

    val nz = config.meta match {
      case 0 => ts.zCoord - 1
      case 1 => ts.zCoord
      case 2 => ts.zCoord + 1
      case 3 => ts.zCoord
    }

    val dir = config.meta match {
      case 0 => ForgeDirection.NORTH
      case 1 => ForgeDirection.WEST
      case 2 => ForgeDirection.SOUTH
      case 3 => ForgeDirection.EAST
    }

    //val dir = ForgeDirection.getOrientation(config.meta + 2)

    //val bId =
    val b = ts.getWorldObj( ).getBlock( nx, ts.yCoord - 1, nz )
    val canMove = hasElevator( ts ) || ( b != null && b.isOpaqueCube )

    if( canMove ) {
      val world = ts.getWorldObj( )
      val x = ts.xCoord
      val y = ts.yCoord
      val z = ts.zCoord
      val done = UtilScala.moveGroup( world, Coords( x, y, z ), dir )


      if( done ) {
        ts.dead = true

        ts.getWorldObj( ).playSoundEffect( ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.out", 0.5F, ts.getWorldObj( ).rand.nextFloat( ) * 0.25F + 0.6F )
        ts.getWorldObj( ).playSoundEffect( ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.in", 0.5F, ts.getWorldObj( ).rand.nextFloat( ) * 0.25F + 0.6F )
      }
    }

  }

  def hasElevator( ts: SocketTileAccess ): Boolean = {
    for( i <- 0 to 5 ) {
      val d = ForgeDirection.getOrientation( i )
      if( ts.getSide( d ).isInstanceOf[ ModElevator ] ) return true
    }
    false
  }

  override def onAdjChange( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ) {
    onSocketPlaced( config, ts, side )
  }

  override def onSocketPlaced( config: SideConfig, ts: SocketTileAccess, side: ForgeDirection ): Unit = {
    val t = ts.getWorldObj( ).getTileEntity( ts.xCoord, ts.yCoord - 1, ts.zCoord )
    if( t != null && t.isInstanceOf[ TileDirectionChanger ] ) {
      val td = t.asInstanceOf[ TileDirectionChanger ]
      td.directions( ForgeDirection.UP.ordinal( ) ) match {
        case ForgeDirection.NORTH => config.meta = 0
        case ForgeDirection.SOUTH => config.meta = 2
        case ForgeDirection.EAST => config.meta = 3
        case ForgeDirection.WEST => config.meta = 1
        case _ =>
      }
    }
  }
}
