package emasher.util

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.common.config.Configuration

object Config {
  var retroGen: Boolean = false
  var spawnAlgae: Boolean = false
  var spawnHemp: Boolean = false
  var algaeDepth: Int = 0
  var spawnLimestone: Boolean = false
  var spawnRedSandstone: Boolean = false
  var spawnBauxite: Boolean = false
  var spawnCassiterite: Boolean = false
  var spawnEmery: Boolean = false
  var spawnGalena: Boolean = false
  var spawnNativeCopper: Boolean = false
  var spawnPentlandite: Boolean = false
  var spawnRuby: Boolean = false
  var spawnSapphire: Boolean = false
  var limestonePerChunk: Int = 0
  var redSandstonePerChunk: Int = 0
  var bauxitePerChunk: Int = 0
  var cassiteritePerChunk: Int = 0
  var emeryPerChunk: Int = 0
  var galenaPerChunk: Int = 0
  var nativeCopperPerChunk: Int = 0
  var pentlanditePerChunk: Int = 0
  var rubyPerChunk: Int = 0
  var sapphirePerChunk: Int = 0
  var redSandstonePerVein: Int = 0
  var limestonePerVein: Int = 0
  var bauxitePerVein: Int = 0
  var cassiteritePerVein: Int = 0
  var emeryPerVein: Int = 0
  var galenaPerVein: Int = 0
  var nativeCopperPerVein: Int = 0
  var pentlanditePerVein: Int = 0
  var rubyPerVein: Int = 0
  var sapphirePerVein: Int = 0
  var redSandstoneMinHeight: Int = 0
  var limestoneMinHeight: Int = 0
  var bauxiteMinHeight: Int = 0
  var cassiteriteMinHeight: Int = 0
  var emeryMinHeight: Int = 0
  var galenaMinHeight: Int = 0
  var nativeCopperMinHeight: Int = 0
  var pentlanditeMinHeight: Int = 0
  var rubyMinHeight: Int = 0
  var sapphireMinHeight: Int = 0
  var redSandstoneMaxHeight: Int = 0
  var limestoneMaxHeight: Int = 0
  var bauxiteMaxHeight: Int = 0
  var cassiteriteMaxHeight: Int = 0
  var emeryMaxHeight: Int = 0
  var galenaMaxHeight: Int = 0
  var nativeCopperMaxHeight: Int = 0
  var pentlanditeMaxHeight: Int = 0
  var rubyMaxHeight: Int = 0
  var sapphireMaxHeight: Int = 0
  var limestoneBiomes: String = null
  var redSandstoneBiomes: String = null
  var bauxiteBiomes: String = null
  var cassiteriteBiomes: String = null
  var emeryBiomes: String = null
  var galenaBiomes: String = null
  var nativeCopperBiomes: String = null
  var pentlanditeBiomes: String = null
  var rubyBiomes: String = null
  var sapphireBiomes: String = null
  var spawnMineGas: Boolean = false
  var flatBedrock: Boolean = false
  var flatNetherBedrock: Boolean = false
  var flatBedrockTop: Int = 0
  var flatNetherBedrockTop: Int = 0
  var maxGasInVent: Int = 0
  var minGasInVent: Int = 0
  var infiniteGasInVent: Boolean = false
  var gasBlocksInWorld: Boolean = false
  var gasBlocksCanExplode: Boolean = false
  var cbTextures: Boolean = false
  var smeltSand: Boolean = false
  var enableMiniPortal: Boolean = false
  var miniPortalLava: Boolean = false
  var slickwaterAmount: Int = 0
  var enableGrinder: Boolean = false
  var enableSolars: Boolean = false
  var enableWaterIntake: Boolean = false
  var enableHydro: Boolean = false
  var enablePiezo: Boolean = false
  var enableMultiSmelter: Boolean = false
  var enableKiln: Boolean = false
  var enableCentrifuge: Boolean = false
  var enableHusher: Boolean = false
  var oreRetrogenValue: Int = 1
  var shaleRetrogenValue: Int = 1
  var allowMicrocontrollers: Boolean = true

  var PREF_BLUE: AnyRef = EnumChatFormatting.BLUE
  var PREF_GREEN: AnyRef = EnumChatFormatting.GREEN
  var PREF_RED: AnyRef = EnumChatFormatting.RED
  var PREF_DARK_PURPLE: AnyRef = EnumChatFormatting.DARK_PURPLE
  var PREF_YELLOW: AnyRef = EnumChatFormatting.YELLOW
  var PREF_AQUA: AnyRef = EnumChatFormatting.AQUA
  var PREF_WHITE: AnyRef = EnumChatFormatting.WHITE

  def load( event: FMLPreInitializationEvent ): Unit = {
    val config: Configuration = new Configuration( event.getSuggestedConfigurationFile )
    config.load()

    retroGen = config.get( Configuration.CATEGORY_GENERAL, "A: Retro Gen Ores", false ).getBoolean( false )
    algaeDepth = config.get( Configuration.CATEGORY_GENERAL, "A: Max Water Depth Alage Can Grow In", 3 ).getInt

    spawnAlgae = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Algae", true ).getBoolean( true )
    spawnHemp = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Hemp", true ).getBoolean( true )

    spawnLimestone = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Limestone", true ).getBoolean( true )
    spawnRedSandstone = config.get( Configuration.CATEGORY_GENERAL, "C: Generate Red Sandstone", true ).getBoolean( true )
    spawnBauxite = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Bauxite Ore", true ).getBoolean( true )
    spawnCassiterite = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Cassiterite Ore", true ).getBoolean( true )
    spawnEmery = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Emery Ore", true ).getBoolean( true )
    spawnGalena = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Galena Ore", true ).getBoolean( true )
    spawnNativeCopper = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Native Copper Ore", true ).getBoolean( true )
    spawnPentlandite = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Pentlandite Ore", true ).getBoolean( true )
    spawnRuby = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Ruby Ore", true ).getBoolean( true )
    spawnSapphire = config.get( Configuration.CATEGORY_GENERAL, "D: Generate Sapphire Ore", true ).getBoolean( true )

    limestonePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Limestone Per Chunk", 20 ).getInt
    redSandstonePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Red SandStone Per Chunk", 20 ).getInt
    bauxitePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Bauxite Ore Per Chunk", 6 ).getInt
    cassiteritePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Cassiterite Ore Per Chunk", 6 ).getInt
    emeryPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Emery Ore Per Chunk", 6 ).getInt
    galenaPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Galena Ore Per Chunk", 6 ).getInt
    nativeCopperPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Native Copper Ore Per Chunk", 12 ).getInt
    pentlanditePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Pentlandite Ore Per Chunk", 4 ).getInt
    rubyPerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Ruby Ore Per Chunk", 2 ).getInt
    sapphirePerChunk = config.get( Configuration.CATEGORY_GENERAL, "E: Sapphire Ore Per Chunk", 2 ).getInt

    limestonePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Limestone Per Vein", 32 ).getInt
    redSandstonePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Red Sandstone Per Vein", 32 ).getInt
    bauxitePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Bauxite Ore Per Vein", 8 ).getInt
    cassiteritePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Cassiterite Ore Per Vein", 16 ).getInt
    emeryPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Emery Ore Per Vein", 16 ).getInt
    galenaPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Galena Ore Per Vein", 8 ).getInt
    nativeCopperPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Native Copper Ore Per Vein", 16 ).getInt
    pentlanditePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Pentlandite Ore Per Vein", 4 ).getInt
    rubyPerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Ruby Ore Per Vein", 4 ).getInt
    sapphirePerVein = config.get( Configuration.CATEGORY_GENERAL, "F: Sapphire Ore Per Vein", 4 ).getInt

    limestoneMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Limestone Min Height", 32 ).getInt
    redSandstoneMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Red Sandstone Min Height", 32 ).getInt
    bauxiteMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Bauxite Ore Min Height", 0 ).getInt
    cassiteriteMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Cassiterite Ore Min Height", 16 ).getInt
    emeryMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Emery Ore Min Height", 32 ).getInt
    galenaMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Galena Ore Min Height", 12 ).getInt
    nativeCopperMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Native Copper Ore Min Height", 32 ).getInt
    pentlanditeMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Pentlandite Ore Min Height", 0 ).getInt
    rubyMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Ruby Ore Min Height", 0 ).getInt
    sapphireMinHeight = config.get( Configuration.CATEGORY_GENERAL, "G: Sapphire Ore Min Height", 0 ).getInt

    limestoneMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Limestone Max Height", 127 ).getInt
    redSandstoneMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Red Sandstone Max Height", 127 ).getInt
    bauxiteMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Bauxite Ore Max Height", 50 ).getInt
    cassiteriteMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Cassiterite Ore Max Height", 32 ).getInt
    emeryMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Emery Ore Max Height", 127 ).getInt
    galenaMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Galena Ore Max Height", 32 ).getInt
    nativeCopperMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Native Copper Ore Max Height", 127 ).getInt
    pentlanditeMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Pentlandite Ore Max Height", 24 ).getInt
    rubyMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Ruby Ore Max Height", 16 ).getInt
    sapphireMaxHeight = config.get( Configuration.CATEGORY_GENERAL, "H: Sapphire Ore Max Height", 16 ).getInt

    limestoneBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Limestone Biomes", "PLAINS,FOREST,HILLS,MOUNTAIN,WATER" ).getString
    redSandstoneBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Red Sandstone Biomes", "DESERT" ).getString
    bauxiteBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Bauxite Ore Biomes", "PLAINS,JUNGLE,DESERT" ).getString
    cassiteriteBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Cassiterite Ore Biomes", "" ).getString
    emeryBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Emery Ore Biomes", "DESERT,HILLS,MOUNTAIN" ).getString
    galenaBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Galena Ore Biomes", "" ).getString
    nativeCopperBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Native Copper Ore Biomes", "" ).getString
    pentlanditeBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Pentlandite Ore Biomes", "HILLS,MOUNTAIN,FOREST" ).getString
    rubyBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Ruby Ore Biomes", "JUNGLE" ).getString
    sapphireBiomes = config.get( Configuration.CATEGORY_GENERAL, "I: Sapphire Ore Biomes", "DESERT" ).getString

    maxGasInVent = config.get( Configuration.CATEGORY_GENERAL, "Max Fluid In Shale Resources ( In Buckets )", 50000 ).getInt
    minGasInVent = config.get( Configuration.CATEGORY_GENERAL, "Min Fluid In Shale Resources ( In Buckets )", 5000 ).getInt
    infiniteGasInVent = config.get( Configuration.CATEGORY_GENERAL, "Infinite Gas In Vents", false ).getBoolean( false )
    spawnMineGas = config.get( Configuration.CATEGORY_GENERAL, "Spawn Gas Pockets in the world", false ).getBoolean( false )
    flatBedrock = config.get( Configuration.CATEGORY_GENERAL, "Flat Bedrock Compatibility Mode", false ).getBoolean( false )
    flatNetherBedrock = config.get( Configuration.CATEGORY_GENERAL, "Nether Flat Bedrock Compatibility Mode", false ).getBoolean( false )
    flatBedrockTop = config.get( Configuration.CATEGORY_GENERAL, "Flat Bedrock Top Layer", 0 ).getInt
    flatNetherBedrockTop = config.get( Configuration.CATEGORY_GENERAL, "Flat Nether Bedrock Top Layer", 0 ).getInt
    gasBlocksInWorld = config.get( Configuration.CATEGORY_GENERAL, "Allow gas blocks", true ).getBoolean( true )
    gasBlocksCanExplode = config.get( Configuration.CATEGORY_GENERAL, "Allow gas blocks to explode", true ).getBoolean( true )

    enableGrinder = config.get( Configuration.CATEGORY_GENERAL, "Enable Grinder Module", true ).getBoolean( true )
    enableKiln = config.get( Configuration.CATEGORY_GENERAL, "Enable Kiln Module", true ).getBoolean( true )
    enableMultiSmelter = config.get( Configuration.CATEGORY_GENERAL, "Enable Multi Smelter Module", true ).getBoolean( true )
    enableCentrifuge = config.get( Configuration.CATEGORY_GENERAL, "Enable Centrifuge Module", true ).getBoolean( true )
    enableSolars = config.get( Configuration.CATEGORY_GENERAL, "Enable Solar Panel Modules", true ).getBoolean( true )
    enableHydro = config.get( Configuration.CATEGORY_GENERAL, "Enable Hydroelectric Turbines", true ).getBoolean( true )
    enablePiezo = config.get( Configuration.CATEGORY_GENERAL, "Enable Piezo Electric Tiles", true ).getBoolean( true )
    enableWaterIntake = config.get( Configuration.CATEGORY_GENERAL, "Enable Water Intake", true ).getBoolean( true )
    enableHusher = config.get( Configuration.CATEGORY_GENERAL, "Enable Husher", true ).getBoolean( true )
    cbTextures = config.get( Configuration.CATEGORY_GENERAL, "Enable Colour Blind Mode", false ).getBoolean( false )
    enableMiniPortal = config.get( Configuration.CATEGORY_GENERAL, "Enable Fluidic Nether Portal", true ).getBoolean( true )
    miniPortalLava = config.get( Configuration.CATEGORY_GENERAL, "Allow Lava In Fluidic Nether Portal", true ).getBoolean( true )
    slickwaterAmount = config.get( Configuration.CATEGORY_GENERAL, "Amount of slickwater produced per operation ( mb )", 1000 ).getInt

    oreRetrogenValue = config.get( Configuration.CATEGORY_GENERAL, "Change to a different value between 1 and 15 inclusive to re-generate ores", 1 ).getInt
    shaleRetrogenValue = config.get( Configuration.CATEGORY_GENERAL, "Change to a different value between 1 and 15 inclusive to re-generate shale resources", 1 ).getInt

    allowMicrocontrollers = config.get( Configuration.CATEGORY_GENERAL, "Allow Microcontrollers", true ).getBoolean

    if( oreRetrogenValue < 1 || oreRetrogenValue > 15 ) {
      oreRetrogenValue = 1
    }

    if( shaleRetrogenValue < 1 || oreRetrogenValue > 15 ) {
      shaleRetrogenValue = 1
    }

    if (slickwaterAmount > 32000 || slickwaterAmount <= 0) {
      System.err.println("[Engineer's Toolbox] slickwaterAmount is not between (0..32000]")
      slickwaterAmount = 1000
    }

    if (cbTextures) {
      PREF_BLUE = "Blue: "
      PREF_GREEN = "Green: "
      PREF_RED = "Red: "
      PREF_DARK_PURPLE = "Purple: "
      PREF_YELLOW = "MO: "
      PREF_AQUA = "EN: "
      PREF_WHITE = "GEN: "
    }

    if( config.hasChanged ) config.save()
  }
}
