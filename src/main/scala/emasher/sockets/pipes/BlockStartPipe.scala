package emasher.sockets.pipes

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SocketModule;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

class BlockStartPipe(id: Int) extends BlockContainer(Material.iron)
{
	override def createNewTileEntity(world: World, metadata: Int):TileEntity = new TileStartPipe();
	
	override def registerBlockIcons(ir: IIconRegister)
	{
		this.blockIcon = ir.registerIcon("sockets:startPipe");
	}
	
	override def canConnectRedstone(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int):Boolean = true;
}