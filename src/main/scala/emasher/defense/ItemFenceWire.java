package emasher.defense;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemFenceWire extends Item {
	public ItemFenceWire() {
		super();
		maxStackSize = 64;
		
		this.setCreativeTab( EmasherDefense.tabDefense );
		//setIconIndex(1);
		setUnlocalizedName( "fenceWire" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "EmasherDefense:wire" );
	}
}
