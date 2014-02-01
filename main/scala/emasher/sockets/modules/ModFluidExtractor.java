package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.fluids.FluidStack;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModFluidExtractor extends SocketModule
{

	public ModFluidExtractor(int id)
	{
		super(id, "sockets:fluidExtractor");
	}

	@Override
	public String getLocalizedName()
	{
		return "Fluid Extractor";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Pulls fluid from adjacent");
		l.add("tanks/machines/etc.");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Tank to input to");
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "u", "b", Character.valueOf('i'), Item.ingotIron, Character.valueOf('h'), Block.hopperBlock,
				Character.valueOf('u'), Block.fenceIron, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public boolean isFluidInterface() { return true; }
	
	@Override
	public boolean canInsertFluid() { return true; }
	
	@Override
	public int fluidFill(FluidStack fluid, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.tank != -1) return ts.fillInternal(config.tank, fluid, doFill);
		return 0;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.tank < 0 || config.tank > 2) return;
		
		boolean allOff = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					ts.tryExtractFluid(config.tank, side, 100);
					return;
				}
				allOff = false;
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					ts.tryExtractFluid(config.tank, side, 100);
					return;
				}
				allOff = false;
			}
		}
		
		if(allOff) ts.tryExtractFluid(config.tank, side, 100);
	}
}
