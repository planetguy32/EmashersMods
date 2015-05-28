package emasher.sockets.pipes

import cofh.api.energy._
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._;

abstract class BlockPipeBase( fluid: Boolean ) extends BlockContainer( Material.iron ) {
  var textures: Array[ IIcon ] = new Array( 17 );

  override def hasTileEntity( ): Boolean = true;

  @SideOnly( Side.CLIENT )
  override def getIcon( world: IBlockAccess, x: Int, y: Int, z: Int, side: Int ): IIcon = {
    var d: ForgeDirection = ForgeDirection.getOrientation( side );
    var ent: TileEntity = world.getTileEntity( x + d.offsetX, y + d.offsetY, z + d.offsetZ );

    if( fluid ) {
      if( ent.isInstanceOf[ IFluidHandler ] || ent.isInstanceOf[ TileFluidPipe ] ) {
        return textures( 0 );
      }
    }
    else {
      if( ent.isInstanceOf[ IEnergyHandler ] || ent.isInstanceOf[ TileEnergyPipe ] ) {
        return textures( 0 );
      }
    }


    var ents: Array[ TileEntity ] = new Array( 4 );
    var cons: Array[ Boolean ] = Array( false, false, false, false );
    side match {
      case 0 => {
        ents( 0 ) = world.getTileEntity( x, y, z - 1 );
        ents( 1 ) = world.getTileEntity( x + 1, y, z );
        ents( 2 ) = world.getTileEntity( x, y, z + 1 );
        ents( 3 ) = world.getTileEntity( x - 1, y, z );
      }

      case 1 => {
        ents( 0 ) = world.getTileEntity( x, y, z - 1 );
        ents( 1 ) = world.getTileEntity( x + 1, y, z );
        ents( 2 ) = world.getTileEntity( x, y, z + 1 );
        ents( 3 ) = world.getTileEntity( x - 1, y, z );
      }
      case 2 => {
        ents( 0 ) = world.getTileEntity( x, y + 1, z );
        ents( 1 ) = world.getTileEntity( x - 1, y, z );
        ents( 2 ) = world.getTileEntity( x, y - 1, z );
        ents( 3 ) = world.getTileEntity( x + 1, y, z );
      }
      case 3 => {
        ents( 0 ) = world.getTileEntity( x, y + 1, z );
        ents( 1 ) = world.getTileEntity( x + 1, y, z );
        ents( 2 ) = world.getTileEntity( x, y - 1, z );
        ents( 3 ) = world.getTileEntity( x - 1, y, z );
      }
      case 4 => {
        ents( 0 ) = world.getTileEntity( x, y + 1, z );
        ents( 1 ) = world.getTileEntity( x, y, z + 1 );
        ents( 2 ) = world.getTileEntity( x, y - 1, z );
        ents( 3 ) = world.getTileEntity( x, y, z - 1 );
      }
      case 5 => {
        ents( 0 ) = world.getTileEntity( x, y + 1, z );
        ents( 1 ) = world.getTileEntity( x, y, z - 1 );
        ents( 2 ) = world.getTileEntity( x, y - 1, z );
        ents( 3 ) = world.getTileEntity( x, y, z + 1 );
      }
      case _ => {

      }
    }

    cons = fillSides( ents, world, x, y, z );

    return selectTexture( cons );
  }

  @SideOnly( Side.CLIENT )
  def fillSides( ents: Array[ TileEntity ], world: IBlockAccess, x: Int, y: Int, z: Int ): Array[ Boolean ] = {
    var cons: Array[ Boolean ] = new Array( 4 );

    var te: TileEntity = world.getTileEntity( x, y, z );
    var col: Int = -1;

    if( te.isInstanceOf[ TilePipeBase ] ) {
      col = te.asInstanceOf[ TilePipeBase ].colour;
    }

    for( i <- 0 to 3 ) {
      if( fluid ) {
        if( ( ents( i ).isInstanceOf[ IFluidHandler ] && !ents( i ).isInstanceOf[ TileFluidPipe ] ) || ( ents( i ).isInstanceOf[ TileFluidPipe ] && ( ents( i ).asInstanceOf[ TilePipeBase ].colour == col || col == -1 || ents( i ).asInstanceOf[ TilePipeBase ].colour == -1 ) ) ) {
          cons( i ) = true;
        }
        else {
          cons( i ) = false;
        }
      }
      else {
        if( ( ents( i ).isInstanceOf[ IEnergyHandler ] && !ents( i ).isInstanceOf[ TileEnergyPipe ] ) || ( ents( i ).isInstanceOf[ TileEnergyPipe ] && ( ents( i ).asInstanceOf[ TilePipeBase ].colour == col || col == -1 || ents( i ).asInstanceOf[ TilePipeBase ].colour == -1 ) ) ) {
          cons( i ) = true;
        }
        else {
          cons( i ) = false;
        }
      }
    }

    return cons;
  }

  @SideOnly( Side.CLIENT )
  def selectTexture( cons: Array[ Boolean ] ): IIcon = {
    var result: Int = 0;
    var allFalse: Boolean = true;

    for( i <- 0 to 3 ) {
      if( cons( i ) ) allFalse = false;
    }

    if( allFalse ) return textures( 16 );

    if( cons( 0 ) ) {
      if( cons( 2 ) ) {
        if( cons( 3 ) ) {
          if( cons( 1 ) ) {
            result = 15;
          }
          else {
            result = 13;
          }
        }
        else {
          if( cons( 1 ) ) {
            result = 12;
          }
          else {
            result = 6;
          }
        }
      }
      else {
        if( cons( 3 ) ) {
          if( cons( 1 ) ) {
            result = 11;
          }
          else {
            result = 7;
          }
        }
        else {
          if( cons( 1 ) ) {
            result = 8;
          }
          else {
            result = 1;
          }
        }
      }
    }
    else {
      if( cons( 2 ) ) {
        if( cons( 3 ) ) {
          if( cons( 1 ) ) {
            result = 14;
          }
          else {
            result = 10;
          }
        }
        else {
          if( cons( 1 ) ) {
            result = 9;
          }
          else {
            result = 4;
          }
        }
      }
      else {
        if( cons( 3 ) ) {
          if( cons( 1 ) ) {
            result = 5;
          }
          else {
            result = 2;
          }
        }
        else {
          if( cons( 1 ) ) {
            result = 3;
          }
          else {
            result = 0;
          }
        }
      }
    }

    return textures( result );
  }
}