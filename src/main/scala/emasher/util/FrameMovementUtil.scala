package emasher.util

import emasher.EngineersToolbox
import emasher.api.{SocketTileAccess, Util}
import emasher.modules.ModMagnet
import emasher.packethandling.{AdapterSideMessage, ChangerSideMessage}
import emasher.tileentities.{TileAdapterBase, TileDirectionChanger, TileFrame}
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._

object FrameMovementUtil {
  def moveGroup( world: World, root: Coords, dir: ForgeDirection ): Boolean = {
    val rb = world.getBlock( root.x, root.y, root.z )
    if( rb == emasher.blocks.Blocks.socket ) {
      val te = world.getTileEntity( root.x, root.y, root.z )

      te match {
        case ts: SocketTileAccess =>
          val magnetDirs = getMagnetDirs( ts )

          if( magnetDirs.isEmpty ) {
            val nx = root.x + dir.offsetX
            val ny = root.y + dir.offsetY
            val nz = root.z + dir.offsetZ
            Util.moveBlock( world, root.x, root.y, root.z, nx, ny, nz )
          } else {
            val moveSet = new mutable.ListBuffer[ Coords ]( )
            val nonNormalSet = new mutable.ListBuffer[ NonNormalBlock ]( )
            val checkQueue = new mutable.Queue[ Coords ]( )

            moveSet += root
            addToQueueForBlock( checkQueue, moveSet, root, world )

            while( checkQueue.nonEmpty ) {
              val curr = checkQueue.dequeue( )
              if( shouldBlockBeMovable( world, curr.x, curr.y, curr.z ) ) {
                moveSet += curr
                val theBlock = world.getBlock( curr.x, curr.y, curr.z )
                if( theBlock != null ) {
                  if( !theBlock.isOpaqueCube ) {
                    val t = world.getTileEntity( curr.x, curr.y, curr.z )
                    val data = new NBTTagCompound( )
                    if( t != null ) t.writeToNBT( data )
                    val theBlock = world.getBlock( curr.x, curr.y, curr.z )
                    val theBlockMeta = world.getBlockMetadata( curr.x, curr.y, curr.z )
                    nonNormalSet += NonNormalBlock( curr, theBlock, theBlockMeta, data )
                    world.removeTileEntity( curr.x, curr.y, curr.z )
                    world.setBlock( curr.x, curr.y, curr.z, Blocks.air, 0, 3 )
                  }
                }
                addToQueueForBlock( checkQueue, moveSet, curr, world )
              }
            }

            var failure = false
            breakable {
              for( el <- moveSet ) {
                if( !Util.isBlockReplaceable( world, el.x + dir.offsetX, el.y + dir.offsetY, el.z + dir.offsetZ ) ) {
                  var found = false
                  moveSet.foreach{ u =>
                    if( u.x == el.x + dir.offsetX && u.y == el.y + dir.offsetY && u.z == el.z + dir.offsetZ ) {
                      found = true
                    }
                  }

                  if( !found ) {
                    failure = true
                    break( )
                  }
                }
              }
            }

            if( !failure ) {
              val ordering = getOrdering( dir )
              val sorted = moveSet.sorted( ordering )

              sorted.foreach { u =>
                Util.moveBlock( world, u.x, u.y, u.z, u.x + dir.offsetX, u.y + dir.offsetY, u.z + dir.offsetZ, false )
              }

              for( n <- nonNormalSet ) {
                world.setBlock( n.t.x + dir.offsetX, n.t.y + dir.offsetY, n.t.z + dir.offsetZ, n.block, n.meta, 3 )
                val tileEntity = world.getTileEntity( n.t.x + dir.offsetX, n.t.y + dir.offsetY, n.t.z + dir.offsetZ )
                if( tileEntity != null && n.data != null ) {
                  tileEntity.readFromNBT( n.data )
                  tileEntity.xCoord = n.t.x + dir.offsetX
                  tileEntity.yCoord = n.t.y + dir.offsetY
                  tileEntity.zCoord = n.t.z + dir.offsetZ
                }
              }

              sorted.foreach { u =>
                val te = world.getTileEntity( u.x + dir.offsetX, u.y + dir.offsetY, u.z + dir.offsetZ )
                if( te != null && te.isInstanceOf[ SocketTileAccess ] ) {
                  val ts = te.asInstanceOf[ SocketTileAccess ]
                  for( i <- 0 to 5 ) {
                    val side = ForgeDirection.getOrientation( i )
                    val m = ts.getSide( side )
                    m.onSocketPlaced( ts.getConfigForSide( side ), ts, side )
                    ts.sendClientSideState( i )
                  }
                }

                if( te != null && te.isInstanceOf[ TileAdapterBase ] ) {
                  val ta = te.asInstanceOf[ TileAdapterBase ]
                  for( i <- 0 to 5 ) {
                    EngineersToolbox.network.sendToDimension( new AdapterSideMessage( ta, i.asInstanceOf[ Byte ] ), world.provider.dimensionId )
                  }
                }

                if( te != null && te.isInstanceOf[ TileDirectionChanger ] ) {
                  val td = te.asInstanceOf[ TileDirectionChanger ]
                  for (i <- 0 to 5) {
                    EngineersToolbox.network.sendToDimension( new ChangerSideMessage(td, i.asInstanceOf[ Byte ] ), world.provider.dimensionId )
                  }
                }

              }
            } else {
              for( n <- nonNormalSet ) {
                world.setBlock( n.t.x, n.t.y, n.t.z, n.block, n.meta, 3 )
                val tileEntity = world.getTileEntity( n.t.x, n.t.y, n.t.z )
                if( tileEntity != null && n.data != null ) {
                  tileEntity.readFromNBT( n.data )
                }
              }
            }

            !failure
          }

        case _ =>
          val nx = root.x + dir.offsetX
          val ny = root.y + dir.offsetY
          val nz = root.z + dir.offsetZ
          Util.moveBlock( world, root.x, root.y, root.z, nx, ny, nz )
      }
    } else {
      val nx = root.x + dir.offsetX
      val ny = root.y + dir.offsetY
      val nz = root.z + dir.offsetZ
      Util.moveBlock( world, root.x, root.y, root.z, nx, ny, nz )
    }

  }

  def getMagnetDirs( ts: SocketTileAccess ): ListBuffer[ ForgeDirection ] = {
    val result = new ListBuffer[ ForgeDirection ]( )

    for( i <- 0 to 5 ) {
      val d = ForgeDirection.getOrientation( i )
      val m = ts.getSide( d )
      if( m.isInstanceOf[ ModMagnet ] ) {
        result.append( d )
      }
    }

    result
  }

  def getOrdering( dir: ForgeDirection ): Ordering[ Coords ] = {
    (dir.offsetX, dir.offsetY, dir.offsetZ) match {
      case (-1, 0, 0) => new Ordering[ Coords ] {
        def compare( t1: Coords, t2: Coords ): Int = {
          t1.x - t2.x
        }
      }
      case (1, 0, 0) => new Ordering[ Coords ] {
        def compare( t1: Coords, t2: Coords ): Int = {
          t2.x - t1.x
        }
      }
      case (0, -1, 0) => new Ordering[ Coords ] {
        def compare( t1: Coords, t2: Coords ): Int = {
          t1.y - t2.y
        }
      }
      case (0, 1, 0) => new Ordering[ Coords ] {
        def compare( t1: Coords, t2: Coords ): Int = {
          t2.y - t1.y
        }
      }
      case (0, 0, -1) => new Ordering[ Coords ] {
        def compare( t1: Coords, t2: Coords ): Int = {
          t1.z - t2.z
        }
      }
      case (0, 0, 1) => new Ordering[ Coords ] {
        def compare( t1: Coords, t2: Coords ): Int = {
          t2.z - t1.z
        }
      }
    }
  }

  def shouldBlockBeMovable( world: World, x: Int, y: Int, z: Int ): Boolean = {
    val b: Block = world.getBlock( x, y, z )
    if( emasher.blocks.Blocks.miniPortal != null && b == emasher.blocks.Blocks.miniPortal ) return false
    !( b != null && b.getBlockHardness( world, x, y, z ) < 0 )
  }

  def addToQueueForBlock( q: mutable.Queue[ Coords ], s: mutable.ListBuffer[ Coords ], t: Coords, world: World ) {

    val te = world.getTileEntity( t.x, t.y, t.z )
    te match {
      case tile: SocketTileAccess =>

        for( i <- 0 to 5 ) {
          val d = ForgeDirection.getOrientation( i )
          val m = tile.getSide( d )

          if( m.isInstanceOf[ ModMagnet ] ) {
            val config = tile.getConfigForSide( d )
            var allOff = true
            var isOn = false

            for( j <- 0 to 2 ) {
              if( config.rsControl( j ) ) {
                allOff = false
                if( tile.getRSControl( j ) ) isOn = true
              }

              if( config.rsLatch( j ) ) {
                allOff = false
                if( tile.getRSLatch( j ) ) isOn = true
              }
            }

            if( isOn || allOff ) {
              val xo = t.x + d.offsetX
              val yo = t.y + d.offsetY
              val zo = t.z + d.offsetZ

              if( !Util.isBlockReplaceable( world, xo, yo, zo ) && !s.contains( Coords( xo, yo, zo ) ) ) {
                q.enqueue( Coords( xo, yo, zo ) )
              }
            }
          }
        }

      case tile: TileFrame =>

        for( i <- 0 to 5 ) {
          val d = ForgeDirection.getOrientation( i )
          val isOn = tile.outputs( i )
          val xo = t.x + d.offsetX
          val yo = t.y + d.offsetY
          val zo = t.z + d.offsetZ

          if( isOn || world.getBlock( xo, yo, zo ) == emasher.blocks.Blocks.frame ) {
            if( !Util.isBlockReplaceable( world, xo, yo, zo ) && !s.contains( Coords( xo, yo, zo ) ) ) {
              q.enqueue( Coords( xo, yo, zo ) )
            }
          }
        }

      case _ =>
    }


  }
}

case class Coords( x: Int, y: Int, z: Int )

case class NonNormalBlock( t: Coords, block: Block, meta: Int, data: NBTTagCompound )