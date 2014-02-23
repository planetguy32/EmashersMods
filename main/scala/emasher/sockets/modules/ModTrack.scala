package emasher.sockets.modules

import emasher.api.{Util, SideConfig, SocketTileAccess, SocketModule}
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.item.{Item, ItemStack}
import emasher.sockets.SocketsMod
import net.minecraftforge.common.ForgeDirection
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import scala.collection.JavaConverters._
import emasher.sockets.pipes.TileDirectionChanger
import net.minecraft.block.Block

class ModTrack(id: Int) extends SocketModule(id, "sockets:trackUp", "sockets:trackLeft", "sockets:trackDown", "sockets:trackRight"){

  override def getLocalizedName: String = "Track"

  override def addRecipe(): Unit = {
    CraftingManager.getInstance().getRecipeList.asInstanceOf[java.util.List[Object]]
      .add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 2, moduleID), " m ", "iii",
      Character.valueOf('i'), Item.ingotIron,
      Character.valueOf('m'), new ItemStack(SocketsMod.blankSide)))
  }

  override def getCurrentTexture(config: SideConfig): Int = {
    config.meta
  }

  override def getToolTip(l: java.util.List[Object]): Unit = {
    l.add("Moves a socket horizontally in the set direction")
    l.add("on an internal redstone pulse")
  }

  override def getIndicatorKey(l: java.util.List[Object]): Unit = {
    l.add(SocketsMod.PREF_RED + "Activate")
    l.add(SocketsMod.PREF_WHITE + "Change Direction")
  }

  override def hasRSIndicator: Boolean = true

  override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection): Boolean = {
    side == ForgeDirection.DOWN
  }

  override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection): Unit = {
    config.meta += 1
    if(config.meta == 4) config.meta = 0
    ts.sendClientSideState(side.ordinal())
  }

  override def onRSInterfaceChange(config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean): Unit = {
    if(! on) return
    if(! config.rsControl(index)) return

    val nx = config.meta match {
      case 0 => ts.xCoord
      case 1 => ts.xCoord - 1
      case 2 => ts.xCoord
      case 3 => ts.xCoord + 1
    }

    val nz = config.meta match {
      case 0 => ts.zCoord - 1
      case 1 => ts.zCoord
      case 2 => ts.zCoord + 1
      case 3 => ts.zCoord
    }

    val bId = ts.worldObj.getBlockId(nx, ts.yCoord - 1, nz)
    val b = Block.blocksList(bId)
    val canMove = hasElevator(ts) || (b != null && b.isOpaqueCube)

    if(canMove) {
      val done = Util.moveBlock(ts.worldObj, ts.xCoord, ts.yCoord, ts.zCoord, nx, ts.yCoord, nz)
      val world = ts.worldObj
      val x = ts.xCoord
      val y = ts.yCoord
      val z = ts.zCoord

      if(done) {
        ts.dead = true
        val ents = world.getEntitiesWithinAABBExcludingEntity(null.asInstanceOf[Entity], AxisAlignedBB.getAABBPool.getAABB(x, y + 1, z, x + 1, y + 3, z + 1))
        for(e <- ents.asScala) {
            e.asInstanceOf[Entity].posX += (nx - x)
            e.asInstanceOf[Entity].posZ += (nz - z)
        }
        ts.worldObj.playSoundEffect(ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.out", 0.5F, ts.worldObj.rand.nextFloat() * 0.25F + 0.6F)
        ts.worldObj.playSoundEffect(ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.in", 0.5F, ts.worldObj.rand.nextFloat() * 0.25F + 0.6F)
      }
    }

  }

  override def onSocketPlaced(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection): Unit = {
      val t = ts.worldObj.getBlockTileEntity(ts.xCoord, ts.yCoord - 1, ts.zCoord)
      if(t != null && t.isInstanceOf[TileDirectionChanger]) {
        val td = t.asInstanceOf[TileDirectionChanger]
        td.directions(ForgeDirection.UP.ordinal()) match {
          case ForgeDirection.NORTH => config.meta = 0
          case ForgeDirection.SOUTH => config.meta = 2
          case ForgeDirection.EAST => config.meta = 3
          case ForgeDirection.WEST => config.meta = 1
          case _ =>
        }
      }
  }

  def hasElevator(ts: SocketTileAccess): Boolean = {
    for(i <- 0 to 5) {
      val d = ForgeDirection.getOrientation(i)
      if(ts.getSide(d).isInstanceOf[ModElevator]) return true
    }
    false
  }
}
