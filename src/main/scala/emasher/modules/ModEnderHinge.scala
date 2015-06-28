package emasher.modules

import emasher.api.{SideConfig, SocketModule, SocketTileAccess, Util}
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict.ShapedOreRecipe

class ModEnderHinge( id: Int ) extends SocketModule( id, "eng_toolbox:enderHinge" ) {

  override def getLocalizedName: String = "Ender Hinge"

  override def addRecipe( ): Unit = {
    CraftingManager.getInstance( ).getRecipeList.asInstanceOf[ java.util.List[ Object ] ]
      .add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module, 2, moduleID ), "mpm",
      Character.valueOf( 'p' ), Items.ender_pearl,
      Character.valueOf( 'm' ), new ItemStack( emasher.items.Items.module, 1, 40 ) ) )
  }

  override def getToolTip( l: java.util.List[ Object ] ): Unit = {
    l.add( "Swaps the blocks adjacent to each of" )
    l.add( "the two installed Ender Hinge modules on" )
    l.add( "a redstone pulse" )
  }

  override def getIndicatorKey( l: java.util.List[ Object ] ): Unit = {
    l.add( emasher.util.Config.PREF_RED + "Activate" )
  }

  override def hasRSIndicator: Boolean = true

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ): Boolean = {
    var times = 0
    var other = ForgeDirection.UNKNOWN
    for( i <- 0 to 5 ) {
      val d = ForgeDirection.getOrientation( i )
      val m = ts.getSide( d )
      if( m.isInstanceOf[ ModHinge ] ) {
        times += 1
        other = d
      }
      if( times >= 2 ) return false
    }

    true
  }

  override def onRSInterfaceChange( config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean ): Unit = {
    if( !on ) return
    if( !config.rsControl( index ) ) return
    val otherHinge = getOtherHinge( side, ts )
    if( otherHinge == ForgeDirection.UNKNOWN ) return
    if( otherHinge.ordinal( ) < side.ordinal( ) ) return

    val x = ts.xCoord + side.offsetX
    val y = ts.yCoord + side.offsetY
    val z = ts.zCoord + side.offsetZ

    val nx = ts.xCoord + otherHinge.offsetX
    val ny = ts.yCoord + otherHinge.offsetY
    val nz = ts.zCoord + otherHinge.offsetZ

    if( Util.swapBlocks( ts.getWorldObj( ), x, y, z, nx, ny, nz ) ) {
      ts.getWorldObj( ).playSoundEffect( ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.out", 0.5F, ts.getWorldObj( ).rand.nextFloat( ) * 0.25F + 0.6F )
      ts.getWorldObj( ).playSoundEffect( ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.in", 0.5F, ts.getWorldObj( ).rand.nextFloat( ) * 0.25F + 0.6F )
    }
  }

  def getOtherHinge( side: ForgeDirection, ts: SocketTileAccess ): ForgeDirection = {
    for( i <- 0 to 5 ) {
      val d = ForgeDirection.getOrientation( i )
      if( d != side && ts.getSide( d ).moduleID == moduleID ) return d
    }

    ForgeDirection.UNKNOWN
  }
}
