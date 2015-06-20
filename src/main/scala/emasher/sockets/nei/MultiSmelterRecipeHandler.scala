package emasher.sockets.nei

import codechicken.nei.{NEIServerUtils, PositionedStack}
import emasher.api.MultiSmelterRecipeRegistry
import emasher.api.MultiSmelterRecipeRegistry.MultiSmelterRecipe
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.oredict.OreDictionary
import scala.collection.JavaConversions._

class MultiSmelterRecipeHandler extends BaseRecipeHandler {
  final val recipeId = "sockets.multismelter"

  override def getRecipeName: String = StatCollector.translateToLocal( "item.socket_module.92.name" ) + " Module"

  override def getGuiTexture: String = "sockets:textures/gui/nei-grinder.png"

  case class CachedMultiSmelterRecipe( input: List[Object], output: List[ItemStack] ) extends CachedRecipe {

    override def getResult: PositionedStack = {
      new PositionedStack( output.head, 98, 18, false )
    }

    override def getIngredients: java.util.List[PositionedStack] = {
      val input1 = input.head match {
        case s: String =>
          val ores = OreDictionary.getOres( s )
          new PositionedStack( ores, 44, 26, true )
        case i: ItemStack =>
          new PositionedStack( i, 44, 26, false )
      }

      val input2 = input.last match {
        case s: String =>
          val ores = OreDictionary.getOres( s )
          new PositionedStack( ores, 44, 10, true )
        case i: ItemStack =>
          new PositionedStack( i, 44, 10, false )
      }

      List( List( input1 ), List( input2 ) ) flatMap { ingredients =>
        getCycledIngredients( cycleticks / 20, ingredients )
      }
    }
  }

  def makeCached( recipe: MultiSmelterRecipe ): Option[CachedMultiSmelterRecipe] = {

    if( recipe.getInput1 != null && recipe.getInput2 != null && !recipe.getOutput.isEmpty ) {
      val input1 = extractItemStackList( recipe.getInput1 )
      val input2 = extractItemStackList( recipe.getInput2 )
      if( input1.nonEmpty && input2.nonEmpty ) {
        Option( CachedMultiSmelterRecipe( List( input1.head, input2.head ), recipe.getOutput.toList ) )
      } else {
        None
      }
    } else {
      None
    }
  }

  private def extractItemStackList( in: Object ): List[ItemStack] = in match {
    case s: String =>
      OreDictionary.getOres( s ).toList
    case i: ItemStack =>
      List( i )
  }

  override def loadCraftingRecipes( result: ItemStack ): Unit = {
    MultiSmelterRecipeRegistry.list.toList.filter { recipe =>
      val rec = recipe.getOutput
      ! rec.isEmpty && NEIServerUtils.areStacksSameTypeCrafting( recipe.getOutput()( 0 ), result )
    } flatMap makeCached foreach arecipes.add
  }

  override def loadCraftingRecipes( id: String, result: Object* ): Unit = {
    if( id == recipeId && this.getClass == classOf[MultiSmelterRecipeHandler] ) {
      MultiSmelterRecipeRegistry.list.toList.flatMap( makeCached ).filter { cached =>
        cached.getIngredients.nonEmpty
      }.zipWithIndex.foreach {
        case( r, i ) if i % 2 == 0 =>
          arecipes.add( r )
        case _ =>
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
    MultiSmelterRecipeRegistry.list.toList.filter { recipe =>
      val input1 = recipe.getInput1 match {
        case s: String =>
          OreDictionary.getOres( s ).toList.exists { ore =>
            NEIServerUtils.areStacksSameTypeCrafting( ore, ingredient )
          }
        case i: ItemStack =>
          NEIServerUtils.areStacksSameTypeCrafting( i, ingredient )
      }

      val input2 = recipe.getInput2 match {
        case s: String =>
          OreDictionary.getOres( s ).toList.exists { ore =>
            NEIServerUtils.areStacksSameTypeCrafting( ore, ingredient )
          }
        case i: ItemStack =>
          NEIServerUtils.areStacksSameTypeCrafting( i, ingredient )
      }

      input1 || input2
    } flatMap makeCached foreach arecipes.add
  }

}
