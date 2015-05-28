package emasher.gas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.gas.EmasherGas;
import emasher.gas.tileentity.TileDuct;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockDuct extends BlockContainer {
	@SideOnly( Side.CLIENT )
	public IIcon topTexture;
	
	public BlockDuct() {
		super( Material.rock );
		this.setCreativeTab( EmasherGas.tabGasCraft );
	}

	@Override
	public TileEntity createNewTileEntity( World var1, int metadata ) {
		return new TileDuct();
	}
	
	@Override
	public boolean hasTileEntity( int metadata ) {
		return true;
	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		this.blockIcon = par1IconRegister.registerIcon( "brick" );
		this.topTexture = par1IconRegister.registerIcon( "gascraft:chimney" );
	}
	
	@Override
	public IIcon getIcon( int par1, int par2 ) {
		if( par1 != 0 && par1 != 1 ) return this.blockIcon;
		return this.topTexture;
	}

}
