package emasher.core.hemp;

import java.util.Random;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.item.*;

public class BlockHemp extends Block
{
	private static final int GROWTH_TIME = 15;
	
	public BlockHemp(int i, int j)
	{
		super(i, Material.plants);
        float f = 0.375F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        setTickRandomly(true);
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emashercore:hemp");
    }
	
	@Override
	public Icon getIcon(int side, int meta)
	{
		return this.blockIcon;
	}
	
	@Override
	public int idDropped(int i, Random random, int j)
	{
		return EmasherCore.hempPlant.itemID;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        checkBlockCoordValid(world, i, j, k);
    }

    protected final void checkBlockCoordValid(World world, int i, int j, int k)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem_do(world, i, j, k, new ItemStack(EmasherCore.hempPlant, 1));
            world.setBlock(i, j, k, 0, 0, 3);
        }
    }
	
    @Override
	public boolean canBlockStay(World world, int i, int j, int k)
    {
        return canPlaceBlockAt(world, i, j, k);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
    @Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
		boolean toReturn = false;
        int l = world.getBlockId(i, j - 1, k);
		
		if(l == Block.grass.blockID || l == Block.dirt.blockID || l == Block.gravel.blockID || l == Block.sand.blockID || l == EmasherCore.mixedDirt.blockID || l == EmasherCore.mixedSand.blockID || l == blockID)
		{
			toReturn = true;
		}
		
        return toReturn;
    }
	
    @Override
	public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isAirBlock(i, j + 1, k))
        {
            int l;
            for (l = 1; world.getBlockId(i, j - l, k) == blockID; l++) { }
            if (l < 3)
            {
                int i1 = world.getBlockMetadata(i, j, k);
                if (i1 == GROWTH_TIME)
                {
                    world.setBlock(i, j + 1, k, blockID, 0, 2);
                    world.setBlockMetadataWithNotify(i, j, k, 0, 2);
                }
                else if(world.getBlockLightValue(i, j + 1, k) >= 9 && nearWater(world, i, j, k))
                {
                    world.setBlockMetadataWithNotify(i, j, k, i1 + 1, 2);
                }
            }
        }
    }
	
	public boolean nearWater(World world, int i, int j, int k)
	{
		boolean toReturn = false;
		
		if(world.getBlockId(i, j - 1, k) == blockID && nearWater(world, i, j - 1, k))
		{
			toReturn = true;
		}
		else if (world.getBlockMaterial(i - 1, j - 1, k) == Material.water)
        {
            toReturn = true;
        }
        else if (world.getBlockMaterial(i + 1, j - 1, k) == Material.water)
        {
            toReturn = true;
        }
        else if (world.getBlockMaterial(i, j - 1, k - 1) == Material.water)
        {
            toReturn = true;
        }
        else if (world.getBlockMaterial(i, j - 1, k + 1) == Material.water)
		{
			toReturn = true;
		}
		
		return toReturn;
	}
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    public int getRenderType()
    {
        return 1;
    }
}