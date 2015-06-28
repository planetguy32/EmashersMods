package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemHempPlant extends Item {
	public ItemHempPlant() {
		super();
		maxStackSize = 64;
		this.setCreativeTab( EngineersToolbox.tabItems() );
		setUnlocalizedName( "hempPlant" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:hemp" );
	}
	
	
}