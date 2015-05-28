package emasher.sockets.modules

import java.util._

import emasher.api.{SideConfig, SocketModule, SocketTileAccess}
import emasher.sockets._
import net.minecraft.item._
import net.minecraft.item.crafting._
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._
import net.minecraftforge.oredict._;

class ModPressurizer( id: Int ) extends SocketModule( id, "sockets:pressurizer" ) {
  override def getLocalizedName = "Pressurizer";

  override def getToolTip( l: List[ Object ] ) {
    l.add( "Increases tank capacity" );
    l.add( "to 32000 mB" );
  }

  override def addRecipe {
    CraftingManager.getInstance( ).getRecipeList( ).asInstanceOf[ List[ Object ] ].add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "cuc", "cuc", " b ", Character.valueOf( 'c' ), "ingotCopper", Character.valueOf( 'u' ), "blockCopper",
      Character.valueOf( 'b' ), SocketsMod.blankSide ) );
  }

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ):
  Boolean = {
    for( i <- 0 to 5 ) {
      if( ts.getSide( ForgeDirection.getOrientation( i ) ).isInstanceOf[ ModPressurizer ] ) return false;
    }

    true;
  }

  override def init( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ) {
    for( i <- 0 to 2 ) {
      var fs = ts.getFluidInTank( i );
      ts.asInstanceOf[ TileSocket ].tanks( i ) = new FluidTank( 32000 );
      ts.fillInternal( i, fs, true );
    }
  }

  override def onRemoved( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ) {
    for( i <- 0 to 2 ) {
      var fs = ts.getFluidInTank( i );
      if( fs != null ) fs.amount = Math.min( fs.amount, 8000 );
      ts.asInstanceOf[ TileSocket ].tanks( i ) = new FluidTank( 8000 );
      ts.fillInternal( i, fs, true );
    }
  }
}