package emasher.sockets.pipes

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SocketModule;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids._;
import emasher.sockets.client;

class BlockEnergyPipe(id: Int) extends BlockPipeBase(id, false)
{	
	override def createNewTileEntity(world: World, metadata: Int):TileEntity = new TileEnergyPipe();
	
	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(ir: IIconRegister)
	{
		for(i <- 0 to 16)
		{
			textures(i) = ir.registerIcon("sockets:ePipe" + i);
		}
		
		this.blockIcon = textures(0);
	}
	
}