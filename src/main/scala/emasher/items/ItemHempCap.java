package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class ItemHempCap extends ItemArmor {
	public ItemHempCap( ArmorMaterial par2EnumArmorMaterial,
						int par3, int par4 ) {
		super( par2EnumArmorMaterial, par3, par4 );

		this.setCreativeTab(EngineersToolbox.tabItems() );
		setUnlocalizedName( "hempCap" );
	}

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, int slot, String type ) {
		return "eng_toolbox:textures/models/armour/hemp_1.png";
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:hempCap" );
	}
}
