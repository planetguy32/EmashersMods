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

class ModTrack(id: Int) extends SocketModule(id, "sockets:trackUp", "sockets:trackRight", "sockets:trackDown", "sockets:trackLeft"){

  override def getLocalizedName: String = "Track"

  override def addRecipe(): Unit = {
    CraftingManager.getInstance().getRecipeList.asInstanceOf[java.util.List[Object]]
      .add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 2, moduleID), "gmg",
      Character.valueOf('g'), Item.ghastTear,
      Character.valueOf('m'), new ItemStack(SocketsMod.blankSide)))
  }

  override def getCurrentTexture(config: SideConfig): Int = {
    if(config.rsLatch(0)) {
      1
    } else {
      0
    }
  }

  override def getToolTip(l: java.util.List[Object]): Unit = {
    l.add("Makes the socket move up or down when activated")
    l.add("with an internal redstone pulse")
    l.add("Also allows a track to move a socket through the air")
  }

  override def getIndicatorKey(l: java.util.List[Object]): Unit = {
    l.add(SocketsMod.PREF_RED + "Activate")
    l.add(SocketsMod.PREF_WHITE + "Change Mode")
  }

  override def hasRSIndicator: Boolean = true

  override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection): Boolean = {
    side != ForgeDirection.UP && side != ForgeDirection.DOWN
  }

  override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection): Unit = {
    config.rsLatch(0) = ! config.rsLatch(0)
    ts.sendClientSideState(side.ordinal())
  }

  override def onRSInterfaceChange(config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean): Unit = {
    if(! on) return
    if(! config.rsControl(index)) return

    val ny = config.rsLatch(0) match {
      case true => ts.yCoord - 1
      case false => ts.yCoord + 1
    }

    val done = Util.moveBlock(ts.worldObj, ts.xCoord, ts.yCoord, ts.zCoord, ts.xCoord, ny, ts.zCoord)
    val world = ts.worldObj
    val x = ts.xCoord
    val y = ts.yCoord
    val z = ts.zCoord

    if(done) {
      ts.dead = true
      val ents = world.getEntitiesWithinAABBExcludingEntity(null.asInstanceOf[Entity], AxisAlignedBB.getAABBPool.getAABB(x, y + 1, z, x + 1, y + 3, z + 1))
      for(e <- ents.asScala) {
        config.rsLatch(0) match {
          case true => e.asInstanceOf[Entity].posY -= 1
          case false => e.asInstanceOf[Entity].posY += 1
        }
      }
      ts.worldObj.playSoundEffect(ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.out", 0.5F, ts.worldObj.rand.nextFloat() * 0.25F + 0.6F)
      ts.worldObj.playSoundEffect(ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.in", 0.5F, ts.worldObj.rand.nextFloat() * 0.25F + 0.6F)
    }

  }

  override def onSocketPlaced(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection): Unit = {
    for(i <- 2 to 5) {
      val d = ForgeDirection.getOrientation(i)
      val xo = ts.xCoord + d.offsetX
      val zo = ts.zCoord + d.offsetZ

      val t = ts.worldObj.getBlockTileEntity(xo, ts.yCoord, zo)
      if(t != null && t.isInstanceOf[TileDirectionChanger]) {
        val td = t.asInstanceOf[TileDirectionChanger]
        td.directions(d.getOpposite.ordinal()) match {
          case ForgeDirection.UP => config.rsLatch(0) = false
          case ForgeDirection.DOWN => config.rsLatch(0) = true
          case _ =>
        }
      }
    }
  }
}
