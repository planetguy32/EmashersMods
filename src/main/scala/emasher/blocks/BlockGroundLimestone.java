package emasher.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import net.minecraft.block.BlockFalling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockGroundLimestone extends BlockFalling {
	@SideOnly( Side.CLIENT )
	protected IIcon texture_g;
	
	public BlockGroundLimestone() {
		super();
		this.setCreativeTab( EngineersToolbox.tabBlocks() );
		this.useNeighborBrightness = true;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerBlockIcons( IIconRegister ir ) {
		blockIcon = ir.registerIcon( "eng_toolbox:groundLimestone" );
		texture_g = ir.registerIcon( "eng_toolbox:groundLimestone_g" );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public IIcon getIcon( int side, int meta ) {
		if( meta == 0 ) return blockIcon;
		else return texture_g;
	}

	@Override
	public int damageDropped( int meta ) {
		return 0;
	}

    /*@Override
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        //if(meta == 0) return super.getBlockBrightness(world, x, y, z);
        //return Math.max(super.getBlockBrightness(world, x, y, z), 12.0F);
        if(meta == 0) return super.getMixedBrightnessForBlock(world, x, y, z);
        return Math.max(super.getMixedBrightnessForBlock(world, x, y, z), 12);
    }*/

	@Override
	@SideOnly( Side.CLIENT )
	public void getSubBlocks( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		par3List.add( new ItemStack( par1, 1, 0 ) );
	}

}
