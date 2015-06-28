package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemCircuit extends Item {
	public ItemCircuit() {
		super();
		this.setMaxStackSize( 64 );
		this.setCreativeTab( EngineersToolbox.tabItems() );
		this.setUnlocalizedName( "circuit" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:circuit" );
	}
	
}
