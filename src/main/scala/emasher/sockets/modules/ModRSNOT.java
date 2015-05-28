package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModRSNOT extends RSGateModule {

	public ModRSNOT( int id ) {
		super( id, "sockets:NOT_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone NOT";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs an external redstone signal" );
		l.add( "when the NOT function is satisfied" );
		l.add( "based on its internal inputs" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( SocketsMod.PREF_RED + "RS control inputs" );
		l.add( SocketsMod.PREF_DARK_PURPLE + "RS latche inputs" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "t", "b", Character.valueOf( 't' ), Blocks.redstone_torch, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'b' ), new ItemStack( SocketsMod.module, 1, 17 ) );
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