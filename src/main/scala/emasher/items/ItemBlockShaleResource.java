package emasher.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockShaleResource extends ItemBlock {
	public ItemBlockShaleResource( Block par1 ) {
		super( par1 );
		this.setHasSubtypes( true );
	}

	public int getMetadata( int par1 ) {
		return par1;

	}
	
	@Override
	public String getUnlocalizedName( ItemStack itemstack ) {
		String name = "";
		switch(itemstack.getItemDamage()) {
			case 0:
				name = "gas";
				break;
			case 1:
				name = "oil";
				break;
			case 2:
				name = "plasma";
				break;
		}
		return getUnlocalizedName() + "." + name;
	}
}
