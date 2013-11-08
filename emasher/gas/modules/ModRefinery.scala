package emasher.gas.modules

import emasher.api._;
import emasher.sockets._;
import emasher.core._;
import java.util.List;
import net.minecraft.item.crafting._;
import net.minecraftforge.oredict._;
import net.minecraftforge.common._;
import net.minecraft.item._;
import net.minecraft.block._;
import buildcraft.api.recipes._;
import net.minecraft.item._;
import net.minecraftforge.fluids._;

class ModRefinery(id: Int) extends SocketModule(id, "gascraft:refinery", "gascraft:refineryActive")
{
	override def getLocalizedName = "Refinery";
	
	override def getToolTip(l: List[Object])
	{
		l.add("Refines certain fluids");
		l.add("into other fluids");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Input tank");
		l.add(SocketsMod.PREF_AQUA + "Uses variable energy");
		l.add(SocketsMod.PREF_YELLOW + "Outputs to machine output");
	}
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "dpd", "dud", " b ", Character.valueOf('p'), EmasherCore.psu, Character.valueOf('d'), Item.diamond,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('f'), Block.glass, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	override def getCurrentTexture(config: SideConfig):
	Int = 
	{
		if(config.meta == 0 || config.rsControl(1)) 0;
		else 1;
	}
	
	override def isMachine = true;
	
	override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection):
	Boolean = 
	{
		if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) return false;
		
		for(i <- 0 to 5)
		{
			var m = ts.getSide(ForgeDirection.getOrientation(i))
			if(m != null && m.isMachine)
			{
				return false;
			}
		}
		
		true;
	}
	
	override def hasTankIndicator = true;
	
	override def onRemoved(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection)
	{
		ts.sideInventory.setInventorySlotContents(side.ordinal, null);
	}
	
	override def updateSide(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	{
		if(config.tank >= 0 && config.tank < 3)
		{
			if(config.meta <= 0)
			{
				if(config.rsControl(0)) ts.sendClientSideState(side.ordinal);
				config.rsControl(0) = false;
				
				var fluid = ts.getFluidInTank(config.tank);
				
				var rec = RefineryRecipes.findRefineryRecipe(fluid, null);
				ts.sideInventory.setInventorySlotContents(side.ordinal, null);
				
				if(rec != null)
				{
					if(ts.powerHandler.getEnergyStored() > rec.energy && ts.getFluidInTank(config.tank).amount >= rec.ingredient1.amount)
					{
						ts.powerHandler.useEnergy(rec.energy, rec.energy, true);
						ts.drainInternal(config.tank, rec.ingredient1.amount, true);
						ts.sideInventory.setInventorySlotContents(side.ordinal, new ItemStack(rec.result.fluidID, 1, rec.result.amount));
						config.meta = rec.delay;
						ts.sendClientSideState(side.ordinal);
						config.rsControl(0) = true;
					}
				}
				
			}
			else
			{
				config.meta = config.meta - 1;
				if(config.meta == 0)
				{
					var is = ts.sideInventory.getStackInSlot(side.ordinal);
					var fs = new FluidStack(is.itemID, is.getItemDamage());
					var amnt = ts.forceOutputFluid(fs, false);
					if(amnt == fs.amount) ts.forceOutputFluid(fs, true);
					else
					{
						config.rsControl(1) = true;
						config.meta = 1;
					}
				}
			}
		}
		else
		{
			config.meta = 0;
			if(config.rsControl(0)) ts.sendClientSideState(side.ordinal);
			config.rsControl(0) = false;
		}
	}
}