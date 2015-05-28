package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemEmasherGeneric extends Item {
	String textureString;
	
	public ItemEmasherGeneric( String texture, String name ) {
		super();
		
		textureString = texture;
		
		this.setCreativeTab( EmasherCore.tabEmasher );
		
		this.setUnlocalizedName( name );
	}
	
	@Override
	public void registerIcons( IIconRegister register ) {
		itemIcon = register.registerIcon( textureString );
	}
}
