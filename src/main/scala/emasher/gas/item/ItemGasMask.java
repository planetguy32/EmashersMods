package emasher.gas.item;

import emasher.gas.EmasherGas;
import net.minecraft.item.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import org.lwjgl.Sys;

public class ItemGasMask extends ItemArmor
{
	public ItemGasMask(ItemArmor.ArmorMaterial par2EnumArmorMaterial,
			int par3, int par4) {
		super(par2EnumArmorMaterial, par3, par4);
        this.setMaxDamage(256);
		this.setCreativeTab(EmasherGas.tabGasCraft);
		this.setUnlocalizedName("gasmask");

	}

//	@Override
//	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
//	{
//		return "emasher:gasmask.png";
//	}
	
	public void registerIcons(IIconRegister registry)
	{
		this.itemIcon = registry.registerIcon("gascraft:gasmask");
	}
}
