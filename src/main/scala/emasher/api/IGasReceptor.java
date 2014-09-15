package emasher.api;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public interface IGasReceptor
{
	/**
	 * Called to offer gas to a particular receptor
	 * 
	 * @param gas - A LiquidStack specifying the type of gas, and amount offered
	 * @return An integer representing the amount of gas actually received in mB
	 */
	int recieveGas(FluidStack gas, ForgeDirection direction, boolean doFill);
}
