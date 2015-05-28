package emasher.gas.item;

import emasher.gas.EmasherGas;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemGasVial extends Item {
	public ItemGasVial() {
		super();
		
		this.setCreativeTab( EmasherGas.tabGasCraft );
		setMaxStackSize( 16 );
		setUnlocalizedName( "gasVial" );
	}
	
	public void registerIcons( IIconRegister registry ) {
		this.itemIcon = registry.registerIcon( "gascraft:vial" );
	}
	
	
}
