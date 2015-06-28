package emasher.blocks

import cpw.mods.fml.relauncher.{Side, SideOnly}
import emasher.tileentities.TileFluidPipe
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World;

class BlockFluidPipe( ) extends BlockPipeBase( true ) {
  var colIcon: Array[ IIcon ] = new Array( 16 );

  override def createNewTileEntity( world: World, metadata: Int ): TileEntity = new TileFluidPipe( );

  def getColIcon( i: Int ) = colIcon( i );

  @SideOnly( Side.CLIENT )
  override def registerBlockIcons( ir: IIconRegister ) {
    for( i <- 0 to 16 ) {
      textures( i ) = ir.registerIcon( "eng_toolbox:fPipe" + i );

      if( i < 16 ) {
        colIcon( i ) = ir.registerIcon( "eng_toolbox:paint" + i );
      }
    }

    this.blockIcon = textures( 0 );
  }

}