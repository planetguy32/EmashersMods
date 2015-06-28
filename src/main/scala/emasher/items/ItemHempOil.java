package emasher.items;

import cpw.mods.fml.common.IFuelHandler;
import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHempOil extends Item implements IFuelHandler {
	public ItemHempOil() {
		super();
		this.setCreativeTab( EngineersToolbox.tabItems() );
		setUnlocalizedName( "hempOil" );
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "eng_toolbox:hempSeedOil" );
	}

	@Override
	public int getBurnTime( ItemStack fuel ) {
		if( fuel.getItem() == emasher.items.Items.hempOil() ) {
			return 500;
		} else {
			
			return 0;
		}
	}
	
	
}