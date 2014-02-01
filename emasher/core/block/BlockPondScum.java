package emasher.core.block;

import java.util.Random;

import emasher.core.EmasherCore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPondScum extends BlockLilyPad
{
	private static final int GROWTH_TIME = 6;
	
	
	public BlockPondScum(int par1)
	{
		super(par1);
		this.setTickRandomly(true);
		this.setCreativeTab(EmasherCore.tabEmasher);
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon =  par1IconRegister.registerIcon("emashercore:algae");
    }
	
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(world.getBlockLightValue(x, y, z) >= 14)
		{
			if(meta >= GROWTH_TIME)
			{
				int xInc, zInc;
				boolean canPlace = false;
				int tries = 0;
				
				do
				{
					xInc = random.nextInt(3) - 1;
					zInc = random.nextInt(3) - 1;
					canPlace = world.isAirBlock(x + xInc, y, z + zInc) && world.getBlockId(x + xInc, y - 1, z + zInc) == Block.waterStill.blockID;
					
					tries++;
				}
				while(!(xInc == 0 && zInc == 0) && !canPlace && tries <= 9);
				
				int tx = x + xInc;
				int tz = z + zInc;
				
				int ty = y - 1;
				int i = 0;
				
				while(world.getBlockId(tx, ty, tz) == Block.waterStill.blockID && i < EmasherCore.algaeDepth)
				{
					ty--;
					i++;
				}
				
				if(i >= EmasherCore.algaeDepth)
				{
					canPlace = false;
				}
				
				if(canPlace)
				{
					world.setBlock(x + xInc, y, z + zInc, this.blockID, 0, 3);
					world.setBlockMetadataWithNotify(x, y, z, 0, 2);
				}
			}
			else
			{
				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
			}
			
		}
		
	}

}
