package emasher.core.item;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;

public class ItemBlockOre extends ItemBlock
{
	public ItemBlockOre(Block b)
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
			name = "e_oreBauxite";
			break;
		case 1:
			name = "e_oreCassiterite";
			break;
		case 2:
			name = "e_oreEmery";
			break;
		case 3:
			name = "e_oreGalena";
			break;
		case 4:
			name = "e_oreNativeCopper";
			break;
		case 5:
			name = "e_orePentlandite";
			break;
		case 6:
			name = "e_oreRuby";
			break;
		case 7:
			name = "e_oreSapphire";
			break;
		}
		return getUnlocalizedName() + "." + name;
	 }
	

}
