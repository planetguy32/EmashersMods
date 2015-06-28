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

public class ModFluidOutput extends SocketModule {

	public ModFluidOutput( int id ) {
		super( id, "eng_toolbox:fluidOutput" );
	}

	@Override
	public String getLocalizedName() {
		return "Fluid Output";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs fluid from its configured" );
		l.add( "tank to adjacent pipes/tanks/etc." );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to output from" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "d", "u", "b", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'd' ), Blocks.iron_bars,
				Character.valueOf( 'u' ), Blocks.dropper, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( config.tank < 0 || config.tank > 2 ) return;
		
		boolean allOff = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					ts.tryInsertFluid( config.tank, side );
					return;
				}
				allOff = false;
			}
			
			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					ts.tryInsertFluid( config.tank, side );
					return;
				}
				allOff = false;
			}
		}
		
		if( allOff ) ts.tryInsertFluid( config.tank, side );
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
	public boolean canExtractFluid() {
		return true;
	}
	
	/*@Override
	public ILiquidTank getAssociatedTank(SideConfig config, SocketTileAccess ts)
	{
		if(config.tank == -1) return null;
		return ts.tanks[config.tank];
	}*/
	
	@Override
	public FluidStack fluidExtract( int amount, boolean doExtract, SideConfig config, SocketTileAccess ts ) {
		if( config.tank != -1 ) return ts.drainInternal( config.tank, amount, doExtract );
		return null;
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
