package emasher.modules

import dan200.computercraft.api.filesystem.IMount
import dan200.computercraft.api.media.IMedia
import emasher.api.{SocketTileAccess, SideConfig, SocketModule}
import emasher.microcontrollers.LuaScript
import emasher.tileentities.TileSocket
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection

class ModMicroController( id: Int ) extends SocketModule( id, "eng_toolbox:rangeSelector" ) {
  override def getLocalizedName = "Microcontroller"

  override def onSideActivated( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, player: EntityPlayer ): Unit =  {
    val is: ItemStack = player.getCurrentEquippedItem

    if( is != null && is.getItem != null && is.getItem.isInstanceOf[IMedia] ) {
      if( ts.sideInventory.getStackInSlot( side.ordinal ) == null ) {
        ts.sideInventory.setInventorySlotContents( side.ordinal, is.copy() )
        is.stackSize -= 1
        val media = is.getItem.asInstanceOf[IMedia]
        val mount = media.createDataMount( ts.sideInventory.getStackInSlot( side.ordinal ), ts.getWorldObj )

        initScripts( mount, ts, config )
      }
    } else if( ts.sideInventory.getStackInSlot( side.ordinal ) != null ) {
      val xo: Int = ts.xCoord + side.offsetX
      val yo: Int = ts.yCoord + side.offsetY
      val zo: Int = ts.zCoord + side.offsetZ
      dropItemsOnSide( ts, config, side, xo, yo, zo, ts.sideInventory.getStackInSlot( side.ordinal ) )
      ts.sideInventory.setInventorySlotContents( side.ordinal, null )
      config.tags = new NBTTagCompound

      val te = ts.asInstanceOf[TileSocket]
      te.genericScript = null
    }
  }

  override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection): Unit =  {
    val t = ts.asInstanceOf[TileSocket]
    if( t.genericScript != null ) {
      runScript( t.genericScript, config )
    }
  }

  override def onSocketLoad( config: SideConfig, ts: SocketTileAccess, side: ForgeDirection ): Unit = {
    val is = ts.sideInventory.getStackInSlot( side.ordinal() )
    if( is != null && is.getItem != null && is.getItem.isInstanceOf[IMedia] ) {
      val media = is.getItem.asInstanceOf[IMedia]
      val mount = media.createDataMount( is, ts.getWorldObj )
      initScripts( mount, ts, config )
    }
  }

  private def dropItemsOnSide( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, xo: Int, yo: Int, zo: Int, stack: ItemStack ): Unit =  {
    if( !ts.getWorldObj.isRemote ) {
      val f: Float = 0.7F
      val d0: Double = ( ts.getWorldObj.rand.nextFloat * f ).toDouble + ( 1.0F - f ).toDouble * 0.5D
      val d1: Double = ( ts.getWorldObj.rand.nextFloat * f ).toDouble + ( 1.0F - f ).toDouble * 0.5D
      val d2: Double = ( ts.getWorldObj.rand.nextFloat * f ).toDouble + ( 1.0F - f ).toDouble * 0.5D
      val entityitem: EntityItem = new EntityItem( ts.getWorldObj, xo.toDouble + d0, yo.toDouble + d1, zo.toDouble + d2, stack )
      entityitem.delayBeforeCanPickup = 1
      ts.getWorldObj.spawnEntityInWorld( entityitem )
    }
  }

  private def initScripts( mount: IMount, ts: SocketTileAccess, config: SideConfig ): Unit = {
    val te = ts.asInstanceOf[TileSocket]
    te.genericScript = prepareScript( mount, te, "generic" )
  }

  def prepareScript( mount: IMount, ts: TileSocket, name: String ): LuaScript = {
    if( mount.exists( name + ".lua" ) ) {
      val stream = mount.openForRead( name + ".lua" )
      LuaScript.createFromStream( stream, name, ts )
    } else {
      null
    }
  }

  def runScript( script: LuaScript, config: SideConfig ): Unit = {
    val data = if( config.tags.hasKey( "luaData" ) ) {
      val d = config.tags.getCompoundTag( "luaData" )
      script.readGlobalsFromNBT( d )
      d
    } else {
      new NBTTagCompound
    }

    script.run()

    script.saveGlobalsToNBT( data )
    config.tags.setTag( "luaData", data )
  }
}
