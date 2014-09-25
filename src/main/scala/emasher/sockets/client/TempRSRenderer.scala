package emasher.sockets.client

import net.minecraft.client.renderer.{RenderBlocks, Tessellator}
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import emasher.sockets.{BlockTempRS, SocketsMod}

class TempRSRenderer extends TileEntitySpecialRenderer {

  override def renderTileEntityAt(t: TileEntity, x: Double, y: Double, z: Double, counter: Float): Unit = {

    val block = SocketsMod.tempRS.asInstanceOf[BlockTempRS]
    val tessellator = Tessellator.instance

    for(i <- 0 to 5) {

      GL11.glPushMatrix()
      GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
      GL11.glEnable(GL11.GL_CULL_FACE)
      GL11.glEnable(GL11.GL_BLEND)
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
      GL11.glTranslatef(x.asInstanceOf[Float], y.asInstanceOf[Float], z.asInstanceOf[Float])

      i match {
        case 0 =>
          GL11.glRotatef(270, 1, 0, 0)
          GL11.glRotatef(180, 0, 0, 1)
          GL11.glTranslatef(-1.0F, 0.0F, -0.0F)
        case 1 =>
          GL11.glRotatef(90, 1, 0, 0)
          GL11.glTranslatef(0.0F, 0.0F, -1.0F)
        case 2 =>
          GL11.glRotatef(180, 0, 0, 1)
          GL11.glTranslatef(-1.0F, -1.0F, -0.0F)
        case 3 =>
          GL11.glRotatef(180, 0, 0, 1)
          GL11.glRotatef(180, 0, 1, 0)
          GL11.glTranslatef(0.0F, -1.0F, -1.0F)
        case 4 =>
          GL11.glRotatef(180, 0, 0, 1)
          GL11.glRotatef(270, 0, 1, 0)
          GL11.glTranslatef(0.0F, -1.0F, -0.0F)
        case 5 =>
          GL11.glRotatef(180, 0, 0, 1)
          GL11.glRotatef(90, 0, 1, 0)
          GL11.glTranslatef(-1.0F, -1.0F, -1.0F)
      }

      Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation("textures/atlas/blocks.png"))

      val icon = block.getIcon(0, 0)

      tessellator.startDrawingQuads()
      tessellator.addVertexWithUV(0.4, 0.6, 0.4, icon.getMinU, icon.getMaxV)
      tessellator.addVertexWithUV(0.6, 0.6, 0.4, icon.getMaxU, icon.getMaxV)
      tessellator.addVertexWithUV(0.6, 0.4, 0.4, icon.getMaxU, icon.getMinV)
      tessellator.addVertexWithUV(0.4, 0.4, 0.4, icon.getMinU, icon.getMinV)

      tessellator.draw()

      GL11.glDisable(GL11.GL_CULL_FACE)
      GL11.glPopAttrib()
      GL11.glPopMatrix()
    }


  }
}

object TempRSRenderer {
  val instance = new TempRSRenderer
}
