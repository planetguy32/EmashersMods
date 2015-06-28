package emasher.blocks

import cpw.mods.fml.common.registry.GameRegistry
import emasher.EngineersToolbox
import emasher.api.Registry
import emasher.items._
import emasher.util.Config
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import emasher.fluids.Fluids._
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

object Blocks {
  var mixedDirt: Block = null
  var mixedSand: Block = null
  var algae: Block = null
  var deadAlgae: Block = null
  var superAlgae: Block = null
  var machine: Block = null
  var normalCube: Block = null
  var redSandStone: Block = null
  var limestone: Block = null
  var nutrientWater: Block = null
  var metal: Block = null
  var ore: Block = null
  var chainFence: Block = null
  var sandbag: Block = null
  var emeryTile: Block = null
  var deflectorBase: Block = null
  var deflector: Block = null
  var naturalGas: BlockGasGeneric = null
  var propellent: BlockGasGeneric = null
  var hydrogen: BlockGasGeneric = null
  var smoke: BlockGasGeneric = null
  var toxicGas: BlockGasGeneric = null
  var neurotoxin: BlockGasGeneric = null
  var corrosiveGas: BlockGasGeneric = null
  var plasma: BlockGasGeneric = null
  var shaleResource: Block = null
  var chimney: Block = null
  var gasPocket: Block = null
  var plasmaPocket: Block = null
  var socket: BlockSocket = null
  var tempRS: Block = null
  var paintedPlanks: Block = null
  var groundLimestone: Block = null
  var blockSlickwater: Block = null
  var blockStartPipe: Block = null
  var blockFluidPipe: Block = null
  var blockEnergyPipe: Block = null
  var miniPortal: Block = null
  var directionChanger: Block = null
  var frame: Block = null
  var hemp: Block = null

  def init(): Unit = {
    mixedDirt = new BlockMixedDirt(Material.ground).setHardness(0.5F).setStepSound(Block.soundTypeSand).setBlockName("mixedDirt")
    mixedSand = new BlockMixedSand(Material.sand).setHardness(0.5F).setStepSound(Block.soundTypeSand).setBlockName("mixedsand")
    machine = new BlockMachine().setHardness(1.0F).setStepSound(Block.soundTypeMetal).setBlockName("machine")
    redSandStone = new BlockRedSandstone(Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeStone).setBlockName("redSandstone")
    limestone = new BlockLimestone().setHardness(1.5F).setStepSound(Block.soundTypeStone).setBlockName("limestone")
    normalCube = new BlockNormalCube(0, Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeStone).setBlockName("normalCube")
    metal = new BlockMetal(Material.iron).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("e_metal")
    ore = new BlockOre(0, Material.rock).setHardness(1.8F).setStepSound(Block.soundTypeStone).setBlockName("e_ore")
    algae = new BlockPondScum().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("algae")
    superAlgae = new BlockSuperAlgae().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("superAlgae")
    deadAlgae = new BlockDeadAlgae().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("deadAlgae")
    hemp = new BlockHemp().setStepSound(Block.soundTypeGrass).setHardness(0.0F).setResistance(0.0F).setBlockName("Hemp")
    gasPocket = (new BlockMineGas).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone).setBlockName("gasPocket")
    plasmaPocket = new BlockNetherGas().setHardness(0.4F).setResistance(10.0F).setStepSound(Block.soundTypeStone).setBlockName("plasmaPocket")
    shaleResource = new BlockShaleResource
    chimney = new BlockDuct().setResistance(5.0F).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("chimney")
    chainFence = new BlockThin(Material.iron).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("chainFence")
    sandbag = new BlockSandBag(Material.cloth).setHardness(2.0F).setResistance(20.0F).setStepSound(Block.soundTypeCloth).setBlockName("sandbag")
    emeryTile = new BlockEmeryTile(Material.rock).setHardness(2.0F).setResistance(20.0F).setStepSound(Block.soundTypeStone).setBlockName("emeryTile")
    deflectorBase = new BlockDeflectorGen(Material.iron).setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundTypeMetal).setBlockName("deflectorGenerator")
    deflector = new BlockDeflector().setBlockUnbreakable().setStepSound(Block.soundTypeGlass).setBlockName("deflector")
    socket = new BlockSocket().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("modular_socket").asInstanceOf[BlockSocket]
    tempRS = new BlockTempRS().setBlockUnbreakable()
    blockStartPipe = new BlockStartPipe().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("start_pipe")
    blockFluidPipe = new BlockFluidPipe().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("fluid_pipe")
    blockEnergyPipe = new BlockEnergyPipe().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("energy_pipe")
    miniPortal = new BlockMiniPortal().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeStone).setBlockName("emasher_mini_portal")
    directionChanger = new BlockDirectionChanger().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeGlass).setBlockName("emasher_direction_changer")
    frame = new BlockFrame().setResistance(8.0F).setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("emasher_frame")
    paintedPlanks = new BlockPaintedWood(0, Material.wood).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setBlockName("paintedPlanks")
    groundLimestone = new BlockGroundLimestone().setHardness(0.6F).setStepSound(Block.soundTypeGravel).setBlockName("groundLimestone")
  }

  def register(): Unit = {
    GameRegistry.registerBlock(hemp, "hemp")
    GameRegistry.registerBlock(redSandStone, "redSandstone")
    GameRegistry.registerBlock(limestone, "limestone")
    GameRegistry.registerBlock(normalCube, classOf[ItemBlockNormalCube], "normalCube")
    GameRegistry.registerBlock(metal, classOf[ItemBlockMetal], "metal")
    GameRegistry.registerBlock(ore, classOf[ItemBlockOre], "ore")
    GameRegistry.registerBlock(nutrientWater, "nutrientWater")
    GameRegistry.registerBlock(mixedDirt, "mixedDirt")
    GameRegistry.registerBlock(mixedSand, "mixedSand")
    GameRegistry.registerBlock(machine, "machine")
    GameRegistry.registerBlock(algae, classOf[ItemPondScum], "algae", "eng_toolbox:algae".asInstanceOf[AnyRef])
    GameRegistry.registerBlock(superAlgae, classOf[ItemPondScum], "superAlgae", "eng_toolbox:superAlgae".asInstanceOf[AnyRef])
    GameRegistry.registerBlock(deadAlgae, classOf[ItemPondScum], "deadAlgae", "eng_toolbox:deadAlgae".asInstanceOf[AnyRef])
    GameRegistry.registerBlock(shaleResource, classOf[ItemBlockShaleResource], "shaleResource")
    GameRegistry.registerBlock(chimney, "chimney")
    GameRegistry.registerBlock(naturalGas, "naturalGas")
    GameRegistry.registerBlock(propellent, "propellent")
    GameRegistry.registerBlock(hydrogen, "hydrogen")
    GameRegistry.registerBlock(smoke, "smoke")
    GameRegistry.registerBlock(toxicGas, "weaponizedGas")
    GameRegistry.registerBlock(neurotoxin, "neurotoxin")
    GameRegistry.registerBlock(corrosiveGas, "corrosiveGas")
    GameRegistry.registerBlock(gasPocket, "gasPocket")
    GameRegistry.registerBlock(plasma, "plasma")
    GameRegistry.registerBlock(chainFence, classOf[ItemBlockThin], "chainFence")
    GameRegistry.registerBlock(sandbag, "sandbag")
    GameRegistry.registerBlock(emeryTile, "emeryTile")
    GameRegistry.registerBlock(deflectorBase, "deflectorBase")
    GameRegistry.registerBlock(deflector, "deflector")
    GameRegistry.registerBlock(socket, classOf[ItemBlockSocket], "modular_socket")
    GameRegistry.registerBlock(tempRS, "tempRS")
    GameRegistry.registerBlock(blockStartPipe, "start_pipe")
    GameRegistry.registerBlock(blockFluidPipe, "fluid_pipe")
    GameRegistry.registerBlock(blockEnergyPipe, "energy_pipe")
    GameRegistry.registerBlock(directionChanger, "emasher_direction_changer")
    GameRegistry.registerBlock(frame, "emasher_frame")
    GameRegistry.registerBlock(paintedPlanks, classOf[ItemBlockPaintedWood], "paintedPlanks")
    GameRegistry.registerBlock(groundLimestone, "groundLimestone")
    GameRegistry.registerBlock(blockSlickwater, "slickwater")
    GameRegistry.registerBlock(miniPortal, "emasher_mini_portal")

    OreDictionary.registerOre("blockAluminum", new ItemStack(metal, 1, 0))
    OreDictionary.registerOre("blockBronze", new ItemStack(metal, 1, 1))
    OreDictionary.registerOre("blockCopper", new ItemStack(metal, 1, 2))
    OreDictionary.registerOre("blockLead", new ItemStack(metal, 1, 3))
    OreDictionary.registerOre("blockNickel", new ItemStack(metal, 1, 4))
    OreDictionary.registerOre("blockPlatinum", new ItemStack(metal, 1, 5))
    OreDictionary.registerOre("blockSilver", new ItemStack(metal, 1, 6))
    OreDictionary.registerOre("blockSteel", new ItemStack(metal, 1, 7))
    OreDictionary.registerOre("blockTin", new ItemStack(metal, 1, 8))
    OreDictionary.registerOre("oreAluminum", new ItemStack(ore, 1, 0))
    OreDictionary.registerOre("oreTin", new ItemStack(ore, 1, 1))
    OreDictionary.registerOre("oreEmery", new ItemStack(ore, 1, 2))
    OreDictionary.registerOre("oreLead", new ItemStack(ore, 1, 3))
    OreDictionary.registerOre("oreCopper", new ItemStack(ore, 1, 4))
    OreDictionary.registerOre("oreNickel", new ItemStack(ore, 1, 5))
    OreDictionary.registerOre("oreRuby", new ItemStack(ore, 1, 6))
    OreDictionary.registerOre("oreSapphire", new ItemStack(ore, 1, 7))
    for( i <- 0 to 15 ) {
      OreDictionary.registerOre("plankWood", new ItemStack(paintedPlanks, 1, i))
    }
    OreDictionary.registerOre("limestone", limestone)

    metal.setHarvestLevel("pickaxe", 2, 0)
    metal.setHarvestLevel("pickaxe", 2, 1)
    metal.setHarvestLevel("pickaxe", 2, 2)
    metal.setHarvestLevel("pickaxe", 2, 3)
    metal.setHarvestLevel("pickaxe", 2, 4)
    metal.setHarvestLevel("pickaxe", 2, 5)
    metal.setHarvestLevel("pickaxe", 2, 6)
    metal.setHarvestLevel("pickaxe", 3, 7)
    metal.setHarvestLevel("pickaxe", 2, 8)
    ore.setHarvestLevel("pickaxe", 2, 0)
    ore.setHarvestLevel("pickaxe", 1, 1)
    ore.setHarvestLevel("pickaxe", 1, 2)
    ore.setHarvestLevel("pickaxe", 2, 3)
    ore.setHarvestLevel("pickaxe", 1, 4)
    ore.setHarvestLevel("pickaxe", 2, 5)
    ore.setHarvestLevel("pickaxe", 2, 6)
    ore.setHarvestLevel("pickaxe", 2, 7)
    mixedDirt.setHarvestLevel("shovel", 2)
    mixedSand.setHarvestLevel("shovel", 2)

    Registry.addBlock("limestone", limestone)
    Registry.addBlock("redSandstone", redSandStone)
    Registry.addBlock("mixedDirt", mixedDirt)
    Registry.addBlock("mixedSand", mixedSand)
    Registry.addBlock("normalCube", normalCube)
    Registry.addBlock("machine", machine)
    Registry.addBlock("ore", ore)
    Registry.addBlock("metal", metal)
    Registry.addBlock("algae", algae)
    Registry.addBlock("shaleResource", shaleResource)
    Registry.addBlock("chimney", chimney)
    Registry.addBlock("naturalGas", naturalGas)
    Registry.addBlock("propellent", propellent)
    Registry.addBlock("hydrogen", hydrogen)
    Registry.addBlock("smoke", smoke)
    Registry.addBlock("toxicGas", toxicGas)
    Registry.addBlock("neurotoxin", neurotoxin)
    Registry.addBlock("plasma", plasma)
    Registry.addBlock("socket", socket)
    Registry.addBlock("colouredPlanks", paintedPlanks)
    Registry.addBlock("groundLimestone", groundLimestone)
    Registry.addBlock("Slickwater", blockSlickwater)

    net.minecraft.init.Blocks.fire.setFireInfo(paintedPlanks, 5, 20)

    blockStartPipe.setCreativeTab( EngineersToolbox.tabBlocks)
    blockFluidPipe.setCreativeTab( EngineersToolbox.tabBlocks)
    blockEnergyPipe.setCreativeTab( EngineersToolbox.tabBlocks)
  }
}