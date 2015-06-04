package emasher.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class RSGateModule extends SocketModule {
	public RSGateModule( int id, String... textureFiles ) {
		super( id, textureFiles );
	}
	
	@Override
	public void init( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		updateOutput( ts, config );
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void indicatorUpdated( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		updateOutput( ts, config );
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public boolean hasRSIndicator() {
		return true;
	}
	
	@Override
	public boolean hasLatchIndicator() {
		return true;
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.meta == 0 ) return "sockets:inner_redstone_inactive";
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
	public boolean isRedstoneInterface() {
		return true;
	}
	
	@Override
	public boolean isOutputtingRedstone( SideConfig config, SocketTileAccess ts ) {
		return config.meta == 1;
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		updateOutput( ts, config );
		ts.updateAdj( side );
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void onRSLatchChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		updateOutput( ts, config );
		ts.updateAdj( side );
		ts.sendClientSideState( side.ordinal() );
	}
	
	public abstract void updateOutput( SocketTileAccess ts, SideConfig config );

}