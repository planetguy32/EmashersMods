package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.*;

public class ModSelfDestruct extends SocketModule
{
	
	public static final int[] settings = new int[] { 10, 20, 40, 80, 160, 200, 600, 1200 };

	public ModSelfDestruct(int id)
	{
		super(id, "sockets:selfDestruct");
	}

	@Override
	public String getLocalizedName()
	{
		return "Self Destruct System";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Blows up the socket it's installed");
		l.add("on a configured ammount of time");
		l.add("after recieving an internal redstone pulse");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS activation pulse");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS activation latch");
		l.add(SocketsMod.PREF_WHITE + "Modify length of delay");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "t", "b", Character.valueOf('t'), Blocks.tnt, Character.valueOf('r'), Items.redstone,
				Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 36));
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean isRedstoneInterface() { return false; }
	
	@Override
	public int getCurrentTexture(SideConfig config)
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon[] getAdditionalOverlays(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		int setting = 0;
		int time = 0;
		int on = 0;
		
		setting = config.meta >> 3;
		setting &= 7;
		on = config.meta & 7;
		
		time = config.meta >> 6;
		
		time = (int)Math.ceil((time * 7) / settings[setting]);
		
		if(on > 0) time = 7;
		
		if((config.meta >> 6) != 0) return new IIcon[] { ((BlockSocket)SocketsMod.socket).bar1[setting], ((BlockSocket)SocketsMod.socket).bar2[time] };
		else return new IIcon[] { ((BlockSocket)SocketsMod.socket).bar1[setting] };
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean flipBottomOverlay() { return true; }
	
	@Override
	public boolean isOutputingRedstone(SideConfig config, SocketTileAccess ts)
	{
		return false;
	}
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(config.tank != 1 && on)
		{
			config.tank = 1;
		}
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		int setting = 0;
		int oldTime = 0;
		int reBuild = 0;
		int on = config.meta & 7;
		boolean reRender = false;
		boolean updateAdj = false;
		
		setting = config.meta >> 3;
		setting &= 7;
		
		oldTime = config.meta >> 6;
		int time = oldTime;
		
		
		
		if(config.tank == 1)
		{
			time++;
			oldTime = (int)Math.ceil((oldTime * 7) / settings[setting]);
			int timeDisp = (int)Math.ceil((time * 7) / settings[setting]);
			if(oldTime != timeDisp) reRender = true;
			
			
			if(time >= settings[setting])
			{
				time = 0;
				on = 1;
				reRender = true;
				updateAdj = true;
				config.tank = 0;
				ts.getWorldObj().setBlock(ts.xCoord, ts.yCoord, ts.zCoord, Blocks.air);
				ts.getWorldObj().removeTileEntity(ts.xCoord, ts.yCoord, ts.zCoord);
				ts.getWorldObj().createExplosion(null, ts.xCoord, ts.yCoord, ts.zCoord, 4.0F, true);
				return;
			}
		}
		
		if(on != 0)on++;
		if(on >= 5)
		{
			on = 0;
			reRender = true;
			updateAdj = true;
		}
		
		reBuild = time;
		reBuild <<= 3;
		reBuild |= setting;
		reBuild <<= 3;
		reBuild |= on;
		
		config.meta = reBuild;
		
		if(reRender)
		{
			ts.sendClientSideState(side.ordinal());
			
		}
		
		if(updateAdj)
		{
			ts.updateAdj(side);
		}
		
		
		
	}
	
	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		int on = config.meta & 7;
		int setting = config.meta >> 3;
		setting &= 7;
		int time = config.meta >> 6;
		int reBuild = 0;
		
		setting++;
		
		if(setting >= 8) setting = 0;
		
		reBuild = 0;
		reBuild <<= 3;
		reBuild |= setting;
		reBuild <<= 3;
		reBuild |= on;
		
		config.meta = reBuild;
		
		ts.sendClientSideState(side.ordinal());
	}
	
	
	
	
	
}
