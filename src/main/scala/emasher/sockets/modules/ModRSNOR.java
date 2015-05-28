package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.item.ItemStack;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModRSNOR extends RSGateModule {

	public ModRSNOR( int id ) {
		super( id, "sockets:NOR_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone NOR";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs an external redstone signal" );
		l.add( "when the NOR function is satisfied" );
		l.add( "based on its internal inputs" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( SocketsMod.PREF_RED + "RS control inputs" );
		l.add( SocketsMod.PREF_DARK_PURPLE + "RS latche inputs" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapelessRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), new ItemStack( SocketsMod.module, 1, 20 ) );
	}
	
	@Override
	public void updateOutput( SocketTileAccess ts, SideConfig config ) {
		int meta = 0;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] && ts.getRSControl( i ) ) meta = 1;
			if( config.rsLatch[i] && ts.getRSLatch( i ) ) meta = 1;
		}
		
		if( meta == 0 ) meta = 1;
		else meta = 0;
		
		config.meta = meta;
		
	}

}