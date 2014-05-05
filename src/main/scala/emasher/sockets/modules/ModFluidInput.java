package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModFluidInput extends SocketModule
{

	public ModFluidInput(int id)
	{
		super(id, "sockets:fluidInput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Fluid Input";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Accepts fluid from adjacent pipes");
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
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "b", Character.valueOf('i'), Item.ingotIron, Character.valueOf('h'), Block.fenceIron,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('b'), SocketsMod.blankSide);
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
		boolean canIntake = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
			if(config.rsLatch[i] && ts.getRSControl(i)) canIntake = false;
		}
		
		if(canIntake)
		{
			if(config.tank != -1) return ts.fillInternal(config.tank, fluid, doFill);
		}
		
		return 0;
	}
}
