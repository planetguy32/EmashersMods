package emasher.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockNutrientWater extends BlockFluidClassic {
	@SideOnly( Side.CLIENT )
	public IIcon flowingTexture;

	public BlockNutrientWater( Fluid fluid ) {
		super( fluid, Material.water );
		this.setCreativeTab( null );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "emashercore:nutWater_still" );
		flowingTexture = ir.registerIcon( "emashercore:nutWater_flow" );

		this.getFluid().setStillIcon( blockIcon );
		this.getFluid().setFlowingIcon( flowingTexture );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public IIcon getIcon( int side, int meta ) {
		if( side == 0 || side == 1 ) return this.blockIcon;
		else return ( this.flowingTexture );
	}
}
