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

public class ModFluidInput extends SocketModule {

	public ModFluidInput( int id ) {
		super( id, "eng_toolbox:fluidInput" );
	}

	@Override
	public String getLocalizedName() {
		return "Fluid Input";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Accepts fluid from adjacent pipes" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to input to" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "h", "b", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'h' ), Blocks.iron_bars,
				Character.valueOf( 'u' ), Items.bucket, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
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
			if( config.rsLatch[i] && ts.getRSControl( i ) ) canIntake = false;
		}
		
		if( canIntake ) {
			if( config.tank != -1 ) return ts.fillInternal( config.tank, fluid, doFill );
		}
		
		return 0;
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
