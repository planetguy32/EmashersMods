package emasher.util

import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.event.FMLInterModComms
import cpw.mods.fml.common.registry.GameRegistry
import emasher.api._
import emasher.items.{ItemDusts, ItemHempOil}
import mods.railcraft.api.fuel.FuelManager
import net.minecraft.init.{Items, Blocks}
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{FurnaceRecipes, IRecipe, CraftingManager}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.{Fluid, FluidRegistry, FluidStack}
import net.minecraftforge.oredict.{OreDictionary, ShapelessOreRecipe, ShapedOreRecipe}
import emasher.blocks.Blocks._
import emasher.items.Items._
import emasher.util.Config._
import emasher.fluids.Fluids._

object Recipes {
  var dyes: Array[String] = Array("dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite")

  def registerCrafing(): Unit = {
    GameRegistry.addShapelessRecipe(new ItemStack(mixedDirt, 2), Blocks.dirt, Blocks.gravel )
    GameRegistry.addShapelessRecipe(new ItemStack(mixedSand, 2), Blocks.sand, Blocks.gravel)
    GameRegistry.addRecipe(new ItemStack(normalCube, 4, 1), "##", "##", Character.valueOf('#'), redSandStone)
    GameRegistry.addRecipe(new ItemStack(normalCube, 4, 2), "##", "##", Character.valueOf('#'), limestone)
    GameRegistry.addShapelessRecipe(new ItemStack(normalCube, 2, 3), new ItemStack(normalCube, 1, 2), mixedSand)
    GameRegistry.addShapelessRecipe(new ItemStack(normalCube, 2, 4), Blocks.cobblestone, Blocks.dirt)
    GameRegistry.addShapelessRecipe(new ItemStack(normalCube, 2, 4), Blocks.cobblestone, mixedDirt)
    GameRegistry.addShapelessRecipe(new ItemStack(hempSeeds, 1), hempPlant)
    GameRegistry.addShapelessRecipe(new ItemStack(hempOil, 1), hempSeeds, Items.bowl)
    GameRegistry.addRecipe(new ItemStack(Items.string, 3), "#  ", " # ", "  #", Character.valueOf('#'), hempPlant)
    GameRegistry.addRecipe(new ItemStack(Items.paper, 3), "###", Character.valueOf('#'), hempPlant)
    GameRegistry.addRecipe(new ItemStack(hempCap, 1), "###", "# #", Character.valueOf('#'), hempPlant)
    GameRegistry.addRecipe(new ItemStack(hempTunic, 1), "# #", "###", "###", Character.valueOf('#'), hempPlant)
    GameRegistry.addRecipe(new ItemStack(hempPants, 1), "###", "# #", "# #", Character.valueOf('#'), hempPlant)
    GameRegistry.addRecipe(new ItemStack(hempShoes, 1), "# #", "# #", Character.valueOf('#'), hempPlant)
    GameRegistry.addRecipe(new ItemStack(chainSheet, 6), "# #", " # ", "#  ", Character.valueOf('#'), Items.iron_ingot)
    GameRegistry.addRecipe(new ItemStack(fenceWire, 6), "###", " B ", Character.valueOf('#'), Items.iron_ingot, Character.valueOf('B'), Blocks.wooden_button)
    GameRegistry.addRecipe(new ItemStack(chainFence, 16, 0), "###", "###", Character.valueOf('#'), chainSheet)
    GameRegistry.addRecipe(new ItemStack(chainFence, 8, 2), " I ", "###", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('#'), fenceWire)
    GameRegistry.addRecipe(new ItemStack(chainFence, 8, 2), "###", " I ", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('#'), fenceWire)
    GameRegistry.addRecipe(new ItemStack(chainFence, 8, 5), "II ", "###", " II", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('#'), fenceWire)
    GameRegistry.addRecipe(new ItemStack(chainFence, 8, 5), " II", "###", "II ", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('#'), fenceWire)
    GameRegistry.addRecipe(new ItemStack(Items.chainmail_helmet), "###", "# #", Character.valueOf('#'), chainSheet)
    GameRegistry.addRecipe(new ItemStack(Items.chainmail_chestplate), "# #", "###", "###", Character.valueOf('#'), chainSheet)
    GameRegistry.addRecipe(new ItemStack(Items.chainmail_leggings), "###", "# #", "# #", Character.valueOf('#'), chainSheet)
    GameRegistry.addRecipe(new ItemStack(Items.chainmail_boots), "# #", "# #", Character.valueOf('#'), chainSheet)
    GameRegistry.addRecipe(new ItemStack(sandbag, 8), "www", "wsw", "www", Character.valueOf('w'), Blocks.wool, Character.valueOf('s'), Blocks.sand)
    GameRegistry.addRecipe(new ItemStack(deflectorBase, 4), "odo", "clc", "omo", Character.valueOf('o'), Blocks.obsidian, Character.valueOf('d'), Items.diamond, Character.valueOf('c'), circuit, Character.valueOf('l'), Blocks.redstone_lamp, Character.valueOf('m'), machine)
    GameRegistry.addShapelessRecipe(new ItemStack(directionChanger, 4), machine, Items.glowstone_dust, new ItemStack(gem, 1, 0) )
    GameRegistry.addRecipe(new ItemStack(socket), " b ", "pmc", " h ", Character.valueOf('m'), machine, Character.valueOf('h'), Blocks.chest, Character.valueOf('b'), Items.bucket, Character.valueOf('p'), psu, Character.valueOf('i'), Items.iron_ingot, Character.valueOf('c'), circuit)
    GameRegistry.addRecipe(new ItemStack(remote), "e", "c", "s", Character.valueOf('e'), Items.ender_pearl, Character.valueOf('c'), circuit, Character.valueOf('s'), blankSide)
    GameRegistry.addRecipe(new ItemStack(engWrench), " i ", "ii ", "  b", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('b'), Blocks.stone_button)
    GameRegistry.addRecipe(new ItemStack(rsWand), "rt ", "tc ", "  w", Character.valueOf('r'), Blocks.redstone_block, Character.valueOf('t'), Blocks.redstone_torch, Character.valueOf('c'), circuit, Character.valueOf('w'), engWrench)
    GameRegistry.addRecipe(new ItemStack(handboiler, 1), "bbb", "ici", " n ", Character.valueOf('b'), Items.blaze_rod, Character.valueOf('c'), Items.fire_charge, Character.valueOf('i'), Items.iron_ingot, Character.valueOf('n'), circuit)

    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), Items.gold_nugget, Character.valueOf('i'), "ingotAluminum"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), Items.gold_nugget, Character.valueOf('i'), "ingotTin"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), Items.gold_nugget, Character.valueOf('i'), Items.iron_ingot))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), "ingotAluminum"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), "ingotTin"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(machine, 2), "gig", "i i", "gig", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), Items.iron_ingot))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'), Items.gold_nugget, Character.valueOf('i'), "ingotLead", Character.valueOf('r'), Items.redstone))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'), Items.gold_nugget, Character.valueOf('i'), Items.quartz, Character.valueOf('r'), Items.redstone))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), "ingotLead", Character.valueOf('r'), Items.redstone))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(psu, 2), "igi", "rrr", "igi", Character.valueOf('g'), "ingotCopper", Character.valueOf('i'), Items.quartz, Character.valueOf('r'), Items.redstone))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(circuit, 2), "rrr", "ggg", Character.valueOf('r'), Items.redstone, Character.valueOf('g'), Items.gold_nugget))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(circuit, 2), "rrr", "ggg", Character.valueOf('r'), Items.redstone, Character.valueOf('g'), "ingotCopper"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(chainSheet, 6), "# #", " # ", "#  ", Character.valueOf('#'), "ingotAluminum"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(chainSheet, 6), "# #", " # ", "#  ", Character.valueOf('#'), "ingotTin"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(fenceWire, 6), "###", " B ", Character.valueOf('#'), "ingotAluminum", Character.valueOf('B'), Blocks.wooden_button))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(fenceWire, 6), "###", " B ", Character.valueOf('#'), "ingotTin", Character.valueOf('B'), Blocks.wooden_button))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 1), "ingotAluminum", new ItemStack(chainFence, 1, 0)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 1), "ingotTin", new ItemStack(chainFence, 1, 0)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 1), Items.iron_ingot, new ItemStack(chainFence, 1, 0)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 4), "ingotAluminum", new ItemStack(chainFence, 1, 2)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 4), "ingotTin", new ItemStack(chainFence, 1, 2)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 4), Items.iron_ingot, new ItemStack(chainFence, 1, 2)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(chainFence, 1, 3), Blocks.fence, new ItemStack(chainFence, 1, 2)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(emeryTile, 1), "##", "##", Character.valueOf('#'), "gemEmery"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(gem, 4, 0), emeryTile))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(vial, 8), "s", "g", "g", Character.valueOf('g'), Blocks.glass, Character.valueOf('s'), Items.string))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(gasMask), "lll", "lgl", "vwv", Character.valueOf('g'), Blocks.glass, Character.valueOf('l'), Items.leather, Character.valueOf('v'), vial, Character.valueOf('w'), Blocks.wool))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(smokeGrenade), " i ", "isi", " i ", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('s'), new ItemStack(vialFilled, 1, 3)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(smokeGrenade), " i ", "isi", " i ", Character.valueOf('i'), "ingotAluminum", Character.valueOf('s'), new ItemStack(vialFilled, 1, 3)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(smokeGrenade), " i ", "isi", " i ", Character.valueOf('i'), "ingotTin", Character.valueOf('s'), new ItemStack(vialFilled, 1, 3)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(chimney), " i ", "i i", " i ", Character.valueOf('i'), Items.brick))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(frame, 4), "s s", " s ", "s e", Character.valueOf('s'), "ingotSteel", Character.valueOf('e'), new ItemStack(gem, 1, 0)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(frame, 4), "s s", " s ", "s e", Character.valueOf('s'), "ingotBronze", Character.valueOf('e'), new ItemStack(gem, 1, 0)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(blankSide, 12), "ggg", "reb", "sms", Character.valueOf('m'), machine, Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('s'), Items.glowstone_dust, Character.valueOf('r'), "dyeRed", Character.valueOf('e'), "dyeGreen", Character.valueOf('b'), "dyeBlue"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(blockFluidPipe, 16), "sss", "ccc", "sss", Character.valueOf('s'), Blocks.stone, Character.valueOf('c'), "ingotCopper"))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(blockEnergyPipe, 16), "sgs", "rcr", "sgs", Character.valueOf('s'), Blocks.stone, Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('c'), "ingotCopper", Character.valueOf('r'), new ItemStack(rsIngot)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(blockEnergyPipe, 16), "sgs", "rcr", "sgs", Character.valueOf('s'), Blocks.stone, Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('c'), Items.gold_nugget, Character.valueOf('r'), new ItemStack(rsIngot)))
    CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(blockStartPipe, 1), "sss", " l ", "sss", Character.valueOf('s'), Blocks.stone_slab, Character.valueOf('l'), Blocks.lever))


    for( i <- 0 to 8 ) {
      GameRegistry.addRecipe(new ItemStack(metal, 1, i), "iii", "iii", "iii", Character.valueOf('i'), new ItemStack(ingot, 1, i))
      CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapelessOreRecipe(new ItemStack(ingot, 9, i), new ItemStack(metal, 1, i)))
    }

    for( i <- 0 to 15 ) {
      CraftingManager.getInstance.getRecipeList.asInstanceOf[java.util.List[IRecipe]].add(new ShapedOreRecipe(new ItemStack(paintCans(i), 1), "i", "p", "d", Character.valueOf('i'), Blocks.stone_button, Character.valueOf('p'), new ItemStack(vialFilled, 1, 1), Character.valueOf('d'), dyes(i)))
    }

    if( enableMiniPortal ) {
      GameRegistry.addRecipe(new ItemStack(miniPortal), "ooo", "oso", "ooo", Character.valueOf('o'), Blocks.obsidian, Character.valueOf('s'), rsIngot)
    }

    for( i <- 0 to ( ModuleRegistry.numModules - 1 ) ) {
      if (ModuleRegistry.getModule(i) != null) {
        ModuleRegistry.getModule(i).addRecipe()
      }
    }
  }

  def registerSmelting(): Unit = {
    GameRegistry.registerFuelHandler(hempOil.asInstanceOf[ItemHempOil])

    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 0), new ItemStack(ingot, 1, 0), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 1), new ItemStack(ingot, 1, 8), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 2), new ItemStack(gem, 1, 0), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 3), new ItemStack(ingot, 1, 3), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 4), new ItemStack(ingot, 1, 2), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 5), new ItemStack(ingot, 1, 4), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 6), new ItemStack(gem, 1, 1), 1.0F)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(ore, 1, 7), new ItemStack(gem, 1, 2), 1.0F)
    FurnaceRecipes.smelting.func_151396_a(hempPlant, new ItemStack(Items.dye, 1, 2), 0.1F)

    var cobalt: ItemStack = null
    var ardite: ItemStack = null
    var list = OreDictionary.getOres("ingotCobalt")
    if (list.size > 0) cobalt = list.get(0)
    list = OreDictionary.getOres("ingotArdite")
    if (list.size > 0) ardite = list.get(0)

    if (cobalt != null) {
      FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundCobalt.ordinal), new ItemStack(cobalt.getItem, 2, cobalt.getItemDamage), 1)
      FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureCobaltDust.ordinal), new ItemStack(cobalt.getItem, 1, cobalt.getItemDamage), 1)
      FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureCobaltDust.ordinal), new ItemStack(cobalt.getItem, 1, cobalt.getItemDamage), 1)
    }

    if (ardite != null) {
      FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundArdite.ordinal), new ItemStack(ardite.getItem, 2, ardite.getItemDamage), 1)
      FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureArditeDust.ordinal), new ItemStack(ardite.getItem, 1, ardite.getItemDamage), 1)
      FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureArditeDust.ordinal), new ItemStack(ardite.getItem, 1, ardite.getItemDamage), 1)
    }

    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundGold.ordinal), new ItemStack(Items.gold_ingot, 2), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundIron.ordinal), new ItemStack(Items.iron_ingot, 2), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundBauxite.ordinal), new ItemStack(ingot, 2, 0), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundCassiterite.ordinal), new ItemStack(ingot, 2, 8), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundNativeCopper.ordinal), new ItemStack(ingot, 2, 2), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundPentlandite.ordinal), new ItemStack(ingot, 2, 4), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundGalena.ordinal), new ItemStack(ingot, 2, 3), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.groundSilver.ordinal), new ItemStack(ingot, 2, 6), 1)

    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureGoldDust.ordinal), new ItemStack(Items.gold_ingot), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureIronDust.ordinal), new ItemStack(Items.iron_ingot), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureAluminiumDust.ordinal), new ItemStack(ingot, 1, 0), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureTinDust.ordinal), new ItemStack(ingot, 1, 8), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureCopperDust.ordinal), new ItemStack(ingot, 1, 2), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureNickelDust.ordinal), new ItemStack(ingot, 1, 4), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureLeadDust.ordinal), new ItemStack(ingot, 1, 3), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.impureSilverDust.ordinal), new ItemStack(ingot, 1, 6), 1)

    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureGoldDust.ordinal), new ItemStack(Items.gold_ingot), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureIronDust.ordinal), new ItemStack(Items.iron_ingot), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureAluminiumDust.ordinal), new ItemStack(ingot, 1, 0), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureTinDust.ordinal), new ItemStack(ingot, 1, 8), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureCopperDust.ordinal), new ItemStack(ingot, 1, 2), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureNickelDust.ordinal), new ItemStack(ingot, 1, 4), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureLeadDust.ordinal), new ItemStack(ingot, 1, 3), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.pureSilverDust.ordinal), new ItemStack(ingot, 1, 6), 1)
    FurnaceRecipes.smelting.func_151394_a(new ItemStack(dusts, 1, ItemDusts.Const.purePlatinumDust.ordinal), new ItemStack(ingot, 1, 5), 1)
  }

  def registerGrinder(): Unit = {
    GrinderRecipeRegistry.registerRecipe(new ItemStack(limestone), new ItemStack(groundLimestone))

    GrinderRecipeRegistry.registerRecipe("oreGold", new ItemStack(dusts, 1, ItemDusts.Const.groundGold.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreIron", new ItemStack(dusts, 1, ItemDusts.Const.groundIron.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreAluminum", new ItemStack(dusts, 1, ItemDusts.Const.groundBauxite.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreAluminium", new ItemStack(dusts, 1, ItemDusts.Const.groundBauxite.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreTin", new ItemStack(dusts, 1, ItemDusts.Const.groundCassiterite.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreCopper", new ItemStack(dusts, 1, ItemDusts.Const.groundNativeCopper.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreNickel", new ItemStack(dusts, 1, ItemDusts.Const.groundPentlandite.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreLead", new ItemStack(dusts, 1, ItemDusts.Const.groundGalena.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreSilver", new ItemStack(dusts, 1, ItemDusts.Const.groundSilver.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreCobalt", new ItemStack(dusts, 1, ItemDusts.Const.groundCobalt.ordinal))
    GrinderRecipeRegistry.registerRecipe("oreArdite", new ItemStack(dusts, 1, ItemDusts.Const.groundArdite.ordinal))

    GrinderRecipeRegistry.registerRecipe("oreLapis", new ItemStack(Items.dye, 16, 4))
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Blocks.coal_ore), new ItemStack(Items.coal, 2))
    GrinderRecipeRegistry.registerRecipe("oreDiamond", new ItemStack(Items.diamond, 2))
    GrinderRecipeRegistry.registerRecipe("oreEmerald", new ItemStack(Items.emerald, 2))
    GrinderRecipeRegistry.registerRecipe("oreRedstone", new ItemStack(Items.redstone, 8))
    GrinderRecipeRegistry.registerRecipe("oreQuartz", new ItemStack(Items.quartz, 2))
    GrinderRecipeRegistry.registerRecipe("oreEmery", new ItemStack(gem, 4, 0))
    GrinderRecipeRegistry.registerRecipe("oreRuby", new ItemStack(gem, 2, 1))
    GrinderRecipeRegistry.registerRecipe("oreSapphire", new ItemStack(gem, 2, 2))

    GrinderRecipeRegistry.registerRecipe(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.sand))
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Blocks.stone), new ItemStack(Blocks.gravel))
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Items.blaze_rod), new ItemStack(Items.blaze_powder, 5))
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Items.bone), new ItemStack(Items.dye, 5, 15))
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Blocks.obsidian), "dustObsidian")
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Blocks.netherrack), "dustNetherrack")
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Items.coal), "dustCoal")
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Items.coal, 1), "dustCharcoal")
    GrinderRecipeRegistry.registerRecipe(new ItemStack(Items.clay_ball), "dustClay")
  }

  def registerMultiSmelter(): Unit = {
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundGold", new ItemStack(dusts, 3, ItemDusts.Const.impureGoldDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundIron", new ItemStack(dusts, 3, ItemDusts.Const.impureIronDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundAluminum", new ItemStack(dusts, 3, ItemDusts.Const.impureAluminiumDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundTin", new ItemStack(dusts, 3, ItemDusts.Const.impureTinDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundCopper", new ItemStack(dusts, 3, ItemDusts.Const.impureCopperDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundNickel", new ItemStack(dusts, 3, ItemDusts.Const.impureNickelDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundLead", new ItemStack(dusts, 3, ItemDusts.Const.impureLeadDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundSilver", new ItemStack(dusts, 3, ItemDusts.Const.impureSilverDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundCobalt", new ItemStack(dusts, 3, ItemDusts.Const.impureCobaltDust.ordinal))
    MultiSmelterRecipeRegistry.registerRecipe("dustQuicklime", "groundArdite", new ItemStack(dusts, 3, ItemDusts.Const.impureArditeDust.ordinal))

    MultiSmelterRecipeRegistry.registerRecipe("ingotCopper", "ingotTin", new ItemStack(ingot, 1, 1))
    MultiSmelterRecipeRegistry.registerRecipe(new ItemStack(Items.redstone), new ItemStack(Items.sugar), new ItemStack(rsIngot))
    MultiSmelterRecipeRegistry.registerRecipe("ingotCopper", new ItemStack(Items.gunpowder), new ItemStack(bluestone, 2))
  }

  def registerCentrifuge(): Unit = {
    CentrifugeRecipeRegistry.registerRecipe("dustImpureGold", new ItemStack(dusts, 1, ItemDusts.Const.pureGoldDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureSilverDust.ordinal), 5)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureIron", new ItemStack(dusts, 1, ItemDusts.Const.pureIronDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureIronDust.ordinal), 33)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureAluminum", new ItemStack(dusts, 1, ItemDusts.Const.pureAluminiumDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureIronDust.ordinal), 5)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureTin", new ItemStack(dusts, 1, ItemDusts.Const.pureTinDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureTinDust.ordinal), 33)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureCopper", new ItemStack(dusts, 1, ItemDusts.Const.pureCopperDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureCopperDust.ordinal), 33)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureNickel", new ItemStack(dusts, 1, ItemDusts.Const.pureNickelDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.purePlatinumDust.ordinal), 20)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureLead", new ItemStack(dusts, 1, ItemDusts.Const.pureLeadDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureSilverDust.ordinal), 50)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureSilver", new ItemStack(dusts, 1, ItemDusts.Const.pureSilverDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureLeadDust.ordinal), 10)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureCobalt", new ItemStack(dusts, 1, ItemDusts.Const.pureCobaltDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureArditeDust.ordinal), 10)
    CentrifugeRecipeRegistry.registerRecipe("dustImpureArdite", new ItemStack(dusts, 1, ItemDusts.Const.pureArditeDust.ordinal), new ItemStack(dusts, 1, ItemDusts.Const.pureCobaltDust.ordinal), 10)
  }

  def registerMixer(): Unit = {
    MixerRecipeRegistry.registerRecipe(new ItemStack(mixedDirt), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(fluidSlickwater, slickwaterAmount))
    MixerRecipeRegistry.registerRecipe(new ItemStack(mixedSand), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(fluidSlickwater, slickwaterAmount))
    MixerRecipeRegistry.registerRecipe(new ItemStack(Blocks.sand), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(fluidSlickwater, slickwaterAmount))
    MixerRecipeRegistry.registerRecipe(new ItemStack(groundLimestone), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(fluidSlickwater, slickwaterAmount))
    MixerRecipeRegistry.registerRecipe(new ItemStack(Items.dye, 1, 15), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(nutrientWaterFluid, 1000))
    MixerRecipeRegistry.registerRecipe(new ItemStack(Items.gunpowder), new FluidStack(fluidPropellent, 1000), new FluidStack(fluidToxicGas, 500))
    MixerRecipeRegistry.registerRecipe(new ItemStack(dusts, 1, ItemDusts.Const.lime.ordinal), new FluidStack(fluidPropellent, 100), new FluidStack(fluidCorrosiveGas, 100))
  }

  def registerPhotobioreactor(): Unit = {
    PhotobioReactorRecipeRegistry.registerRecipe(new ItemStack(algae), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(fluidHydrogen, 1000))
    PhotobioReactorRecipeRegistry.registerRecipe(new ItemStack(superAlgae), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(fluidHydrogen, 3000))
    PhotobioReactorRecipeRegistry.registerRecipe(new ItemStack(superAlgae), new FluidStack(fluidToxicGas, 1000), new FluidStack(fluidNeurotoxin, 500))
  }

  def registerGasGenerator(): Unit = {
    GeneratorFuelRegistry.registerFuel(new FluidStack(fluidNaturalGas, 1000), 500, 60, true)
    GeneratorFuelRegistry.registerFuel(new FluidStack(fluidHydrogen, 1000), 500, 60, false)

    val ethanol: FluidStack = FluidRegistry.getFluidStack("bioethanol", 1000)
    if (ethanol != null) GeneratorFuelRegistry.registerFuel(ethanol, 750, 40, false)
  }

  def register3rdPartyRecipes(): Unit = {
    //Railcraft
    FuelManager.addBoilerFuel(fluidNaturalGas, 20000)
    FuelManager.addBoilerFuel(fluidHydrogen, 20000)
    FuelManager.addBoilerFuel(fluidPlasma, 100000)

    //BuildCraft
    if (Loader.isModLoaded("BuildCraft|Core") && FluidRegistry.getFluid("fuel") != null) {
      val fuel: Fluid = FluidRegistry.getFluid("fuel")
      GeneratorFuelRegistry.registerFuel(new FluidStack(fuel, 1000), 1000, 60, true)
    }

    //Thermal Expansion
    var toSend: NBTTagCompound = new NBTTagCompound
    toSend.setString("fluidName", "gasCraft_naturalGas")
    toSend.setInteger("energy", 30000)
    FMLInterModComms.sendMessage("ThermalExpansion", "CompressionFuel", toSend)
    toSend = new NBTTagCompound
    toSend.setString("fluidName", "gasCraft_hydrogen")
    toSend.setInteger("energy", 30000)
    FMLInterModComms.sendMessage("ThermalExpansion", "CompressionFuel", toSend)
  }
}
