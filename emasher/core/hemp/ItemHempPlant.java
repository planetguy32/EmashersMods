package emasher.core.hemp;

import java.util.Random;

import emasher.core.EmasherCore;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.src.*;

public class ItemHempPlant extends Item
{
	public ItemHempPlant(int i)
	{
		super(i);
		maxStackSize = 64;
		this.setCreativeTab(EmasherCore.tabEmasher);
		//setIconIndex(3);
		setUnlocalizedName("hempPlant");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("emashercore:hemp");
	}
	
	
}