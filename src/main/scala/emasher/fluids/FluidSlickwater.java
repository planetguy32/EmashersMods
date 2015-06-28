package emasher.fluids;

import net.minecraftforge.fluids.Fluid;

public class FluidSlickwater extends Fluid {
	public FluidSlickwater() {
		super( "Slickwater" );
		this.setUnlocalizedName( "slickwater" );
	}

	@Override
	public String getLocalizedName() {
		return "Slickwater";
	}
	
	
}
