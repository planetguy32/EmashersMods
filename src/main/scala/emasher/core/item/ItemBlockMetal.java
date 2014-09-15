package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;

public class ItemBlockMetal extends ItemBlock
{
	public ItemBlockMetal(Block b)
	{
		super(b);
		setHasSubtypes(true);
	}

    @Override
	public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
	 public String getUnlocalizedName(ItemStack itemstack)
	 {
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0:
			name = "e_blockAluminium";
			break;
		case 1:
			name = "e_blockBronze";
			break;
		case 2:
			name = "e_blockCopper";
			break;
		case 3:
			name = "e_blockLead";
			break;
		case 4:
			name = "e_blockNickel";
			break;
		case 5:
			name = "e_blockPlatinum";
			break;
		case 6:
			name = "e_blockSilver";
			break;
		case 7:
			name = "e_blockSteel";
			break;
		case 8:
			name = "e_blockTin";
			break;
		}
		return getUnlocalizedName() + "." + name;
	 }
	

}
