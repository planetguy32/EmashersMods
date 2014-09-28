package emasher.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import emasher.core.EmasherCore;

public class BlockLimestone extends Block
{
	public BlockLimestone()
	{
		super(Material.rock);
		this.setCreativeTab(EmasherCore.tabEmasher);

	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emashercore:limestone");
    }
}
