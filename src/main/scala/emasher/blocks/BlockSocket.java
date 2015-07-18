package emasher.blocks;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import emasher.api.ModuleRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.tileentities.TileSocket;
import emasher.items.ItemEngWrench;
import emasher.packethandling.SocketStateMessage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class BlockSocket extends BlockContainer {
	public static final String[] dyes =
			{
					"dyeBlack",
					"dyeRed",
					"dyeGreen",
					"dyeBrown",
					"dyeBlue",
					"dyePurple",
					"dyeCyan",
					"dyeLightGray",
					"dyeGray",
					"dyePink",
					"dyeLime",
					"dyeYellow",
					"dyeLightBlue",
					"dyeMagenta",
					"dyeOrange",
					"dyeWhite"
			};
	@SideOnly( Side.CLIENT )
	public IIcon[][] textures;
	@SideOnly( Side.CLIENT )
	public IIcon[] tankIndicator;
	@SideOnly( Side.CLIENT )
	public IIcon[] inventoryIndicator;
	@SideOnly( Side.CLIENT )
	public IIcon[] rsIndicator;
	@SideOnly( Side.CLIENT )
	public IIcon[] latchIndicator;
	@SideOnly( Side.CLIENT )
	public IIcon[] bar1;
	@SideOnly( Side.CLIENT )
	public IIcon[] bar2;
	@SideOnly( Side.CLIENT )
	public IIcon[] chargeInd;
	@SideOnly( Side.CLIENT )
	public IIcon hasData;
	private boolean wrenched = false;
	
	public BlockSocket() {
		super( Material.iron );
		setCreativeTab( EngineersToolbox.tabBlocks() );
	}
	
	@Override
	public TileEntity createNewTileEntity( World world, int metadata ) {
		return new TileSocket();
	}

	
	@Override
	public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ ) {
		TileEntity t = world.getTileEntity( x, y, z );

		if( !world.isRemote && t != null && t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			
			if( player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == emasher.items.Items.remote() && !player.isSneaking() ) {
				switch(player.getCurrentEquippedItem().getItemDamage()) {
					case 0:
						if( ts.getSide( ForgeDirection.getOrientation( side ) ).hasTankIndicator() && !ts.sideLocked[side] )
							ts.nextTank( side );
						break;
					case 1:
						if( ts.getSide( ForgeDirection.getOrientation( side ) ).hasInventoryIndicator() && !ts.sideLocked[side] )
							ts.nextInventory( side );
						break;
					case 2:
						if( ts.getSide( ForgeDirection.getOrientation( side ) ).hasRSIndicator() && !ts.sideLocked[side] )
							ts.nextRS( side );
						break;
					case 3:
						if( ts.getSide( ForgeDirection.getOrientation( side ) ).hasLatchIndicator() && !ts.sideLocked[side] )
							ts.nextLatch( side );
						break;
					case 4:
						if( !ts.sideLocked[side] )
							ts.getSide( ForgeDirection.getOrientation( side ) ).onGenericRemoteSignal( ts, ts.configs[side], ForgeDirection.getOrientation( side ), subX, subY, subZ );
						break;
					case 5:
						ts.lockSide( side );
						break;
					case 6:
						ts.facID[side] = 0;
						ts.facMeta[side] = 0;
						EngineersToolbox.network().sendToDimension( new SocketStateMessage( ts, ( byte ) side ), ts.getWorldObj().provider.dimensionId );
						break;
				}
			} else if( player.getCurrentEquippedItem() != null && ( player.getCurrentEquippedItem().getItem() instanceof IToolWrench ) ) {
				if( player.getCurrentEquippedItem().getItem() instanceof ItemEngWrench && player.getCurrentEquippedItem().getItemDamage() > 0 ) {
					int dam = player.getCurrentEquippedItem().getItemDamage();
					if( dam == 1 ) {
						ItemStack theStack = new ItemStack( this, 1, 0 );

						for( int i = 0; i < 6; i++ ) {
							ts.getSide( ForgeDirection.getOrientation( i ) ).onSocketSave( ts.configs[i], ts, ForgeDirection.getOrientation( side ) );
						}
						
						NBTTagCompound data = new NBTTagCompound();
						ts.writeToNBT( data );
						theStack.setTagCompound( data );
						
						if( !world.isRemote ) {
							float f = 0.7F;
							double d0 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
							double d1 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
							double d2 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
							EntityItem entityitem = new EntityItem( world, ( double ) x + d0, ( double ) y + d1, ( double ) z + d2, theStack );
							entityitem.delayBeforeCanPickup = 10;
							world.spawnEntityInWorld( entityitem );

							for( int i = 0; i < 6; i++ ) {
								ts.getSide( ForgeDirection.getOrientation( i ) ).onSocketRemoved( ts.configs[i], ts, ForgeDirection.getOrientation( side ), true );
							}
						}

						wrenched = true;
						world.setBlockToAir( x, y, z );
					} else {
						int sideID = ts.sides[side];
						
						if( sideID != 0 && !ts.sideLocked[side] ) {
							ItemStack theStack = new ItemStack( emasher.items.Items.module(), 1, sideID );
							
							if( !world.isRemote ) {
								ForgeDirection d = ForgeDirection.getOrientation( side );
								int xo = x + d.offsetX;
								int yo = y + d.offsetY;
								int zo = z + d.offsetZ;
								
								float f = 0.7F;
								double d0 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
								double d1 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
								double d2 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
								EntityItem entityitem = new EntityItem( world, ( double ) xo + d0, ( double ) yo + d1, ( double ) zo + d2, theStack );
								entityitem.delayBeforeCanPickup = 5;
								world.spawnEntityInWorld( entityitem );
								ts.getSide( d ).onRemoved( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );
							}
							
							
							ts.sides[side] = 0;
							ts.resetConfig( side );
						}
					}
				} else if( player.isSneaking() ) {
					ItemStack theStack = new ItemStack( this, 1, 0 );
					
					NBTTagCompound data = new NBTTagCompound();
					ts.writeToNBT( data );
					//data.setBoolean("ench", true);
					theStack.setTagCompound( data );
					
					if( !world.isRemote ) {
						float f = 0.7F;
						double d0 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
						double d1 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
						double d2 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
						EntityItem entityitem = new EntityItem( world, ( double ) x + d0, ( double ) y + d1, ( double ) z + d2, theStack );
						entityitem.delayBeforeCanPickup = 10;
						world.spawnEntityInWorld( entityitem );

						for( int i = 0; i < 6; i++ ) {
							ts.getSide( ForgeDirection.getOrientation( i ) ).onSocketRemoved( ts.configs[i], ts, ForgeDirection.getOrientation( side ), true );
						}
					}

					wrenched = true;
					world.setBlockToAir( x, y, z );
					world.removeTileEntity( x, y, z );
				} else {
					int sideID = ts.sides[side];
					
					if( sideID != 0 && !ts.sideLocked[side] ) {
						ItemStack theStack = new ItemStack( emasher.items.Items.module(), 1, sideID );
						
						if( !world.isRemote ) {
							float f = 0.7F;
							double d0 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
							double d1 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
							double d2 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
							EntityItem entityitem = new EntityItem( world, ( double ) x + d0, ( double ) y + d1, ( double ) z + d2, theStack );
							entityitem.delayBeforeCanPickup = 10;
							world.spawnEntityInWorld( entityitem );
							ts.getSide( ForgeDirection.getOrientation( side ) )
									.onRemoved( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );
						}
						
						
						ts.sides[side] = 0;
						ts.resetConfig( side );
					}
				}
			} else if( player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == emasher.items.Items.module() ) {
				if( ts.sides[side] == 0 || player.capabilities.isCreativeMode ) {
					if( ModuleRegistry.getModule( player.getCurrentEquippedItem().getItemDamage() ).canBeInstalled( ts, ForgeDirection.getOrientation( side ) ) ) {
						ts.sides[side] = player.getCurrentEquippedItem().getItemDamage();
						ts.resetConfig( side );
						ts.getSide( ForgeDirection.getOrientation( side ) ).init( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );
						if( !player.capabilities.isCreativeMode ) player.getCurrentEquippedItem().stackSize--;
					}
				}
			}
//			else if(Loader.isModLoaded("BuildCraft|Core") && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemFacade)
//			{
//				int bId = ItemFacade.getBlockId(player.getCurrentEquippedItem());
//
//				if(Block.blocksList[bId] != null && Block.blocksList[bId].isOpaqueCube())
//				{
//					ts.facID[side] = bId;
//					ts.facMeta[side] =ItemFacade.getMetaData(player.getCurrentEquippedItem());
//					PacketHandler.instance.SendClientSideState(ts, (byte)side);
//					player.getCurrentEquippedItem().stackSize--;
//				}
//			}
			else {
				SocketModule m = ts.getSide( ForgeDirection.getOrientation( side ) );

				if( m.canModuleBeDyed() ) {

					if( player.getCurrentEquippedItem() != null ) {

						for( int i = 0; i < 16; ++i ) {
							if( OreDictionary.itemMatches( player.getCurrentEquippedItem(), new ItemStack( Items.dye, 1, i ), true ) ) {
								m.changeColour( i, ts.configs[side], ts, ForgeDirection.getOrientation( side ) );
								player.getCurrentEquippedItem().stackSize--;
								return true;
							}
						}
					}
				}

				m.onSideActivated( ts, ts.configs[side], ForgeDirection.getOrientation( side ), player, subX, subY, subZ );
			}
			
		}
		
		if( t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			ItemStack item = player.getCurrentEquippedItem();
			if( ( item == null || ( !( item.getItem() instanceof IToolWrench ) && item.getItem() != emasher.items.Items.module() && item.getItem() != emasher.items.Items.remote() ) ) && side >= 0 && side < 6 ) {
				int oreId = OreDictionary.getOreID( player.getCurrentEquippedItem() );
				boolean wasDye = false;
				for( int i = 0; i < dyes.length; i++ ) {
					if( oreId == OreDictionary.getOreID( dyes[i] ) ) {
						wasDye = true;
						break;
					}
				}
				SocketModule m = ts.getSide( ForgeDirection.getOrientation( side ) );
				if( m != null && !wasDye )
					m.onSideActivatedClient( ts, ts.configs[side], ForgeDirection.getOrientation( side ), player, subX, subY, subZ );
			}
		}
		return true;
	}
	
	@Override
	public void onEntityWalking( World world, int x, int y, int z, Entity entity ) {
		TileEntity t = world.getTileEntity( x, y, z );
		SocketModule m;
		
		if( !world.isRemote && t != null && t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			for( int i = 0; i < 6; i++ ) {
				m = ts.getSide( ForgeDirection.getOrientation( i ) );
				m.onEntityWalkOn( ts, ts.configs[i], ForgeDirection.getOrientation( i ), entity );
			}
		}
	}

	@Override
	public void onBlockPreDestroy( World world, int x, int y, int z, int oldmeta ) {
		if( wrenched ) return;

		TileEntity te = world.getTileEntity( x, y, z );

		if( te != null && te instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) te;
			int sideID;

			for( int side = 0; side < 6; side++ ) {
				sideID = ts.sides[side];

				if( sideID != 0 && !ts.sideLocked[side] ) {
					ItemStack theStack = new ItemStack( emasher.items.Items.module(), 1, sideID );

					if( !world.isRemote ) {
						float f = 0.7F;
						double d0 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
						double d1 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
						double d2 = ( double ) ( world.rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
						EntityItem entityitem = new EntityItem( world, ( double ) x + d0, ( double ) y + d1, ( double ) z + d2, theStack );
						entityitem.delayBeforeCanPickup = 10;
						world.spawnEntityInWorld( entityitem );
						ts.getSide( ForgeDirection.getOrientation( side ) )
								.onSocketRemoved( ts.configs[side], ts, ForgeDirection.getOrientation( side ), false );
					}


					ts.sides[side] = 0;
					ts.resetConfig( side );
				}
			}
		}
	}
	
	@Override
	public void breakBlock( World par1World, int par2, int par3, int par4, Block par5, int par6 ) {
		//System.out.println("Break");
		super.breakBlock( par1World, par2, par3, par4, par5, par6 );
		par1World.removeTileEntity( par2, par3, par4 );
	}

	@Override
	public void onBlockPlacedBy( World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack ) {
		wrenched = false;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public IIcon getIcon( IBlockAccess world, int x, int y, int z, int blockSide ) {
		IIcon result = blockIcon;
		
		TileEntity t = world.getTileEntity( x, y, z );
		
		if( t != null && t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			
			if( ts.facID[blockSide] > 0 && Block.getBlockById( ts.facID[blockSide] ) != null ) {
				Block b = Block.getBlockById( ts.facID[blockSide] );
				result = b.getIcon( blockSide, ts.facMeta[blockSide] );
			} else if( ts.sides[blockSide] != 0 ) {
				SocketModule m = ts.getSide( ForgeDirection.getOrientation( blockSide ) );
				SideConfig c = ts.configs[blockSide];
				int index = m.getCurrentTexture( c, ts, ForgeDirection.getOrientation( blockSide ) );
				result = textures[m.moduleID][index];
			}
		}
		
		return result;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerBlockIcons( IIconRegister ir ) {
		SocketModule m;
		int l;
		int temp;
		
		this.blockIcon = ir.registerIcon( "eng_toolbox:bg" );
		
		textures = new IIcon[ModuleRegistry.numModules][];
		tankIndicator = new IIcon[4];
		inventoryIndicator = new IIcon[4];
		rsIndicator = new IIcon[8];
		latchIndicator = new IIcon[8];
		bar1 = new IIcon[8];
		bar2 = new IIcon[8];
		chargeInd = new IIcon[13];
		
		for( int i = 0; i < ModuleRegistry.numModules; i++ ) {
			m = ModuleRegistry.getModule( i );
			if( m != null ) {
				l = m.textureFiles.length;
				textures[i] = new IIcon[l];
				for( int j = 0; j < l; j++ ) {
					textures[i][j] = ir.registerIcon( m.textureFiles[j] );
				}

				//Register inner textures
				String[] innerTextures = m.getAllInternalTextures();
				for( int j = 0; j < innerTextures.length; j++ ) {
					String texture = innerTextures[j];
					if( !EngineersToolbox.innerTextures().containsKey( texture ) ) {
						EngineersToolbox.innerTextures().put( texture, ir.registerIcon( texture ) );
					}
				}
			}
		}
		
		for( int i = -1; i < 3; i++ ) {
			if( i == -1 ) temp = 3;
			else temp = i;
			tankIndicator[temp] = ir.registerIcon( "eng_toolbox:ind_l_" + i );
			inventoryIndicator[temp] = ir.registerIcon( "eng_toolbox:ind_i_" + i );
		}
		
		for( int i = 0; i < 8; i++ ) {
			rsIndicator[i] = ir.registerIcon( "eng_toolbox:ind_r_" + i );
			latchIndicator[i] = ir.registerIcon( "eng_toolbox:ind_a_" + i );
			bar1[i] = ir.registerIcon( "eng_toolbox:rsInd" + i );
			bar2[i] = ir.registerIcon( "eng_toolbox:timeInd" + i );
		}
		
		for( int i = 0; i < 13; i++ ) {
			chargeInd[i] = ir.registerIcon( "eng_toolbox:chargeInd" + i );
		}

		hasData = ir.registerIcon( "eng_toolbox:hasData" );
		
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public boolean canConnectRedstone( IBlockAccess world, int x, int y, int z, int side ) {
		if( side < 0 || side > 5 ) return false;
		
		TileEntity t = world.getTileEntity( x, y, z );
		
		if( t != null && t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( side ) );
			//System.out.println(side + ", " + ts.sides[side]);
			return m.isRedstoneInterface();
		}
		
		return false;
	}
	
	
	@Override
	public void onNeighborBlockChange( World world, int x, int y, int z, Block nblock ) {
		TileEntity t = world.getTileEntity( x, y, z );
		
		if( t != null && t instanceof TileSocket && !world.isRemote ) {
			TileSocket ts = ( TileSocket ) t;
			SocketModule m;
			
			for( int i = 0; i < 6; i++ ) {
				ForgeDirection d = ForgeDirection.getOrientation( i );
				ForgeDirection opposite = d.getOpposite();
				m = ts.getSide( opposite );
				
				if( nblock != Blocks.redstone_wire && ts.initialized ) {
					m.onAdjChange( ts, ts.configs[opposite.ordinal()], opposite );
					ts.checkSideForChange( i );
				}
				
				boolean rs = world.getIndirectPowerOutput( x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ, opposite.ordinal() );
				
				boolean oldRS = ts.sideRS[opposite.ordinal()];
				if( rs != oldRS ) {
					ts.sideRS[opposite.ordinal()] = rs;
					m.updateRestone( rs, ts.configs[opposite.ordinal()], ts );
					EngineersToolbox.network().sendToDimension( new SocketStateMessage( ts, ( byte ) opposite.ordinal() ), ts.getWorldObj().provider.dimensionId );
				}
			}
		}
	}
	
	@Override
	public int isProvidingStrongPower( IBlockAccess world, int x, int y, int z, int side ) {
		TileEntity t = world.getTileEntity( x, y, z );
		
		side = ForgeDirection.OPPOSITES[side];
		
		if( t != null && t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( side ) );
			SideConfig c = ts.configs[side];
			
			if( m.isOutputtingRedstone( c, ts ) ) return 15;
		}
		
		return 0;
	}
	
	public int isProvidingWeakPower( IBlockAccess world, int x, int y, int z, int side ) {
		TileEntity t = world.getTileEntity( x, y, z );
		
		side = ForgeDirection.OPPOSITES[side];
		
		if( t != null && t instanceof TileSocket ) {
			TileSocket ts = ( TileSocket ) t;
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( side ) );
			SideConfig c = ts.configs[side];
			
			if( m.isOutputtingRedstone( c, ts ) ) return 15;
		}
		
		return 0;
	}
	
	@Override
	public boolean isBlockSolid( IBlockAccess world, int x, int y, int z, int side ) {
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean shouldSideBeRendered( IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5 ) {
		return true;
	}

}
