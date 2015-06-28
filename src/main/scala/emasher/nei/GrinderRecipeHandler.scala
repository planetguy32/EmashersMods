package emasher.nei

import codechicken.nei.{NEIServerUtils, PositionedStack}
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import emasher.api.GrinderRecipeRegistry._
import emasher.api.GrinderRecipeRegistry
import net.minecraftforge.oredict.OreDictionary
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

class GrinderRecipeHandler extends BaseRecipeHandler {

  final val recipeId = "sockets.grinder"

  override def getRecipeName: String = StatCollector.translateToLocal( "item.socket_module.72.name" ) + " Module"

  case class CachedGrinderRecipe( input: Object, output: List[ItemStack] ) extends CachedRecipe {

    override def getResult: PositionedStack = {
      new PositionedStack( output.head, 98, 18, false )
    }

    override def getIngredients: java.util.List[PositionedStack] = {
      val all = List( input match {
        case s: String =>
          val ores = OreDictionary.getOres( s )
          new PositionedStack( ores, 44, 18, false )
        case i: ItemStack =>
          new PositionedStack( i, 44, 18, false )
      } )
      getCycledIngredients( cycleticks / 20, all )
    }
  }

  def makeCached( recipe: GrinderRecipe ): Option[CachedGrinderRecipe] = {
    if( recipe.getInput != null && ! recipe.getOutput.isEmpty ) {
      recipe.getInput match {
        case s: String =>
          if( OreDictionary.getOres( s ).nonEmpty ) {
            Option( CachedGrinderRecipe( s, recipe.getOutput.asScala.toList ) )
          } else {
            None
          }
        case i: ItemStack => Option( CachedGrinderRecipe( i, recipe.getOutput.asScala.toList ) )
      }
    } else {
      None
    }
  }

  override def getGuiTexture: String = "eng_toolbox:textures/gui/nei-grinder.png"

  override def loadCraftingRecipes( result: ItemStack ): Unit = {
    GrinderRecipeRegistry.recipes.toList.filter { recipe =>
      val rec = recipe.getOutput
      ! rec.isEmpty && NEIServerUtils.areStacksSameTypeCrafting( recipe.getOutput()( 0 ), result )
    } flatMap makeCached foreach arecipes.add
  }

  override def loadCraftingRecipes( id: String, result: Object* ): Unit = {
    if( id == recipeId && this.getClass == classOf[GrinderRecipeHandler] ) {
      GrinderRecipeRegistry.recipes.toList.flatMap( makeCached ).filter { cached =>
        cached.getIngredients.nonEmpty
      } foreach { r =>
        arecipes.add( r )
      }
    } else if( result.nonEmpty ) {
      result.head match {
        case res: ItemStack =>
          loadCraftingRecipes( res )
        case _ =>
      }
    }
  }

  override def loadUsageRecipes( ingredient: ItemStack ): Unit = {
    GrinderRecipeRegistry.recipes.toList.filter { recipe =>
      recipe.getInput match {
        case s: String =>
          OreDictionary.getOres( s ).toList.exists { ore =>
            NEIServerUtils.areStacksSameTypeCrafting( ore, ingredient )
          }
        case i: ItemStack =>
          NEIServerUtils.areStacksSameTypeCrafting( i, ingredient )
      }
    } flatMap makeCached foreach arecipes.add
  }


}
