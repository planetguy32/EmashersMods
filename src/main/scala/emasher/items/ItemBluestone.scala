package emasher.items

import emasher.util.Tuple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

import scala.collection.mutable

class ItemBluestone( ) extends ItemEmasherGeneric( "eng_toolbox:bluestone", "bluestone" ) {

  override def onItemRightClick( itemStack: ItemStack, world: World, player: EntityPlayer ): ItemStack = {
    val position = getMovingObjectPositionFromPlayer( world, player, true )

    if( position != null ) {
      position.typeOfHit match {
        case MovingObjectPosition.MovingObjectType.ENTITY => itemStack
        case MovingObjectPosition.MovingObjectType.MISS => itemStack
        case MovingObjectPosition.MovingObjectType.BLOCK =>
          val x = position.blockX
          val y = position.blockY
          val z = position.blockZ

          val id = world.getBlock( x, y, z )

          if( id == emasher.blocks.Blocks.algae ) {
            val queue = new mutable.Queue[ Tuple ]
            var queueFilled = false
            val offsets = List( new Tuple( 1, 0, 0 ), new Tuple( -1, 0, 0 ), new Tuple( 0, 0, 1 ), new Tuple( 0, 0, -1 ) )
            queue.enqueue( new Tuple( x, y, z ) )

            while( queue.length > 0 ) {
              val curr = queue.dequeue( )
              val cX = curr.x
              val cY = curr.y
              val cZ = curr.z

              world.setBlock( cX, cY, cZ, emasher.blocks.Blocks.deadAlgae )

              if( !queueFilled ) {
                offsets.foreach { t =>
                  val bId = world.getBlock( cX + t.x, cY + t.y, cZ + t.z )
                  if( bId == emasher.blocks.Blocks.algae ) {
                    queue.enqueue( new Tuple( cX + t.x, cY + t.y, cZ + t.z ) )
                  }
                }
              }

              if( queue.length > 16 ) queueFilled = true
            }

            val result = itemStack.copy( )
            result.stackSize -= 1
            return result
          }

      }
    }

    itemStack
  }

}
