package emasher.nei

import codechicken.lib.gui.GuiDraw
import codechicken.nei.{NEIServerUtils, PositionedStack}
import emasher.api.MixerRecipeRegistry.MixerRecipe
import emasher.api.MixerRecipeRegistry
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.fluids.{FluidStack, FluidContainerRegistry}
import net.minecraftforge.oredict.OreDictionary
import scala.collection.JavaConversions._

class MixerRecipeHandler extends BaseRecipeHandler {
  final val recipeId = "sockets.mixer"

  override def getRecipeName: String = StatCollector.translateToLocal( "item.socket_module.94.name" ) + " Module"

  override def getGuiTexture: String = "eng_toolbox:textures/gui/nei-mixer.png"

  case class CachedMixerRecipe( input: List[Object], output: ItemStack, inputFluidAmount: Int, outputFluidAmount: Int ) extends CachedRecipe {

    override def getResult: PositionedStack = {
      new PositionedStack( output, 98, 18, false )
    }

    override def getIngredients: java.util.List[PositionedStack] = {
      val input1 = input.head match {
        case s: String =>
          val ores = OreDictionary.getOres( s )
          new PositionedStack( ores, 44, 26, true )
        case i: ItemStack =>
          new PositionedStack( i, 44, 26, true )
      }

      val input2 = input.last match {
        case s: String =>
          val ores = OreDictionary.getOres( s )
          new PositionedStack( ores, 44, 10, true )
        case i: ItemStack =>
          new PositionedStack( i, 44, 10, true )
      }

      val inputs = List( input1, input2 )
      getCycledIngredients( cycleticks / 20, inputs )
    }
  }

  def makeCached( recipe: MixerRecipe ): Option[CachedMixerRecipe] = {

    if( recipe.getInput != null && recipe.getFluidInput != null && recipe.getOutput != null ) {
      val input1 = extractItemStackList( recipe.getInput )
      if( input1.nonEmpty ) {
        Option( CachedMixerRecipe( List( recipe.getInput, new ItemStack( recipe.getFluidInput.getFluid.getBlock ) ),
          new ItemStack( recipe.getOutput.getFluid.getBlock ), recipe.getFluidInput.amount, recipe.getOutput.amount ) )
      } else {
        None
      }
    } else {
      None
    }
  }

  override def loadCraftingRecipes( result: ItemStack ): Unit = {
    MixerRecipeRegistry.recipes.toList.filter { recipe =>
      val outputBlock = new ItemStack( recipe.getOutput.getFluid.getBlock )
      NEIServerUtils.areStacksSameTypeCrafting( outputBlock, result ) ||
        ( FluidContainerRegistry.getFluidForFilledItem( result ) match {
          case null => false
          case f: FluidStack => NEIServerUtils.areStacksSameTypeCrafting( new ItemStack( f.getFluid.getBlock ), outputBlock )
        } )
    }.flatMap( makeCached ).foreach( arecipes.add ( _ ) )
  }

  override def loadCraftingRecipes( id: String, result: Object* ): Unit = {
    if( id == recipeId && this.getClass == classOf[MixerRecipeHandler] ) {
      MixerRecipeRegistry.recipes.toList.flatMap( makeCached ).filter { cached =>
        cached.getIngredients.nonEmpty
      }.foreach( arecipes.add( _ ) )
    } else if( result.nonEmpty ) {
      result.head match {
        case res: ItemStack =>
          loadCraftingRecipes( res )
        case _ =>
      }
    }
  }

  override def loadUsageRecipes( ingredient: ItemStack ): Unit = {
    MixerRecipeRegistry.recipes.toList.filter { recipe =>
      val input = recipe.getInput match {
        case s: String =>
          OreDictionary.getOres( s ).toList.exists { ore =>
            NEIServerUtils.areStacksSameTypeCrafting( ore, ingredient )
          }
        case i: ItemStack =>
          NEIServerUtils.areStacksSameTypeCrafting( i, ingredient )
      }

      val inputBlock = new ItemStack( recipe.getFluidInput.getFluid.getBlock )
      val fluidInput = NEIServerUtils.areStacksSameTypeCrafting( inputBlock, ingredient ) ||
        ( FluidContainerRegistry.getFluidForFilledItem( ingredient ) match {
          case null => false
          case f: FluidStack => NEIServerUtils.areStacksSameTypeCrafting( new ItemStack( f.getFluid.getBlock ), inputBlock )
        } )

      input || fluidInput
    }.flatMap( makeCached ).foreach( arecipes.add( _ ) )
  }

  override def drawExtras( recipe: Int ): Unit = {
    val cachedRecipe = arecipes.get( recipe ).asInstanceOf[CachedMixerRecipe]
    GuiDraw.drawString( cachedRecipe.outputFluidAmount + " mB", 116, 23, 0x808080, false )
    GuiDraw.drawString( cachedRecipe.inputFluidAmount + " mB", 2, 15, 0x808080, false )
  }

}
