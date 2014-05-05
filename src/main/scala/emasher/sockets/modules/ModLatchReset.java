package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModLatchReset extends SocketModule
{

	public ModLatchReset(int id)
	{
		super(id, "sockets:RESET_0", "sockets:RESET_1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Redstone Latch Reset";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Tuns off selected internal RS latches");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS latches to turn off");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "tgr", " b ", Character.valueOf('t'), Block.torchRedstoneActive, Character.valueOf('r'), Item.redstone,
				Character.valueOf('g'), Item.ingotIron, Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 16));
	}
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public int getCurrentTexture(SideConfig config) { return config.meta; }
	
	@Override
	public boolean isRedstoneInterface() { return true; }
	
	@Override
	public void updateRestone(boolean on, SideConfig config, SocketTileAccess ts)
	{
		for(int i = 0; i < 3; i++)
		{
			if(config.rsLatch[i])
			{
				if(on)
				{
					ts.modifyLatch(i, false);
					config.meta = 1;
				}
				else
				{
					config.meta = 0;
				}
			}
		}
	}
	
}
