package emasher.modules

import java.util._

import cpw.mods.fml.relauncher.{Side, SideOnly}
import emasher.api.{SideConfig, SocketModule, SocketTileAccess}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item._
import net.minecraft.item.crafting._
import net.minecraft.util._
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict._;

class ModRangeSelector( id: Int ) extends SocketModule( id, "eng_toolbox:rangeSelector" ) {
  override def getLocalizedName = "Range Selector";

  @SideOnly( Side.CLIENT )
  override def getAdditionalOverlays( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection )
  : Array[ IIcon ] = {
    Array( emasher.blocks.Blocks.socket.bar1( config.meta ) );
  }

  override def addRecipe {
    CraftingManager.getInstance( ).getRecipeList( ).asInstanceOf[ List[ Object ] ].add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module, 1, moduleID ), "ggg", "sls", " b ", Character.valueOf( 'g' ), Blocks.glass_pane, Character.valueOf( 's' ), Items.glowstone_dust,
      Character.valueOf( 'l' ), "dyeLime", Character.valueOf( 'b' ), emasher.items.Items.blankSide ) );
  }

  override def onGenericRemoteSignal( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ) {
    config.meta += 1;
    if( config.meta >= 8 ) config.meta = 0;
    ts.sendClientSideState( side.ordinal );
  }


  override def getToolTip( l: List[ Object ] ) {
    l.add( "Allows for range selection" );
    l.add( "for certain machines" );
  }

  override def getIndicatorKey( l: List[ Object ] ) {
    l.add( emasher.util.Config.PREF_WHITE + "Change range" );
  }
}