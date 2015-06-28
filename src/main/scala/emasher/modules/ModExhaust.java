package emasher.modules;

import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.blocks.BlockGasGeneric;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModExhaust extends SocketModule {
	public ModExhaust( int id ) {
		super( id, "eng_toolbox:gasExhaust" );
	}

	@Override
	public String getLocalizedName() {
		return "Exhaust";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Releases gas from an internal tank" );
		l.add( "when there are 4000 mB in the tank" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to output from" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ),
				"i", "b", Character.valueOf( 'c' ), emasher.items.Items.circuit(), Character.valueOf( 'i' ), Blocks.iron_bars,
				Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 5 ) ) );
	}
	
	@Override
	public boolean hasTankIndicator() {
		return true;
	}
	
	@Override
	public boolean hasRSIndicator() {
		return true;
	}
	
	@Override
	public boolean hasLatchIndicator() {
		return true;
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		tryOutputGas( config, ts, side );
	}
	
	@Override
	public void onRSLatchChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		tryOutputGas( config, ts, side );
	}
	
	@Override
	public void onAdjChangeSide( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		tryOutputGas( config, ts, side );
	}
	
	@Override
	public void onTankChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add ) {
		tryOutputGas( config, ts, side );
	}
	
	private void tryOutputGas( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		boolean canOutput = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] && ts.getRSControl( i ) ) canOutput = false;
			if( config.rsLatch[i] && ts.getRSLatch( i ) ) canOutput = false;
		}
		
		if( canOutput && config.tank >= 0 && config.tank < 3 ) {
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			
			if( ts.getWorldObj().isAirBlock( xo, yo, zo ) ) {
				FluidStack f = ts.getFluidInTank( config.tank );
				if( f != null && f.amount >= 4000 ) {
					Fluid fl = f.getFluid();
					Block b = fl.getBlock();
					if( b != null && b instanceof BlockGasGeneric ) {
						ts.getWorldObj().setBlock( xo, yo, zo, b );
						ts.drainInternal( config.tank, 4000, true );
					}
				}
			}
		}
	}
}
