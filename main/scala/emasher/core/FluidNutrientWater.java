package emasher.core;

import net.minecraftforge.fluids.Fluid;

public class FluidNutrientWater extends Fluid {

    public FluidNutrientWater()
    {
        super("nutrientWater");
        this.setUnlocalizedName("nutrientWater");
    }

    @Override
    public String getLocalizedName()
    {
        return "Nutrient Water";
    }
}
