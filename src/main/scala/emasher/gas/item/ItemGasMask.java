package emasher.gas.item;

import emasher.core.EmasherCore;
import emasher.gas.CommonProxy;
import emasher.gas.EmasherGas;
import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;
import net.minecraftforge.common.*;

public class ItemGasMask extends ItemArmor
{
	public ItemGasMask(ArmorMaterial par2EnumArmorMaterial,
			int par3, int par4) {
		super(par2EnumArmorMaterial, par3, par4);

		setCreativeTab(EmasherGas.tabGasCraft);
		this.setUnlocalizedName("gasmask");
		this.setMaxDamage(256);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
	{
		return "emasher:gasmask.png";
	}
	
	public void registerIcons(IIconRegister registry)
	{
		this.itemIcon = registry.registerIcon("gascraft:gasmask");
	}
}
