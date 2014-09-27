package emasher.gas;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.network.INetworkManager;
//import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
//import cpw.mods.fml.common.network.IPacketHandler;
//import cpw.mods.fml.common.network.PacketDispatcher;
//import cpw.mods.fml.common.network.Player;

public class PacketHandler// implements IPacketHandler
{
	public static PacketHandler instance = new PacketHandler();
	public final static String networkChannel = "gascraft";
	
	/*@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		//Stub
	}*/
	
	

}
