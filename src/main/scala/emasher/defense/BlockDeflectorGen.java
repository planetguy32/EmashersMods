package emasher.defense;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.EntityPlayer;

public class BlockDeflectorGen extends BlockContainer
{
	
	public static IIcon topTexture;
	public static IIcon sideTexture;
	public static IIcon bottomTexture;

	protected BlockDeflectorGen(int par1, Material par2Material)
	{
		super(par2Material);
		this.setCreativeTab(EmasherDefense.tabDefense);
		this.setLightLevel(5.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileDeflectorGen();
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
	{
		this.onNeighborBlockChange(world, x, y, z, this);
		return meta;
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion exp)
	{
		this.onNeighborBlockChange(world, x, y + 1, z, this);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
	{
		this.onNeighborBlockChange(world, x, y + 1, z, this);
	}
	
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		int str = 3;
		TileEntity entity = world.getTileEntity(x, y, z);
		TileDeflectorGen castEntity = null;
		
		if(world.isBlockIndirectlyGettingPowered(x, y, z))
		{
			world.setBlockMetadataWithNotify(x, y, z, 1, 0);
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, 0, 0);
		}
		
		if(entity != null && entity instanceof TileDeflectorGen)
		{
			castEntity = (TileDeflectorGen)entity;
		}
		
		if(castEntity != null)
		{
			if(world.getBlock(x, y - 1, z) == this)
			{
				TileEntity otherEntity = world.getTileEntity(x, y - 1, z);
				if(otherEntity != null && otherEntity instanceof TileDeflectorGen)
				{
					TileDeflectorGen otherEntityCast = (TileDeflectorGen)otherEntity;
					str = otherEntityCast.getStrenth() + 1;
				}
			}
			castEntity.setStrenght(str);
		}
		
		
		if(world.getBlock(x, y + 1, z) == this)
		{
			onNeighborBlockChange(world, x, y + 1, z, block);
		}
	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		super.registerBlockIcons(par1IconRegister);
		this.blockIcon = par1IconRegister.registerIcon("emasherdefense:deflector_base");
		bottomTexture = this.blockIcon;
		sideTexture = par1IconRegister.registerIcon("emasherdefense:deflector_side");
		topTexture = par1IconRegister.registerIcon("emasherdefense:deflector_top");
    }
	
	@Override
	public IIcon getIcon(int par1, int par2)
	{
		IIcon result = this.sideTexture;
		if(par1 == 1)
		{
			result = this.topTexture;
		}
		else if(par1 == 0)
		{
			result = this.bottomTexture;
		}
		
		return result;
	}
	
}
