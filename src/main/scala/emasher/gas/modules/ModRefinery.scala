package emasher.gas.modules

import emasher.api._
import emasher.sockets._
import emasher.core._
import java.util.List
import net.minecraft.item.crafting._
import net.minecraftforge.oredict._
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.item._
import net.minecraft.block._
import buildcraft.api.recipes._
import net.minecraft.item._
import net.minecraftforge.fluids._
import net.minecraft.init.{Blocks, Items}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import buildcraft.api.recipes.IRefineryRecipeManager.IRefineryRecipe
import emasher.gas.EmasherGas

class ModRefinery(id: Int) extends SocketModule(id, "gascraft:refinery")
{
	override def getLocalizedName = "Refinery"
	
	override def getToolTip(l: List[Object])
	{
		l.add("Refines certain fluids")
		l.add("into other fluids")
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Input tank")
		l.add(SocketsMod.PREF_AQUA + "Uses variable energy")
		l.add(SocketsMod.PREF_YELLOW + "Outputs to machine output")
	}
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "dpd", "dud", " b ", Character.valueOf('p'), EmasherCore.psu, Character.valueOf('d'), Items.diamond,
				Character.valueOf('u'), Items.bucket, Character.valueOf('f'), Blocks.glass, Character.valueOf('b'), SocketsMod.blankSide))
	}
	
	override def isMachine = true
	
	override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection):
	Boolean = 
	{
		if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) return false
		
		for(i <- 0 to 5)
		{
			var m = ts.getSide(ForgeDirection.getOrientation(i))
			if(m != null && m.isMachine)
			{
				return false
			}
		}
		
		true
	}
	
	override def hasTankIndicator = true
	
	override def onRemoved(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection)
	{
		ts.sideInventory.setInventorySlotContents(side.ordinal, null)
	}
	
	override def updateSide(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	{
		if(config.tank >= 0 && config.tank < 3)
		{
			if(config.meta <= 0) {
        if (config.rsControl(0)) ts.sendClientSideState(side.ordinal)
        config.rsControl(0) = false

        var fluid = ts.getFluidInTank(config.tank)
        var rec: IRefineryRecipe = null

        if(fluid != null) {

          if (BuildcraftRecipes.refinery != null) {
            rec = BuildcraftRecipes.refinery.findRefineryRecipe(fluid, null)
          }

          if (rec == null && fluid.isFluidEqual(new FluidStack(EmasherGas.fluidNaturalGas, 1000))) {
            rec = PropellentRecipe
          }

          ts.sideInventory.setInventorySlotContents(side.ordinal, null)

          if (rec != null) {
            if (ts.getEnergyStored() > rec.getEnergyCost * 5 && ts.getFluidInTank(config.tank).amount >= rec.getIngredient1.amount) {
              ts.useEnergy(rec.getEnergyCost * 5, false)
              ts.drainInternal(config.tank, rec.getIngredient1.amount, true)
              ts.sideInventory.setInventorySlotContents(side.ordinal, new ItemStack(Blocks.cobblestone, 1, rec.getResult.amount))
              config.inventory = rec.getResult.fluidID
              config.meta = rec.getTimeRequired
              ts.sendClientSideState(side.ordinal)
              config.rsControl(0) = true
            }
          }
        }
				
			}
			else
			{
				config.meta = config.meta - 1
				if(config.meta == 0)
				{
					var is = ts.sideInventory.getStackInSlot(side.ordinal)
					var fs = new FluidStack(config.inventory, is.getItemDamage())
					var amnt = ts.forceOutputFluid(fs, false)
					if(amnt == fs.amount) ts.forceOutputFluid(fs, true)
					else
					{
						config.rsControl(1) = true
						config.meta = 1
					}
				}
			}
		}
		else
		{
			config.meta = 0
			if(config.rsControl(0)) ts.sendClientSideState(side.ordinal)
			config.rsControl(0) = false
		}
	}

  @SideOnly(Side.CLIENT)
  override def getInternalTexture(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection): String = {
    if(config.meta == 0 || config.rsControl(1)) "sockets:inner_black"
    else "gascraft:inner_refinery"
  }

  @SideOnly(Side.CLIENT)
  override def getAllInternalTextures: Array[String] = {
    return Array[String]("gascraft:inner_refinery")
  }
}

object PropellentRecipe extends IRefineryRecipe {
  def getIngredient1: FluidStack = {
    new FluidStack(EmasherGas.fluidNaturalGas, 2)
  }

  def getIngredient2: FluidStack = {
    null
  }

  def getResult: FluidStack = {
    new FluidStack(EmasherGas.fluidPropellent, 1)
  }

  def getEnergyCost: Int = {
    1
  }

  def getTimeRequired: Int = {
    1
  }
}