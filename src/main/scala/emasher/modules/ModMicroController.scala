package emasher.modules

import dan200.computercraft.api.filesystem.IMount
import dan200.computercraft.api.media.IMedia
import emasher.api.{SocketTileAccess, SideConfig, SocketModule}
import emasher.microcontrollers.LuaScript
import emasher.tileentities.TileSocket
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.oredict.ShapedOreRecipe

class ModMicroController( id: Int ) extends SocketModule( id, "eng_toolbox:microcontrollerEmpty",
  "eng_toolbox:microcontrollerRunning", "eng_toolbox:microcontrollerError" ) {

  override def getLocalizedName = "Microcontroller"

  override def addRecipe( ): Unit = {
    CraftingManager.getInstance( ).getRecipeList.asInstanceOf[ java.util.List[ Object ] ]
      .add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module, 1, moduleID ), "cgc", " m ",
      Character.valueOf( 'c' ), emasher.items.Items.circuit,
      Character.valueOf( 'g' ), new ItemStack( emasher.items.Items.gem, 1, 1),
      Character.valueOf( 'm' ), new ItemStack( emasher.items.Items.blankSide ) ) )
  }

  override def getCurrentTexture( config: SideConfig ): Int = config.meta

  override def getToolTip( l: java.util.List[ Object ] ): Unit = {
    l.add( "Yeah... you're just going to have to read the manual" )
  }

  override def onSideActivated( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, player: EntityPlayer ): Unit =  {
    val is: ItemStack = player.getCurrentEquippedItem

    if( is != null && is.getItem != null && is.getItem.isInstanceOf[IMedia] ) {
      if( ts.sideInventory.getStackInSlot( side.ordinal ) == null ) {
        ts.sideInventory.setInventorySlotContents( side.ordinal, is.copy() )
        is.stackSize -= 1
        val media = is.getItem.asInstanceOf[IMedia]
        val mount = media.createDataMount( ts.sideInventory.getStackInSlot( side.ordinal ), ts.getWorldObj )

        initScripts( mount, ts, config, side )
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
      config.meta = 0
      ts.sendClientSideState( side.ordinal )
    }
  }

  override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection): Unit =  {
    val t = ts.asInstanceOf[TileSocket]
    if( t.genericScript != null ) {
      runScript( t.genericScript, config, t, side )
    }
  }

  override def onSocketLoad( config: SideConfig, ts: SocketTileAccess, side: ForgeDirection ): Unit = {
    val is = ts.sideInventory.getStackInSlot( side.ordinal() )
    if( is != null && is.getItem != null && is.getItem.isInstanceOf[IMedia] ) {
      val media = is.getItem.asInstanceOf[IMedia]
      val mount = media.createDataMount( is, ts.getWorldObj )
      initScripts( mount, ts, config, side )
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

  private def initScripts( mount: IMount, ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): Unit = {
    val te = ts.asInstanceOf[TileSocket]
    te.genericScript = prepareScript( mount, te, "generic" )

    config.meta = 1
    ts.sendClientSideState( side.ordinal )
  }

  private def prepareScript( mount: IMount, ts: TileSocket, name: String ): LuaScript = {
    if( mount.exists( name + ".lua" ) ) {
      val stream = mount.openForRead( name + ".lua" )
      LuaScript.createFromStream( stream, name, ts )
    } else {
      null
    }
  }

  def runScript( script: LuaScript, config: SideConfig, t: TileSocket, side: ForgeDirection ): Unit = {
    if( t.scriptStack < 16 && config.meta == 1 ) {
      t.scriptStack += 1
      val data = if( config.tags.hasKey( "luaData" ) ) {
        val d = config.tags.getCompoundTag( "luaData" )
        script.readGlobalsFromNBT( d )
        d
      } else {
        new NBTTagCompound
      }

      if( script.run() ) {
        script.saveGlobalsToNBT( data )
        config.tags.setTag( "luaData", data )
        t.scriptStack -= 1
      } else {
        config.meta = 2
        t.sendClientSideState( side.ordinal )
      }

      t.scriptStack -= 1

    }
  }
}
