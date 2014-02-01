package emasher.sockets;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileTempRS extends TileEntity
{
	int timer;
	
	public TileTempRS()
	{
		timer = 0;
	}
	
	@Override
	public void updateEntity()
	{
		if(! worldObj.isRemote)
		{
			timer++;
			if(timer >= 25)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
				worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
			}
		}
	}
}
