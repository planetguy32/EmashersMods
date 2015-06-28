package emasher.modules

import java.util._

import cpw.mods.fml.relauncher.{Side, SideOnly}
import emasher.api._
import net.minecraft.item._
import net.minecraft.item.crafting._
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._
import net.minecraftforge.oredict._

class ModPlasmaReactor( id: Int ) extends SocketModule( id, "eng_toolbox:plasmaReactor", "eng_toolbox:plasmaReactorActive" ) {
  override def getLocalizedName = "Plasma Reactor"

  override def getToolTip( l: List[ Object ] ) {
    l.add( "Generates power by combining plasma" )
    l.add( "with propellent and hydrogen" )
    l.add( "The reactor will not shut off" )
    l.add( "when the socket's capacitor is full" )
  }

  override def getIndicatorKey( l: List[ Object ] ) {
    l.add( emasher.util.Config.PREF_AQUA + "Outputs 256 RF/t" )
  }

  override def getCurrentTexture( config: SideConfig ):
  Int = {
    if( config.meta != 0 ) 1
    else 0
  }

  override def addRecipe {
    CraftingManager.getInstance( ).getRecipeList( ).asInstanceOf[ List[ Object ] ].add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module, 1, moduleID ),
      "scs", "rpr", "sbs", Character.valueOf( 's' ), emasher.items.Items.psu, Character.valueOf( 's' ), "blockSteel",
      Character.valueOf( 'c' ), emasher.items.Items.circuit, Character.valueOf( 'r' ), emasher.items.Items.rsIngot, Character.valueOf( 'b' ), emasher.items.Items.blankSide ) )
  }

  override def isMachine = true

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ):
  Boolean = {
    for( i <- 0 to 5 ) {
      val m = ts.getSide( ForgeDirection.getOrientation( i ) )
      if( m != null && m.isMachine ) {
        return false
      }
    }

    true
  }

  override def updateSide( config: SideConfig, ts: SocketTileAccess, side: ForgeDirection ) {
    var plasmaTank = -1
    var propellentTank = -1
    var hydrogenTank = -1

    var i = 0

    for( i <- 0 to 2 ) {
      val fs: FluidStack = ts.getFluidInTank( i )
      if( fs != null && fs.amount > 10 ) {
        if( fs.isFluidEqual( new FluidStack( emasher.fluids.Fluids.fluidPlasma, 4000 ) ) && plasmaTank == -1 ) plasmaTank = i
        if( fs.isFluidEqual( new FluidStack( emasher.fluids.Fluids.fluidPropellent, 4000 ) ) && propellentTank == -1 ) propellentTank = i
        if( fs.isFluidEqual( new FluidStack( emasher.fluids.Fluids.fluidHydrogen, 4000 ) ) && hydrogenTank == -1 ) hydrogenTank = i
      }
    }

    if( plasmaTank != -1 && propellentTank != -1 && hydrogenTank != -1 ) {
      for( i <- 0 to 2 ) {
        ts.drainInternal( i, 10, true )
      }

      ts.addEnergy( 256, false )

      if( config.meta == 0 ) {
        config.meta = 1
        ts.sendClientSideState( side.ordinal( ) )
      }

    }
    else {
      if( config.meta != 0 ) {
        config.meta = 0
        ts.sendClientSideState( side.ordinal( ) )
      }
    }
  }

  @SideOnly( Side.CLIENT )
  override def getInternalTexture( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): String = {
    if( config.meta != 0 ) "eng_toolbox:inner_plasma"
    else "eng_toolbox:inner_black"
  }

  @SideOnly( Side.CLIENT )
  override def getAllInternalTextures: Array[ String ] = {
    return Array[ String ]( "eng_toolbox:inner_plasma" )
  }
}