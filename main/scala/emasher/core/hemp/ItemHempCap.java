package emasher.core.hemp;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraftforge.common.*;
import emasher.core.*;

public class ItemHempCap extends ItemArmor
{
	public ItemHempCap(ArmorMaterial par2EnumArmorMaterial,
			int par3, int par4) {
		super(par2EnumArmorMaterial, par3, par4);

		this.setCreativeTab(EmasherCore.tabEmasher);
		setUnlocalizedName("hempCap");
	}

	/*@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
	{
		return "emasher:hemp_1.png";
	}*/
	
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("emashercore:hempCap");
	}
}
