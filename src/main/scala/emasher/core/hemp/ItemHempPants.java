package emasher.core.hemp;

import emasher.core.EmasherCore;
import emasher.core.CommonProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraftforge.common.*;

public class ItemHempPants extends ItemArmor
{
	public ItemHempPants(ArmorMaterial par2EnumArmorMaterial,
			int par3, int par4) {
		super(par2EnumArmorMaterial, par3, par4);
		
		this.setCreativeTab(EmasherCore.tabEmasher);
		//setIconIndex(48);
		setUnlocalizedName("hempPants");
	}

	/*@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
	{
		return "emasher:hemp_2.png";
	}*/
	
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("emashercore:hempPants");
	}
}
