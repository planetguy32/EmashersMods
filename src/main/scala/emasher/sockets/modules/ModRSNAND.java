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

public class ModRSNAND extends RSGateModule {

	public ModRSNAND( int id ) {
		super( id, "sockets:NAND_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone NAND";
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), " r ", "trt", " b ", Character.valueOf( 't' ), Blocks.redstone_torch, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'b' ), new ItemStack( SocketsMod.module, 1, 17 ) );
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs an external redstone signal" );
		l.add( "when the NAND function is satisfied" );
		l.add( "based on its internal inputs" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( SocketsMod.PREF_RED + "RS control inputs" );
		l.add( SocketsMod.PREF_DARK_PURPLE + "RS latche inputs" );
	}
	
	@Override
	public void updateOutput( SocketTileAccess ts, SideConfig config ) {
		int meta = 1;
		boolean none = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				none = false;
				if( !ts.getRSControl( i ) ) meta = 0;
			}
			
			if( config.rsLatch[i] ) {
				none = false;
				if( !ts.getRSLatch( i ) ) meta = 0;
			}
		}
		
		if( none ) meta = 0;
		
		if( meta == 1 ) meta = 0;
		else meta = 1;
		
		config.meta = meta;
		
	}

}
