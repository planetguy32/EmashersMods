package emasher.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntitySmokeBomb extends EntityThrowable {
	
	public EntitySmokeBomb( World par1World, EntityLivingBase par2EntityLivingBase ) {
		super( par1World, par2EntityLivingBase );
	}

	public EntitySmokeBomb( World par1World ) {
		super( par1World );
	}

	public EntitySmokeBomb( World par1World, int x, int y, int z ) {
		super( par1World, x, y, z );
	}


	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	public void onImpact( MovingObjectPosition mop ) {
		if( !this.worldObj.isRemote ) {
			ForgeDirection d = ForgeDirection.UP;
			boolean set = false;

			if( worldObj.getBlock( mop.blockX, mop.blockY, mop.blockZ ) == Blocks.air ) {
				worldObj.setBlock( mop.blockX, mop.blockY, mop.blockZ, emasher.blocks.Blocks.smoke() );
			} else if( worldObj.getBlock( mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ ) == Blocks.air ) {
				worldObj.setBlock( mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ, emasher.blocks.Blocks.smoke() );
			} else {
				for( int i = 2; i < 6; i++ ) {
					d = ForgeDirection.getOrientation( i );
					if( worldObj.getBlock( mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ ) == Blocks.air ) {
						worldObj.setBlock( mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ, emasher.blocks.Blocks.smoke() );
						set = true;
						break;
					}
				}

				if( !set ) {
					d = ForgeDirection.DOWN;

					if( worldObj.getBlock( mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ ) == Blocks.air ) {
						worldObj.setBlock( mop.blockX + d.offsetX, mop.blockY + d.offsetY, mop.blockZ + d.offsetZ, emasher.blocks.Blocks.smoke() );
					}

				}
			}

			this.setDead();
		}
	}


}
