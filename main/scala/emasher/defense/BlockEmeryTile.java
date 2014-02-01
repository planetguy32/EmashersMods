package emasher.defense;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEmeryTile extends Block 
{

	public BlockEmeryTile(int par1, Material par2Material) 
	{
		super(par1, par2Material);
		this.setCreativeTab(EmasherDefense.tabDefense);
		//blockIndexInblockIcon = 5;
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emasherdefense:emeryTile");
    }
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
		return false;
    }

}
