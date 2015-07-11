package emasher.nei

import codechicken.nei.{NEIServerUtils, PositionedStack}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class SpinningWheelRecipeHandler extends BaseRecipeHandler {
  final val recipeId = "sockets.spinningWheel"

  override def getRecipeName: String = StatCollector.translateToLocal( "item.socket_module.39.name" ) + " Module"

  case object CachedSpinningWheelRecipe extends CachedRecipe {
    lazy val wools = List.tabulate(16) { i =>
      new ItemStack( Blocks.wool, 1, i )
    }

    override def getResult: PositionedStack = {
      new PositionedStack( new ItemStack( Items.string ), 98, 18, false )
    }

    override def getIngredients: java.util.List[PositionedStack] = {
      val all = List( new PositionedStack( wools.asJava, 44, 18, true ) )
      getCycledIngredients( cycleticks / 20, all )
    }
  }

  override def getGuiTexture: String = "eng_toolbox:textures/gui/nei-spinning-wheel.png"

  override def loadCraftingRecipes( result: ItemStack ): Unit = {
    if( NEIServerUtils.areStacksSameTypeCrafting( new ItemStack( Items.string ), result ) ) {
      arecipes.add( CachedSpinningWheelRecipe )
    }
  }

  override def loadCraftingRecipes( id: String, result: Object* ): Unit = {
    if( id == recipeId && this.getClass == classOf[SpinningWheelRecipeHandler] ) {
      arecipes.add( CachedSpinningWheelRecipe )
    } else if( result.nonEmpty ) {
      result.head match {
        case res: ItemStack =>
          loadCraftingRecipes( res )
        case _ =>
      }
    }
  }

  override def loadUsageRecipes( ingredient: ItemStack ): Unit = {
    if( ingredient.getItem == new ItemStack( Blocks.wool ).getItem ){
      arecipes.add( CachedSpinningWheelRecipe )
    }
  }
}
