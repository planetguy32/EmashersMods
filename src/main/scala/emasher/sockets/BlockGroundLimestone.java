package emasher.sockets;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockSand;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockGroundLimestone extends BlockSand
{
	@SideOnly(Side.CLIENT)
	protected Icon texture_g;
	
    public BlockGroundLimestone(int par1)
    {
        super(par1);
        this.setCreativeTab(SocketsMod.tabSockets);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
    	blockIcon = ir.registerIcon("sockets:groundLimestone");
    	texture_g = ir.registerIcon("sockets:groundLimestone_g");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
    	if(meta == 0) return blockIcon;
    	else return texture_g;
    }
    
    @Override
    public int damageDropped(int meta)
    {
    	return 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public float getBlockBrightness(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if(meta == 0) return super.getBlockBrightness(world, x, y, z);
        return Math.max(super.getBlockBrightness(world, x, y, z), 12.0F);
    }

}
