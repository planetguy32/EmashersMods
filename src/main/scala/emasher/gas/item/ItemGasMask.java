package emasher.gas.item;

import emasher.gas.EmasherGas;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemGasMask extends ItemArmor {
	public ItemGasMask( ItemArmor.ArmorMaterial par2EnumArmorMaterial,
						int par3, int par4 ) {
		super( par2EnumArmorMaterial, par3, par4 );
		this.setMaxDamage( 256 );
		this.setCreativeTab( EmasherGas.tabGasCraft );
		this.setUnlocalizedName( "gasmask" );

	}

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, int slot, String type ) {
		return "emasher:textures/models/armour/gasmask.png";
	}
	
	public void registerIcons( IIconRegister registry ) {
		this.itemIcon = registry.registerIcon( "gascraft:gasmask" );
	}
}
