package emasher.sockets.pipes

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.{IBlockAccess, World};

class BlockStartPipe( ) extends BlockContainer( Material.iron ) {
  override def createNewTileEntity( world: World, metadata: Int ): TileEntity = new TileStartPipe( );

  override def registerBlockIcons( ir: IIconRegister ) {
    this.blockIcon = ir.registerIcon( "sockets:startPipe" );
  }

  override def canConnectRedstone( world: IBlockAccess, x: Int, y: Int, z: Int, side: Int ): Boolean = true;
}