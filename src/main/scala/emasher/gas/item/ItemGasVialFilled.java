package emasher.gas.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.gas.EmasherGas;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

public class ItemGasVialFilled extends Item {
	@SideOnly( Side.CLIENT )
	public IIcon[] textures;
	
	public ItemGasVialFilled() {
		super();
		
		setCreativeTab( EmasherGas.tabGasCraft );
		setMaxStackSize( 1 );
		setUnlocalizedName( "gasVialFilled" );
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public IIcon getIconFromDamage( int damage ) {
		if( damage == 0 ) return this.itemIcon;
		return textures[damage];
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister ir ) {
		textures = new IIcon[8];
		
		textures[0] = ir.registerIcon( "gascraft:naturalGasVial" );
		textures[1] = ir.registerIcon( "gascraft:propellentVial" );
		textures[2] = ir.registerIcon( "gascraft:hydrogenVial" );
		textures[3] = ir.registerIcon( "gascraft:smokeVial" );
		textures[4] = ir.registerIcon( "gascraft:toxicGasVial" );
		textures[5] = ir.registerIcon( "gascraft:neurotoxinVial" );
		textures[6] = ir.registerIcon( "gascraft:corrosiveGasVial" );
		textures[7] = ir.registerIcon( "gascraft:plasmaVial" );
		
		this.itemIcon = textures[0];
	}
	
	public FluidStack getFluid( ItemStack stack ) {
		int meta = stack.getItemDamage();
		int v = FluidContainerRegistry.BUCKET_VOLUME;
		
		switch(meta) {
			case 0:
				return new FluidStack( EmasherGas.fluidNaturalGas, v );
			case 1:
				return new FluidStack( EmasherGas.fluidPropellent, v );
			case 2:
				return new FluidStack( EmasherGas.fluidHydrogen, v );
			case 3:
				return new FluidStack( EmasherGas.fluidSmoke, v );
			case 4:
				return new FluidStack( EmasherGas.fluidToxicGas, v );
			case 5:
				return new FluidStack( EmasherGas.fluidNeurotoxin, v );
			case 6:
				return new FluidStack( EmasherGas.fluidCorrosiveGas, v );
			case 7:
				return new FluidStack( EmasherGas.fluidPlasma, v );
			default:
				return new FluidStack( EmasherGas.fluidNaturalGas, v );
		}
	}
	
	@Override
	public String getUnlocalizedName( ItemStack itemstack ) {
		String name = "";
		switch(itemstack.getItemDamage()) {
			case 0:
				name = "naturalGas";
				break;
			case 1:
				name = "propellent";
				break;
			case 2:
				name = "hydrogen";
				break;
			case 3:
				name = "smoke";
				break;
			case 4:
				name = "toxicGas";
				break;
			case 5:
				name = "neurotoxin";
				break;
			case 6:
				name = "corrosiveGas";
				break;
			case 7:
				name = "plasma";
				break;
		}
		return getUnlocalizedName() + "." + name;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void getSubItems( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		for( int i = 0; i < 8; i++ ) par3List.add( new ItemStack( par1, 1, i ) );
	}

	
	@Override
	public ItemStack onItemRightClick( ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer ) {
		float var4 = 1.0F;
		double var5 = par3EntityPlayer.prevPosX + ( par3EntityPlayer.posX - par3EntityPlayer.prevPosX ) * ( double ) var4;
		double var7 = par3EntityPlayer.prevPosY + ( par3EntityPlayer.posY - par3EntityPlayer.prevPosY ) * ( double ) var4 + 1.62D - ( double ) par3EntityPlayer.yOffset;
		double var9 = par3EntityPlayer.prevPosZ + ( par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ ) * ( double ) var4;
		MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer( par2World, par3EntityPlayer, true );

		if( var12 == null ) {
			return par1ItemStack;
		} else if( var12.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK ) {
			int var13 = var12.blockX;
			int var14 = var12.blockY;
			int var15 = var12.blockZ;

			Block block = par2World.getBlock( var13, var14, var15 );


			if( var12.sideHit == 0 ) {
				--var14;
			}

			if( var12.sideHit == 1 ) {
				++var14;
			}

			if( var12.sideHit == 2 ) {
				--var15;
			}

			if( var12.sideHit == 3 ) {
				++var15;
			}

			if( var12.sideHit == 4 ) {
				--var13;
			}

			if( var12.sideHit == 5 ) {
				++var13;
			}

			//int i = par2World.getBlockId(var13, var14, var15);
			Block b = par2World.getBlock( var13, var14, var15 );

			if( b == Blocks.air && !( block instanceof IFluidHandler ) ) {
				par2World.playSoundEffect( ( double ) var13 + 0.5D, ( double ) var14 + 0.5D, ( double ) var15 + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F );
				par2World.setBlock( var13, var14, var15, getFluid( par1ItemStack ).getFluid().getBlock() );

				if( !par3EntityPlayer.capabilities.isCreativeMode ) {
					par1ItemStack.stackSize--;
					return ( new ItemStack( EmasherGas.vial, 1, 0 ) );
				} else {
					return par1ItemStack;
				}

			}

		}

		return par1ItemStack;
	}

	
}