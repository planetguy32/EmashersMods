package emasher.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrinderRecipeRegistry
{
	public static class GrinderRecipe
	{
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - an ItemStack
		 * 
		 */
		private Object input;
		private ItemStack output;
		
		public GrinderRecipe(ItemStack input, ItemStack output)
		{
			this.input = input;
			this.output = output;
		}
		
		public GrinderRecipe(String input, ItemStack output)
		{
			this.input = input;
			this.output = output;
		}
		
		public Object getInput()
		{
			return input;
		}
		
		public ItemStack getOutput()
		{
			return output;
		}
		
	}
	
	private static ArrayList<GrinderRecipe> recipes = new ArrayList<GrinderRecipe>();
	
	public static void registerRecipe(GrinderRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static void registerRecipe(ItemStack input, ItemStack output) { registerRecipe(new GrinderRecipe(input, output)); }
	public static void registerRecipe(String input, ItemStack output) { registerRecipe(new GrinderRecipe(input, output)); }

	public static boolean registerRecipe(ItemStack input, String output)
	{
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		if( ores.size() <= 0 )
			return false;
		registerRecipe(input, ores.get(0));
		return true;
	}

	public static boolean registerRecipe(String input, String output)
	{
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		if( ores.size() <= 0 )
			return false;
		registerRecipe(input, ores.get(0));
		return true;
	}

	public static boolean unregisterRecipe(Object input)
	{
		int ndx = getRecipeIndex(input);
		if(ndx == -1)
			return false;
		recipes.remove(ndx);
		return true;
	}

	public static GrinderRecipe getRecipe(Object input)
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
				GrinderRecipe r = recipes.get(i);
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
				GrinderRecipe r = recipes.get(i);
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
