package emasher.defense;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

//public class BlockSandBag extends BlockSand
public class BlockSandBag extends BlockFalling //TODO Check it really needed to extend BlockSand and not just BlockFalling
{

	public BlockSandBag(int par1, Material par2Material) 
	{
        super(par2Material);
		this.setCreativeTab(EmasherDefense.tabDefense);
		//blockIndexInblockIcon = 5;
	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emasherdefense:sandbag");
    }
	

}
