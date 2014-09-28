package emasher.gas.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import emasher.core.EmasherCore;
import emasher.gas.CommonProxy;
import emasher.gas.EmasherGas;

import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;

import net.minecraftforge.common.*;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.*;

public class BlockNaturalGas extends BlockGasGeneric
{	
	public BlockNaturalGas(int ID)
    {
        super(ID, 0, true);
		//this.setCreativeTab(EmasherCore.tabEmasher);
    }
	
    
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
		this.blockIcon = ir.registerIcon("gascraft:naturalGas");
    }
	
    @Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity ent)
    {
    	if(! par1World.isRemote)
    	{	
			Random rand = par1World.rand;
			Item helmet = null;
			int helmetIndex = 3;
			if(ent instanceof EntityPlayer)
			{
				if(((EntityPlayer)ent).inventory.armorItemInSlot(helmetIndex) != null)
				{
					helmet = ((EntityPlayer)ent).inventory.armorItemInSlot(helmetIndex).getItem();
				}
			}
			
			if(ent instanceof EntityPlayer && helmet == EmasherGas.gasMask)
			{
				ItemStack helmStack = ((EntityPlayer)ent).inventory.armorItemInSlot(helmetIndex);
				
				if(rand.nextInt(10) == 0)
				{
					helmStack.damageItem(1, (EntityLivingBase)ent);
				}
				
				if(helmStack.getItemDamage() >= helmStack.getMaxDamage())
				{
					((EntityPlayer)ent).inventory.armorInventory[helmetIndex] = null;
				}
				
			}
			else if(ent instanceof EntityLivingBase)
			{
				ent.attackEntityFrom(DamageSource.drown, 1);
				((EntityLivingBase)ent).addPotionEffect(new PotionEffect(9, 500));
				((EntityLivingBase)ent).addPotionEffect(new PotionEffect(19, 500));
			}
    	}
		
    }
}