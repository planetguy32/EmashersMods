package emasher.sockets.modules

import emasher.api
import emasher.api.{SideConfig, SocketTileAccess, SocketModule}
;
import emasher.sockets._;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util._;
import net.minecraft.init.{Items, Blocks}
import net.minecraft.item.crafting._;
import net.minecraftforge.oredict._;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.item._;
import net.minecraft.block._;
import net.minecraft.util._;

class ModRangeSelector(id: Int) extends SocketModule(id, "sockets:rangeSelector")
{
	override def getLocalizedName = "Range Selector";
	
	@SideOnly(Side.CLIENT)
	override def getAdditionalOverlays(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection)
	:Array[IIcon] =
	{
		Array(SocketsMod.socket.asInstanceOf[BlockSocket].bar1(config.meta));
	}
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ggg", "sls", " b ", Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('s'), Items.glowstone_dust,
				Character.valueOf('l'), "dyeLime", Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	override def onGenericRemoteSignal(ts: SocketTileAccess, config: SideConfig, side: ForgeDirection)
	{
		config.meta += 1;
		if(config.meta >= 8) config.meta = 0;
		ts.sendClientSideState(side.ordinal);
	}
	
	
	override def getToolTip(l: List[Object])
	{
		l.add("Allows for range selection");
		l.add("for certain machines");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_WHITE + "Change range");
	}
}