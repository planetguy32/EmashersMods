package emasher.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;

public class PhotobioReactorRecipeRegistry
{
	public static class PhotobioReactorRecipe
	{
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - an ItemStack
		 * 
		 */
		
		private Object input;
		private FluidStack fluidInput;
		private FluidStack output;
		
		public PhotobioReactorRecipe(ItemStack input, FluidStack fluidInput, FluidStack output)
		{
			this.input = input;
			this.fluidInput = fluidInput;
			this.output = output;
		}
		
		public PhotobioReactorRecipe(String input, FluidStack fluidInput, FluidStack output)
		{
			this.input = input;
			this.fluidInput = fluidInput;
			this.output = output;
		}
		
		public Object getInput()
		{
			return input;
		}
		
		public FluidStack getFluidInput()
		{
			return fluidInput;
		}
		
		public FluidStack getOutput()
		{
			return output;
		}
	}
	
	private static ArrayList<PhotobioReactorRecipe> recipes = new ArrayList<PhotobioReactorRecipe>();
	
	public static void registerRecipe(PhotobioReactorRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static void registerRecipe(ItemStack input, FluidStack fluidInput, FluidStack output)
	{
		registerRecipe(new PhotobioReactorRecipe(input, fluidInput, output));
	}
	public static void registerRecipe(String input, FluidStack fluidInput, FluidStack output)
	{
		registerRecipe(new PhotobioReactorRecipe(input, fluidInput, output));
	}

	public static PhotobioReactorRecipe getRecipe(Object input, FluidStack fluidInput)
	{
		if(input instanceof ItemStack)
		{
			int oreID = OreDictionary.getOreID((ItemStack)input);
			for(PhotobioReactorRecipe r: recipes)
			{
				int otherID = -1;
				
				if(r.getInput() instanceof ItemStack)
				{
					otherID = OreDictionary.getOreID((ItemStack)r.getInput());
				}
				else if(r.getInput() instanceof String)
				{
					otherID = OreDictionary.getOreID((String)r.getInput());
				}
				
				if((otherID != -1 && otherID == oreID) || (r.getInput() instanceof ItemStack && ((ItemStack)input).isItemEqual((ItemStack)r.getInput())))
				{
					if(fluidInput.isFluidEqual(r.fluidInput)) return r;
				}
				
			}
		}
		else if(input instanceof String)
		{
			int oreID = OreDictionary.getOreID((String)input);
			for(PhotobioReactorRecipe r: recipes)
			{
				int otherID = -1;
				
				if(r.getInput() instanceof ItemStack)
				{
					otherID = OreDictionary.getOreID((ItemStack)r.getInput());
				}
				else if(r.getInput() instanceof String)
				{
					otherID = OreDictionary.getOreID((String)r.getInput());
				}
				
				if(otherID != -1 && otherID == oreID)
				{
					if(fluidInput.isFluidEqual(r.fluidInput)) return r;
				}
			}
		}
		
		return null;
	}
}
