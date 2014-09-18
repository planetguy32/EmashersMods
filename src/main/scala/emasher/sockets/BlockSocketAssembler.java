package emasher.sockets;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSocketAssembler extends BlockContainer
{

	protected BlockSocketAssembler(int par1)
	{
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileSocketAssembler();
	}
	
}
