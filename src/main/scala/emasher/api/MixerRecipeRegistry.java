package emasher.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class MixerRecipeRegistry {
	public static ArrayList<MixerRecipe> recipes = new ArrayList<MixerRecipe>();
	
	public static void registerRecipe( MixerRecipe recipe ) {
		recipes.add( recipe );
	}
	
	public static void registerRecipe( ItemStack input, FluidStack fluidInput, FluidStack output ) {
		registerRecipe( new MixerRecipe( input, fluidInput, output ) );
	}
	
	public static void registerRecipe( String input, FluidStack fluidInput, FluidStack output ) {
		registerRecipe( new MixerRecipe( input, fluidInput, output ) );
	}

	public static MixerRecipe getRecipe( Object input, FluidStack fluidInput ) {
		if( input instanceof ItemStack ) {
			int[] oreIDs = OreDictionary.getOreIDs((ItemStack) input);
			for( MixerRecipe r : recipes ) {
				int[] otherIDs = null;

				if( r.getInput() instanceof ItemStack ) {
					otherIDs = OreDictionary.getOreIDs( ( ItemStack ) r.getInput() );
				} else if( r.getInput() instanceof String ) {
					otherIDs = new int[] { OreDictionary.getOreID( ( String ) r.getInput() ) };
				}

				if( ( Util.checkArrays( oreIDs, otherIDs ) ) || ( r.getInput() instanceof ItemStack && ( ( ItemStack ) input ).isItemEqual( ( ItemStack ) r.getInput() ) ) ) {
					if (fluidInput.isFluidEqual(r.fluidInput))
						return r;
				}
			}
		} else if( input instanceof String ) {
			int[] oreIDs = new int[] { OreDictionary.getOreID( ( String ) input ) };
			for( MixerRecipe r : recipes ) {
				int[] otherIDs = null;

				if( r.getInput() instanceof ItemStack ) {
					otherIDs = OreDictionary.getOreIDs( ( ItemStack ) r.getInput() );
				} else if( r.getInput() instanceof String ) {
					otherIDs = new int [] { OreDictionary.getOreID( ( String ) r.getInput() ) };
				}

				if( Util.checkArrays( oreIDs, otherIDs) ) {
					if( fluidInput.isFluidEqual( r.fluidInput ) )
						return r;
				}
			}
		}

		return null;
	}

	public static class MixerRecipe {
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - an ItemStack
		 *
		 */

		private Object input;
		private FluidStack fluidInput;
		private FluidStack output;

		public MixerRecipe( ItemStack input, FluidStack fluidInput, FluidStack output ) {
			this.input = input;
			this.fluidInput = fluidInput;
			this.output = output;
		}

		public MixerRecipe( String input, FluidStack fluidInput, FluidStack output ) {
			this.input = input;
			this.fluidInput = fluidInput;
			this.output = output;
		}

		public Object getInput() {
			return input;
		}

		public FluidStack getFluidInput() {
			return fluidInput;
		}

		public FluidStack getOutput() {
			return output;
		}
	}
}
