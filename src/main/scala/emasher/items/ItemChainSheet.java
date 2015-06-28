package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemChainSheet extends Item {
	public ItemChainSheet() {
		super();
		maxStackSize = 64;
		
		this.setCreativeTab(EngineersToolbox.tabItems() );
		setUnlocalizedName( "chainSheet" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:chainSheet" );
	}
}
