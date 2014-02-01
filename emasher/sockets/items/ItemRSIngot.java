package emasher.sockets.items;

import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class ItemRSIngot extends ItemFood
{
	public ItemRSIngot(int id)
	{
		super(id, 4, 4, false);
		this.setMaxStackSize(64);
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setUnlocalizedName("rsIngot");
		this.setAlwaysEdible();
		this.setPotionEffect(9, 30, 1, 1);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("sockets:ingotRed");
	}
	
}
