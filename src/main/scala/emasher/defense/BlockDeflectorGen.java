package emasher.defense;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.EntityPlayer;

public class BlockDeflectorGen extends BlockContainer
{
	
	public static Icon topTexture;
	public static Icon sideTexture;
	public static Icon bottomTexture;

	protected BlockDeflectorGen(int par1, Material par2Material)
	{
		super(par1, par2Material);
		this.setCreativeTab(EmasherDefense.tabDefense);
		this.setLightValue(5.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
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
		this.onNeighborBlockChange(world, x, y, z, this.blockID);
		return meta;
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion exp)
	{
		this.onNeighborBlockChange(world, x, y + 1, z, this.blockID);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
	{
		this.onNeighborBlockChange(world, x, y + 1, z, this.blockID);
	}
	
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		int str = 3;
		TileEntity entity = world.getBlockTileEntity(x, y, z);
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
			if(world.getBlockId(x, y - 1, z) == this.blockID)
			{
				TileEntity otherEntity = world.getBlockTileEntity(x, y - 1, z);
				if(otherEntity != null && otherEntity instanceof TileDeflectorGen)
				{
					TileDeflectorGen otherEntityCast = (TileDeflectorGen)otherEntity;
					str = otherEntityCast.getStrenth() + 1;
				}
			}
			castEntity.setStrenght(str);
		}
		
		
		if(world.getBlockId(x, y + 1, z) == this.blockID)
		{
			onNeighborBlockChange(world, x, y + 1, z, id);
		}
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		super.registerIcons(par1IconRegister);
		this.blockIcon = par1IconRegister.registerIcon("emasherdefense:deflector_base");
		bottomTexture = this.blockIcon;
		sideTexture = par1IconRegister.registerIcon("emasherdefense:deflector_side");
		topTexture = par1IconRegister.registerIcon("emasherdefense:deflector_top");
    }
	
	@Override
	public Icon getIcon(int par1, int par2)
	{
		Icon result = this.sideTexture;
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
