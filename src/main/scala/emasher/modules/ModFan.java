package emasher.modules;

import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.blocks.BlockGasGeneric;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModFan extends SocketModule {

	public ModFan( int id ) {
		super( id, "eng_toolbox:fan", "eng_toolbox:fanActive" );
	}

	@Override
	public String getLocalizedName() {
		return "Fan";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Sucks gas into an internal tank" );
		l.add( "within a 3 block radius" );
		l.add( "on a sustained internal RS signal" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to input into" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public int getCurrentTexture( SideConfig config ) {
		if( config.inventory < 1 ) return 0;
		else return 1;
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ipi", " b ", Character.valueOf( 'c' ), emasher.items.Items.circuit(), Character.valueOf( 'i' ), Items.iron_ingot,
				Character.valueOf( 'p' ), emasher.items.Items.psu(), Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 4 ) ) );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ipi", " b ", Character.valueOf( 'c' ), emasher.items.Items.circuit(), Character.valueOf( 'i' ), "ingotAluminum",
				Character.valueOf( 'p' ), emasher.items.Items.psu(), Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 4 ) ) );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ipi", " b ", Character.valueOf( 'c' ), emasher.items.Items.circuit(), Character.valueOf( 'i' ), "ingotTin",
				Character.valueOf( 'p' ), emasher.items.Items.psu(), Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 4 ) ) );
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
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( config.inventory == 1 && config.tank >= 0 && config.tank < 3 ) {
			config.meta++;
			if( config.meta == 20 ) {
				config.meta = 0;
				int x = ts.xCoord + side.offsetX;
				int y = ts.yCoord + side.offsetY;
				int z = ts.zCoord + side.offsetZ;
				
				int xMin = x;
				int yMin = y;
				int zMin = z;
				
				if( side.offsetX == 0 ) {
					xMin--;
					x++;
				}
				
				if( side.offsetY == 0 ) {
					yMin--;
					y++;
				}
				
				if( side.offsetZ == 0 ) {
					zMin--;
					z++;
				}
				
				for( int i = xMin; i < x + 1; i++ ) {
					for( int j = yMin; j < y + 1; j++ ) {
						for( int k = zMin; k < z + 1; k++ ) {
							Block b = ts.getWorldObj().getBlock( i, j, k );
							if( b != null && b instanceof BlockGasGeneric ) {
								BlockGasGeneric theBlock = ( BlockGasGeneric ) b;
								FluidStack avaliable = theBlock.drain( ts.getWorldObj(), i, j, k, false );
								if( ts.fillInternal( config.tank, avaliable, false ) == avaliable.amount ) {
									ts.fillInternal( config.tank, theBlock.drain( ts.getWorldObj(), i, j, k, true ), true );
								}
							}
						}
					}
				}
				
			}
		}
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( on && config.rsControl[index] ) {
			config.inventory = 1;
			
		} else if( !on && config.rsControl[index] ) {
			boolean turnOff = true;
			
			for( int i = 0; i < 3; i++ ) {
				if( config.rsControl[i] && ts.getRSControl( i ) ) turnOff = false;
				if( config.rsLatch[i] && ts.getRSLatch( i ) ) turnOff = false;
			}
			
			if( turnOff ) {
				config.inventory = 0;
				config.meta = 0;
			}
		}
		
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void onRSLatchChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( on && config.rsLatch[index] ) {
			config.inventory = 1;
			
		} else if( !on && config.rsLatch[index] ) {
			boolean turnOff = true;
			
			for( int i = 0; i < 3; i++ ) {
				if( config.rsControl[i] && ts.getRSControl( i ) ) turnOff = false;
				if( config.rsLatch[i] && ts.getRSLatch( i ) ) turnOff = false;
			}
			
			if( turnOff ) {
				config.inventory = 0;
				config.meta = 0;
			}
		}
		
		ts.sendClientSideState( side.ordinal() );
	}
	
}
