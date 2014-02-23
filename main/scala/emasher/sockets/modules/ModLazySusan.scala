package emasher.sockets.modules

import emasher.api.{Util, SideConfig, SocketTileAccess, SocketModule}
import net.minecraft.item.crafting.CraftingManager
import java.util.List
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.item.{Item, ItemStack}
import emasher.sockets.SocketsMod
import net.minecraftforge.common.ForgeDirection
import emasher.sockets.modules.Orientation.Orientation
import emasher.core.Tuple

object Orientation extends Enumeration {
  type Orientation = Value
  val HORIZONTAL, VERTICAL_EW, VERTICAL_NS, INVALID = Value
}

class ModLazySusan(id: Int) extends SocketModule(id, "sockets:lazySusan") {

  override def getLocalizedName: String = "Lazy Susan"

  override def addRecipe(): Unit = {
    CraftingManager.getInstance().getRecipeList.asInstanceOf[java.util.List[Object]]
      .add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 2, moduleID), " m ", "mpm", " m ",
      Character.valueOf('p'), Item.ingotIron,
      Character.valueOf('m'), new ItemStack(SocketsMod.module, 1, 40)))
  }

  override def getToolTip(l: java.util.List[Object]): Unit = {
    l.add("'Rotates' the blocks adjacent to them around")
    l.add("on a redstone pulse")
    l.add("They must be set up in a ring formation around")
    l.add("the socket either vertically or horizontally")
  }

  override def getIndicatorKey(l: java.util.List[Object]): Unit = {
    l.add(SocketsMod.PREF_RED + "Activate")
  }

  override def hasRSIndicator: Boolean = true

  override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection): Boolean = {
    var numOnSocket = 0
    for(i <- 0 to 5) {
      if(isOn(ts, i)) numOnSocket += 1
    }

    if(numOnSocket >= 4) {
      false
    } else if(isOn(ts, side.getOpposite)) {
      true
    } else {
      var adjNS = false
      var adjEW = false
      var adjUD = false
      for(i <- 0 to 5) {
        val d = ForgeDirection.getOrientation(i)
        if(d != side.getOpposite && d != side && isOn(ts, d)) {
          d match {
            case ForgeDirection.UP | ForgeDirection.DOWN => adjUD = true
            case ForgeDirection.NORTH | ForgeDirection.SOUTH => adjNS = true
            case ForgeDirection.EAST | ForgeDirection.WEST => adjEW = true
          }
        }
      }

      if((adjNS && adjEW) || (adjNS && adjUD) || (adjEW && adjUD)) {
        false
      } else {
        true
      }

    }
  }

  def getOrientation(ts: SocketTileAccess): Orientation = {
    var numOnSocket = 0
    for(i <- 0 to 5) {
      if(isOn(ts, i)) numOnSocket += 1
    }

    if(numOnSocket == 4) {
      var adjNS = false
      var adjEW = false
      var adjUD = false

      for(i <- 0 to 5) {
        val d = ForgeDirection.getOrientation(i)
        if(isOn(ts, d)) {
          d match {
            case ForgeDirection.UP | ForgeDirection.DOWN => adjUD = true
            case ForgeDirection.NORTH | ForgeDirection.SOUTH => adjNS = true
            case ForgeDirection.EAST | ForgeDirection.WEST => adjEW = true
          }
        }
      }

      (adjNS, adjEW, adjUD) match {
        case (true, true, false) => Orientation.HORIZONTAL
        case (true, false, true) => Orientation.VERTICAL_NS
        case (false, true, true) => Orientation.VERTICAL_EW
        case _ => Orientation.INVALID
      }
    } else {
      Orientation.INVALID
    }
  }

  def isOn(ts: SocketTileAccess, side: ForgeDirection): Boolean = {
    val m = ts.getSide(side)
    m.moduleID == moduleID
  }

  def isOn(ts: SocketTileAccess, side: Int): Boolean = {
    val d = ForgeDirection.getOrientation(side)
    val m = ts.getSide(d)
    m.moduleID == moduleID
  }

  def isLeadForOrientation(o: Orientation, d: ForgeDirection): Boolean = {
    o match {
      case Orientation.HORIZONTAL => d == ForgeDirection.NORTH
      case Orientation.VERTICAL_EW | Orientation.VERTICAL_NS => d == ForgeDirection.UP
      case _ => false
    }
  }

  override def onRSInterfaceChange(config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean): Unit = {
    if(!on || ! config.rsControl(index)) return
    val orientation = getOrientation(ts)
    if(! isLeadForOrientation(orientation, side)) return

    var one = null.asInstanceOf[Tuple]
    var two = null.asInstanceOf[Tuple]
    var three = null.asInstanceOf[Tuple]
    var four = null.asInstanceOf[Tuple]

    orientation match {
      case Orientation.HORIZONTAL =>
        one = new Tuple(ts.xCoord + ForgeDirection.NORTH.offsetX, ts.yCoord + ForgeDirection.NORTH.offsetY, ts.zCoord + ForgeDirection.NORTH.offsetZ)
        two = new Tuple(ts.xCoord + ForgeDirection.EAST.offsetX, ts.yCoord + ForgeDirection.EAST.offsetY, ts.zCoord + ForgeDirection.EAST.offsetZ)
        three = new Tuple(ts.xCoord + ForgeDirection.SOUTH.offsetX, ts.yCoord + ForgeDirection.SOUTH.offsetY, ts.zCoord + ForgeDirection.SOUTH.offsetZ)
        four = new Tuple(ts.xCoord + ForgeDirection.WEST.offsetX, ts.yCoord + ForgeDirection.WEST.offsetY, ts.zCoord + ForgeDirection.WEST.offsetZ)
      case Orientation.VERTICAL_EW =>
        one = new Tuple(ts.xCoord + ForgeDirection.UP.offsetX, ts.yCoord + ForgeDirection.UP.offsetY, ts.zCoord + ForgeDirection.UP.offsetZ)
        two = new Tuple(ts.xCoord + ForgeDirection.EAST.offsetX, ts.yCoord + ForgeDirection.EAST.offsetY, ts.zCoord + ForgeDirection.EAST.offsetZ)
        three = new Tuple(ts.xCoord + ForgeDirection.DOWN.offsetX, ts.yCoord + ForgeDirection.DOWN.offsetY, ts.zCoord + ForgeDirection.DOWN.offsetZ)
        four = new Tuple(ts.xCoord + ForgeDirection.WEST.offsetX, ts.yCoord + ForgeDirection.WEST.offsetY, ts.zCoord + ForgeDirection.WEST.offsetZ)
      case Orientation.VERTICAL_NS =>
        one = new Tuple(ts.xCoord + ForgeDirection.NORTH.offsetX, ts.yCoord + ForgeDirection.NORTH.offsetY, ts.zCoord + ForgeDirection.NORTH.offsetZ)
        two = new Tuple(ts.xCoord + ForgeDirection.UP.offsetX, ts.yCoord + ForgeDirection.UP.offsetY, ts.zCoord + ForgeDirection.UP.offsetZ)
        three = new Tuple(ts.xCoord + ForgeDirection.SOUTH.offsetX, ts.yCoord + ForgeDirection.SOUTH.offsetY, ts.zCoord + ForgeDirection.SOUTH.offsetZ)
        four = new Tuple(ts.xCoord + ForgeDirection.DOWN.offsetX, ts.yCoord + ForgeDirection.DOWN.offsetY, ts.zCoord + ForgeDirection.DOWN.offsetZ)
    }

    if(Util.swapBlocks(ts.worldObj, one.x, one.y, one.z, two.x, two.y, two.z))
      if(Util.swapBlocks(ts.worldObj, one.x, one.y, one.z, three.x, three.y, three.z))
        if(Util.swapBlocks(ts.worldObj, one.x, one.y, one.z, four.x, four.y, four.z)) {
          ts.worldObj.playSoundEffect(ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.out", 0.5F, ts.worldObj.rand.nextFloat() * 0.25F + 0.6F)
          ts.worldObj.playSoundEffect(ts.xCoord + 0.5D, ts.yCoord + 0.5D, ts.zCoord + 0.5D, "tile.piston.in", 0.5F, ts.worldObj.rand.nextFloat() * 0.25F + 0.6F)
        }
    }
}
