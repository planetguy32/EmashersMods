package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSPulseModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModPressurePlate extends RSPulseModule {

	public ModPressurePlate( int id ) {
		super( id, "eng_toolbox:pressurePlate" );
	}

	@Override
	public String getLocalizedName() {
		return "Pressure Plate";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Creates an internal redstone" );
		l.add( "pulse when stepped on" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS channels to pulse" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS latches to toggle" );
		l.add( "Can only be placed on the top of a socket" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "eie", " b ",
				Character.valueOf( 'i' ), Blocks.stone_pressure_plate, Character.valueOf( 'b' ), emasher.items.Items.blankSide(),
				'e', new ItemStack( emasher.items.Items.gem(), 1, 0 ));
	}
	
	@Override
	public boolean isOutputtingRedstone( SideConfig config, SocketTileAccess ts ) {
		return false;
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
	public boolean canBeInstalled( SocketTileAccess ts, ForgeDirection side ) {
		if( side != ForgeDirection.UP ) return false;
		return true;
	}
	
	@Override
	public void onEntityWalkOn( SocketTileAccess ts, SideConfig config, ForgeDirection side, Entity entitiy ) {
		if( config.meta == 0 ) {
			config.meta = 1;
			for( int i = 0; i < 3; i++ ) {
				if( config.rsControl[i] ) {
					ts.modifyRS( i, true );
				}
				
				if( config.rsLatch[i] ) {
					ts.modifyLatch( i, !ts.getRSLatch( i ) );
				}
			}
		}
	}
	
}
