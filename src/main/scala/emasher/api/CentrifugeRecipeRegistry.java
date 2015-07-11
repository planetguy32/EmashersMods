package emasher.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Random;

public class CentrifugeRecipeRegistry {
	public static ArrayList<CentrifugeRecipe> recipes = new ArrayList<CentrifugeRecipe>();
	
	public static void registerRecipe( CentrifugeRecipe recipe ) {
		recipes.add( recipe );
	}
	
	public static void registerRecipe( ItemStack input, ItemStack output, ItemStack secondaryOutput, int percent ) {
		registerRecipe( new CentrifugeRecipe( input, output, secondaryOutput, percent ) );
	}
	
	public static void registerRecipe( String input, ItemStack output, ItemStack secondaryOutput, int percent ) {
		registerRecipe( new CentrifugeRecipe( input, output, secondaryOutput, percent ) );
	}

	public static void registerRecipe( String input, String output, String secondaryOutput, int percent ) {
		registerRecipe( new CentrifugeRecipe( input, output, secondaryOutput, percent ) );
	}

	public static CentrifugeRecipe getRecipe( Object input ) {
		if( input instanceof ItemStack ) {
			int[] oreIDs = OreDictionary.getOreIDs( ( ItemStack ) input );
			for( CentrifugeRecipe r : recipes ) {
				int[] otherIDs = null;

				if( r.getInput() instanceof ItemStack ) {
					otherIDs = OreDictionary.getOreIDs( ( ItemStack ) r.getInput() );
				} else if( r.getInput() instanceof String ) {
					otherIDs = new int[] { OreDictionary.getOreID( ( String ) r.getInput() ) };
				}

				if( Util.checkArrays( oreIDs, otherIDs )  || ( r.getInput() instanceof ItemStack && ( ( ItemStack ) input ).isItemEqual( ( ItemStack ) r.getInput() ) ) ) {
					if( r.getOutput() != null )
						return r;
				}

			}
		} else if( input instanceof String ) {
			int[] oreIDs = new int[] { OreDictionary.getOreID( ( String ) input ) };
			for( CentrifugeRecipe r : recipes ) {
				int[] otherIDs = null;

				if( r.getInput() instanceof ItemStack ) {
					otherIDs = OreDictionary.getOreIDs( ( ItemStack ) r.getInput() );
				} else if( r.getInput() instanceof String ) {
					otherIDs = new int[] { OreDictionary.getOreID( ( String ) r.getInput() ) };
				}

				if( Util.checkArrays( oreIDs, otherIDs) ) {
					if( r.getOutput() != null )
						return r;
				}
			}
		}

		return null;
	}

	public static class CentrifugeRecipe {
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - either an OreDictionary string, or an ItemStack
		 * secondaryOutput - either an OreDictionary string, or an ItemStack
		 */
		private Object input;
		private Object output;
		private Object secondaryOutput;
		private int percent;

		public CentrifugeRecipe( ItemStack input, ItemStack output, ItemStack secondaryOutput, int percent ) {
			this.input = input;
			this.output = output;
			this.secondaryOutput = secondaryOutput;
			this.percent = percent;
			( ( ItemStack ) this.output ).stackSize = 1;
			( ( ItemStack ) this.secondaryOutput ).stackSize = 1;
		}

		public CentrifugeRecipe( String input, ItemStack output, ItemStack secondaryOutput, int percent ) {
			this.input = input;
			this.output = output;
			this.secondaryOutput = secondaryOutput;
			this.percent = percent;
			( ( ItemStack ) this.output ).stackSize = 1;
			( ( ItemStack ) this.secondaryOutput ).stackSize = 1;
		}

		public CentrifugeRecipe( String input, String output, String secondaryOutput, int percent ) {
			this.input = input;
			this.output = output;
			this.secondaryOutput = secondaryOutput;
			this.percent = percent;
		}

		public Object getInput() {
			return input;
		}

		public ItemStack getOutput() {
			// convert to ItemStack on the first getOutput call
			if( this.output instanceof String ) {
				ArrayList<ItemStack> ores = OreDictionary.getOres( ( String ) this.output );
				if( ores.size() > 0 ) {
					ItemStack stack = ores.get( 0 ).copy();
					stack.stackSize = 1;
					this.output = stack;
				} else
					return null;
			}
			return ( ItemStack ) output;
		}

		public ItemStack getSecondaryOutput() {
			// convert to ItemStack
			if( secondaryOutput instanceof String ) {
				ArrayList<ItemStack> ores = OreDictionary.getOres( ( String ) this.secondaryOutput );
				if( ores.size() > 0 ) {
					ItemStack stack = ores.get( 0 ).copy();
					stack.stackSize = 1;
					this.secondaryOutput = stack;
				} else
					return null;
			}
			return ( ItemStack ) secondaryOutput;
		}

		public int getPercent() {
			return percent;
		}

		public boolean shouldOuputSecondary( Random r ) {
			if( secondaryOutput instanceof String && this.getSecondaryOutput() == null )
				return false;
			return ( r.nextInt( 100 ) < percent );
		}
	}
	
}
