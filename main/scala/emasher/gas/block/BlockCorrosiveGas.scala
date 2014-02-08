package emasher.gas.block

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraft.entity._;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive._;
import net.minecraft.entity.monster._;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.item._
import net.minecraft.block.material.Material
import net.minecraft.block.Block
import emasher.core.EmasherCore
;

class BlockCorrosiveGas(id: Int) extends BlockGasGeneric(id, 0, false)
{
	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IconRegister)
	{
		blockIcon = ir.registerIcon("gascraft:corrosiveGas");
	}
	
	override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, ent: Entity)
	{
		if((! world.isRemote) && ent.isInstanceOf[EntityLivingBase])
		{
			var x = ent.posX;
			var y = ent.posY;
			var z = ent.posZ;
			var is:Array[ItemStack] = new Array[ItemStack](5);
			
			for(i <- 0 to 4)
			{
				is(i) = ent.asInstanceOf[EntityLivingBase].getCurrentItemOrArmor(i);
				if(is(i) != null) is(i) = is(i).copy();
			}
			
			if(ent.isInstanceOf[EntityVillager] || ent.isInstanceOf[EntityZombie] || ent.isInstanceOf[EntityWitch])
			{
				world.removeEntity(ent);
				
				var temp = EntityList.createEntityByName("Skeleton", world).asInstanceOf[EntitySkeleton];
				
				temp.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
				temp.rotationYawHead = temp.rotationYaw;
				temp.renderYawOffset = temp.rotationYaw;
				temp.onSpawnWithEgg(null);
				
				for(i <- 0 to 4)
				{
					temp.asInstanceOf[EntityLivingBase].setCurrentItemOrArmor(i, is(i));
				}
				
				if(ent.isInstanceOf[EntityPigZombie])
				{
					if(world.rand.nextInt(4) == 0) temp.setSkeletonType(1);
				}
				
				world.spawnEntityInWorld(temp);
			}
			else if(ent.isInstanceOf[EntityEnderman])
			{
				world.removeEntity(ent);
				var f = 0.7F;
				var d0 = (world.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
				var d1 = (world.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
				var d2 = (world.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
				var entityitem = new EntityItem(world, x.asInstanceOf[Double] + d0, y.asInstanceOf[Double] + d1, z.asInstanceOf[Double] + d2, new ItemStack(Item.enderPearl, 2));
				entityitem.delayBeforeCanPickup = 1;
				world.spawnEntityInWorld(entityitem);
			}
			else if(! ent.isInstanceOf[EntitySkeleton])
			{
				ent.attackEntityFrom(DamageSource.inFire, 2);
			}
			
		}
	}

  override def canDestroyBlock(blockID: Int, x: Int, y: Int, z: Int, world: World): Boolean = {
    if(world.getBlockTileEntity(x, y, z) != null) {
      false
    } else {
      val mat = world.getBlockMaterial(x, y, z)

      if(mat == Material.cactus || mat == Material.cake || mat == Material.circuits || mat == Material.cloth || mat == Material.grass ||
          mat == Material.leaves || mat == Material.materialCarpet || mat == Material.plants || mat == Material.pumpkin || mat == Material.vine ||
          mat == Material.web || mat == Material.wood) {
        true
      } else {
        false
      }
    }
  }
}