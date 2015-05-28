package emasher.gas.block;

import emasher.gas.EmasherGas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Random;

public class BlockNetherGas extends Block {

	public BlockNetherGas() {
		super( Material.rock );
		this.setCreativeTab( null );
	}
	
	
	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "netherrack" );
	}

	public int quantityDropped( Random rand ) {
		return 0;
	}
	
	
	@Override
	public void onBlockClicked( World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer ) {
		par1World.setBlock( par2, par3, par4, EmasherGas.plasma );
	}
}
