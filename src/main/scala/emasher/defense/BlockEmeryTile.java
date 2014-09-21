package emasher.defense;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEmeryTile extends Block 
{

	public BlockEmeryTile(int par1, Material par2Material) 
	{
		super(par2Material);
		this.setCreativeTab(EmasherDefense.tabDefense);
		//blockIndexInblockIcon = 5;
	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emasherdefense:emeryTile");
    }
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
    {
		return false;
    }

}
