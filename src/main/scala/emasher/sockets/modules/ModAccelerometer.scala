package emasher.sockets.modules

import emasher.api.{SocketTileAccess, SideConfig, RSPulseModule}
import emasher.sockets.SocketsMod
import net.minecraft.init.Items
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.util.ForgeDirection

class ModAccelerometer(id: Int) extends RSPulseModule(id, "sockets:accelerometer", "sockets:accelerometerActive") {
  def getLocalizedName: String = "Accelerometer"

  override def getToolTip(l: java.util.List[Object]) {
    l.add("Creats an internal redstone pulse")
    l.add("when the socket is moved")
  }

  override def getIndicatorKey(l: java.util.List[Object]) {
    l.add(SocketsMod.PREF_RED + "RS Circuit to pulse")
  }

  override def addRecipe(): Unit = {
    CraftingManager.getInstance().getRecipeList.asInstanceOf[java.util.List[Object]]
      .add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "rbr", " m ",
      Character.valueOf('b'), Items.quartz,
      Character.valueOf('r'), Items.redstone,
      Character.valueOf('m'), new ItemStack(SocketsMod.blankSide)))
  }

  override def isOutputingRedstone(config: SideConfig, ts: SocketTileAccess): Boolean = false

  override def onSocketPlaced(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection): Unit = {
    config.meta = 1
    for(i <- 0 to 2) {
      if(config.rsControl(i)) {
        ts.modifyRS(i, true)
      }
    }
    ts.sendClientSideState(side.ordinal())
  }

}
