package emasher.gas.modules

import emasher.api.SocketModule;
import emasher.sockets.SocketsMod;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.core.EmasherCore;
import emasher.gas.EmasherGas;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import emasher.gas.fluids.FluidGas;
import emasher.api._;

class ModGasGenerator(id: Int) extends SocketModule(id, "gascraft:gasGenerator", "gascraft:gasGeneratorActive")
{
	override def getLocalizedName = "Gas Generator";
	
	override def getToolTip(l: List[Object])
	{
		l.add("Generates power when fuled");
		l.add("with certain gases");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Fuel Tank");
		l.add(SocketsMod.PREF_AQUA + "Outputs Variable f/t");
	}
	
	override def getCurrentTexture(config: SideConfig):
	Int = 
	{
		if(config.rsControl(0)) 1;
		else 0;
	}
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ipi", "ufu", " b ", Character.valueOf('p'), EmasherCore.psu, Character.valueOf('i'), Item.ingotIron,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('f'), Block.furnaceIdle, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	override def hasTankIndicator = true;
	
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
	
	
	/*
	 * Config Dictionary
	 * 
	 * rsControl(0) -> Persistent generator is on or off
	 * rsControl(1) -> Smoke must be output before generation can continue
	 * rsControl(2) -> Current fuel produces smoke
	 * 
	 * Side Inventory
	 * 
	 * itemID 		-> Energy per tick
	 * itemDamage	-> Remaining time
	*/
	
	override def updateSide(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	{
		if(!config.rsLatch(2))
		{
			//Compatibility
			//This code block should eventually be removed
			config.rsLatch(2) = true;
			
			config.meta = 0;
			ts.sideInventory.setInventorySlotContents(side.ordinal, null);
			config.rsControl(0) = false;
			config.rsControl(1) = false;
			config.rsControl(2) = false;
		}
		
		if(config.tank >= 0 && config.tank < 3)
		{
			var initState = config.rsControl(0);
			var newState = initState;
			
			if(initState || config.rsControl(1)) //Generator is on
			{
				//Check to see if there's still fuel
				var is = ts.sideInventory.getStackInSlot(side.ordinal);
				
				if(config.rsControl(2) && (ts.worldObj.rand.nextInt(200) == 0 || config.rsControl(1)))
				{
					if(! outputSmoke(ts))
					{
						newState = false;
						config.rsControl(1) = true;
					}
					else
					{
						config.rsControl(1) = false;
					}
				}
				else
				{
					config.rsControl(1) = false;
				}
				
				if(is != null && newState)
				{
					//Check if the internal capacitor can hold any more energy
					
					if(ts.getMaxEnergyStored() - ts.getEnergyStored() >= is.itemID)
					{
						ts.addEnergy(is.itemID, false);
						is.setItemDamage(is.getItemDamage - 1);
						if(is.getItemDamage() <= 0)
						{
							ts.sideInventory.setInventorySlotContents(side.ordinal, null);
							newState = false;
						}
					}
					else
					{
						newState = false;
					}
				}
				
				if(is == null && ! newState && ! config.rsControl(1))
				{
					var theFluid = ts.getFluidInTank(config.tank);
					var fluidRec = null.asInstanceOf[FluidStack];
                    if(theFluid != null)
                    {
                    	fluidRec = GeneratorFuelRegistry.getFuel(theFluid.getFluid.getName);
                    	if(GeneratorFuelRegistry.isFuel(theFluid.getFluid.getName) && theFluid.amount >= fluidRec.amount 
                    			&& ts.getMaxEnergyStored() - ts.getEnergyStored() >= GeneratorFuelRegistry.getEnergyPerTick(theFluid.getFluid.getName))
                    	{
                    		var newStack = new ItemStack(GeneratorFuelRegistry.getEnergyPerTick(theFluid.getFluid.getName), 1, GeneratorFuelRegistry.getBurnTime(theFluid.getFluid.getName));
                    		ts.sideInventory.setInventorySlotContents(side.ordinal, newStack);
                    		config.rsControl(2) = GeneratorFuelRegistry.producesSmoke(theFluid.getFluid.getName);
                    		ts.drainInternal(config.tank, fluidRec.amount, true);
                    		newState = true;
                    	}
                    }
                    
				}
			}
			else if(! config.rsControl(1)) //Generator is off
			{
				var is = ts.sideInventory.getStackInSlot(side.ordinal);
				if(is == null)
				{
					var theFluid = ts.getFluidInTank(config.tank);
					var fluidRec = null.asInstanceOf[FluidStack];
                    if(theFluid != null)
                    {
                    	fluidRec = GeneratorFuelRegistry.getFuel(theFluid.getFluid.getName);
                    	if(GeneratorFuelRegistry.isFuel(theFluid.getFluid.getName) && theFluid.amount >= fluidRec.amount 
                    			&& ts.getMaxEnergyStored() - ts.getEnergyStored() >= GeneratorFuelRegistry.getEnergyPerTick(theFluid.getFluid.getName))
                    	{
                    		var newStack = new ItemStack(GeneratorFuelRegistry.getEnergyPerTick(theFluid.getFluid.getName), 1, GeneratorFuelRegistry.getBurnTime(theFluid.getFluid.getName));
                    		ts.sideInventory.setInventorySlotContents(side.ordinal, newStack);
                    		config.rsControl(2) = GeneratorFuelRegistry.producesSmoke(theFluid.getFluid.getName);
                    		ts.drainInternal(config.tank, fluidRec.amount, true);
                    		newState = true;
                    	}
                    }
				}
				else
				{
					if(ts.getMaxEnergyStored() - ts.getEnergyStored() >= is.itemID) newState = true;
				}
			}
			
			if(newState != initState)
			{
				config.rsControl(0) = newState;
				ts.sendClientSideState(side.ordinal);
			}
		}
		else
		{
			config.meta = 0;
			ts.sideInventory.setInventorySlotContents(side.ordinal, null);
			
			config.rsControl(1) = false;
			config.rsControl(2) = false;
			
			if(config.rsControl(0))
			{
				config.rsControl(0) = false;
				ts.sendClientSideState(side.ordinal);
			}
			
			
		}
		
	}
	
	def outputSmoke(ts: SocketTileAccess):
	Boolean = 
	{
		var te = ts.worldObj.getBlockTileEntity(ts.xCoord, ts.yCoord + 1, ts.zCoord);
		if(te != null && te.isInstanceOf[IGasReceptor])
		{
			if(te.asInstanceOf[IGasReceptor].recieveGas(new FluidStack(EmasherGas.fluidSmoke, 4000), ForgeDirection.DOWN, false) == 4000)
			{
				te.asInstanceOf[IGasReceptor].recieveGas(new FluidStack(EmasherGas.fluidSmoke, 4000), ForgeDirection.DOWN, true)
				true;
			}
			else false;
		}
		else if(ts.worldObj.isAirBlock(ts.xCoord, ts.yCoord + 1, ts.zCoord))
		{
			ts.worldObj.setBlock(ts.xCoord, ts.yCoord + 1, ts.zCoord, EmasherGas.smoke.blockID);
			true;
		}
		else false;
	}
	
	
}