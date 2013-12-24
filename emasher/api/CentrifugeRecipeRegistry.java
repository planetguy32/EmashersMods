package emasher.api;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CentrifugeRecipeRegistry
{
	public static class CentrifugeRecipe
	{
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - an ItemStack
		 * 
		 */
		private Object input;
		private ItemStack output;
		private ItemStack secondaryOutput;
		private int percent;
		
		public CentrifugeRecipe(ItemStack input, ItemStack output, ItemStack secondaryOutput, int percent)
		{
			this.input = input;
			this.output = output;
			this.secondaryOutput = secondaryOutput;
			this.percent = percent;
			this.output.stackSize = 1;
			this.secondaryOutput.stackSize = 1;
		}
		
		public CentrifugeRecipe(String input, ItemStack output, ItemStack secondaryOutput, int percent)
		{
			this.input = input;
			this.output = output;
			this.secondaryOutput = secondaryOutput;
			this.percent = percent;
			this.output.stackSize = 1;
			this.secondaryOutput.stackSize = 1;
		}
		
		public Object getInput()
		{
			return input;
		}
		
		public ItemStack getOutput()
		{
			return output;
		}
		
		public ItemStack getSecondaryOutput()
		{
			return secondaryOutput;
		}
		
		public boolean shouldOuputSecondary(Random r)
		{
			return (r.nextInt(100) < percent);
		}
		
	}
	
	private static ArrayList<CentrifugeRecipe> recipes = new ArrayList<CentrifugeRecipe>();
	
	public static void registerRecipe(CentrifugeRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static void registerRecipe(ItemStack input, ItemStack output, ItemStack secondaryOutput, int percent) { registerRecipe(new CentrifugeRecipe(input, output, secondaryOutput, percent)); }
	public static void registerRecipe(String input, ItemStack output, ItemStack secondaryOutput, int percent) { registerRecipe(new CentrifugeRecipe(input, output, secondaryOutput, percent)); }

	public static boolean unregisterRecipe(Object input)
	{
		int ndx = getRecipeIndex(input);
		if(ndx == -1)
			return false;
		recipes.remove(ndx);
		return true;
	}

	public static CentrifugeRecipe getRecipe(Object input)
	{
		int ndx = getRecipeIndex(input);
		if(ndx == -1)
			return null;
		return recipes.get(ndx);
	}
	
	private static int getRecipeIndex(Object input)
	{
		if(input instanceof ItemStack)
		{
			int oreID = OreDictionary.getOreID((ItemStack)input);
			for(int i = 0; i < recipes.size(); i++)
			{
				CentrifugeRecipe r = recipes.get(i);
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
					return i;
				}
				
			}
		}
		else if(input instanceof String)
		{
			int oreID = OreDictionary.getOreID((String)input);
			for(int i = 0; i < recipes.size(); i++)
			{
				CentrifugeRecipe r = recipes.get(i);
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
					return i;
				}
			}
		}
		
		return -1;
	}
	
}
