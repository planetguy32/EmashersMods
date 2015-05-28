package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModFurnace extends SocketModule {

	public ModFurnace( int id ) {
		super( id, "sockets:furnaceIdle", "sockets:furnaceActive" );
	}

	@Override
	public String getLocalizedName() {
		return "Furnace";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Uses energy to smelt items" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( SocketsMod.PREF_GREEN + "Input inventory" );
		l.add( SocketsMod.PREF_YELLOW + "Outputs to Machine Output" );
		l.add( SocketsMod.PREF_AQUA + "Requires 10 RF/t" );
		l.add( "Cannot be installed on a socket with other machines" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), " h ", "iui", " b ", Character.valueOf( 'i' ), Blocks.brick_block, Character.valueOf( 'h' ), EmasherCore.psu,
				Character.valueOf( 'u' ), Blocks.furnace, Character.valueOf( 'b' ), SocketsMod.blankSide );
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
					ItemStack product = FurnaceRecipes.smelting().getSmeltingResult( toIntake );
					if( product != null ) {
						ts.extractItemInternal( true, config.inventory, 1 );
						ts.sideInventory.setInventorySlotContents( side.ordinal(), product.copy() );
						config.meta = 180;
						config.rsControl[0] = false;
						updateClient = true;
					}
				}
			} else if( ts.useEnergy( 10, true ) >= 10 && config.meta > 0 ) {
				ts.useEnergy( 10, false );
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
			
			if( config.meta == 0 && ts.sideInventory.getStackInSlot( side.ordinal() ) != null ) {
				int num = ts.forceOutputItem( ts.sideInventory.getStackInSlot( side.ordinal() ) );
				ts.sideInventory.getStackInSlot( side.ordinal() ).stackSize -= num;
				if( ts.sideInventory.getStackInSlot( side.ordinal() ).stackSize <= 0 )
					ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
				
			}
			if( updateClient ) ts.sendClientSideState( side.ordinal() );
		}
	}

	@Override
	public int getCurrentTexture( SideConfig config ) {
		if( config.meta == 0 || !config.rsControl[0] ) return 0;
		return 1;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.meta == 0 || !config.rsControl[0] ) return "sockets:inner_black";
		return "sockets:inner_fire_blue";
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"sockets:inner_fire_blue"};
	}

}
