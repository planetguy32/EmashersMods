package emasher.gas.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.entity._
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive._
import net.minecraft.entity.monster._
import net.minecraft.util.DamageSource
import net.minecraft.util.MathHelper
import net.minecraft.item._
import net.minecraft.entity.player._
import emasher.gas.EmasherGas
import emasher.core.EmasherCore
import net.minecraft.block.Block
import net.minecraftforge.common.util.ForgeDirection
import scala.util.control.Breaks._
import net.minecraft.block.material.Material
import net.minecraft.item.crafting.FurnaceRecipes

class BlockPlasma(id: Int) extends BlockGasGeneric(id, 0, false)
{
	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister)
	{
		blockIcon = ir.registerIcon("gascraft:plasma")
	}
	
	override def onBlockAdded(world: World, x: Int, y: Int, z: Int)
	{
		if(world.isAirBlock(x, y - 1, z))
		{
			var ya: Int = y - 1
			while(world.isAirBlock(x, ya - 1, z)) ya -= 1
			
			if(y - ya < 24) world.setBlock(x, ya, z, Blocks.fire)
			
		}
		
		var i: Int = 0
		breakable {for(i <- 0 to 5)
		{
			val dir: ForgeDirection = ForgeDirection.getOrientation(i)
			val xo: Int = x + dir.offsetX
			val yo: Int = y + dir.offsetY
			val zo: Int = z + dir.offsetZ
			
			if(this.canDrain(world, x, y, z) && world.getBlock(xo, yo, zo) == Blocks.iron_block)
			{
				this.drain(world, x, y, z, true)
				world.setBlock(xo, yo, zo, EmasherCore.metal, 7, 3)
				break
			}
		}}
	}
	
	override def onEntityCollidedWithBlock(world: World, x: Int, y: Int, z: Int, ent: Entity)
	{
		if((! world.isRemote) && ent.isInstanceOf[EntityLivingBase])
		{
			var xe = ent.posX
			var ye = ent.posY
			var ze = ent.posZ
			var is:Array[ItemStack] = new Array[ItemStack](5)
			
			if(! ent.isInstanceOf[EntityPlayer]) 
			{
	            var f = 0.7F;
	            var d0 = (world.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
	            var d1 = (world.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
	            var d2 = (world.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
	            var entityitem = new EntityItem(world, xe.asInstanceOf[Double] + d0, ye.asInstanceOf[Double] + d1, ze.asInstanceOf[Double] + d2, new ItemStack(EmasherGas.ash));
	            entityitem.delayBeforeCanPickup = 1;
	            world.spawnEntityInWorld(entityitem);
	            
	            ent.setDead()
			}
			else
			{
				ent.setFire(10);
				ent.asInstanceOf[EntityLivingBase].attackEntityFrom(DamageSource.inFire, 3);
			}
				
				
		}
	}

  override def canDestroyBlock(block: Block, x: Int, y: Int, z: Int, world: World): Boolean = {
    val is: ItemStack = new ItemStack(block, 1, world.getBlockMetadata(x, y, z))
    var product: ItemStack = null
    if(is != null) {
      product = FurnaceRecipes.smelting.getSmeltingResult(is)
    }
    if(world.getTileEntity(x, y, z) != null) {
      false
    }else if(product != null){
      false
    } else {
      val mat = world.getBlock(x, y, z).getMaterial()

      if( mat != Material.lava && mat != Material.fire && block != Blocks.diamond_block && block != Blocks.emerald_block
        && block != Blocks.iron_block && block != EmasherCore.metal && mat != Material.iron) {
        true
      } else {
        false
      }
    }
  }
}