package emasher.core.item

import net.minecraft.item.{ItemStack, Item}
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumMovingObjectType
import emasher.core.{Tuple, EmasherCore}
import scala.collection.mutable

class ItemBluestone(id: Int) extends ItemEmasherGeneric(id, "emashercore:bluestone", "bluestone") {

  override def onItemRightClick(itemStack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    val position = getMovingObjectPositionFromPlayer(world, player, true)

    if(position != null) {
      position.typeOfHit match {
        case EnumMovingObjectType.TILE =>
          val x = position.blockX
          val y = position.blockY
          val z = position.blockZ

          val id = world.getBlockId(x, y, z)

          if(id == EmasherCore.algae.blockID) {
            val queue = new mutable.Queue[Tuple]
            var queueFilled = false
            val offsets = List(new Tuple(1, 0, 0), new Tuple(-1, 0, 0), new Tuple(0, 0, 1), new Tuple(0, 0, -1))
            queue.enqueue(new Tuple(x, y, z))

            while(queue.length > 0) {
              val curr = queue.dequeue()
              val cX = curr.x
              val cY = curr.y
              val cZ = curr.z

              world.setBlock(cX, cY, cZ, EmasherCore.deadAlgae.blockID)

              if(! queueFilled) {
                offsets.map { t =>
                  val bId = world.getBlockId(cX + t.x, cY + t.y, cZ + t.z)
                  if(bId == EmasherCore.algae.blockID) {
                    queue.enqueue(new Tuple(cX + t.x, cY + t.y, cZ + t.z))
                  }
                }
              }

              if(queue.length > 16) queueFilled = true
            }

            val result = itemStack.copy()
            result.stackSize -= 1
            return result
          }

      }
    }

    itemStack
  }

}
