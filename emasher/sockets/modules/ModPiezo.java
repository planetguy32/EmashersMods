package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModPiezo extends SocketModule
{

	public ModPiezo(int id)
	{
		super(id, "sockets:piezoElectric");
	}

	@Override
	public String getLocalizedName()
	{
		return "Piezo Electric Tile";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Generates power when steppend on by entities");
		l.add("Has a 10 tick cooldown time (~0.5 seconds)");
		l.add("Randomly damages entities that walk on it");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_AQUA + "Generates 80 f/step");
		l.add("Can only be placed on the top of a socket");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "pip", " b ", Character.valueOf('i'), Block.pressurePlateStone, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side != ForgeDirection.UP) return false;
		return true;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.meta >  0) config.meta--;
	}
	
	@Override
	public void onEntityWalkOn(SocketTileAccess ts, SideConfig config, ForgeDirection side, Entity entity)
	{
		if(config.meta <= 0 && side == ForgeDirection.UP && ts.getMaxEnergyStored() - ts.getEnergyStored() >= 80 && entity != null && entity instanceof EntityLiving)
		{
			EntityLiving el = (EntityLiving)entity;
			ts.addEnergy(80, false);
			config.meta = 10;
			if(ts.worldObj.rand.nextInt(20) == 0)el.attackEntityFrom(DamageSource.inFire, 4);
		}
	}
	
}
