package emasher.modules

import dan200.computercraft.api.media.IMedia
import emasher.api.{SocketTileAccess, SideConfig, SocketModule}
import emasher.microcontrollers.LuaScript
import emasher.tileentities.TileSocket
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection

class ModMicroController( id: Int ) extends SocketModule( id, "eng_toolbox:rangeSelector" ) {
  override def getLocalizedName = "Microcontroller"

  override def onSideActivated( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, player: EntityPlayer ) {
    val is: ItemStack = player.getCurrentEquippedItem

    if( is != null && is.getItem != null && is.getItem.isInstanceOf[IMedia] ) {
      if( ts.sideInventory.getStackInSlot( side.ordinal ) == null ) {
        ts.sideInventory.setInventorySlotContents( side.ordinal, is.copy() )
        val media = is.getItem.asInstanceOf[IMedia]
        val mount = media.createDataMount( ts.sideInventory.getStackInSlot( side.ordinal ), ts.getWorldObj )

        if( mount.exists( "socket.lua" ) ) {
          val stream = mount.openForRead( "socket.lua" )

          val te = ts.asInstanceOf[TileSocket]

          ts.addGenericScript( LuaScript.createFromStream( stream, "generic", te ) )

          is.stackSize -= 1
        } else {
          ts.sideInventory.setInventorySlotContents( side.ordinal, null )
        }
      }
    } else if( ts.sideInventory.getStackInSlot( side.ordinal ) != null ) {
      val xo: Int = ts.xCoord + side.offsetX
      val yo: Int = ts.yCoord + side.offsetY
      val zo: Int = ts.zCoord + side.offsetZ
      dropItemsOnSide( ts, config, side, xo, yo, zo, ts.sideInventory.getStackInSlot( side.ordinal ) )
      ts.sideInventory.setInventorySlotContents( side.ordinal, null )
    }
  }

  override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection) {
    val t = ts.asInstanceOf[TileSocket]
    if( t.genericScript != null ) t.genericScript.run()
  }

  def dropItemsOnSide(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, xo: Int, yo: Int, zo: Int, stack: ItemStack) {
    if (!ts.getWorldObj.isRemote) {
      val f: Float = 0.7F
      val d0: Double = (ts.getWorldObj.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
      val d1: Double = (ts.getWorldObj.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
      val d2: Double = (ts.getWorldObj.rand.nextFloat * f).toDouble + (1.0F - f).toDouble * 0.5D
      val entityitem: EntityItem = new EntityItem(ts.getWorldObj, xo.toDouble + d0, yo.toDouble + d1, zo.toDouble + d2, stack)
      entityitem.delayBeforeCanPickup = 1
      ts.getWorldObj.spawnEntityInWorld(entityitem)
    }
  }
}
