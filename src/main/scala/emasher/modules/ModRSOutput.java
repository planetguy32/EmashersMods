package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModRSOutput extends RSGateModule {

	public ModRSOutput( int id ) {
		super( id, "eng_toolbox:RSOUT_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone Output";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs an external redstone signal when" );
		l.add( "When one or more of it's configured RS control" );
		l.add( "channels or latches is activated" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS control inputs" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS latche inputs" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "r", "b", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'r' ), Items.repeater,
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void updateOutput( SocketTileAccess ts, SideConfig config ) {
		int meta = 0;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] && ts.getRSControl( i ) ) meta = 1;
			if( config.rsLatch[i] && ts.getRSLatch( i ) ) meta = 1;
		}
		
		config.meta = meta;
		
	}

}
