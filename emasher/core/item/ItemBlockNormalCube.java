package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;

public class ItemBlockNormalCube extends ItemBlock
{
	public ItemBlockNormalCube(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	public int getMetadata(int par1)
    {
        return par1;
    }
	
	 public String getUnlocalizedName(ItemStack itemstack)
	 {
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0:
			name = "litchenStone";
			break;
		case 1:
			name = "redSandstoneBricks";
			break;
		case 2:
			name = "limestoneBricks";
			break;
		case 3:
			name = "roadWay";
			break;
		case 4:
			name = "dirtyCobblestone";
			break;
		}
		return getUnlocalizedName() + "." + name;
	 }
	

}
