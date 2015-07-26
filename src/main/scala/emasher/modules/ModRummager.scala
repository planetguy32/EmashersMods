package emasher.modules

import emasher.api.{SocketTileAccess, SideConfig, SocketModule}
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict.ShapedOreRecipe

class ModRummager( id: Int ) extends SocketModule( id, "eng_toolbox:rummager", "eng_toolbox:rummager-magnet" ) {

  override def getLocalizedName = "Rummager"

  override def getToolTip( l: java.util.List[ Object ] ): Unit = {
    l.add( "Allows other modules to access external inventories." )
  }

  override def getCurrentTexture( config: SideConfig ): Int = config.meta

  override def addRecipe( ): Unit = {
    CraftingManager.getInstance( ).getRecipeList.asInstanceOf[ java.util.List[ Object ] ]
      .add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module, 1, moduleID ), "c", "m",
      Character.valueOf( 'c' ), emasher.items.Items.engWrench,
      Character.valueOf( 'm' ), new ItemStack( emasher.items.Items.module, 1, 86 ) ) )
  }

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ): Boolean = {
    ForgeDirection.VALID_DIRECTIONS.foldLeft( true ) { ( acc: Boolean, it: ForgeDirection ) =>
      ( ! ts.getSide( it ).isInstanceOf[ModRummager] ) && acc
    }
  }

  override def onGenericRemoteSignal( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): Unit = {
    config.meta match {
      case 1 => config.meta = 0
      case _ => config.meta = 1
    }
  }

  def extractItemToInventory( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, nameFilter: String,
                              amount: Int, inventory: Int ): Unit = {
    val is = ts.pullItem( side, false, amount, nameFilter )
    if( is != null ) {
      val amountAdded = ts.addItemInternal( is, true, inventory )
      ts.pullItem( side, true, amountAdded, nameFilter )
    }
  }

  def pushItemFromInventory( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection,
                             inventory: Int, amount: Int ): Unit = {
    val is = ts.getStackInInventorySlot( inventory )
    if( is != null ) {
      val amountAdded = ts.tryInsertItem( is, side, amount )
      is.stackSize -= amountAdded
      if( is.stackSize <= 0 ) {
        ts.setInventoryStack( inventory, null )
      }
    }
  }
}
