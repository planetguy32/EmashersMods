package emasher

import akka.actor.ActorSystem
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLPostInitializationEvent, FMLInitializationEvent}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.{SidedProxy, Mod}
import emasher.api.Util
import emasher.blocks.Blocks
import emasher.entities.Entities
import emasher.fluids.Fluids
import emasher.items.{ItemEngWrench, Items}
import emasher.modules.Modules
import emasher.tileentities.TileEntities
import emasher.util.{Recipes, BucketEventHandler, Config}
import emasher.worldgeneration.{WorldGenerators, WorldGenerationUpdater}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.util.IIcon
import net.minecraftforge.common.MinecraftForge

@Mod( modid = "eng_toolbox", name = "Engineer's Toolbox", version = "1.2.2.1", modLanguage="scala" )
object EngineersToolbox {
  val tabItems = new CreativeTabs( "tabItems" ) {
    def getTabIconItem: Item = {
      new ItemEngWrench()
    }

    override def getIconItemStack: ItemStack = {
      new ItemStack( Items.rsWand )
    }
  }

  val tabBlocks = new CreativeTabs( "tabBlocks" ) {
    def getTabIconItem: Item = {
      new ItemEngWrench()
    }

    override def getIconItemStack: ItemStack = {
      new ItemStack( Blocks.miniPortal )
    }
  }

  val tabModules = new CreativeTabs( "tabModules" ) {
    def getTabIconItem: Item = {
      new ItemEngWrench()
    }

    override def getIconItemStack: ItemStack = {
      new ItemStack( Items.module, 1, 8 )
    }
  }

  //val system = ActorSystem( "microcontrollers" )

  var innerTextures: java.util.Map[String, IIcon] = null

  @SidedProxy(clientSide = "emasher.client.ClientProxy", serverSide = "emasher.CommonProxy")
  var proxy: CommonProxy = null

  var network: SimpleNetworkWrapper = null

  @EventHandler
  def preInit( event: FMLPreInitializationEvent ): Unit = {
    Config.load( event )

    EngineersToolbox.innerTextures = new java.util.HashMap[String, IIcon]

    network = NetworkRegistry.INSTANCE.newSimpleChannel("engineers_toolbox")

    EngineersToolbox.proxy.registerMessages()

    Fluids.init()
    Blocks.init()
    Items.init()
  }

  @EventHandler
  def load( event: FMLInitializationEvent ): Unit = {
    MinecraftForge.EVENT_BUS.register(new Util)
    MinecraftForge.EVENT_BUS.register(new BucketEventHandler)
    MinecraftForge.EVENT_BUS.register(new WorldGenerationUpdater)

    TileEntities.register()
    Blocks.register()
    Items.register()
    Fluids.register()
    Modules.register()
    Entities.register()
    WorldGenerators.register()

    Recipes.registerCrafing()
    Recipes.registerSmelting()
    Recipes.registerGrinder()
    Recipes.registerMultiSmelter()
    Recipes.registerCentrifuge()
    Recipes.registerMixer()
    Recipes.registerPhotobioreactor()

    EngineersToolbox.proxy.registerRenderers()
  }

  @EventHandler
  def postInit( event: FMLPostInitializationEvent ): Unit = {
    Recipes.registerGasGenerator()
    Recipes.register3rdPartyRecipes()

    proxy.registerNEI()
  }

}
