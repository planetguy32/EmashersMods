package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ModFluidExtractor extends SocketModule {

	public ModFluidExtractor( int id ) {
		super( id, "eng_toolbox:fluidExtractor" );
	}

	@Override
	public String getLocalizedName() {
		return "Fluid Extractor";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Pulls fluid from adjacent" );
		l.add( "tanks/machines/etc." );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to input to" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "h", "u", "b", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'h' ), Blocks.hopper,
				Character.valueOf( 'u' ), Blocks.iron_bars, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
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
	public boolean isFluidInterface() {
		return true;
	}
	
	@Override
	public boolean canInsertFluid() {
		return true;
	}
	
	@Override
	public int fluidFill( FluidStack fluid, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( config.tank != -1 ) return ts.fillInternal( config.tank, fluid, doFill );
		return 0;
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( config.tank < 0 || config.tank > 2 ) return;
		
		boolean allOff = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					ts.tryExtractFluid( config.tank, side, 100 );
					return;
				}
				allOff = false;
			}
			
			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					ts.tryExtractFluid( config.tank, side, 100 );
					return;
				}
				allOff = false;
			}
		}
		
		if( allOff ) ts.tryExtractFluid( config.tank, side, 100 );
	}

	@Override
	public void onTankChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add ) {
		if( index == config.tank ) {
			ts.sendClientTankSlot( index );
		}
	}

	@Override
	@SideOnly( Side.CLIENT )
	public int getTankToRender( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		return config.tank;
	}
}
