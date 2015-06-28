package emasher.worldgeneration

import java.util

import cpw.mods.fml.common.registry.GameRegistry
import emasher.util.Config._
import emasher.blocks.Blocks._
import net.minecraft.init.Blocks
import net.minecraftforge.common.{BiomeDictionary, MinecraftForge}
import scala.collection.JavaConversions._

object WorldGenerators {
  var gen: WorldGenMine = new WorldGenMine
  var scumGenerator: WorldGenPondScum = new WorldGenPondScum
  var generateHemp: WorldGenHemp = new WorldGenHemp
  var gasGenerator: WorldGenGas = new WorldGenGas
  var gasVentGenerator: WorldGenGasVent = new WorldGenGasVent

  def register(): Unit = {
    if (spawnAlgae) GameRegistry.registerWorldGenerator(scumGenerator, 1)

    if (retroGen) {
      MinecraftForge.EVENT_BUS.register(new CoreWorldGenUpdater)
    } else {
      GameRegistry.registerWorldGenerator(gen, 1)
    }

    if (spawnHemp) {
      GameRegistry.registerWorldGenerator(generateHemp, 1)
    }

    var oreGen: WorldGenMinableSafe = null
    var container: WorldGenMinableWrap = null

    if (spawnLimestone) {
      oreGen = new WorldGenMinableSafe(limestone, limestonePerVein)
      container = new WorldGenMinableWrap(oreGen, limestonePerChunk, limestoneMinHeight, limestoneMaxHeight)
      for (bType <- parseBiomeList(limestoneBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnRedSandstone) {
      oreGen = new WorldGenMinableSafe(redSandStone, redSandstonePerVein)
      container = new WorldGenMinableWrap(oreGen, redSandstonePerChunk, redSandstoneMinHeight, redSandstoneMaxHeight)
      for (bType <- parseBiomeList(redSandstoneBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnBauxite) {
      oreGen = new WorldGenMinableSafe(ore, 0, bauxitePerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, bauxitePerChunk, bauxiteMinHeight, bauxiteMaxHeight)
      for (bType <- parseBiomeList(bauxiteBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnCassiterite) {
      oreGen = new WorldGenMinableSafe(ore, 1, cassiteritePerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, cassiteritePerChunk, cassiteriteMinHeight, cassiteriteMaxHeight)
      for (bType <- parseBiomeList(cassiteriteBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnEmery) {
      oreGen = new WorldGenMinableSafe(ore, 2, emeryPerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, emeryPerChunk, emeryMinHeight, emeryMaxHeight)
      for (bType <- parseBiomeList(emeryBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnGalena) {
      oreGen = new WorldGenMinableSafe(ore, 3, galenaPerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, galenaPerChunk, galenaMinHeight, galenaMaxHeight)
      for (bType <- parseBiomeList(galenaBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnNativeCopper) {
      oreGen = new WorldGenMinableSafe(ore, 4, nativeCopperPerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, nativeCopperPerChunk, nativeCopperMinHeight, nativeCopperMaxHeight)
      for (bType <- parseBiomeList(nativeCopperBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnPentlandite) {
      oreGen = new WorldGenMinableSafe(ore, 5, pentlanditePerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, pentlanditePerChunk, pentlanditeMinHeight, pentlanditeMaxHeight)
      for (bType <- parseBiomeList(pentlanditeBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnRuby) {
      oreGen = new WorldGenMinableSafe(ore, 6, rubyPerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, rubyPerChunk, rubyMinHeight, rubyMaxHeight)
      for (bType <- parseBiomeList(rubyBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnSapphire) {
      oreGen = new WorldGenMinableSafe(ore, 7, sapphirePerVein, Blocks.stone)
      container = new WorldGenMinableWrap(oreGen, sapphirePerChunk, sapphireMinHeight, sapphireMaxHeight)
      for (bType <- parseBiomeList(sapphireBiomes)) container.add(bType)
      gen.add(container)
    }

    if (spawnMineGas) {
      GameRegistry.registerWorldGenerator(gasGenerator, 1)
    }
  }

  def parseBiomeList(s: String): util.ArrayList[BiomeDictionary.Type] = {
    val result: util.ArrayList[BiomeDictionary.Type] = new util.ArrayList[BiomeDictionary.Type]
    var temp: String = null
    var temp2: String = null
    var temp3: BiomeDictionary.Type = null
    temp = s.concat("")
    var loc: Int = 0
    if (temp.length == 0) return result
    while (temp.indexOf(",") != -1) {
      loc = temp.indexOf(",")
      temp2 = temp.substring(0, loc)
      temp3 = BiomeDictionary.Type.valueOf(temp2)
      result.add(temp3)
      temp = temp.substring(loc + 1)
    }
    result.add(BiomeDictionary.Type.valueOf(temp))
    result
  }
}
