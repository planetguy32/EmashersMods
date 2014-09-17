package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import buildcraft.api.tools.IToolWrench;

public abstract class BlockAdapterBase extends BlockContainer
{
	public IIcon outputIcon;
	
	public BlockAdapterBase(int id, Material m)
	{
		super(m);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
	{
        if(! world.isRemote)
		{
			ItemStack is = player.getCurrentEquippedItem();
			Item item = null;
			if(is != null) item = is.getItem();
            if(item != null && item instanceof IToolWrench)
			{
				TileEntity te = world.getTileEntity(x, y, z);

				if(te != null && te instanceof TileAdapterBase)
				{
					((TileAdapterBase)te).toggleOutput(side);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
        TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TileAdapterBase)
		{
			if(((TileAdapterBase)te).outputs[side])
			{
				return outputIcon;
			}
		}
		
		return blockIcon;
	}
}
