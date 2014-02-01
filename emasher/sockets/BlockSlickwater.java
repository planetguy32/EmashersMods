package emasher.sockets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockSlickwater extends BlockFluidClassic
{
	@SideOnly(Side.CLIENT)
	public Icon flowingTexture;
	
	public BlockSlickwater(int id, Fluid fluid)
	{
		super(id, fluid, Material.water);
		this.setCreativeTab(null);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		this.blockIcon = ir.registerIcon("sockets:slickwater_still");
		flowingTexture = ir.registerIcon("sockets:slickwater_flow");
		
		this.getFluid().setStillIcon(blockIcon);
		this.getFluid().setFlowingIcon(flowingTexture);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if(side == 0 || side == 1) return this.blockIcon;
		else return (this.flowingTexture);
	}

}
