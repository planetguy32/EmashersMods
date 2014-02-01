package emasher.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import emasher.core.EmasherCore;

public class BlockRedSandstone extends Block
{
	public BlockRedSandstone(int par1, Material par2Material) 
	{
		super(par1, par2Material);
		this.setCreativeTab(EmasherCore.tabEmasher);

	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emashercore:redSandstone");
    }
}
