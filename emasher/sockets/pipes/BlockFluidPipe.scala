package emasher.sockets.pipes

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SocketModule;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids._;
import emasher.sockets.client;

class BlockFluidPipe(id: Int) extends BlockPipeBase(id, true)
{	
	override def createNewTileEntity(world: World):TileEntity = new TileFluidPipe();
	
	var colIcon:Array[Icon] = new Array(16);
	
	def getColIcon(i: Int) = colIcon(i);
	
	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IconRegister)
	{
		for(i <- 0 to 16)
		{
			textures(i) = ir.registerIcon("sockets:fPipe" + i);
			
			if(i < 16)
			{
				colIcon(i) = ir.registerIcon("sockets:paint" + i);
			}
		}
		
		this.blockIcon = textures(0);
	}
	
}