package emasher.gas.block

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity._
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster._
import net.minecraft.entity.passive._
import net.minecraft.init.Items
import net.minecraft.item._
import net.minecraft.util.{DamageSource, MathHelper}
import net.minecraft.world.World


class BlockCorrosiveGas( ) extends BlockGasGeneric( 0, false ) {
  @SideOnly( Side.CLIENT )
  override def registerBlockIcons( ir: IIconRegister ) {
    blockIcon = ir.registerIcon( "gascraft:corrosiveGas" )
  }

  override def onEntityCollidedWithBlock( world: World, x: Int, y: Int, z: Int, ent: Entity ) {
    if( ( !world.isRemote ) && ent.isInstanceOf[ EntityLivingBase ] ) {
      val x = ent.posX
      val y = ent.posY
      val z = ent.posZ
      val is: Array[ ItemStack ] = new Array[ ItemStack ]( 5 )

      for( i <- 0 to 4 ) {
        is( i ) = ent.asInstanceOf[ EntityLivingBase ].getEquipmentInSlot( i )
        if( is( i ) != null ) is( i ) = is( i ).copy( )
      }

      if( ent.isInstanceOf[ EntityVillager ] || ent.isInstanceOf[ EntityZombie ] || ent.isInstanceOf[ EntityWitch ] ) {
        world.removeEntity( ent )

        val temp = EntityList.createEntityByName( "Skeleton", world ).asInstanceOf[ EntitySkeleton ]

        temp.setLocationAndAngles( x, y, z, MathHelper.wrapAngleTo180_float( world.rand.nextFloat( ) * 360.0F ), 0.0F )
        temp.rotationYawHead = temp.rotationYaw
        temp.renderYawOffset = temp.rotationYaw
        temp.onSpawnWithEgg( null )

        for( i <- 0 to 4 ) {
          temp.asInstanceOf[ EntityLivingBase ].setCurrentItemOrArmor( i, is( i ) )
        }

        if( ent.isInstanceOf[ EntityPigZombie ] ) {
          if( world.rand.nextInt( 4 ) == 0 ) temp.setSkeletonType( 1 )
        }

        world.spawnEntityInWorld( temp )
      }
      else if( ent.isInstanceOf[ EntityEnderman ] ) {
        world.removeEntity( ent )
        val f = 0.7F
        val d0 = ( world.rand.nextFloat( ) * f ).asInstanceOf[ Double ] + ( 1.0F - f ).asInstanceOf[ Double ] * 0.5D
        val d1 = ( world.rand.nextFloat( ) * f ).asInstanceOf[ Double ] + ( 1.0F - f ).asInstanceOf[ Double ] * 0.5D
        val d2 = ( world.rand.nextFloat( ) * f ).asInstanceOf[ Double ] + ( 1.0F - f ).asInstanceOf[ Double ] * 0.5D
        val entityitem = new EntityItem( world, x.asInstanceOf[ Double ] + d0, y.asInstanceOf[ Double ] + d1, z.asInstanceOf[ Double ] + d2, new ItemStack( Items.ender_pearl, 2 ) )
        entityitem.delayBeforeCanPickup = 1
        world.spawnEntityInWorld( entityitem )
      }
      else if( !ent.isInstanceOf[ EntitySkeleton ] ) {
        ent.attackEntityFrom( DamageSource.inFire, 2 )
      }

    }
  }

  override def canDestroyBlock( block: Block, x: Int, y: Int, z: Int, world: World ): Boolean = {
    if( world.getTileEntity( x, y, z ) != null ) {
      false
    } else {
      val mat = world.getBlock( x, y, z ).getMaterial( )

      if( mat == Material.cactus || mat == Material.cake || mat == Material.circuits || mat == Material.cloth || mat == Material.grass ||
        mat == Material.leaves || mat == Material.carpet || mat == Material.plants || mat == Material.gourd || mat == Material.vine ||
        mat == Material.web || mat == Material.wood ) {
        true
      } else {
        false
      }
    }
  }
}