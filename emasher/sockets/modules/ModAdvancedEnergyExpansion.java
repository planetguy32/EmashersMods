package emasher.sockets.modules;

import java.util.List;

import ic2.api.energy.tile.IEnergyAcceptor;
import buildcraft.api.power.IPowerReceptor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModAdvancedEnergyExpansion extends SocketModule
{
	public ModAdvancedEnergyExpansion(int id)
	{
		super(id, "sockets:advEnExpansion", "sockets:advEnExpansionIn", "sockets:advEnExpansionOut");
	}

	@Override
	public String getLocalizedName()
	{
		return "Advanced Energy Storage Upgrade";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Adds 10 000 000 f");
		l.add("of extra energy storage");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_WHITE + "Configure if input or output or neither");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "pgp", "grg", "pgp", Character.valueOf('g'), Item.redstone, Character.valueOf('p'), new ItemStack(SocketsMod.module, 1, 74),
				Character.valueOf('r'), Block.blockGold);
	}
	
	@Override
	public int getCurrentTexture(SideConfig config) { return config.meta; }
	
	/*@Override
	public boolean hasTankIndicator() {return true; }*/
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public boolean isEnergyInterface(SideConfig config) { return config.meta != 0; }
	
	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		config.meta++;
		if(config.meta == 3) config.meta = 0;
		ts.sendClientSideState(side.ordinal());
		ts.updateAdj(side);
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		
		boolean allOff = true;
		if(config.meta == 2)
		{
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						ts.outputEnergy(1000, side);
						return;
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						ts.outputEnergy(1000, side);
						return;
					}
					allOff = false;
				}
			}
			
			if(allOff)
			{
				ts.outputEnergy(1000, side);
				
			}
		}
			
	}
	
	@Override
	public int receiveEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
	{
		boolean allOff = true;
		if(config.meta == 1)
		{
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						return ts.addEnergy(amount, simulate);
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						return ts.addEnergy(amount, simulate);
					}
					allOff = false;
				}
			}
			
			if(allOff)
			{
				return ts.addEnergy(amount, simulate);
				
			}
		}
		
		return 0;
	}
	
	@Override
	public int extractEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
	{
		boolean allOff = true;
		if(config.meta == 2)
		{
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						return ts.useEnergy(amount, simulate);
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						return ts.useEnergy(amount, simulate);
					}
					allOff = false;
				}
			}
			
			if(allOff)
			{
				return ts.addEnergy(amount, simulate);
				
			}
		}
		
		return 0;
	}
	
	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.setMaxEnergyStored(ts.getMaxEnergyStored() + 10000000);
	}
	
	@Override
	public void onRemoved(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.setMaxEnergyStored(ts.getMaxEnergyStored() - 10000000);
	}
}
