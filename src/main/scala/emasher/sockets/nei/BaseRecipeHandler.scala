package emasher.sockets.nei

import java.awt.Rectangle

import codechicken.lib.gui.GuiDraw
import codechicken.nei.recipe.TemplateRecipeHandler
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect
import org.lwjgl.opengl.GL11

abstract class BaseRecipeHandler extends TemplateRecipeHandler {

  val recipeId: String

  override def loadTransferRects(): Unit = {
    transferRects.add( new RecipeTransferRect( new Rectangle( 68, 20, 22, 15 ), recipeId ) )
  }

  override def drawBackground( recipe: Int ): Unit = {
    GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F )
    GuiDraw.changeTexture( getGuiTexture )
    GuiDraw.drawTexturedModalRect( 0, 0, 0, 0, 160, 65 )
  }
}
