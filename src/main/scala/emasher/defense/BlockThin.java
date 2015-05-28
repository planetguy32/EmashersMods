package emasher.defense;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockThin extends BlockPane {
	private static final int NUM_BLOCKS = 6;
	public static IIcon chainlink;
	public static IIcon barb;
	public static IIcon razor;
	public static IIcon chainPost;
	public static IIcon barbPost;
	public static IIcon barbPostWood;

	public BlockThin( Material par4Material ) {
		super( "emasherdefense:blank", "emasherdefense:blank", par4Material, true );
		this.setCreativeTab( null );
		this.setCreativeTab( EmasherDefense.tabDefense );
	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		super.registerBlockIcons( par1IconRegister );
		this.blockIcon = par1IconRegister.registerIcon( "emasherdefense:chainlink" );
		chainlink = this.blockIcon;
		barb = par1IconRegister.registerIcon( "emasherdefense:barb" );
		razor = par1IconRegister.registerIcon( "emasherdefense:razor" );
		chainPost = par1IconRegister.registerIcon( "emasherdefense:chainPost" );
		barbPost = par1IconRegister.registerIcon( "emasherdefense:barbPost" );
		barbPostWood = par1IconRegister.registerIcon( "emasherdefense:barbPostWood" );
	}
	
	@Override
	public IIcon getIcon( int par1, int par2 ) {
		int meta = par2;
		IIcon result;
		
		switch(meta) {
			case 0:
				result = chainlink;
				break;
			case 1:
				result = chainPost;
				break;
			case 3:
				result = barbPostWood;
				break;
			case 2:
				result = barb;
				break;
			case 4:
				result = barbPost;
				break;
			case 5:
				result = razor;
				break;
			default:
				result = chainlink;
		}
		
		return result;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@SideOnly( Side.CLIENT )
	public void getSubBlocks( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		for( int var4 = 0; var4 < NUM_BLOCKS; ++var4 ) {
			par3List.add( new ItemStack( par1, 1, var4 ) );
		}
	}
	
	
	@Override
	public boolean isLadder( IBlockAccess world, int x, int y, int z, EntityLivingBase entity ) {
		int meta = world.getBlockMetadata( x, y, z );

		if( meta == 0 || meta == 1 ) {
			if( entity instanceof EntityPlayer ) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public int damageDropped( int par1 ) {
		return par1;
	}
	
	
	@Override
	public void onEntityCollidedWithBlock( World par1World, int par2, int par3, int par4, Entity par5Entity ) {
		super.onEntityCollidedWithBlock( par1World, par2, par3, par4, par5Entity );
		
		int damage;
		int meta = par1World.getBlockMetadata( par2, par3, par4 );
		
		switch(meta) {
			case 0:
				damage = 0;
				break;
			case 1:
				damage = 0;
				break;
			case 2:
				damage = 2;
				break;
			case 3:
				damage = 2;
				break;
			case 4:
				damage = 2;
				break;
			case 5:
				damage = 4;
				break;
			default:
				damage = 0;
		}
		
		
		if( par5Entity instanceof EntityLiving && damage != 0 ) {
			( ( EntityLiving ) par5Entity ).attackEntityFrom( DamageSource.cactus, damage );
		}
	}
	
	/*@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        //return par1World.isRemote ? true : ItemLeash.func_135066_a(par5EntityPlayer, par1World, par2, par3, par4);
        
        if(! par1World.isRemote)
        {
        	int meta = par1World.getBlockMetadata(par2, par3, par4);
        	if(meta == 1 || meta == 3 || meta == 4)
        	{
        		return ItemLeash.func_135066_a(par5EntityPlayer, par1World, par2, par3, par4);
        	}
        }
        
        return true;
    }*/
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool( World par1World, int par2, int par3, int par4 ) {
		int meta = par1World.getBlockMetadata( par2, par3, par4 );
		
		this.setBlockBoundsBasedOnState( par1World, par2, par3, par4 );
		if( meta >= 2 ) this.maxY = 1.5D;
		return super.getCollisionBoundingBoxFromPool( par1World, par2, par3, par4 );
	}
	

}
