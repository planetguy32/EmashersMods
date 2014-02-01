package emasher.core.block;

import emasher.core.EmasherCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;


public class BlockMixedSand extends BlockSand
{

	
	public BlockMixedSand(int par1, Material par2Material) 
	{
		super(par1, par2Material);
		this.setCreativeTab(EmasherCore.tabEmasher);

	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emashercore:mixedSand");
    }
	
	

}