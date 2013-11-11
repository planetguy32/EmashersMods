package emasher.sockets.modules

import emasher.api._;
import emasher.core._;
import java.util._;
import emasher.sockets._;
import net.minecraft.item.crafting._;
import net.minecraftforge.oredict._;
import net.minecraftforge.common._;
import net.minecraft.item._;
import net.minecraft.entity.item._;
import net.minecraft.block._;
import net.minecraftforge.fluids._;

class ModHusher(id: Int) extends SocketModule(id, "sockets:husher")
{
	override def getLocalizedName = "Husher";
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "dad", "nbn", "dad", Character.valueOf('d'), Item.diamond, Character.valueOf('a'), "ingotAluminum",
				Character.valueOf('n'), "ingotNickel", Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 5)));
	}
	
	override def getToolTip(l: List[Object])
	{
		l.add("Sprays highly pressurized fluid");
		l.add("into the ground to extract resources");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Fluid Tank");
		l.add(SocketsMod.PREF_AQUA + "Uses 0 or 240 f/t");
		l.add(SocketsMod.PREF_YELLOW + "Outputs to machine output");
		l.add("See the wiki for further instructions");
	}
	
	override def hasTankIndicator = true;
	
	override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection):
	Boolean = 
	{
		if(side == ForgeDirection.DOWN) true;
		else false;
	}
	
	override def updateSide(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	{
		if(config.tank >= 0 && config.tank < 3)
		{
			config.meta -= 1;
			
			if(config.meta <= 0)
			{
				config.meta = 10;
				
				var pressure = false;
				var f = ts.getFluidInTank(config.tank);
				
				if(f != null && f.amount >= 1000)
				{
					for(i <- 0 to 5)
					{
						if(ts.getSide(ForgeDirection.getOrientation(i)).isInstanceOf[ModPressurizer])
						{
							pressure = true;
						}
					}
					
					if(ts.getEnergyStored() < 240) pressure = false;
					
					var tile = getTileToMine(config, ts, side);
					var canExtractBlock = false;
					var block = Block.blocksList(ts.worldObj.getBlockId(tile.x, tile.y, tile.z));
					
					if(block != null)
					{
						if(f.fluidID == FluidRegistry.WATER.getID)
						{
							if(block.getBlockHardness(ts.worldObj, tile.x, tile.y, tile.z) < Block.stone.blockHardness) canExtractBlock = true;
							else if(pressure && tile.y > 32) canExtractBlock = true;
							
							if(block.getBlockHardness(ts.worldObj, tile.x, tile.y, tile.z) >= Block.obsidian.blockHardness) canExtractBlock = false;
						}
						else if(f.fluidID == SocketsMod.fluidSlickwater.getID)
						{
							if(block.getBlockHardness(ts.worldObj, tile.x, tile.y, tile.z) < Block.obsidian.blockHardness) canExtractBlock = true;
							else if(pressure) canExtractBlock = true;
						}
						
						if(block.blockHardness < 0) canExtractBlock = false;
						
						if(canExtractBlock)
						{
							var items = block.getBlockDropped(ts.worldObj, tile.x, tile.y, tile.z, ts.worldObj.getBlockMetadata(tile.x, tile.y, tile.z), 0);
							for(i <- 0 to items.size - 1)
							{
								var item = items.get(i).copy;
								if(ts.forceOutputItem(item, false) == item.stackSize)
								{
									ts.forceOutputItem(item, true);
								}
								else
								{
									this.dropItemsOnSide(ts, ForgeDirection.UP, item);
								}
							}
							
							if(pressure) ts.useEnergy(240, false);
							ts.worldObj.removeBlockTileEntity(tile.x, tile.y, tile.z);
							if(f.fluidID == FluidRegistry.WATER.getID) ts.worldObj.setBlock(tile.x, tile.y, tile.z, Block.waterStill.blockID);
							else ts.worldObj.setBlock(tile.x, tile.y, tile.z, SocketsMod.blockSlickwater.blockID);
							ts.drainInternal(config.tank, 1000, true);
						}
					}
				}
			}
		}
		
	}
	
	def getTileToMine(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	:Tuple = 
	{
		var range = 1;
		for(i <- 0 to 5)
		{
			var m = ts.getSide(ForgeDirection.getOrientation(i));
			if(m.isInstanceOf[ModRangeSelector])
			{
				range = ts.getConfigForSide(ForgeDirection.getOrientation(i)).meta;
			}
		}
		
		var x = ts.xCoord + ts.worldObj.rand.nextInt(range * 2 + 1) - range;
		var z = ts.zCoord + ts.worldObj.rand.nextInt(range * 2 + 1) - range;
		
		var curY = ts.yCoord - 1;
		
		while((ts.worldObj.isAirBlock(x, curY, z) || ts.worldObj.getBlockId(x, curY, z) == Block.waterStill.blockID 
				|| ts.worldObj.getBlockId(x, curY, z) == SocketsMod.blockSlickwater.blockID) && curY > 0)
			
		{
			curY -= 1;
		}
		
		new Tuple(x, curY, z);
	}
	
	def dropItemsOnSide(ts: SocketTileAccess, side: ForgeDirection, stack: ItemStack)
	{
		if (! ts.worldObj.isRemote)
        {
			var xo = ts.xCoord + side.offsetX;
			var yo = ts.yCoord + side.offsetY;
			var zo = ts.zCoord + side.offsetZ;
            var f = 0.7F;
            var d0 = (ts.worldObj.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
            var d1 = (ts.worldObj.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
            var d2 = (ts.worldObj.rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
            var entityitem = new EntityItem(ts.worldObj, xo.asInstanceOf[Double] + d0, yo.asInstanceOf[Double] + d1, zo.asInstanceOf[Double] + d2, stack.copy());
            entityitem.delayBeforeCanPickup = 1;
            ts.worldObj.spawnEntityInWorld(entityitem);
        }
	}
	
	
}