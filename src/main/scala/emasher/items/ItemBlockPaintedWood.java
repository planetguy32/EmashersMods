package emasher.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPaintedWood extends ItemBlock {
	public ItemBlockPaintedWood( Block par1 ) {
		super( par1 );
		setHasSubtypes( true );
	}
	
	public int getMetadata( int par1 ) {
		return par1;
	}
	
	@Override
	public String getUnlocalizedName( ItemStack itemstack ) {
		return getUnlocalizedName() + "." + emasher.items.Items.colours()[itemstack.getItemDamage()];
	}
	

}
