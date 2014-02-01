package emasher.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import emasher.core.EmasherCore;

public class BlockLimestone extends Block
{
	public BlockLimestone(int id) 
	{
		super(id, Material.rock);
		this.setCreativeTab(EmasherCore.tabEmasher);

	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("emashercore:limestone");
    }
}
