package emasher.defense;

import java.util.Random;

import emasher.core.EmasherCore;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemFenceWire extends Item
{
	public ItemFenceWire(int i)
	{
		super(i);
		maxStackSize = 64;
		
		this.setCreativeTab(EmasherDefense.tabDefense);
		//setIconIndex(1);
		setUnlocalizedName("fenceWire");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("EmasherDefense:wire");
	}
}
