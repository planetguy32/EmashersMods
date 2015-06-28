package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSPulseModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModBUD extends RSPulseModule {

	public ModBUD( int id ) {
		super( id, "eng_toolbox:BUD_0", "eng_toolbox:DBUDS" );
	}

	@Override
	public String getLocalizedName() {
		return "Block Update Sensor";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Creats an internal redstone pulse" );
		l.add( "upon an adjacent block update" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS Circuit to pulse" );
		l.add( emasher.util.Config.PREF_WHITE() + "Toggle directional mode" );
	}
	
	@Override
	public int getCurrentTexture( SideConfig config ) {
		if( config.tank == 0 ) {
			return 0;
		}

		return 1;
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "i", "b", Character.valueOf( 'i' ), Blocks.light_weighted_pressure_plate, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void init( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		config.tank = 0;
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void onGenericRemoteSignal( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.tank == 0 ) config.tank = 1;
		else config.tank = 0;
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void onAdjChange( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.tank == 0 ) {
			config.meta = 1;
			
			for( int i = 0; i < 3; i++ ) {
				if( config.rsControl[i] ) {
					ts.modifyRS( i, true );
				}
			}
			
			ts.updateAdj( side );
			ts.sendClientSideState( side.ordinal() );
		}
	}

	
	@Override
	public void onAdjChangeSide( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.tank == 1 ) {
			config.meta = 1;
			
			for( int i = 0; i < 3; i++ ) {
				if( config.rsControl[i] ) {
					ts.modifyRS( i, true );
				}
			}
			
			ts.updateAdj( side );
			ts.sendClientSideState( side.ordinal() );
		}
	}

	@Override
	public boolean isOutputtingRedstone( SideConfig config, SocketTileAccess ts ) {
		return false;
	}

}
