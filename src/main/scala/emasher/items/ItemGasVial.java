package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemGasVial extends Item {
	public ItemGasVial() {
		super();
		
		this.setCreativeTab( EngineersToolbox.tabItems() );
		setMaxStackSize( 16 );
		setUnlocalizedName( "gasVial" );
	}
	
	public void registerIcons( IIconRegister registry ) {
		this.itemIcon = registry.registerIcon( "eng_toolbox:vial" );
	}
	
	
}
