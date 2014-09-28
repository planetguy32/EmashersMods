package emasher.sockets.items;

import emasher.sockets.SocketsMod;
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
	public ItemBlockPaintedWood(Block par1)
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
		return getUnlocalizedName() + "." + SocketsMod.colours[itemstack.getItemDamage()];
	}
	

}
