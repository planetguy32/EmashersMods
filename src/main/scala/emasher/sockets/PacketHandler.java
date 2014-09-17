package emasher.sockets;


import cpw.mods.fml.common.network.NetworkRegistry;
import emasher.sockets.packethandling.PacketTileEntity;
import emasher.sockets.pipes.TileDirectionChanger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.fluids.FluidStack;
import emasher.api.SideConfig;
import emasher.sockets.pipes.TileAdapterBase;
import emasher.sockets.pipes.TilePipeBase;

public class PacketHandler
{
	public static PacketHandler instance = new PacketHandler();
	public final static String networkChannel = "Emasher_Sockets";

	public void SendClientSideState(TileSocket ts, byte side)
	{
		SideConfig c = ts.configs[side];
		
		byte[] out = new byte[36];
		
		out[0] = 0;
		out[2] = (byte)c.tank;
		out[3] = (byte)c.inventory;
		out[4] = (byte)ts.rsIndicatorIndex(side);
		out[5] = (byte)ts.latchIndicatorIndex(side);
		
		toByte(out, c.meta, 6);
		toByte(out, ts.xCoord, 10);
		toByte(out, ts.yCoord, 14);
		toByte(out, ts.zCoord, 18);
		toByte(out, ts.facID[side], 27);
		toByte(out, ts.facMeta[side], 31);
		
		out[1] = (byte)side;
		toByte(out, ts.sides[side], 22);
		out[26] = boolToByte(ts.sideLocked[side]);
		
        SocketsMod.packetPipeline.sendToAllAround(new PacketTileEntity(ts, out), new NetworkRegistry.TargetPoint(ts.getWorldObj().provider.dimensionId, ts.xCoord, ts.yCoord, ts.zCoord, 160));
	}
	
	public void SendClientInventorySlot(TileSocket ts, int inventory)
	{
		ItemStack s = ts.inventory.getStackInSlot(inventory);
		byte[] NBTData = new byte[]{};
		
		try
		{
			if(s != null && s.getTagCompound() != null) NBTData = CompressedStreamTools.compress(s.getTagCompound());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		byte[] out = new byte[27 + NBTData.length];
		
		out[0] = 1;
		
		if(s != null)
		{
			toByte(out, Item.getIdFromItem(s.getItem()), 1);
			toByte(out, s.getItemDamage(), 5);
			toByte(out, s.stackSize, 23);
		}
		else
		{
			toByte(out, -1, 1);
		}
		
		toByte(out, ts.xCoord, 9);
		toByte(out, ts.yCoord, 13);
		toByte(out, ts.zCoord, 17);
		out[22] = (byte)inventory;
		
		for(int i = 0; i < NBTData.length; i++)
		{
			out[27 + i] = NBTData[i];
		}
		
		
        SocketsMod.packetPipeline.sendToAllAround(new PacketTileEntity(ts, out), new NetworkRegistry.TargetPoint(ts.getWorldObj().provider.dimensionId, ts.xCoord, ts.yCoord, ts.zCoord, 160));
	}
	
	public void SendClientTankSlot(TileSocket ts, int tank)
	{
		FluidStack l = ts.tanks[tank].getFluid();
		
		int id = -1;
		int meta = 0;
		int amnt = 0;
		
		byte[] out = new byte[27];
		out[0] = 2;
		
		if(l != null)
		{
			id = l.fluidID;
			meta = 0;
			amnt = l.amount;
		}
		
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, id, 13);
		toByte(out, meta, 17);
		toByte(out, amnt, 22);
		out[26] = (byte)tank;
		
        SocketsMod.packetPipeline.sendToAllAround(new PacketTileEntity(ts, out), new NetworkRegistry.TargetPoint(ts.getWorldObj().provider.dimensionId, ts.xCoord, ts.yCoord, ts.zCoord, 160));
	}
	
	public void sendClientPipeColour(TilePipeBase p)
	{
		byte[] out = new byte[18];
		
		out[0] = 3;
		toByte(out, p.xCoord, 1);
		toByte(out, p.yCoord, 5);
		toByte(out, p.zCoord, 9);
		toByte(out, p.getWorldObj().provider.dimensionId, 13);
		out[17] = (byte)p.colour;
		
        SocketsMod.packetPipeline.sendToDimension(new PacketTileEntity(p, out), p.getWorldObj().provider.dimensionId);
	}
	
	public void sendClientAdapterSide(TileAdapterBase t, int side)
	{
		byte[] out = new byte[19];
		
		out[0] = 4;
		toByte(out, t.xCoord, 1);
		toByte(out, t.yCoord, 5);
		toByte(out, t.zCoord, 9);
		toByte(out, t.getWorldObj().provider.dimensionId, 13);
		if(t.outputs[side]) out[17] = 1;
		else out[17] = 0;
		out[18] = (byte)side;

        SocketsMod.packetPipeline.sendToDimension(new PacketTileEntity(t, out), t.getWorldObj().provider.dimensionId);
	}

    public void sendClientChangerSide(TileDirectionChanger t, int side)
    {
        byte[] out = new byte[19];

        out[0] = 5;
        toByte(out, t.xCoord, 1);
        toByte(out, t.yCoord, 5);
        toByte(out, t.zCoord, 9);
        toByte(out, t.getWorldObj().provider.dimensionId, 13);
        out[17] = (byte)t.directions[side].ordinal();
        out[18] = (byte)side;

        SocketsMod.packetPipeline.sendToDimension(new PacketTileEntity(t, out), t.getWorldObj().provider.dimensionId);
    }
	
	public static void toByte(byte[] out, int in, int start)
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
	
	public static byte boolToByte(boolean b)
	{
		if(b) return (byte)1;
		return (byte) 0;
	}
}
