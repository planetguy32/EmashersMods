package emasher.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MultiSmelterRecipeRegistry
{
	public static class MultiSmelterRecipe
	{
		protected Object input1;
		protected Object input2;
		protected ItemStack output;
		
		public MultiSmelterRecipe(String input1, String input2, ItemStack output)
		{
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}
		
		public MultiSmelterRecipe(ItemStack input1, String input2, ItemStack output)
		{
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}
		
		public MultiSmelterRecipe(String input1, ItemStack input2, ItemStack output)
		{
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}
		
		public MultiSmelterRecipe(ItemStack input1, ItemStack input2, ItemStack output)
		{
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}
		
		public Object getInput1() { return input1; }
		public Object getInput2() { return input2; }
		public ItemStack getOutput() { return output; }
	}
	
	private static ArrayList<MultiSmelterRecipe> list = new ArrayList<MultiSmelterRecipe>();
	
	public static void registerRecipe(String input1, String input2, ItemStack output)
	{
		list.add(new MultiSmelterRecipe(input1, input2, output));
		list.add(new MultiSmelterRecipe(input2, input1, output));
	}
	
	public static void registerRecipe(ItemStack input1, String input2, ItemStack output)
	{
		list.add(new MultiSmelterRecipe(input1, input2, output));
		list.add(new MultiSmelterRecipe(input2, input1, output));
	}
	
	public static void registerRecipe(String input1, ItemStack input2, ItemStack output)
	{
		list.add(new MultiSmelterRecipe(input1, input2, output));
		list.add(new MultiSmelterRecipe(input2, input1, output));
	}
	
	public static void registerRecipe(ItemStack input1, ItemStack input2, ItemStack output)
	{
		list.add(new MultiSmelterRecipe(input1, input2, output));
		list.add(new MultiSmelterRecipe(input2, input1, output));
	}

	public static boolean registerRecipe(String input1, String input2, String output)
	{
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		if(ores.size() <= 0)
			return false;

		ItemStack output2 = ores.get(0);
		list.add(new MultiSmelterRecipe(input1, input2, output2));
		list.add(new MultiSmelterRecipe(input2, input1, output2));
		return true;
	}

	/*
	Need to handle removal of BOTH indeces!

	public static boolean unregisterRecipe(Object input1, Object input2)
	{
		int ndx = getRecipeIndexFor(input1, input2);
		if(ndx == -1)
			return false;
		list.remove(ndx);
		return true;
	}
	*/

	public static MultiSmelterRecipe getRecipeFor(Object input1, Object input2)
	{
		int ndx = getRecipeIndexFor(input1, input2);
		if(ndx == -1)
			return null;
		return list.get(ndx);
	}

	private static int getRecipeIndexFor(Object input1, Object input2)
	{
		int rID1, rID2, id1, id2;
		boolean firstMatch, secondMatch;

		for( int i = 0; i < list.size(); i++ )
		{
			MultiSmelterRecipe r = list.get(i);
			rID1 = -1;
			rID2 = -1;
			id1 = -1;
			id2 = -1;
			firstMatch = false;
			secondMatch = false;
			
			if(input1 instanceof ItemStack)
			{
				if(input2 instanceof ItemStack)
				{
					id1 = OreDictionary.getOreID((ItemStack)input1);
					id2 = OreDictionary.getOreID((ItemStack)input2);
					
					if(r.input1 instanceof ItemStack) rID1 = OreDictionary.getOreID((ItemStack)r.input1);
					else rID1 = OreDictionary.getOreID((String)r.input1);
					if(r.input2 instanceof ItemStack) rID2 = OreDictionary.getOreID((ItemStack)r.input2);
					else rID2 = OreDictionary.getOreID((String)r.input2);
					
					if(id1 == rID1 && id1 != -1) firstMatch = true;
					else if(r.input1 instanceof ItemStack && ((ItemStack)r.input1).isItemEqual((ItemStack)input1)) firstMatch = true;
					if(id2 == rID2 && id2 != -1) secondMatch = true;
					else if(r.input2 instanceof ItemStack && ((ItemStack)r.input2).isItemEqual((ItemStack)input2)) secondMatch = true;
					
					if(firstMatch && secondMatch) return i;
				}
				else if(input2 instanceof String)
				{
					id1 = OreDictionary.getOreID((ItemStack)input1);
					id2 = OreDictionary.getOreID((String)input2);
					
					if(r.input1 instanceof ItemStack) rID1 = OreDictionary.getOreID((ItemStack)r.input1);
					else rID1 = OreDictionary.getOreID((String)r.input1);
					if(r.input2 instanceof ItemStack) rID2 = OreDictionary.getOreID((ItemStack)r.input2);
					else rID2 = OreDictionary.getOreID((String)r.input2);
					
					if(id1 == rID1 && id1 != -1) firstMatch = true;
					else if(r.input1 instanceof ItemStack && ((ItemStack)r.input1).isItemEqual((ItemStack)input1)) firstMatch = true;
					if(id2 == rID2 && id2 != -1) secondMatch = true;
					
					if(firstMatch && secondMatch) return i;
				}
			}
			else if(input1 instanceof String)
			{
				if(input2 instanceof ItemStack)
				{
					id1 = OreDictionary.getOreID((String)input1);
					id2 = OreDictionary.getOreID((ItemStack)input2);
					
					if(r.input1 instanceof ItemStack) rID1 = OreDictionary.getOreID((ItemStack)r.input1);
					else rID1 = OreDictionary.getOreID((String)r.input1);
					if(r.input2 instanceof ItemStack) rID2 = OreDictionary.getOreID((ItemStack)r.input2);
					else rID2 = OreDictionary.getOreID((String)r.input2);
					
					if(id1 == rID1 && id1 != -1) firstMatch = true;
					if(id2 == rID2 && id2 != -1) secondMatch = true;
					else if(r.input2 instanceof ItemStack && ((ItemStack)r.input2).isItemEqual((ItemStack)input2)) secondMatch = true;
					
					if(firstMatch && secondMatch) return i;
				}
				else if(input2 instanceof String)
				{
					id1 = OreDictionary.getOreID((String)input1);
					id2 = OreDictionary.getOreID((String)input2);
					
					if(r.input1 instanceof ItemStack) rID1 = OreDictionary.getOreID((ItemStack)r.input1);
					else rID1 = OreDictionary.getOreID((String)r.input1);
					if(r.input2 instanceof ItemStack) rID2 = OreDictionary.getOreID((ItemStack)r.input2);
					else rID2 = OreDictionary.getOreID((String)r.input2);
					
					if(id1 == rID1 && id1 != -1) firstMatch = true;
					if(id2 == rID2 && id2 != -1) secondMatch = true;
					
					if(firstMatch && secondMatch) return i;
				}
			}
		}
		
		return -1;
	}
}
