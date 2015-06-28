package emasher.items;

import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemFood;

public class ItemRSIngot extends ItemFood {
	public ItemRSIngot() {
		super( 4, 4, false );
		this.setMaxStackSize( 64 );
		this.setCreativeTab( EngineersToolbox.tabItems() );
		this.setUnlocalizedName( "rsIngot" );
		this.setAlwaysEdible();
		this.setPotionEffect( 9, 30, 1, 1 );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:ingotRed" );
	}
	
}
