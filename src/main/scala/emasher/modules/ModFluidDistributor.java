package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ModFluidDistributor extends SocketModule {

	public ModFluidDistributor( int id ) {
		super( id, "eng_toolbox:fluidDistributor" );
	}

	@Override
	public String getLocalizedName() {
		return "Fluid Distributor";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Accepts fluid from pipes and evenly" );
		l.add( "distributes it between internal tanks" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to exclude" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "t", "b", Character.valueOf( 't' ), Items.clock, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 4 ) );
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
		boolean canIntake = true;
		
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] && ts.getRSControl( i ) ) canIntake = false;
			if( config.rsLatch[i] && ts.getRSLatch( i ) ) canIntake = false;
		}
		
		if( canIntake ) {
			int taken = 0;
			FluidStack balancedFluid = fluid.copy();
			int returned = 0;
			
			do {
				while( config.inventory == -1 || config.inventory == 3 || config.inventory == config.tank )
					ts.nextInventory( side.ordinal() );
				
				FluidStack bf = balancedFluid.copy();
				bf.amount = Math.min( 300 - config.meta, balancedFluid.amount );
				
				returned = ts.fillInternal( config.inventory, bf, doFill );
				balancedFluid.amount -= returned;
				config.meta += returned;
				taken += returned;
				
				if( config.meta >= 300 ) {
					ts.nextInventory( side.ordinal() );
					config.meta = 0;
				}
				
			} while( returned != 0 && balancedFluid.amount != 0 );
			
			return taken;
			
		}
		
		return 0;
	}
}
