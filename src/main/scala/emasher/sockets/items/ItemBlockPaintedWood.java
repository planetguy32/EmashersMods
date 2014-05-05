package emasher.sockets.items;

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

public class ItemBlockPaintedWood extends ItemBlock
{
	public ItemBlockPaintedWood(int par1)
	{
		super(par1);
		setHasSubtypes(true);
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
		case 0: name = "blackPlanks"; break;
		case 1: name = "redPlanks"; break;
		case 2: name = "greenPlanks"; break;
		case 3: name = "brownPlanks"; break;
		case 4: name = "bluePlanks"; break;
		case 5: name = "purplePlanks"; break;
		case 6: name = "cyanPlanks"; break;
		case 7: name = "lightGrayPlanks"; break;
		case 8: name = "grayPlanks"; break;
		case 9: name = "pinkPlanks"; break;
		case 10: name = "limePlanks"; break;
		case 11: name = "yellowPlanks"; break;
		case 12: name = "lightBluePlanks"; break;
		case 13: name = "magentaPlanks"; break;
		case 14: name = "orangePlanks"; break;
		case 15: name = "whitePlanks"; break;
		}
		return getUnlocalizedName() + "." + name;
	}
	

}
