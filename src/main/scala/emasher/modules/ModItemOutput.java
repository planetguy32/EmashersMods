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

import java.util.List;

public class ModItemOutput extends SocketModule {

	public ModItemOutput( int id ) {
		super( id, "eng_toolbox:itemOutput" );
	}

	@Override
	public String getLocalizedName() {
		return "Item Output";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs items from its configured inventory" );
		l.add( "to adjacent pipes/inventories/etc." );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_GREEN() + "Inventory to output from" );
		l.add( emasher.util.Config.PREF_RED() + "RS control circuit" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS control latch" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "h", "d", "b", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'h' ), Blocks.trapdoor,
				Character.valueOf( 'd' ), Blocks.dropper, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( config.inventory < 0 || config.inventory > 2 ) return;
		
		boolean allOff = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					if( ts.tryInsertItem( ts.getStackInInventorySlot( config.inventory ), side ) )
						ts.extractItemInternal( true, config.inventory, 1 );
					return;
				}
				allOff = false;
			}
			
			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					if( ts.tryInsertItem( ts.getStackInInventorySlot( config.inventory ), side ) )
						ts.extractItemInternal( true, config.inventory, 1 );
					return;
				}
				allOff = false;
			}
		}
		
		if( allOff ) if( ts.tryInsertItem( ts.getStackInInventorySlot( config.inventory ), side ) )
			ts.extractItemInternal( true, config.inventory, 1 );
	}
	
	@Override
	public boolean hasInventoryIndicator() {
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
	public boolean isItemInterface() {
		return true;
	}
	
	@Override
	public boolean canExtractItems() {
		return true;
	}
	
	@Override
	public boolean canDirectlyExtractItems( SideConfig config, SocketTileAccess ts ) {
		if( config.inventory < 0 || config.inventory > 2 ) return false;
		
		boolean allOff = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					return true;
				}
				allOff = false;
			}
			
			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					return true;
				}
				allOff = false;
			}
		}
		
		return allOff;
	}
	
	@Override
	public ItemStack itemExtract( int amount, boolean doExtract, SideConfig config, SocketTileAccess ts ) {
		if( config.inventory != -1 ) return ts.extractItemInternal( doExtract, config.inventory, amount );
		return null;
	}

	@SideOnly( Side.CLIENT )
	public ItemStack getItemToRender( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.inventory != -1 ) return ts.getStackInInventorySlot( config.inventory );
		return null;
	}

	@Override
	public void onInventoryChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add ) {
		if( index == config.inventory ) {
			ts.sendClientInventorySlot( index );
		}
	}
}
