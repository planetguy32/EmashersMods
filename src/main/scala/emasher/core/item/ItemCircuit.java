package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemCircuit extends Item {
	public ItemCircuit() {
		super();
		this.setMaxStackSize( 64 );
		this.setCreativeTab( EmasherCore.tabEmasher );
		this.setUnlocalizedName( "circuit" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "emashercore:circuit" );
	}
	
}
