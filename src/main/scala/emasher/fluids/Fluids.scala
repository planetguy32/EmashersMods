package emasher.fluids

import emasher.blocks._
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{FluidStack, FluidContainerRegistry, FluidRegistry, Fluid}
import emasher.blocks.Blocks._
import emasher.items.Items._

object Fluids {
  var nutrientWaterFluid: Fluid = null
  var fluidSlickwater: Fluid = null
  var fluidNaturalGas: Fluid = null
  var fluidPropellent: Fluid = null
  var fluidHydrogen: Fluid = null
  var fluidSmoke: Fluid = null
  var fluidToxicGas: Fluid = null
  var fluidNeurotoxin: Fluid = null
  var fluidCorrosiveGas: Fluid = null
  var fluidPlasma: Fluid = null

  def init(): Unit = {
    nutrientWaterFluid = new FluidNutrientWater
    fluidSlickwater = new FluidSlickwater
    FluidRegistry.registerFluid(fluidSlickwater)
    FluidRegistry.registerFluid(nutrientWaterFluid)
    nutrientWater = new BlockNutrientWater(nutrientWaterFluid)
    blockSlickwater = new BlockSlickwater(fluidSlickwater)

    naturalGas = new BlockNaturalGas().setBlockUnbreakable().setBlockName("naturalGas").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    propellent = new BlockPropellent().setBlockUnbreakable().setBlockName("propellent").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    hydrogen = new BlockHydrogen().setBlockUnbreakable().setBlockName("hydrogen").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    smoke = new BlockSmoke().setBlockUnbreakable().setBlockName("smoke").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    toxicGas = new BlockWeaponizedGas().setBlockUnbreakable().setBlockName("toxicGas").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    neurotoxin = new BlockNeurotoxin().setBlockUnbreakable().setBlockName("neurotoxin").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    corrosiveGas = new BlockCorrosiveGas().setBlockUnbreakable().setBlockName("corrosiveGas").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    plasma = new BlockPlasma().setBlockUnbreakable().setBlockName("plasma").setResistance(0.0F).asInstanceOf[BlockGasGeneric]
    fluidNaturalGas = new FluidGas("gasCraft_naturalGas", naturalGas, 0)
    fluidPropellent = new FluidGas("gasCraft_propellent", propellent, 1)
    fluidHydrogen = new FluidGas("gasCraft_hydrogen", hydrogen, 2)
    fluidSmoke = new FluidGas("gasCraft_smoke", smoke, 3)
    fluidToxicGas = new FluidGas("gasCraft_toxicGas", toxicGas, 4)
    fluidNeurotoxin = new FluidGas("gasCraft_neurotoxin", neurotoxin, 5)
    fluidCorrosiveGas = new FluidGas("gasCraft_corrosiveGas", corrosiveGas, 6)
    fluidPlasma = new FluidGas("gasCraft_plasma", plasma, 7).setLuminosity(15).setTemperature(2600)

    naturalGas.blocksFluid = fluidNaturalGas
    propellent.blocksFluid = fluidPropellent
    hydrogen.blocksFluid = fluidHydrogen
    smoke.blocksFluid = fluidSmoke
    toxicGas.blocksFluid = fluidToxicGas
    neurotoxin.blocksFluid = fluidNeurotoxin
    corrosiveGas.blocksFluid = fluidCorrosiveGas
    plasma.blocksFluid = fluidPlasma
  }

  def register(): Unit = {
    FluidRegistry.registerFluid(fluidNaturalGas)
    FluidRegistry.registerFluid(fluidPropellent)
    FluidRegistry.registerFluid(fluidHydrogen)
    FluidRegistry.registerFluid(fluidSmoke)
    FluidRegistry.registerFluid(fluidToxicGas)
    FluidRegistry.registerFluid(fluidNeurotoxin)
    FluidRegistry.registerFluid(fluidCorrosiveGas)
    FluidRegistry.registerFluid(fluidPlasma)

    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidSlickwater, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(slickBucket), new ItemStack(Items.bucket))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(nutrientWaterFluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(nutBucket), new ItemStack(Items.bucket))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidNaturalGas, 4000), new ItemStack(vialFilled, 1, 0), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidPropellent, 4000), new ItemStack(vialFilled, 1, 1), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidHydrogen, 4000), new ItemStack(vialFilled, 1, 2), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidSmoke, 4000), new ItemStack(vialFilled, 1, 3), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidToxicGas, 4000), new ItemStack(vialFilled, 1, 4), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidNeurotoxin, 4000), new ItemStack(vialFilled, 1, 5), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidCorrosiveGas, 4000), new ItemStack(vialFilled, 1, 6), new ItemStack(vial))
    FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidPlasma, 4000), new ItemStack(vialFilled, 1, 7), new ItemStack(vial))
  }
}
