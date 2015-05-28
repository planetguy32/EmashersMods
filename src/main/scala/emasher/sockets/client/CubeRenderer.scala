package emasher.sockets.client

import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.IIcon
import org.lwjgl.opengl.GL11

object CubeRenderer {
  def render( x: Double, y: Double, z: Double, icons: Array[ IIcon ], bounds: CubeRenderBounds, inverted: Boolean ): Unit = {
    val tessellator = Tessellator.instance

    for( i <- 0 to 5 ) {

      GL11.glPushMatrix( )
      GL11.glPushAttrib( GL11.GL_ENABLE_BIT )
      GL11.glEnable( GL11.GL_CULL_FACE )
      if( inverted ) GL11.glCullFace( GL11.GL_FRONT )
      GL11.glEnable( GL11.GL_BLEND )
      GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA )
      GL11.glTranslatef( x.asInstanceOf[ Float ], y.asInstanceOf[ Float ], z.asInstanceOf[ Float ] )

      i match {
        case 0 =>
          GL11.glRotatef( 270, 1, 0, 0 )
          GL11.glRotatef( 180, 0, 0, 1 )
          GL11.glTranslatef( -1.0F, 0.0F, -0.0F )
        case 1 =>
          GL11.glRotatef( 90, 1, 0, 0 )
          GL11.glTranslatef( 0.0F, 0.0F, -1.0F )
        case 2 =>
          GL11.glRotatef( 180, 0, 0, 1 )
          GL11.glTranslatef( -1.0F, -1.0F, -0.0F )
        case 3 =>
          GL11.glRotatef( 180, 0, 0, 1 )
          GL11.glRotatef( 180, 0, 1, 0 )
          GL11.glTranslatef( 0.0F, -1.0F, -1.0F )
        case 4 =>
          GL11.glRotatef( 180, 0, 0, 1 )
          GL11.glRotatef( 270, 0, 1, 0 )
          GL11.glTranslatef( 0.0F, -1.0F, -0.0F )
        case 5 =>
          GL11.glRotatef( 180, 0, 0, 1 )
          GL11.glRotatef( 90, 0, 1, 0 )
          GL11.glTranslatef( -1.0F, -1.0F, -1.0F )
      }

      tessellator.startDrawingQuads( )
      tessellator.addVertexWithUV( bounds.minX, bounds.maxY, bounds.minZ, icons( i ).getMinU, icons( i ).getMaxV )
      tessellator.addVertexWithUV( bounds.maxX, bounds.maxY, bounds.minZ, icons( i ).getMaxU, icons( i ).getMaxV )
      tessellator.addVertexWithUV( bounds.maxX, bounds.minY, bounds.minZ, icons( i ).getMaxU, icons( i ).getMinV )
      tessellator.addVertexWithUV( bounds.minX, bounds.minY, bounds.minZ, icons( i ).getMinU, icons( i ).getMinV )

      tessellator.draw( )

      if( inverted ) GL11.glCullFace( GL11.GL_BACK )
      GL11.glDisable( GL11.GL_CULL_FACE )
      GL11.glPopAttrib( )
      GL11.glPopMatrix( )
    }
  }
}

case class CubeRenderBounds( minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ) {}
