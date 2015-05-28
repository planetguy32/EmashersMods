package emasher.core.hemp;

import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemHempPlant extends Item {
	public ItemHempPlant() {
		super();
		maxStackSize = 64;
		this.setCreativeTab( EmasherCore.tabEmasher );
		setUnlocalizedName( "hempPlant" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "emashercore:hemp" );
	}
	
	
}