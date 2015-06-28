package emasher.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemHandPiston extends Item {
	public ItemHandPiston( int id ) {
		super();
		
		this.setCreativeTab( EngineersToolbox.tabItems() );
		this.setMaxStackSize( 1 );
		this.setUnlocalizedName( "eng_handPiston" );
	}

	@Override
	public boolean isItemTool( ItemStack par1ItemStack ) {
		return true;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister ir ) {
		this.itemIcon = ir.registerIcon( "eng_toolbox:handPiston" );
	}
	
	@Override
	public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10 ) {
		ForgeDirection d = ForgeDirection.getOrientation( side );
		ForgeDirection o = d.getOpposite();
		
		int xo = x + o.offsetX;
		int yo = y + o.offsetY;
		int zo = z + o.offsetZ;
		
		//int id == world.getBlock
		
		if( world.getBlock( xo, yo, zo ) == Blocks.air ) {
			
			
			return true;
		}
		
		return false;
	}
	
}
