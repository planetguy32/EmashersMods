package emasher.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class GrinderRecipeRegistry {
	public static ArrayList<GrinderRecipe> recipes = new ArrayList<GrinderRecipe>();

	public static void registerRecipe( GrinderRecipe recipe ) {
		recipes.add( recipe );
	}

	public static void registerRecipe( ItemStack input, ItemStack output ) {
		registerRecipe( new GrinderRecipe( input, output ) );
	}

	public static void registerRecipe( String input, ItemStack output ) {
		registerRecipe( new GrinderRecipe( input, output ) );
	}

	public static void registerRecipe( String input, String output ) {
		registerRecipe( new GrinderRecipe( input, output ) );
	}

	public static void registerRecipe( ItemStack input, String output ) {
		registerRecipe( new GrinderRecipe( input, output ) );
	}

	public static GrinderRecipe getRecipe( Object input ) {
		if( input instanceof ItemStack ) {
			int[] oreIDs = OreDictionary.getOreIDs( ( ItemStack ) input );
			int oreID = -1;
			if( oreIDs.length > 0 ) {
				oreID = oreIDs[0];
			}
			for( GrinderRecipe r : recipes ) {
				int otherID = -1;

				if( r.getInput() instanceof ItemStack ) {
					int[] otherIDs = OreDictionary.getOreIDs( ( ItemStack ) r.getInput() );
					if( otherIDs.length > 0 ) {
						otherID = otherIDs[0];
					}
				} else if( r.getInput() instanceof String ) {
					otherID = OreDictionary.getOreID( ( String ) r.getInput() );
				}

				if( ( otherID != -1 && otherID == oreID ) ||
						( r.getInput() instanceof ItemStack && ( ( ItemStack ) input ).isItemEqual( ( ItemStack ) r.getInput() ) ) ) {
					if( r.getOutput() != null )
						return r;
				}

			}
		} else if( input instanceof String ) {
			int oreID = OreDictionary.getOreID( ( String ) input );
			for( GrinderRecipe r : recipes ) {
				int otherID = -1;

				if( r.getInput() instanceof ItemStack ) {
					otherID = OreDictionary.getOreID( ( ItemStack ) r.getInput() );
				} else if( r.getInput() instanceof String ) {
					otherID = OreDictionary.getOreID( ( String ) r.getInput() );
				}

				if( otherID != -1 && otherID == oreID ) {
					if( r.getOutput() != null )
						return r;
				}
			}
		}

		return null;
	}

	public static class GrinderRecipe {
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - either an OreDictionary string, or an ItemStack
		 *
		 */
		private Object input;
		private Object output;

		public GrinderRecipe( ItemStack input, ItemStack output ) {
			this.input = input;
			this.output = output;
		}

		public GrinderRecipe( String input, ItemStack output ) {
			this.input = input;
			this.output = output;
		}

		public GrinderRecipe( String input, String output ) {
			this.input = input;
			this.output = output;
		}

		public GrinderRecipe( ItemStack input, String output ) {
			this.input = input;
			this.output = output;
		}

		public Object getInput() {
			return input;
		}

		public List<ItemStack> getOutput() {
			if( this.output instanceof String ) {
				return OreDictionary.getOres( ( String ) this.output );
			} else {
				ArrayList<ItemStack> result = new ArrayList<ItemStack>();
				result.add( ( ItemStack )output );
				return result;
			}


		}

	}
}
