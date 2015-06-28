package emasher.items

import cpw.mods.fml.common.registry.GameRegistry
import emasher.EngineersToolbox
import emasher.api.Registry
import emasher.blocks.Blocks
import net.minecraft.item.{ItemStack, ItemArmor, Item}
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.oredict.OreDictionary

object Items {
  var colours: Array[String] = Array[String]("Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White")


  var enumArmorMaterialHemp: ItemArmor.ArmorMaterial = EnumHelper.addArmorMaterial("Hemp", 5, Array[Int](1, 3, 2, 1), 15)
  var enumArmorMaterialGas: ItemArmor.ArmorMaterial = EnumHelper.addArmorMaterial("Gas", 5, Array[Int](1, 3, 2, 1), 15)

  var circuit: ItemCircuit = null
  var psu: Item = null
  var ingot: Item = null
  var gem: Item = null
  var bluestone: Item = null
  var hempPlant: Item = null
  var hempSeeds: Item = null
  var hempOil: Item = null
  var hempCap: Item = null
  var hempTunic: Item = null
  var hempPants: Item = null
  var hempShoes: Item = null
  var vial: Item = null
  var vialFilled: Item = null
  var gasMask: Item = null
  var smokeGrenade: Item = null
  var ash: Item = null
  var chainSheet: Item = null
  var fenceWire: Item = null
  var module: Item = null
  var remote: Item = null
  var blankSide: Item = null
  var engWrench: Item = null
  var rsWand: Item = null
  var handboiler: Item = null
  var paintCans: Array[ItemPaintCan] = new Array[ItemPaintCan](16)
  var dusts: Item = null
  var slickBucket: Item = null
  var rsIngot: Item = null
  var nutBucket: Item = null

  def init(): Unit = {
    ingot = new ItemIngot().setUnlocalizedName("e_ingot")
    gem = new ItemGem().setUnlocalizedName("e_gem")
    circuit = new ItemCircuit
    psu = new ItemEmasherGeneric("eng_toolbox:psu", "psu")
    bluestone = new ItemBluestone
    hempPlant = new ItemHempPlant
    hempSeeds = new ItemHempSeeds(Blocks.hemp)
    hempOil = new ItemHempOil
    hempCap = new ItemHempCap(enumArmorMaterialHemp, 0, 0)
    hempTunic = new ItemHempTunic(enumArmorMaterialHemp, 0, 1)
    hempPants = new ItemHempPants(enumArmorMaterialHemp, 0, 2)
    hempShoes = new ItemHempShoes(enumArmorMaterialHemp, 0, 3)
    vial = new ItemGasVial
    vialFilled = new ItemGasVialFilled
    gasMask = new ItemGasMask(enumArmorMaterialGas, 0, 0)
    smokeGrenade = new ItemSmokeGrenade
    ash = new ItemEmasherGeneric("eng_toolbox:ash", "ash")
    chainSheet = new ItemChainSheet
    fenceWire = new ItemFenceWire
    rsIngot = new ItemRSIngot
    handboiler = new ItemHandboiler("", "")
    for( i <- 0 to 15 ) {
      paintCans(i) = new ItemPaintCan(i)
    }
    remote = new ItemSocketRemote
    rsWand = new ItemRSWand
    slickBucket = new ItemSlickBucket().setMaxStackSize(1)
    nutBucket = new ItemNutrientBucket().setMaxStackSize(1)
    blankSide = new ItemEmasherGeneric("eng_toolbox:blankmod", "blankSide")
    module = new ItemModule
    engWrench = new ItemEngWrench
    dusts = new ItemDusts
  }

  def register(): Unit = {
    GameRegistry.registerItem(hempPlant, "hempPlant", "emashercore")
    GameRegistry.registerItem(hempSeeds, "hempSeeds", "emashercore")
    GameRegistry.registerItem(hempOil, "hempOil", "emashercore")
    GameRegistry.registerItem(hempCap, "hempCap", "emashercore")
    GameRegistry.registerItem(hempTunic, "hempTunic", "emashercore")
    GameRegistry.registerItem(hempPants, "hempPants", "emashercore")
    GameRegistry.registerItem(hempShoes, "hempShoes", "emashercore")
    GameRegistry.registerItem(ingot, "ingot", "emashercore")
    GameRegistry.registerItem(gem, "gem", "emashercore")
    GameRegistry.registerItem(circuit, "circuit", "emashercore")
    GameRegistry.registerItem(psu, "psu", "emashercore")
    GameRegistry.registerItem(bluestone, "bluestone", "emashercore")
    GameRegistry.registerItem(gasMask, "gasMask")
    GameRegistry.registerItem(smokeGrenade, "smokeGrenade")
    GameRegistry.registerItem(ash, "ash")
    GameRegistry.registerItem(vial, "vial")
    GameRegistry.registerItem(vialFilled, "vialFilled")
    GameRegistry.registerItem(chainSheet, "chainSheet", "emasherdefense")
    GameRegistry.registerItem(fenceWire, "fenceWire", "emasherdefense")
    GameRegistry.registerItem(rsIngot, "rsIngot", "eng_toolbox")
    GameRegistry.registerItem(handboiler, "handBoiler")
    for( i <- 0 to 15 ) {
      GameRegistry.registerItem(paintCans(i), "item.paintCan." + colours(i) + ".name", colours(i) + " Spray Paint")
    }
    GameRegistry.registerItem(remote, "Socket Remote", "eng_toolbox")
    GameRegistry.registerItem(rsWand, "Redstone Wand", "eng_toolbox")
    GameRegistry.registerItem(slickBucket, "Slickwater Bucket", "eng_toolbox")
    GameRegistry.registerItem(nutBucket, "Nutrient Water Bucket", "eng_toolbox")
    GameRegistry.registerItem(blankSide, "Blank Module", "eng_toolbox")
    GameRegistry.registerItem(module, "Module", "eng_toolbox")
    GameRegistry.registerItem(engWrench, "Engineer's Wrench", "eng_toolbox")
    GameRegistry.registerItem(dusts, "Dust", "eng_toolbox")

    OreDictionary.registerOre("ingotAluminum", new ItemStack(ingot, 1, 0))
    OreDictionary.registerOre("ingotBronze", new ItemStack(ingot, 1, 1))
    OreDictionary.registerOre("ingotCopper", new ItemStack(ingot, 1, 2))
    OreDictionary.registerOre("ingotLead", new ItemStack(ingot, 1, 3))
    OreDictionary.registerOre("ingotNickel", new ItemStack(ingot, 1, 4))
    OreDictionary.registerOre("ingotPlatinum", new ItemStack(ingot, 1, 5))
    OreDictionary.registerOre("ingotSilver", new ItemStack(ingot, 1, 6))
    OreDictionary.registerOre("ingotSteel", new ItemStack(ingot, 1, 7))
    OreDictionary.registerOre("ingotTin", new ItemStack(ingot, 1, 8))
    OreDictionary.registerOre("gemEmery", new ItemStack(gem, 1, 0))
    OreDictionary.registerOre("gemRuby", new ItemStack(gem, 1, 1))
    OreDictionary.registerOre("gemSapphire", new ItemStack(gem, 1, 2))
    OreDictionary.registerOre("dustAsh", ash)
    for( i <- 0 to ( ItemDusts.NUM_ITEMS - 1 ) ) {
      OreDictionary.registerOre(ItemDusts.ORE_NAMES(i), new ItemStack(dusts, 1, i))
    }

    Registry.addItem("ingot", ingot)
    Registry.addItem("circuit", circuit)
    Registry.addItem("PSU", psu)
    Registry.addItem("gem", gem)
    Registry.addItem("vialEmpty", vial)
    Registry.addItem("vialFilled", vialFilled)
    Registry.addItem("gasMask", gasMask)
    Registry.addItem("smokeGrenade", smokeGrenade)
    Registry.addItem("ash", ash)
    Registry.addItem("module", module)
    Registry.addItem("blankModule", blankSide)
    Registry.addItem("socketRemote", remote)
    Registry.addItem("wrench", engWrench)
    Registry.addItem("redstoneWand", rsWand)
    Registry.addItem("handBoiler", handboiler)
    Registry.addItem("dust", dusts)
    Registry.addItem("slickwaterBucket", slickBucket)
    Registry.addItem("hemp", hempPlant)
    for( i <- 0 to 15 ) {
      Registry.addItem("paint" + colours(i), paintCans(i))
    }

    nutBucket.setCreativeTab( EngineersToolbox.tabItems )
    blankSide.setCreativeTab( EngineersToolbox.tabItems )
  }
}
