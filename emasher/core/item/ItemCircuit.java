package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCircuit extends Item
{
	public ItemCircuit(int id)
	{
		super(id);
		this.setMaxStackSize(64);
		this.setCreativeTab(EmasherCore.tabEmasher);
		this.setUnlocalizedName("circuit");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("emashercore:circuit");
	}
	
}
