package emasher.nei

import codechicken.lib.gui.GuiDraw
import codechicken.nei
import codechicken.nei.{NEIServerUtils, PositionedStack}
import emasher.api.CentrifugeRecipeRegistry
import emasher.api.CentrifugeRecipeRegistry.CentrifugeRecipe
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.oredict.OreDictionary
import scala.collection.JavaConversions._

class CentrifugeRecipeHandler extends BaseRecipeHandler {
  final val recipeId = "sockets.centrifuge"

  override def getRecipeName: String = StatCollector.translateToLocal( "item.socket_module.93.name" ) + " Module"

  override def getGuiTexture: String = "eng_toolbox:textures/gui/nei-centrifuge.png"

  case class CachedCentrifugeRecipe( input: Object, output: List[ItemStack], bonusPercent: Int ) extends CachedRecipe {

    override def getResult: PositionedStack = {
      new PositionedStack( output.head, 98, 10, false )
    }

    override def getOtherStack: PositionedStack = {
      new PositionedStack( output.last, 98, 26, false )
    }

    override def getIngredients: java.util.List[PositionedStack] = {
      val inputs = List( input match {
        case s: String =>
          val ores = OreDictionary.getOres( s )
          new PositionedStack( ores, 44, 18, true )
        case i: ItemStack =>
          new PositionedStack( i, 44, 18, true )
      } )

      getCycledIngredients( cycleticks / 20, inputs )
    }
  }

  def makeCached( recipe: CentrifugeRecipe ): Option[CachedCentrifugeRecipe] = {
    if( recipe.getInput != null && recipe.getOutput != null && recipe.getSecondaryOutput != null ) {
      val input = extractItemStackList( recipe.getInput )
      if( input.nonEmpty ) {
        Option( CachedCentrifugeRecipe( recipe.getInput, List( recipe.getOutput, recipe.getSecondaryOutput ),
          recipe.getPercent ) )
      } else {
        None
      }
    } else {
      None
    }
  }

  override def loadCraftingRecipes( result: ItemStack ): Unit = {
    CentrifugeRecipeRegistry.recipes.toList.filter { recipe =>
       NEIServerUtils.areStacksSameTypeCrafting( recipe.getOutput, result ) || nei.NEIServerUtils.areStacksSameTypeCrafting( recipe.getSecondaryOutput, result )
    }.flatMap( makeCached ).foreach( arecipes.add( _ ) )
  }

  override def loadCraftingRecipes( id: String, result: Object* ): Unit = {
    if( id == recipeId && this.getClass == classOf[CentrifugeRecipeHandler] ) {
      CentrifugeRecipeRegistry.recipes.toList.flatMap( makeCached ).filter { cached =>
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
    CentrifugeRecipeRegistry.recipes.toList.filter { recipe =>
      recipe.getInput match {
        case s: String =>
          OreDictionary.getOres( s ).toList.exists { ore =>
            NEIServerUtils.areStacksSameTypeCrafting( ore, ingredient )
          }
        case i: ItemStack =>
          NEIServerUtils.areStacksSameTypeCrafting( i, ingredient )
      }
    }.flatMap( makeCached ).foreach( arecipes.add( _ ) )
  }

  override def drawExtras( recipe: Int ): Unit = {
    val percent = arecipes.get( recipe ).asInstanceOf[CachedCentrifugeRecipe].bonusPercent
    GuiDraw.drawString( percent + "%", 116, 31, 0x808080, false )
  }

}
