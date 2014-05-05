package emasher.core.block;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockMachine extends Block
{

	public BlockMachine(int id)
	{
		super(id, Material.iron);
		this.setCreativeTab(EmasherCore.tabEmasher);
	}
	
	@Override
	public void registerIcons(IconRegister register)
	{
		super.registerIcons(register);
		this.blockIcon = register.registerIcon("emashercore:machine");
	}
	
}
