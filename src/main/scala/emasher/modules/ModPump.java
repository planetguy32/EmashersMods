package emasher.modules;

import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.util.Tuple;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class ModPump extends SocketModule {

	public ModPump( int id ) {
		super( id, "eng_toolbox:pump" );
	}

	@Override
	public String getLocalizedName() {
		return "Enderic Fluid Pump";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Pumps fluid blocks below it" );
		l.add( "within a radius set by" );
		l.add( "a Range Selector Module" );
		l.add( "and stores it in an internal" );
		l.add( "tank" );
		l.add( "Note that this module may" );
		l.add( "still be buggy!" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Input Tank" );
		l.add( emasher.util.Config.PREF_AQUA() + "Uses 10 RF/t" );
	}
	
	@Override
	public boolean hasTankIndicator() {
		return true;
	}
	
	@Override
	public boolean hasInventoryIndicator() {
		return true;
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ),
				"uuu",
				"cbc",
				"ded",
				Character.valueOf( 'u' ), Items.bucket,
				Character.valueOf( 'c' ), "ingotCopper",
				Character.valueOf( 'd' ), Items.diamond,
				Character.valueOf( 'e' ), Items.ender_pearl,
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}
	
	@Override
	public boolean canBeInstalled( SocketTileAccess ts, ForgeDirection side ) {
		if( side == ForgeDirection.DOWN ) return true;
		return false;
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		//Make sure there is enough energy to pump and a tank to put it in
		if( config.meta == 0 ) {
			config.meta = 8;
			if( config.tank >= 0 && config.tank <= 2 && ts.getEnergyStored() >= 10 && !config.tags.hasKey( "pump_complete" ) ) {
				if( !config.tags.hasKey( "pump_valid" ) ) {
					init( ts, config, side );
				} else {
					int iteration = config.tags.getInteger( "iteration" );
					if( config.tags.hasKey( "x" + iteration ) ) {
						int x = config.tags.getInteger( "x" + iteration );
						int z = config.tags.getInteger( "z" + iteration );
						int y = config.tags.getInteger( "currY" );
						
						if( drainCurrentBlock( config, ts, side, x, y, z ) ) {
							config.tags.removeTag( "iteration" );
							config.tags.removeTag( "x" + iteration );
							config.tags.removeTag( "z" + iteration );
							
							config.tags.setInteger( "iteration", iteration + 1 );
							
							ts.useEnergy( 10, false );
						}
						
						
					} else {
						onLower( config, ts, side );
					}
					
					
				}
			}
			
		} else {
			config.meta--;
		}
	}
	
	
	@Override
	public void init( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		config.meta = 0;
		config.tags.removeTag( "currY" );
		config.tags.setInteger( "currY", ts.yCoord - 1 );
		config.tags.removeTag( "pump_complete" );
		config.tags.setBoolean( "pump_valid", true );
		config.tags.removeTag( "iteration" );
		generateLayer( config, ts, side );
		config.tags.setInteger( "iteration", 0 );
	}
	
	@Override
	public void onSocketPlaced( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		init( ts, config, side );
	}
	
	public void onLower( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		int y = config.tags.getInteger( "currY" );
		config.tags.removeTag( "currY" );
		config.tags.setInteger( "currY", y - 1 );
		config.tags.removeTag( "iteration" );
		generateLayer( config, ts, side );
		config.tags.setInteger( "iteration", 0 );
	}
	
	public void generateLayer( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		int radius = getRadius( ts );
		int y = config.tags.getInteger( "currY" );
		Fluid filterFluid = getFilterFluid( ts, config );
		int filterID = -1;
		if( filterFluid != null ) filterID = filterFluid.getID();
		
		ArrayList<Tuple> allQuards = new ArrayList<Tuple>();
		
		for( int i = ts.xCoord - radius; i <= ts.xCoord + radius; i++ ) {
			for( int j = ts.zCoord - radius; j <= ts.zCoord + radius; j++ ) {
				if( isFluidBlock( config, ts, side, i, y, j ) ) {
					if( filterFluid == null || ( getBlockFluid( ts, i, y, j ) != null && ( filterID == -1 || filterID == getBlockFluid( ts, i, y, j ).getID() ) ) )
						allQuards.add( new Tuple( i, y, j ) );
				}
			}
		}
		
		if( allQuards.size() == 0 || y == 0 ) {
			config.tags.setBoolean( "pump_complete", true );
			return;
		}
		
		int i = 0;
		
		while( allQuards.size() > 0 ) {
			int rnd = ts.getWorldObj().rand.nextInt( allQuards.size() );
			Tuple quards = allQuards.remove( rnd );
			
			config.tags.setInteger( "x" + i, quards.x() );
			config.tags.setInteger( "z" + i, quards.z() );
			
			i++;
		}
	}
	
	//Returns true iff the pump should move on
	public boolean drainCurrentBlock( SideConfig config, SocketTileAccess ts, ForgeDirection side, int x, int y, int z ) {
		//int bID = ts.getWorldObj().getBlockId(x, y, z);
		Block b = ts.getWorldObj().getBlock( x, y, z );
		Fluid filterFluid = getFilterFluid( ts, config );
		int filterID = -1;
		if( filterFluid != null ) filterID = filterFluid.getID();
		
		
		if( b == Blocks.water ) {
			FluidStack fs = new FluidStack( FluidRegistry.WATER, 1000 );
			
			if( fs != null && ts.fillInternal( config.tank, fs, false ) == fs.amount && ( filterID == -1 || filterID == fs.getFluidID() ) ) {
				ts.fillInternal( config.tank, fs, true );
				ts.getWorldObj().setBlock( x, y, z, Blocks.air );
				return true;
			}
			return false;
		} else if( b == Blocks.lava ) {
			FluidStack fs = new FluidStack( FluidRegistry.LAVA, 1000 );
			
			if( fs != null && ts.fillInternal( config.tank, fs, false ) == fs.amount && ( filterID == -1 || filterID == fs.getFluidID() ) ) {
				ts.fillInternal( config.tank, fs, true );
				ts.getWorldObj().setBlock( x, y, z, Blocks.stone );
				return true;
			}
			return false;
		} else if( b != null && b instanceof IFluidBlock ) {
			IFluidBlock fb = ( IFluidBlock ) b;
			FluidStack fs = fb.drain( ts.getWorldObj(), x, y, z, false );
			if( fs != null && ts.fillInternal( config.tank, fs, false ) == fs.amount && ( filterID == -1 || filterID == fs.getFluidID() ) ) {
				ts.fillInternal( config.tank, fb.drain( ts.getWorldObj(), x, y, z, true ), true );
				ts.getWorldObj().setBlock( x, y, z, Blocks.air );
				ts.getWorldObj().removeTileEntity( x, y, z );
				return true;
			}
		}
		
		Fluid f = null;
		if( b != null && b instanceof IFluidBlock ) f = ( ( IFluidBlock ) b ).getFluid();
		
		if( b == null || ( b != null && !( b instanceof IFluidBlock ) ) || ( f != null && f.getID() != filterID ) )
			return true;
		
		return false;
	}
	
	public boolean isFluidBlock( SideConfig config, SocketTileAccess ts, ForgeDirection side, int x, int y, int z ) {
		//int bID = ts.getWorldObj().getBlockId(x, y, z);
		Block b = ts.getWorldObj().getBlock( x, y, z );

		if( b == Blocks.water || b == Blocks.lava ) return true;
		
		if( b != null && b instanceof IFluidBlock ) {
			return true;
		}
		
		return false;
	}
	
	public Fluid getBlockFluid( SocketTileAccess ts, int x, int y, int z ) {
		//int bID =
		Block b = ts.getWorldObj().getBlock( x, y, z );

		if( b == Blocks.water ) return FluidRegistry.WATER;
		else if( b == Blocks.lava ) return FluidRegistry.LAVA;

		if( b != null && b instanceof IFluidBlock ) {
			return ( ( IFluidBlock ) b ).getFluid();
		}
		
		return null;
	}
	
	public Fluid getFilterFluid( SocketTileAccess ts, SideConfig config ) {
		if( config.inventory >= 0 && config.inventory <= 2 ) {
			ItemStack is = ts.getStackInInventorySlot( config.inventory );
			if( is == null ) return null;
			if( is.getItem() == Items.water_bucket ) return FluidRegistry.WATER;
			else if( is.getItem() == Items.lava_bucket ) return FluidRegistry.LAVA;
			else {
				if( is.getItem() instanceof IFluidContainerItem ) {
					return ( ( IFluidContainerItem ) is.getItem() ).getFluid( is ).getFluid();
				}
			}
		}
		
		return null;
	}
	
	public int getRadius( SocketTileAccess ts ) {
		for( int i = 0; i < 6; i++ ) {
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( i ) );
			if( m != null && m instanceof ModRangeSelector ) {
				return ts.getConfigForSide( ForgeDirection.getOrientation( i ) ).meta;
			}
		}
		
		return 7;
	}
	

}
