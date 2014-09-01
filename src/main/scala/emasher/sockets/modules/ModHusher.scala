package emasher.sockets.modules

import emasher.api
import emasher.api.{SideConfig, SocketTileAccess, SocketModule};
import emasher.core._;
import java.util._;
import emasher.sockets._
import net.minecraft.init.{Blocks, Items}
;
import net.minecraft.item.crafting._;
import net.minecraftforge.oredict._;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.item._;
import net.minecraft.entity.item._;
import net.minecraft.block._;
import net.minecraftforge.fluids._;

class ModHusher(id: Int) extends SocketModule(id, "sockets:husher")
{
	override def getLocalizedName = "Husher";
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "dad", "nbn", "dad", Character.valueOf('d'), Items.diamond, Character.valueOf('a'), "ingotAluminum",
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
		l.add(SocketsMod.PREF_AQUA + "Uses 0 or 240 RF/t");
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
					var block = ts.getWorldObj().getBlock(tile.x, tile.y, tile.z);
					
					if(block != null)
					{
						if(f.fluidID == FluidRegistry.WATER.getID)
						{
							if(block.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z) < Blocks.stone.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z)) canExtractBlock = true;
							else if(pressure && tile.y > 32) canExtractBlock = true;
							
							if(block.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z) >= Blocks.obsidian.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z)) canExtractBlock = false;
						}
						else if(f.fluidID == SocketsMod.fluidSlickwater.getID)
						{
							if(block.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z) < Blocks.obsidian.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z)) canExtractBlock = true;
							else if(pressure) canExtractBlock = true;
						}
						
						if(block.getBlockHardness(ts.getWorldObj(), tile.x, tile.y, tile.z) < 0) canExtractBlock = false;
						
						if(canExtractBlock)
						{
							var items = block.getDrops(ts.getWorldObj(), tile.x, tile.y, tile.z, ts.getWorldObj().getBlockMetadata(tile.x, tile.y, tile.z), 0);
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
							ts.getWorldObj().removeTileEntity(tile.x, tile.y, tile.z);
							if(f.fluidID == FluidRegistry.WATER.getID) ts.getWorldObj().setBlock(tile.x, tile.y, tile.z, Blocks.water);
							else ts.getWorldObj().setBlock(tile.x, tile.y, tile.z, SocketsMod.blockSlickwater);
							ts.drainInternal(config.tank, 1000, true);
						}
					}
				}
			}
		}
		
	}
	
	def getTileToMine(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection): Coords =
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
		
		var x = ts.xCoord + ts.getWorldObj().rand.nextInt(range * 2 + 1) - range;
		var z = ts.zCoord + ts.getWorldObj().rand.nextInt(range * 2 + 1) - range;
		
		var curY = ts.yCoord - 1;
		
		while((ts.getWorldObj().isAirBlock(x, curY, z) || ts.getWorldObj().getBlock(x, curY, z) == Blocks.water
				|| ts.getWorldObj().getBlock(x, curY, z) == SocketsMod.blockSlickwater) && curY > 0)
			
		{
			curY -= 1;
		}
		
		new Coords(x, curY, z);
	}
	
	def dropItemsOnSide(ts: SocketTileAccess, side: ForgeDirection, stack: ItemStack)
	{
		if (! ts.getWorldObj().isRemote)
        {
			var xo = ts.xCoord + side.offsetX;
			var yo = ts.yCoord + side.offsetY;
			var zo = ts.zCoord + side.offsetZ;
            var f = 0.7F;
            var d0 = (ts.getWorldObj().rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
            var d1 = (ts.getWorldObj().rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
            var d2 = (ts.getWorldObj().rand.nextFloat() * f).asInstanceOf[Double] + (1.0F - f).asInstanceOf[Double] * 0.5D;
            var entityitem = new EntityItem(ts.getWorldObj(), xo.asInstanceOf[Double] + d0, yo.asInstanceOf[Double] + d1, zo.asInstanceOf[Double] + d2, stack.copy());
            entityitem.delayBeforeCanPickup = 1;
            ts.getWorldObj().spawnEntityInWorld(entityitem);
        }
	}
	
	
}