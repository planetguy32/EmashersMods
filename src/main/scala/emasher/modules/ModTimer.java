package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.blocks.BlockSocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import emasher.util.Config;

import java.util.List;

public class ModTimer extends SocketModule {
	
	public static final int[] settings = new int[] {10, 20, 40, 80, 160, 200, 600, 1200};

	public ModTimer( int id ) {
		super( id, "eng_toolbox:timer" );
	}

	@Override
	public String getLocalizedName() {
		return "Timer";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Creates an external redstone pulse periodically" );
		l.add( "Can be paused using an internal redstone control" );
		l.add( "circuit or latch" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( Config.PREF_RED() + "RS control channel" );
		l.add( Config.PREF_DARK_PURPLE() + "RS control latch" );
		l.add( Config.PREF_WHITE() + "Modify length of delay" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "t", "b", Character.valueOf( 't' ), Items.clock, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 17 ) );
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
	public boolean isRedstoneInterface() {
		return true;
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		int timer = config.meta & 7;
		if( timer == 0 ) return "eng_toolbox:inner_redstone_inactive";
		return "eng_toolbox:inner_redstone_active";
	}

	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {
				"eng_toolbox:inner_redstone_inactive",
				"eng_toolbox:inner_redstone_active"
		};
	}
	
	@SideOnly( Side.CLIENT )
	public IIcon[] getAdditionalOverlays( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		int setting = 0;
		int time = 0;
		int on = 0;
		
		setting = config.meta >> 3;
		setting &= 7;
		on = config.meta & 7;
		
		time = config.meta >> 6;
		
		time = ( int ) Math.ceil( ( time * 7 ) / settings[setting] );
		
		if( on > 0 ) time = 7;
		
		return new IIcon[] {( emasher.blocks.Blocks.socket() ).bar1[setting], ( emasher.blocks.Blocks.socket() ).bar2[time]};
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public boolean flipBottomOverlay() {
		return true;
	}
	
	@Override
	public boolean isOutputtingRedstone( SideConfig config, SocketTileAccess ts ) {
		boolean result = ( config.meta & 7 ) >= 1;
		return result;
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		int setting = 0;
		int oldTime = 0;
		int reBuild = 0;
		int on = config.meta & 7;
		boolean reRender = false;
		boolean updateAdj = false;
		boolean doInc = false;
		boolean allOff = true;
		
		setting = config.meta >> 3;
		setting &= 7;
		
		oldTime = config.meta >> 6;
		int time = oldTime;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				allOff = false;
				if( ts.getRSControl( i ) ) doInc = true;
			}
			
			if( config.rsLatch[i] ) {
				allOff = false;
				if( ts.getRSLatch( i ) ) doInc = true;
			}
			
			if( allOff ) doInc = true;
		}
		
		if( doInc ) {
			time++;
			oldTime = ( int ) Math.ceil( ( oldTime * 7 ) / settings[setting] );
			int timeDisp = ( int ) Math.ceil( ( time * 7 ) / settings[setting] );
			if( oldTime != timeDisp ) reRender = true;
			
			
			if( time >= settings[setting] ) {
				time = 0;
				on = 1;
				reRender = true;
				updateAdj = true;
			}
		}
		
		if( on != 0 ) on++;
		if( on >= 5 ) {
			on = 0;
			reRender = true;
			updateAdj = true;
		}
		
		reBuild = time;
		reBuild <<= 3;
		reBuild |= setting;
		reBuild <<= 3;
		reBuild |= on;
		
		config.meta = reBuild;
		
		if( reRender ) {
			ts.sendClientSideState( side.ordinal() );
			
		}
		
		if( updateAdj ) {
			ts.updateAdj( side );
		}
		
		
	}
	
	@Override
	public void onGenericRemoteSignal( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		int on = config.meta & 7;
		int setting = config.meta >> 3;
		setting &= 7;
		int time = config.meta >> 6;
		int reBuild = 0;
		
		setting++;
		
		if( setting >= 8 ) setting = 0;
		
		reBuild = 0;
		reBuild <<= 3;
		reBuild |= setting;
		reBuild <<= 3;
		reBuild |= on;
		
		config.meta = reBuild;
		
		ts.sendClientSideState( side.ordinal() );
	}
	
	
}
