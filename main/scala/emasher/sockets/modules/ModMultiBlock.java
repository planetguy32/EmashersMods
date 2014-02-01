package emasher.sockets.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.sockets.TileSocket;

public class ModMultiBlock extends SocketModule
{

	public ModMultiBlock(int id)
	{
		super(id, "sockets:mbInterface");
	}

	@Override
	public String getLocalizedName()
	{
		return "Multi-Block Interface";
	}
	
}
