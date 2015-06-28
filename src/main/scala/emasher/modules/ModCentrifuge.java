package emasher.modules;

import emasher.api.CentrifugeRecipeRegistry;
import emasher.api.CentrifugeRecipeRegistry.CentrifugeRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;


public class ModCentrifuge extends SocketModule {

	public ModCentrifuge( int id ) {
		super( id, "eng_toolbox:centrifuge", "eng_toolbox:centrifuge_active" );
	}

	@Override
	public String getLocalizedName() {
		return "Centrifuge";
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ),
				"tpt", "tct", "gbg", Character.valueOf( 't' ), "ingotTin", Character.valueOf( 'p' ), emasher.items.Items.psu(),
				Character.valueOf( 'c' ), "ingotCopper", Character.valueOf( 'g' ), Items.ghast_tear, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Used to purify impure metal dusts" );
		l.add( "Sometimes gives bonus pure dusts" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_GREEN() + "Input Inventory" );
		l.add( emasher.util.Config.PREF_YELLOW() + "Outputs to Machine Output" );
		l.add( emasher.util.Config.PREF_AQUA() + "Requires 20 RF/tick" );
		l.add( "Cannot be installed on a socket with other machines" );
	}
	
	@Override
	public boolean hasInventoryIndicator() {
		return true;
	}
	
	@Override
	public boolean isMachine() {
		return true;
	}
	
	@Override
	public boolean canBeInstalled( SocketTileAccess ts, ForgeDirection side ) {
		for( int i = 0; i < 6; i++ ) {
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( i ) );
			if( m != null && m.isMachine() ) return false;
		}
		
		return true;
	}
	
	@Override
	public void onRemoved( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		boolean updateClient = false;
		if( config.inventory >= 0 && config.inventory <= 2 ) {
			if( ts.sideInventory.getStackInSlot( side.ordinal() ) == null ) {
				if( ts.getStackInInventorySlot( config.inventory ) != null ) {
					ItemStack toIntake = ts.getStackInInventorySlot( config.inventory );
					
					ItemStack product = null;
					
					CentrifugeRecipe r = CentrifugeRecipeRegistry.getRecipe( toIntake );
					if( r != null ) product = r.getOutput();
					
					if( product != null ) {
						ts.sideInventory.setInventorySlotContents( side.ordinal(), ts.extractItemInternal( true, config.inventory, 1 ) );
						config.meta = 180;
						config.rsControl[0] = false;
						config.rsControl[1] = false;
						config.rsControl[2] = false;
						updateClient = true;
					}
				}
			} else if( ts.useEnergy( 20, true ) >= 20 && config.meta > 0 && !config.rsControl[2] ) {
				ts.useEnergy( 20, false );
				config.meta--;
				if( config.meta == 0 ) updateClient = true;
				if( !config.rsControl[0] && config.meta > 0 ) {
					config.rsControl[0] = true;
					updateClient = true;
				}
			} else {
				if( config.rsControl[0] ) {
					config.rsControl[0] = false;
					updateClient = true;
				}
			}
			
			//Output primary
			if( config.meta == 0 && ts.sideInventory.getStackInSlot( side.ordinal() ) != null ) {
				CentrifugeRecipe re = CentrifugeRecipeRegistry.getRecipe( ts.sideInventory.getStackInSlot( side.ordinal() ) );
				if( re != null ) {
					int num = ts.forceOutputItem( re.getOutput().copy() );
					if( num == 0 ) config.rsControl[2] = true;
					else {
						config.rsControl[2] = false;
						ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
					}
				}
				
			}
			
			//Output secondary
			if( config.meta == 60 && ( !config.rsControl[1] || config.rsControl[2] ) && ts.sideInventory.getStackInSlot( side.ordinal() ) != null ) {
				config.rsControl[1] = true;
				CentrifugeRecipe re = CentrifugeRecipeRegistry.getRecipe( ts.sideInventory.getStackInSlot( side.ordinal() ) );
				if( re != null && ( re.shouldOuputSecondary( ts.getWorldObj().rand ) || config.rsControl[2] ) ) {
					int num = ts.forceOutputItem( re.getSecondaryOutput().copy() );
					if( num == 0 ) config.rsControl[2] = true;
					else config.rsControl[2] = false;
				}
			}
			if( updateClient ) ts.sendClientSideState( side.ordinal() );
		}
	}
	
	@Override
	public int getCurrentTexture( SideConfig config ) {
		if( config.meta == 0 || !config.rsControl[0] ) return 0;
		return 1;
	}

}

