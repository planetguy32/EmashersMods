package emasher.gas;

import cpw.mods.fml.common.Loader;
import emasher.defense.EmasherDefense;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class EntitySmokeBomb extends EntityThrowable
{
	
    public EntitySmokeBomb(World par1World, EntityLivingBase par2EntityLivingBase)
    {
		super(par1World, par2EntityLivingBase);
	}
    
    public EntitySmokeBomb(World par1World)
    {
		super(par1World);
	}
    
    public EntitySmokeBomb(World par1World, int x, int y, int z)
    {
		super(par1World, x, y, z);
	}
    

	/**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    public void onImpact(MovingObjectPosition mop)
    {
    	if (!this.worldObj.isRemote)
    	{
    		ForgeDirection d = ForgeDirection.UP;
    		boolean set = false;
    		
    		if(worldObj.getBlockId(mop.blockX, mop.blockY, mop.blockZ) == 0)
    		{
    			worldObj.setBlock(mop.blockX, mop.blockY, mop.blockZ, EmasherGas.smoke.blockID);
    		}
    		else if(worldObj.getBlockId(mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ) == 0)
    		{
    			worldObj.setBlock(mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ, EmasherGas.smoke.blockID);
    		}
    		else
    		{
    			for(int i = 2; i < 6; i++)
    			{
    				d = ForgeDirection.getOrientation(i);
    				if(worldObj.getBlockId(mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ) == 0)
    	    		{
    	    			worldObj.setBlock(mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ, EmasherGas.smoke.blockID);
    	    			set = true;
    	    			break;
    	    		}
    			}
    			
    			if(! set)
    			{
    				d = ForgeDirection.DOWN;
    				
    				if(worldObj.getBlockId(mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ) == 0)
    	    		{
    	    			worldObj.setBlock(mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ, EmasherGas.smoke.blockID);
    	    		}
    				
    			}
    		}
    			
    		this.setDead();
    	}
    }
    

}
