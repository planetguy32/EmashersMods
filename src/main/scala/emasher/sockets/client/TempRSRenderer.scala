package emasher.sockets.client

import emasher.sockets.{BlockTempRS, SocketsMod}
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{IIcon, ResourceLocation}
import org.lwjgl.opengl.GL11

class TempRSRenderer extends TileEntitySpecialRenderer {

  override def renderTileEntityAt( t: TileEntity, x: Double, y: Double, z: Double, counter: Float ): Unit = {
    GL11.glDisable( GL11.GL_LIGHTING )
    val block = SocketsMod.tempRS.asInstanceOf[ BlockTempRS ]
    Minecraft.getMinecraft.renderEngine.bindTexture( new ResourceLocation( "textures/atlas/blocks.png" ) )
    val bounds = CubeRenderBounds( 0.4, 0.4, 0.4, 0.6, 0.6, 0.6 )
    CubeRenderer.render( x, y, z, Array.fill[ IIcon ]( 6 ) {
      block.getIcon( 0, 0 )
    }, bounds, false )
    GL11.glEnable( GL11.GL_LIGHTING )
  }
}

object TempRSRenderer {
  val instance = new TempRSRenderer
}
