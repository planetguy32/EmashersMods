package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemFenceWire extends Item {
	public ItemFenceWire() {
		super();
		maxStackSize = 64;
		
		this.setCreativeTab( EngineersToolbox.tabItems() );
		setUnlocalizedName( "fenceWire" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:wire" );
	}
}
