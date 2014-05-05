package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemEmasherGeneric extends Item
{
	String textureString;
	
	public ItemEmasherGeneric(int id, String texture, String name)
	{
		super(id);
		
		textureString = texture;
		
		this.setCreativeTab(EmasherCore.tabEmasher);
		
		this.setUnlocalizedName(name);
	}
	
	@Override
	public void registerIcons(IconRegister register)
	{
		itemIcon = register.registerIcon(textureString);
	}
}
