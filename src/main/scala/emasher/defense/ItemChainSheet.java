package emasher.defense;

import java.util.Random;

import emasher.core.EmasherCore;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemChainSheet extends Item
{
	public ItemChainSheet(int i)
	{
		super();
		maxStackSize = 64;
		
		this.setCreativeTab(EmasherDefense.tabDefense);
		//setIconIndex(0);
		setUnlocalizedName("chainSheet");
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("EmasherDefense:chainSheet");
	}
}
