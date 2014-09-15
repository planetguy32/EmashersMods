package emasher.core.hemp;

import java.util.Random;

import cpw.mods.fml.common.IFuelHandler;
import emasher.core.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;

public class ItemHempOil extends Item implements IFuelHandler
{
	public ItemHempOil()
	{
		super();
		this.setCreativeTab(EmasherCore.tabEmasher);
		setUnlocalizedName("hempOil");
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("emashercore:hempSeedOil");
	}

	@Override
	public int getBurnTime(ItemStack fuel) 
	{
		if(fuel.getItem() == EmasherCore.hempOil)
		{
			return 500;
		}
		else
		{
			
			return 0;
		}
	}
	
	
}