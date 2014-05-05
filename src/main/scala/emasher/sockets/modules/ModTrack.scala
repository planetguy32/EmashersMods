package emasher.sockets.modules

import emasher.api._
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.item.{Item, ItemStack}
import emasher.sockets.{Coords, UtilScala, SocketsMod}
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
      .add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 2, moduleID), "pmp", "iii",
      Character.valueOf('i'), Item.ingotIron,
      Character.valueOf('p'), Block.pistonBase,
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

    for(i <- 0 to 5) {
      val d = ForgeDirection.getOrientation(i)
      val m = ts.getSide(d)
      val c = ts.getConfigForSide(d)
      if(m.isInstanceOf[ModAccelerometer] && c.rsControl(index)) return
    }

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

    val dir = config.meta match {
      case 0 => ForgeDirection.NORTH
      case 1 => ForgeDirection.WEST
      case 2 => ForgeDirection.SOUTH
      case 3 => ForgeDirection.EAST
    }

    //val dir = ForgeDirection.getOrientation(config.meta + 2)

    val bId = ts.worldObj.getBlockId(nx, ts.yCoord - 1, nz)
    val b = Block.blocksList(bId)
    val canMove = hasElevator(ts) || (b != null && b.isOpaqueCube)

    if(canMove) {
      val world = ts.worldObj
      val x = ts.xCoord
      val y = ts.yCoord
      val z = ts.zCoord
      val done = UtilScala.moveGroup(world, Coords(x, y, z), dir)


      if(done) {
        ts.dead = true

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

  override def onAdjChange(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection) {
    onSocketPlaced(config, ts, side)
  }

  def hasElevator(ts: SocketTileAccess): Boolean = {
    for(i <- 0 to 5) {
      val d = ForgeDirection.getOrientation(i)
      if(ts.getSide(d).isInstanceOf[ModElevator]) return true
    }
    false
  }
}
