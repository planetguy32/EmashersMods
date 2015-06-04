package emasher.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class RSPulseModule extends RSGateModule {

	public RSPulseModule( int id, String... textureFiles ) {
		super( id, textureFiles );
	}

	@Override
	public int getCurrentTexture( SideConfig config ) {
		return 0;
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( ( config.meta & 1 ) == 0 ) return "sockets:inner_redstone_inactive";
		return "sockets:inner_redstone_active";
	}

	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {
				"sockets:inner_redstone_inactive",
				"sockets:inner_redstone_active"
		};
	}
	
	@Override
	public boolean isOutputtingRedstone( SideConfig config, SocketTileAccess ts ) {
		return config.meta >= 1;
	}
	
	@Override
	public boolean hasLatchIndicator() {
		return false;
	}

	@Override
	public void updateOutput( SocketTileAccess ts, SideConfig config ) {
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( config.meta > 0 ) {
			config.meta++;
			if( config.meta >= 4 ) {
				config.meta = 0;
				ts.updateAdj( side );
				ts.sendClientSideState( side.ordinal() );
				
				for( int i = 0; i < 3; i++ ) {
					if( config.rsControl[i] ) {
						ts.modifyRS( i, false );
					}
				}
			}
			
			
		}
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( !on ) {
			if( config.rsControl[index] && config.meta > 0 ) {
				ts.modifyRS( index, true );
			}
		}
	}
}
