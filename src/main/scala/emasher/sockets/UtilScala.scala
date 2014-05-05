package emasher.sockets

import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection
import emasher.sockets.SocketsMod
import scala.collection.mutable.ListBuffer
import emasher.sockets.modules.ModMagnet
import scala.collection.mutable
import net.minecraft.block.Block
import emasher.sockets.pipes.{TileDirectionChanger, TileAdapterBase, TileFrame}
import net.minecraft.nbt.NBTTagCompound
import scala.util.control.Breaks._
import emasher.api.{Util, SocketTileAccess}

object UtilScala {
  def moveGroup(world: World, root: Coords, dir: ForgeDirection): Boolean = {
    val rb = world.getBlockId(root.x, root.y, root.z)
    if(rb == SocketsMod.socket.blockID) {
      val te = world.getBlockTileEntity(root.x, root.y, root.z)

      te match {
        case ts: SocketTileAccess =>
          val magnetDirs = getMagnetDirs(ts)

          if(magnetDirs.length == 0) {
            val nx = root.x + dir.offsetX
            val ny = root.y + dir.offsetY
            val nz = root.z + dir.offsetZ
            Util.moveBlock(world, root.x, root.y, root.z, nx, ny, nz)
          } else {
            val moveSet = new mutable.ListBuffer[Coords]()
            val nonNormalSet = new mutable.ListBuffer[NonNormalBlock]()
            val checkQueue = new mutable.Queue[Coords]()

            moveSet += root
            addToQueueForBlock(checkQueue, moveSet, root, world)

            while(checkQueue.length > 0) {
              val curr = checkQueue.dequeue()
              if(shouldBlockBeMovable(world, curr.x, curr.y, curr.z)) {
                moveSet += curr
                val bId = world.getBlockId(curr.x, curr.y, curr.z)
                val theBlock = Block.blocksList(bId)
                if(theBlock != null) {
                  if(! theBlock.isOpaqueCube) {
                    val t = world.getBlockTileEntity(curr.x, curr.y, curr.z)
                    val data = new NBTTagCompound()
                    if(t != null) t.writeToNBT(data)
                    val theBlockId = world.getBlockId(curr.x, curr.y, curr.z)
                    val theBlockMeta = world.getBlockMetadata(curr.x, curr.y, curr.z)
                    nonNormalSet += NonNormalBlock(curr, theBlockId, theBlockMeta, data)
                    world.removeBlockTileEntity(curr.x, curr.y, curr.z)
                    world.setBlock(curr.x, curr.y, curr.z, 0, 0, 3)
                  }
                }
                addToQueueForBlock(checkQueue, moveSet, curr, world)
              }
            }

            var failure = false
            breakable { for(el <- moveSet) {
              if(! Util.isBlockReplaceable(world, el.x + dir.offsetX, el.y + dir.offsetY, el.z + dir.offsetZ)) {
                var found = false
                moveSet.map { u =>
                  if(u.x == el.x + dir.offsetX && u.y == el.y + dir.offsetY && u.z == el.z + dir.offsetZ) {
                    found = true
                  }
                }

                if(! found) {
                  failure = true
                  break()
                }
              }
            }}

            if(! failure) {
              val ordering = getOrdering(dir)
              val sorted = moveSet.sorted(ordering)

              sorted.map { u =>
                Util.moveBlock(world, u.x, u.y, u.z, u.x + dir.offsetX, u.y + dir.offsetY, u.z + dir.offsetZ, false)
              }

              for(n <- nonNormalSet) {
                world.setBlock(n.t.x + dir.offsetX, n.t.y + dir.offsetY, n.t.z + dir.offsetZ, n.id, n.meta, 3)
                val tileEntity = world.getBlockTileEntity(n.t.x + dir.offsetX, n.t.y + dir.offsetY, n.t.z + dir.offsetZ)
                if(tileEntity != null && n.data != null) {
                  tileEntity.readFromNBT(n.data)
                  tileEntity.xCoord = n.t.x + dir.offsetX
                  tileEntity.yCoord = n.t.y + dir.offsetY
                  tileEntity.zCoord = n.t.z + dir.offsetZ
                }
              }

              sorted.map { u =>
                val te = world.getBlockTileEntity(u.x + dir.offsetX, u.y + dir.offsetY, u.z + dir.offsetZ)
                if(te != null && te.isInstanceOf[SocketTileAccess]) {
                  val ts = te.asInstanceOf[SocketTileAccess]
                  for(i <- 0 to 5) {
                    val side = ForgeDirection.getOrientation(i)
                    val m = ts.getSide(side)
                    m.onSocketPlaced(ts.getConfigForSide(side), ts, side)
                    ts.sendClientSideState(i)
                  }
                }

                if(te != null && te.isInstanceOf[TileAdapterBase]) {
                  val ta = te.asInstanceOf[TileAdapterBase]
                  for(i <- 0 to 5) {
                    PacketHandler.instance.sendClientAdapterSide(ta, i)
                  }
                }

                if(te != null && te.isInstanceOf[TileDirectionChanger]) {
                  val td = te.asInstanceOf[TileDirectionChanger]
                  for(i <- 0 to 5) {
                    PacketHandler.instance.sendClientChangerSide(td, i)
                  }
                }

              }
            } else {
              for(n <- nonNormalSet) {
                world.setBlock(n.t.x, n.t.y, n.t.z, n.id, n.meta, 3)
                val tileEntity = world.getBlockTileEntity(n.t.x, n.t.y, n.t.z)
                if(tileEntity != null && n.data != null) {
                  tileEntity.readFromNBT(n.data)
                }
              }
            }

            ! failure
          }

        case _ =>
          val nx = root.x + dir.offsetX
          val ny = root.y + dir.offsetY
          val nz = root.z + dir.offsetZ
          Util.moveBlock(world, root.x, root.y, root.z, nx, ny, nz)
      }
    } else {
      val nx = root.x + dir.offsetX
      val ny = root.y + dir.offsetY
      val nz = root.z + dir.offsetZ
      Util.moveBlock(world, root.x, root.y, root.z, nx, ny, nz)
    }

  }

  def getMagnetDirs(ts: SocketTileAccess): ListBuffer[ForgeDirection] = {
    val result = new ListBuffer[ForgeDirection]()

    for(i <- 0 to 5) {
      val d = ForgeDirection.getOrientation(i)
      val m = ts.getSide(d)
      if(m.isInstanceOf[ModMagnet]) {
        result.append(d)
      }
    }

    result
  }

  def getOrdering(dir: ForgeDirection): Ordering[Coords] = {
    (dir.offsetX, dir.offsetY, dir.offsetZ) match {
      case (-1, 0, 0) => new Ordering[Coords] {
        def compare(t1: Coords, t2: Coords):Int = {
          t1.x - t2.x
        }
      }
      case (1, 0, 0) => new Ordering[Coords] {
        def compare(t1: Coords, t2: Coords):Int = {
          t2.x - t1.x
        }
      }
      case (0, -1, 0) => new Ordering[Coords] {
        def compare(t1: Coords, t2: Coords):Int = {
          t1.y - t2.y
        }
      }
      case (0, 1, 0) => new Ordering[Coords] {
        def compare(t1: Coords, t2: Coords):Int = {
          t2.y - t1.y
        }
      }
      case (0, 0, -1) => new Ordering[Coords] {
        def compare(t1: Coords, t2: Coords):Int = {
          t1.z - t2.z
        }
      }
      case (0, 0, 1) => new Ordering[Coords] {
        def compare(t1: Coords, t2: Coords):Int = {
          t2.z - t1.z
        }
      }
    }
  }

  def shouldBlockBeMovable(world: World, x: Int, y: Int, z: Int): Boolean = {
    val id: Int = world.getBlockId(x, y, z)
    if(id == SocketsMod.miniPortal.blockID) return false
    val b: Block = Block.blocksList(id)
    !(b != null && b.blockHardness < 0)
  }

  def addToQueueForBlock(q: mutable.Queue[Coords], s: mutable.ListBuffer[Coords], t: Coords, world: World) {

    val te = world.getBlockTileEntity(t.x, t.y, t.z)
    te match {
      case tile: SocketTileAccess =>

        for(i <- 0 to 5) {
          val d = ForgeDirection.getOrientation(i)
          val m = tile.getSide(d)

          if(m.isInstanceOf[ModMagnet]) {
            val config = tile.getConfigForSide(d)
            var allOff = true
            var isOn = false

            for(j <- 0 to 2) {
              if(config.rsControl(j)) {
                allOff = false
                if(tile.getRSControl(j)) isOn = true
              }

              if(config.rsLatch(j)) {
                allOff = false
                if(tile.getRSLatch(j)) isOn = true
              }
            }

            if(isOn || allOff) {
              val xo = t.x + d.offsetX
              val yo = t.y + d.offsetY
              val zo = t.z + d.offsetZ

              if(! Util.isBlockReplaceable(world, xo, yo, zo) && ! s.contains(Coords(xo, yo, zo))) {
                q.enqueue(Coords(xo, yo, zo))
              }
            }
          }
        }

      case tile: TileFrame =>

        for(i <- 0 to 5) {
          val d = ForgeDirection.getOrientation(i)
          val isOn = tile.outputs(i)
          val xo = t.x + d.offsetX
          val yo = t.y + d.offsetY
          val zo = t.z + d.offsetZ

          if(isOn || world.getBlockId(xo, yo, zo) == SocketsMod.frame.blockID) {
            if(! Util.isBlockReplaceable(world, xo, yo, zo) && ! s.contains(Coords(xo, yo, zo))) {
              q.enqueue(Coords(xo, yo, zo))
            }
          }
        }

      case _ =>
    }


  }
}

case class Coords(x: Int, y: Int, z: Int)

case class NonNormalBlock(t: Coords, id: Int, meta: Int, data: NBTTagCompound)