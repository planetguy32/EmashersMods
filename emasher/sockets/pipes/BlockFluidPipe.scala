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

class BlockFluidPipe(id: Int) extends BlockContainer(id, Material.iron)
{
	@SideOnly(Side.CLIENT)
	var testIcon:Icon = null;
	
	override def createNewTileEntity(world: World):TileEntity = new TileFluidPipe();
	
	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IconRegister)
	{
		this.blockIcon = ir.registerIcon("sockets:fluidPipe");
		testIcon = ir.registerIcon("cobblestone");
		
	}
	
	@SideOnly(Side.CLIENT)
	override def getIcon(side: Int, meta:Int)
	:Icon =
	{
		if(meta == 0) return blockIcon;
		else return testIcon;
	}
}