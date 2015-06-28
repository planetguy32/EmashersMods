package emasher.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.Random;

public class BlockNaturalGas extends BlockGasGeneric {
	public BlockNaturalGas() {
		super( 0, true );
	}
	

	@Override
	@SideOnly( Side.CLIENT )
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "eng_toolbox:naturalGas" );
	}
	
	@Override
	public void onEntityCollidedWithBlock( World par1World, int par2, int par3, int par4, Entity ent ) {
		if( !par1World.isRemote ) {
			Random rand = par1World.rand;
			Item helmet = null;
			int helmetIndex = 3;
			if( ent instanceof EntityPlayer ) {
				if( ( ( EntityPlayer ) ent ).inventory.armorItemInSlot( helmetIndex ) != null ) {
					helmet = ( ( EntityPlayer ) ent ).inventory.armorItemInSlot( helmetIndex ).getItem();
				}
			}
			
			if( ent instanceof EntityPlayer && helmet == emasher.items.Items.gasMask() ) {
				ItemStack helmStack = ( ( EntityPlayer ) ent ).inventory.armorItemInSlot( helmetIndex );
				
				if( rand.nextInt( 10 ) == 0 ) {
					helmStack.damageItem( 1, ( EntityLivingBase ) ent );
				}
				
				if( helmStack.getItemDamage() >= helmStack.getMaxDamage() ) {
					( ( EntityPlayer ) ent ).inventory.armorInventory[helmetIndex] = null;
				}
				
			} else if( ent instanceof EntityLivingBase ) {
				ent.attackEntityFrom( DamageSource.drown, 1 );
				( ( EntityLivingBase ) ent ).addPotionEffect( new PotionEffect( 9, 500 ) );
				( ( EntityLivingBase ) ent ).addPotionEffect( new PotionEffect( 19, 500 ) );
			}
		}
		
	}
}