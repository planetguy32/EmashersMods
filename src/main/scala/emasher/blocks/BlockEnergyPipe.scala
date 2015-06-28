package emasher.blocks

import cpw.mods.fml.relauncher.{Side, SideOnly}
import emasher.tileentities.TileEnergyPipe
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World;

class BlockEnergyPipe( ) extends BlockPipeBase( false ) {
  override def createNewTileEntity( world: World, metadata: Int ): TileEntity = new TileEnergyPipe( )

  @SideOnly( Side.CLIENT )
  override def registerBlockIcons( ir: IIconRegister ) {
    for( i <- 0 to 16 ) {
      textures( i ) = ir.registerIcon( "eng_toolbox:ePipe" + i )
    }

    this.blockIcon = textures( 0 )
  }

}