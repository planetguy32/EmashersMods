package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraft.item.ItemStack;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModRSOR extends RSGateModule {

	public ModRSOR( int id ) {
		super( id, "eng_toolbox:OR_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone OR";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs an external redstone signal" );
		l.add( "when the OR function is satisfied" );
		l.add( "based on its internal inputs" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS control inputs" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS latche inputs" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapelessRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), new ItemStack( emasher.items.Items.module(), 1, 17 ) );
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