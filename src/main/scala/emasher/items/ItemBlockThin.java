package emasher.items;

import emasher.blocks.BlockThin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockThin extends ItemBlock {
	
	public ItemBlockThin( Block par1 ) {
		super( par1 );
		setHasSubtypes( true );
	}
	
	public int getMetadata( int par1 ) {
		return par1;
	}
	
	@Override
	public String getUnlocalizedName( ItemStack itemStack ) {
		String name = "";
		switch(itemStack.getItemDamage()) {
			case 0: {
				name = "chainFence";
				break;
			}
			case 1: {
				name = "chainPost";
				break;
			}
			case 2: {
				name = "barbWireFence";
				break;
			}
			case 3: {
				name = "barbPostWood";
				break;
			}
			case 4: {
				name = "barbPost";
				break;
			}
			default:
				name = "razorWireFence";
		}
		return getUnlocalizedName() + "." + name;
	}
	
	public IIcon getIconFromDamage( int par1 ) {
		IIcon result;

		switch(par1) {
			case 0:
				result = BlockThin.chainlink;
				break;
			case 1:
				result = BlockThin.chainPost;
				break;
			case 2:
				result = BlockThin.barb;
				break;
			case 3:
				result = BlockThin.barbPostWood;
				break;
			case 4:
				result = BlockThin.barbPost;
				break;
			default:
				result = BlockThin.razor;
		}

		return result;
	}
}
