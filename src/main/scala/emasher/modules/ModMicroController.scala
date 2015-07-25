package emasher.modules

import java.io.{FileInputStream, File}

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

class ModMicrocontroller( id: Int ) extends SocketModule( id, "eng_toolbox:microcontrollerEmpty",
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

  override def canBeInstalled( ts: SocketTileAccess, side: ForgeDirection ): Boolean = {
    ForgeDirection.VALID_DIRECTIONS.foldLeft( true ) { ( acc: Boolean, it: ForgeDirection ) =>
      ( ! ts.getSide( it ).isInstanceOf[ModMicrocontroller] ) && acc
    }
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
    } else if( is != null && is.getItem != null ) {
        val ocFileSystem = getOCFileSystem( is, ts )
        ocFileSystem match {
          case Some( f: File ) =>
            ts.sideInventory.setInventorySlotContents( side.ordinal, is.copy() )
            is.stackSize -= 1

            initScripts( f, ts, config, side )
          case _ =>
        }
    } else {
      onRemoved( ts, config, side )
    }
  }

  def printNBT( compound: NBTTagCompound ): Unit = {
    println( compound.toString )
  }

  override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection): Unit =  {
    val t = ts.asInstanceOf[TileSocket]
    if( t.genericScript != null ) {
      runScript( t.genericScript, config, t, side )
    }
  }

  override def onRSInterfaceChange( config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean ): Unit = {
    val t = ts.asInstanceOf[TileSocket]
    if( t.circuitScript != null ) {
      runScript( t.circuitScript, config, t, side, addChannelInfo = true, index, on, "circuit" )
    }
  }

  override def onRSLatchChange( config: SideConfig, index: Int, ts: SocketTileAccess, side: ForgeDirection, on: Boolean ): Unit = {
    val t = ts.asInstanceOf[TileSocket]
    if( t.latchScript != null ) {
      runScript( t.latchScript, config, t, side, addChannelInfo = true, index, on, "latch" )
    }
  }

  override def onSocketLoad( config: SideConfig, ts: SocketTileAccess, side: ForgeDirection ): Unit = {
    val is = ts.sideInventory.getStackInSlot( side.ordinal() )
    if( is != null && is.getItem != null && is.getItem.isInstanceOf[IMedia] ) {
      val media = is.getItem.asInstanceOf[IMedia]
      val mount = media.createDataMount( is, ts.getWorldObj )
      initScripts( mount, ts, config, side )
    } else if( is != null && is.getItem != null ) {
      val ocFileSystem = getOCFileSystem( is, ts )
      ocFileSystem match {
        case Some( f: File ) =>
          ts.sideInventory.setInventorySlotContents( side.ordinal, is.copy() )
          is.stackSize -= 1

          initScripts( f, ts, config, side )
        case _ =>
      }
    }
  }

  override def onRemoved (ts: SocketTileAccess, config: SideConfig, side: ForgeDirection) {
    if( ts.sideInventory.getStackInSlot( side.ordinal ) != null ) {
      val xo: Int = ts.xCoord + side.offsetX
      val yo: Int = ts.yCoord + side.offsetY
      val zo: Int = ts.zCoord + side.offsetZ
      dropItemsOnSide( ts, config, side, xo, yo, zo, ts.sideInventory.getStackInSlot( side.ordinal ) )
      ts.sideInventory.setInventorySlotContents( side.ordinal, null )
      config.tags = new NBTTagCompound

      val te = ts.asInstanceOf[ TileSocket ]
      te.genericScript = null
      config.meta = 0
      ts.sendClientSideState( side.ordinal )
    }
  }

  private def dropItemsOnSide( ts: SocketTileAccess, config: SideConfig, side: ForgeDirection, xo: Int, yo: Int, zo: Int, stack: ItemStack ): Unit =  {
    if( !ts.getWorldObj.isRemote ) {
      val f: Float = 0.7F
      val d0: Double = ( ts.getWorldObj.rand.nextFloat * f ).toDouble + ( 1.0F - f ).toDouble * 0.5D
      val d1: Double = ( ts.getWorldObj.rand.nextFloat * f ).toDouble + ( 1.0F - f ).toDouble * 0.5D
      val d2: Double = ( ts.getWorldObj.rand.nextFloat * f ).toDouble + ( 1.0F - f ).toDouble * 0.5D
      val entityItem: EntityItem = new EntityItem( ts.getWorldObj, xo.toDouble + d0, yo.toDouble + d1, zo.toDouble + d2, stack )
      entityItem.delayBeforeCanPickup = 1
      ts.getWorldObj.spawnEntityInWorld( entityItem )
    }
  }

  private def getOCFileSystem( is: ItemStack, ts: SocketTileAccess ): Option[File] = {
    if( is.hasTagCompound ) {
      val compound = is.getTagCompound
      if( compound.hasKey( "oc:data") ) {
        val dataCompound = compound.getCompoundTag( "oc:data" )
        if( dataCompound.hasKey( "node" ) ) {
          val nodeCompound = dataCompound.getCompoundTag( "node" )
          if( nodeCompound.hasKey( "address" ) ) {
            val address = nodeCompound.getString( "address" )
            val worldFile = ts.getWorldObj.getSaveHandler.getWorldDirectory
            worldFile.listFiles.find { file =>
              file.getName == "opencomputers"
            }.foreach { oc =>
              oc.listFiles.find { addrFile =>
                addrFile.getName == address
              }.foreach { diskDirectory =>
                return Option( diskDirectory )
              }
            }
          }
        }
      }
    }
    None
  }

  private def initScripts( mount: IMount, ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): Unit = {
    val te = ts.asInstanceOf[TileSocket]
    te.genericScript = prepareScript( mount, te, "generic" )
    te.circuitScript = prepareScript( mount, te, "circuit" )
    te.latchScript = prepareScript( mount, te, "latch" )

    config.meta = 1
    ts.sendClientSideState( side.ordinal )
  }

  private def initScripts( file: File, ts: SocketTileAccess, config: SideConfig, side: ForgeDirection ): Unit = {
    val te = ts.asInstanceOf[TileSocket]
    te.genericScript = prepareScript( file, te, "generic" )
    te.circuitScript = prepareScript( file, te, "circuit" )
    te.latchScript = prepareScript( file, te, "latch" )

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

  private def prepareScript( file: File, ts: TileSocket, name: String ): LuaScript = {
    file.listFiles.find { theFile =>
      theFile.getName == name + ".lua"
    } match {
      case Some( f: File ) =>
        val fs = new FileInputStream( f )
        LuaScript.createFromStream( fs, name, ts )
      case _ => null
    }
  }

  private[emasher] def runScript( script: LuaScript, config: SideConfig, t: TileSocket, side: ForgeDirection,
                                  addChannelInfo: Boolean = false, channel: Int = 0, state: Boolean = false, name: String = "" ): Unit = {
    if( t.scriptStack < 1 && config.meta == 1 ) {
      t.scriptStack += 1
      val data = if( config.tags.hasKey( "luaData" ) ) {
        val d = config.tags.getCompoundTag( "luaData" )
        script.readGlobalsFromNBT( d )
        d
      } else {
        new NBTTagCompound
      }

      val scriptResult = addChannelInfo match {
        case true => script.runWithIndexAndState( name, channel, state )
        case false => script.run
      }

      if( scriptResult ) {
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
