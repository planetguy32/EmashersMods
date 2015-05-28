package emasher.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class MultiSmelterRecipeRegistry {
	private static ArrayList<MultiSmelterRecipe> list = new ArrayList<MultiSmelterRecipe>();
	
	public static void registerRecipe( String input1, String input2, ItemStack output ) {
		list.add( new MultiSmelterRecipe( input1, input2, output ) );
		list.add( new MultiSmelterRecipe( input2, input1, output ) );
	}
	
	public static void registerRecipe( ItemStack input1, String input2, ItemStack output ) {
		list.add( new MultiSmelterRecipe( input1, input2, output ) );
		list.add( new MultiSmelterRecipe( input2, input1, output ) );
	}
	
	public static void registerRecipe( String input1, ItemStack input2, ItemStack output ) {
		list.add( new MultiSmelterRecipe( input1, input2, output ) );
		list.add( new MultiSmelterRecipe( input2, input1, output ) );
	}
	
	public static void registerRecipe( ItemStack input1, ItemStack input2, ItemStack output ) {
		list.add( new MultiSmelterRecipe( input1, input2, output ) );
		list.add( new MultiSmelterRecipe( input2, input1, output ) );
	}
	
	public static void registerRecipe( String input1, String input2, String output ) {
		list.add( new MultiSmelterRecipe( input1, input2, output ) );
		list.add( new MultiSmelterRecipe( input2, input1, output ) );
	}

	public static MultiSmelterRecipe getRecipeFor( Object input1, Object input2 ) {
		int rID1, rID2, id1, id2;
		boolean firstMatch, secondMatch;

		for( MultiSmelterRecipe r : list ) {
			rID1 = -1;
			rID2 = -1;
			id1 = -1;
			id2 = -1;
			firstMatch = false;
			secondMatch = false;

			if( input1 instanceof ItemStack ) {
				if( input2 instanceof ItemStack ) {
					id1 = OreDictionary.getOreID( ( ItemStack ) input1 );
					id2 = OreDictionary.getOreID( ( ItemStack ) input2 );

					if( r.input1 instanceof ItemStack ) rID1 = OreDictionary.getOreID( ( ItemStack ) r.input1 );
					else rID1 = OreDictionary.getOreID( ( String ) r.input1 );
					if( r.input2 instanceof ItemStack ) rID2 = OreDictionary.getOreID( ( ItemStack ) r.input2 );
					else rID2 = OreDictionary.getOreID( ( String ) r.input2 );

					if( id1 == rID1 && id1 != -1 ) firstMatch = true;
					else if( r.input1 instanceof ItemStack && ( ( ItemStack ) r.input1 ).isItemEqual( ( ItemStack ) input1 ) )
						firstMatch = true;
					if( id2 == rID2 && id2 != -1 ) secondMatch = true;
					else if( r.input2 instanceof ItemStack && ( ( ItemStack ) r.input2 ).isItemEqual( ( ItemStack ) input2 ) )
						secondMatch = true;

					if( firstMatch && secondMatch ) {
						if( r.getOutput() != null )
							return r;
					}
				} else if( input2 instanceof String ) {
					id1 = OreDictionary.getOreID( ( ItemStack ) input1 );
					id2 = OreDictionary.getOreID( ( String ) input2 );

					if( r.input1 instanceof ItemStack ) rID1 = OreDictionary.getOreID( ( ItemStack ) r.input1 );
					else rID1 = OreDictionary.getOreID( ( String ) r.input1 );
					if( r.input2 instanceof ItemStack ) rID2 = OreDictionary.getOreID( ( ItemStack ) r.input2 );
					else rID2 = OreDictionary.getOreID( ( String ) r.input2 );

					if( id1 == rID1 && id1 != -1 ) firstMatch = true;
					else if( r.input1 instanceof ItemStack && ( ( ItemStack ) r.input1 ).isItemEqual( ( ItemStack ) input1 ) )
						firstMatch = true;
					if( id2 == rID2 && id2 != -1 ) secondMatch = true;

					if( firstMatch && secondMatch ) {
						if( r.getOutput() != null )
							return r;
					}
				}
			} else if( input1 instanceof String ) {
				if( input2 instanceof ItemStack ) {
					id1 = OreDictionary.getOreID( ( String ) input1 );
					id2 = OreDictionary.getOreID( ( ItemStack ) input2 );

					if( r.input1 instanceof ItemStack ) rID1 = OreDictionary.getOreID( ( ItemStack ) r.input1 );
					else rID1 = OreDictionary.getOreID( ( String ) r.input1 );
					if( r.input2 instanceof ItemStack ) rID2 = OreDictionary.getOreID( ( ItemStack ) r.input2 );
					else rID2 = OreDictionary.getOreID( ( String ) r.input2 );

					if( id1 == rID1 && id1 != -1 ) firstMatch = true;
					if( id2 == rID2 && id2 != -1 ) secondMatch = true;
					else if( r.input2 instanceof ItemStack && ( ( ItemStack ) r.input2 ).isItemEqual( ( ItemStack ) input2 ) )
						secondMatch = true;

					if( firstMatch && secondMatch ) {
						if( r.getOutput() != null )
							return r;
					}
				} else if( input2 instanceof String ) {
					id1 = OreDictionary.getOreID( ( String ) input1 );
					id2 = OreDictionary.getOreID( ( String ) input2 );

					if( r.input1 instanceof ItemStack ) rID1 = OreDictionary.getOreID( ( ItemStack ) r.input1 );
					else rID1 = OreDictionary.getOreID( ( String ) r.input1 );
					if( r.input2 instanceof ItemStack ) rID2 = OreDictionary.getOreID( ( ItemStack ) r.input2 );
					else rID2 = OreDictionary.getOreID( ( String ) r.input2 );

					if( id1 == rID1 && id1 != -1 ) firstMatch = true;
					if( id2 == rID2 && id2 != -1 ) secondMatch = true;

					if( firstMatch && secondMatch ) {
						if( r.getOutput() != null )
							return r;
					}
				}
			}
		}

		return null;
	}

	public static class MultiSmelterRecipe {
		protected Object input1;
		protected Object input2;
		protected Object output;

		public MultiSmelterRecipe( String input1, String input2, ItemStack output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public MultiSmelterRecipe( ItemStack input1, String input2, ItemStack output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public MultiSmelterRecipe( String input1, ItemStack input2, ItemStack output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public MultiSmelterRecipe( String input1, String input2, String output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public MultiSmelterRecipe( ItemStack input1, String input2, String output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public MultiSmelterRecipe( String input1, ItemStack input2, String output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public MultiSmelterRecipe( ItemStack input1, ItemStack input2, ItemStack output ) {
			this.input1 = input1;
			this.input2 = input2;
			this.output = output;
		}

		public Object getInput1() {
			return input1;
		}

		public Object getInput2() {
			return input2;
		}

		public ItemStack getOutput() {
			// convert to ItemStack on the first getOutput call
			if( this.output instanceof String ) {
				ArrayList<ItemStack> ores = OreDictionary.getOres( ( String ) this.output );
				if( ores.size() > 0 )
					this.output = ores.get( 0 );
				else
					return null;
			}
			return ( ItemStack ) output;
		}
	}
}
