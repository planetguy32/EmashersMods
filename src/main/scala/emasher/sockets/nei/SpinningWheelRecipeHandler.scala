package emasher.sockets.nei

import codechicken.nei.{NEIServerUtils, PositionedStack}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import scala.collection.JavaConversions._

class SpinningWheelRecipeHandler extends BaseRecipeHandler {
  final val recipeId = "sockets.grinder"

  override def getRecipeName: String = StatCollector.translateToLocal( "item.socket_module.72.name" ) + " Module"

  case object CachedSpinningWheelRecipe extends CachedRecipe {

    override def getResult: PositionedStack = {
      new PositionedStack( Items.string, 98, 18, false )
    }

    override def getIngredients: java.util.List[PositionedStack] = {
      val all = List( new PositionedStack( Blocks.wool, 44, 18, true ) )
      getCycledIngredients( cycleticks / 20, all )
    }
  }

  override def getGuiTexture: String = "sockets:textures/gui/nei-grinder.png"

  override def loadCraftingRecipes( result: ItemStack ): Unit = {
    if( NEIServerUtils.areStacksSameTypeCrafting( new ItemStack( Blocks.wool ), result ) ) {
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
    if( NEIServerUtils.areStacksSameTypeCrafting( new ItemStack( Items.string ), ingredient) ) {
      arecipes.add( CachedSpinningWheelRecipe )
    }
  }
}
