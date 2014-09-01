package emasher.api;

import net.minecraftforge.common.util.ForgeDirection;
import emasher.api.SocketTileAccess;
//import emasher.sockets.PacketHandler;

public abstract class RSGateModule extends SocketModule
{
	public RSGateModule(int id, String ... textureFiles)
	{
		super(id, textureFiles);
	}
	
	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		updateOutput(ts, config);
		ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	public void indicatorUpdated(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		updateOutput(ts, config);
		ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public int getCurrentTexture(SideConfig config) { return config.meta; }
	
	@Override
	public boolean isRedstoneInterface() { return true; }
	
	@Override
	public boolean isOutputingRedstone(SideConfig config, SocketTileAccess ts)
	{
		return config.meta == 1; 
	}
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		updateOutput(ts, config);
		ts.updateAdj(side);
		ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	public void onRSLatchChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		updateOutput(ts, config);
		ts.updateAdj(side);
		ts.sendClientSideState(side.ordinal());
	}
	
	public abstract void updateOutput(SocketTileAccess ts, SideConfig config);

}