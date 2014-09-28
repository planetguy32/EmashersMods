package emasher.sockets.client;

import emasher.sockets.packethandling.PacketTileEntity;
import emasher.sockets.pipes.TileDirectionChanger;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import emasher.sockets.pipes.TileAdapterBase;
import emasher.sockets.pipes.TilePipeBase;

public class ClientPacketHandler
{
	public static ClientPacketHandler instance = new ClientPacketHandler();
	public final static String networkChannel = "Emasher_Sockets";
	

	public void requestSideData(TileSocket ts, byte side)
	{
		byte[] out = new byte[18];
		
		out[0] = 0;
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, ts.getWorldObj().provider.dimensionId, 13);
		out[17] = side;
		
        SocketsMod.packetPipeline.sendToServer(new PacketTileEntity(ts, out));
	}
	
	public void requestInventoryData(TileSocket ts, byte inventory)
	{
		byte[] out = new byte[18];
		
		out[0] = 1;
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, ts.getWorldObj().provider.dimensionId, 13);
		out[17] = inventory;

        SocketsMod.packetPipeline.sendToServer(new PacketTileEntity(ts, out));
	}
	
	public void requestTankData(TileSocket ts, byte tank)
	{
		byte[] out = new byte[18];
		
		out[0] = 2;
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, ts.getWorldObj().provider.dimensionId, 13);
		out[17] = tank;
		
        SocketsMod.packetPipeline.sendToServer(new PacketTileEntity(ts, out));
	}
	
	public void requestPipeColourData(TilePipeBase p)
	{
		byte[] out = new byte[17];
		
		out[0] = 3;
		toByte(out, p.xCoord, 1);
		toByte(out, p.yCoord, 5);
		toByte(out, p.zCoord, 9);
		toByte(out, p.getWorldObj().provider.dimensionId, 13);

        SocketsMod.packetPipeline.sendToServer(new PacketTileEntity(p, out));
	}
	
	public void requestAdapterOutputData(TileAdapterBase p)
	{
		byte[] out = new byte[17];
		
		out[0] = 4;
		toByte(out, p.xCoord, 1);
		toByte(out, p.yCoord, 5);
		toByte(out, p.zCoord, 9);
		toByte(out, p.getWorldObj().provider.dimensionId, 13);

        SocketsMod.packetPipeline.sendToServer(new PacketTileEntity(p, out));
	}

    public void requestDirectionData(TileDirectionChanger p)
    {
        byte[] out = new byte[17];

        out[0] = 5;
        toByte(out, p.xCoord, 1);
        toByte(out, p.yCoord, 5);
        toByte(out, p.zCoord, 9);
        toByte(out, p.getWorldObj().provider.dimensionId, 13);

        SocketsMod.packetPipeline.sendToServer(new PacketTileEntity(p, out));
    }
	
	private void toByte(byte[] out, int in, int start)
	{
		out[start++] = (byte) (in >> 24);
		out[start++] = (byte) (in >> 16);
		out[start++] = (byte) (in >> 8);
		out[start++] = (byte) in;
	}
	
	private static int toInteger(byte[] in, int start)
	{
		int value = 0;
		for (int i = start; i < start + 4; i++) {
			value = (value << 8) + (in[i] & 0xFF);
		}
		return value;
	}
	
	private static boolean byteToBool(byte b)
	{
		if(b == 0) return false;
		return true;
	}
}
