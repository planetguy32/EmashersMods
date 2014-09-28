package emasher.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import emasher.core.EmasherCore;

public class BlockRedSandstone extends Block
{
	public BlockRedSandstone(Material par2Material)
	{
		super(par2Material);
		this.setCreativeTab(EmasherCore.tabEmasher);

	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emashercore:redSandstone");
    }
}
