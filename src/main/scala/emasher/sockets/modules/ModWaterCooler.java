package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.block.material.*;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModWaterCooler extends SocketModule
{

	public ModWaterCooler(int id)
	{
		super(id, "sockets:waterCooler");
	}

	@Override
	public String getLocalizedName()
	{
		return "Water Cooler";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Cools adjacent lava blocks");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "www", " b ", Character.valueOf('w'), Item.bucketWater, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		config.meta++;
		
		if(config.meta >= 20)
		{
			config.meta = 0;
			
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			
			int id = ts.worldObj.getBlockId(xo, yo, zo);
			
			Block b = Block.blocksList[id];
			Material mat = Material.water;
			if(b != null) mat = b.blockMaterial;
			
			if(mat == Material.lava)
			{
				int meta = ts.worldObj.getBlockMetadata(xo, yo, zo);
				
				
				if(meta == 0) ts.worldObj.setBlock(xo, yo, zo, Block.obsidian.blockID);
				else ts.worldObj.setBlock(xo, yo, zo, Block.cobblestone.blockID);
			
				triggerLavaMixEffects(ts.worldObj, xo, yo, zo);
			}
		}
	}
	
	protected void triggerLavaMixEffects(World par1World, int par2, int par3, int par4)
    {
        par1World.playSoundEffect((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l)
        {
            par1World.spawnParticle("largesmoke", (double)par2 + Math.random(), (double)par3 + 1.2D, (double)par4 + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }
	
}
