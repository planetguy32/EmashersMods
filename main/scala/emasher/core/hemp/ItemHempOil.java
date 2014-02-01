package emasher.core.hemp;

import java.util.Random;

import cpw.mods.fml.common.IFuelHandler;
import emasher.core.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;

public class ItemHempOil extends Item implements IFuelHandler
{
	public ItemHempOil(int i)
	{
		super(i);
		maxStackSize = 64;
		
		this.setCreativeTab(EmasherCore.tabEmasher);
		//setIconIndex(2);
		setUnlocalizedName("hempOil");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("emashercore:hempSeedOil");
	}

	@Override
	public int getBurnTime(ItemStack fuel) 
	{
		if(fuel.itemID == EmasherCore.hempOil.itemID)
		{
			return 500;
		}
		else
		{
			
			return 0;
		}
	}
	
	
}