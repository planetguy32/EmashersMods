package emasher.gas.item;

import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;

public class ItemBlockShaleResource extends ItemBlock
{
	public ItemBlockShaleResource(Block par1) {
		super(par1);
		this.setHasSubtypes(true);
	}

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
		case 0: name = "gas"; break;
		case 1: name = "oil"; break;
		case 2: name = "plasma"; break;
		}
		return getUnlocalizedName() + "." + name;
	}
}
