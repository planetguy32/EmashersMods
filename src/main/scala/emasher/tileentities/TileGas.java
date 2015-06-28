package emasher.tileentities;

import emasher.util.Tuple;
import emasher.blocks.BlockGasGeneric;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class TileGas extends TileEntity {
	public static final int VOLUME = FluidContainerRegistry.BUCKET_VOLUME * 4;
	Random rand;
	int count;
	Tuple[] pos = new Tuple[] {new Tuple( 0, 1 ), new Tuple( 1, 0 ), new Tuple( 0, -1 ), new Tuple( -1, 0 )};
	private FluidStack gas;
	
	public TileGas() {
		rand = new Random( System.nanoTime() );
		count = rand.nextInt( 8 );
	}
	
	public TileGas( Fluid gasType ) {
		gas = new FluidStack( gasType, VOLUME );
		rand = new Random( System.nanoTime() );
		count = rand.nextInt( 8 );
	}
	
	@Override
	public void updateEntity() {
		if( !worldObj.isRemote ) {
			//if(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)] instanceof BlockGasGeneric)
			if( worldObj.getBlock( xCoord, yCoord, zCoord ) instanceof BlockGasGeneric ) {
				if( count == 4 ) {
					BlockGasGeneric thisBlock = ( BlockGasGeneric ) worldObj.getBlock( xCoord, yCoord, zCoord );
					for( int i = xCoord - 1; i < xCoord + 2; i++ )
						for( int j = yCoord - 1; j < yCoord + 2; j++ )
							for( int k = zCoord - 1; k < zCoord + 2; k++ ) {
								if( worldObj.getBlock( i, j, k ) == Blocks.fire || ( worldObj.getBlock( i, j, k ) == Blocks.torch && worldObj.difficultySetting == EnumDifficulty.HARD ) ) {
									thisBlock.contactFire( worldObj, xCoord, yCoord, zCoord );
								}
							}

					if( worldObj.getBlock( xCoord, yCoord, zCoord ) == emasher.blocks.Blocks.plasma() )
						for( int i = 0; i < 6; i++ ) {
							if( gas.amount > 1 ) {
								ForgeDirection d = ForgeDirection.getOrientation( i );
								int xo = xCoord + d.offsetX;
								int yo = yCoord + d.offsetY;
								int zo = zCoord + d.offsetZ;

								//int id = worldObj.getBlockId(xo, yo, zo);
								Block b = worldObj.getBlock( xo, yo, zo );
								boolean doDamage = false;

								if( b != Blocks.air && b != Blocks.stone ) {
									ItemStack is = new ItemStack( b, 1, worldObj.getBlockMetadata( xo, yo, zo ) );

									ItemStack product = FurnaceRecipes.smelting().getSmeltingResult( is );

									if( product != null ) {

										//if(Item.itemsList[product.itemID] != null && ! (product.getItem() instanceof ItemBlock))
										if( product.getItem() != null && !( product.getItem() instanceof ItemBlock ) ) {
											product = ItemStack.copyItemStack( product );

											EntityItem drop = new EntityItem( worldObj, xo, yo, zo, product );

											if( product.hasTagCompound() ) {
												drop.getEntityItem().setTagCompound( ( NBTTagCompound ) product.getTagCompound().copy() );
											}

											if( !worldObj.isRemote ) worldObj.spawnEntityInWorld( drop );
											fizz( worldObj, xo, yo, zo );
											doDamage = true;
											worldObj.setBlockToAir( xo, yo, zo );
										}
										//else if(product.itemID < Block.blocksList.length && Block.blocksList[product.itemID] != null && Block.blocksList[product.itemID] instanceof Block)
										else if( product.getItem() != null && product.getItem() instanceof ItemBlock ) {
											if( b != Blocks.sand || emasher.util.Config.smeltSand() ) {
												worldObj.setBlock( xo, yo, zo, Block.getBlockFromItem( product.getItem() ), product.getItemDamage(), 2 );
												fizz( worldObj, xo, yo, zo );
												doDamage = true;
											}
										}


									}
								}

								if( doDamage ) {
									gas.amount /= 2;
								}
							}
						}
				}
				
				if( count == 8 ) {
					//BlockGasGeneric thisBlock = (BlockGasGeneric)Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
					BlockGasGeneric thisBlock = ( BlockGasGeneric ) worldObj.getBlock( xCoord, yCoord, zCoord );

					
					if( gas.amount <= 8 ) {
						if( canDis( 10 ) ) {
							worldObj.setBlockToAir( xCoord, yCoord, zCoord );
						} else if( worldObj.isAirBlock( xCoord, yCoord + 1, zCoord ) ) {
							moveToOffset( 0, 1, 0 );
						} else if( thisBlock.canDestroyBlock( worldObj.getBlock( xCoord, yCoord + 1, zCoord ), xCoord, yCoord + 1, zCoord, worldObj ) && gas.amount > 1 ) {
							gas.amount /= 2;
							moveToOffset( 0, 1, 0 );
						} else {
							int x, z;
							boolean done = false;
							int r = rand.nextInt( 4 );
							
							
							for( int i = 0; i < 4 && !done; i++ ) {
								x = pos[r].x();
								z = pos[r].y();
								
								if( worldObj.isAirBlock( xCoord + x, yCoord, zCoord + z ) ) {
									moveToOffset( x, 0, z );
									done = true;
								} else if( thisBlock.canDestroyBlock( worldObj.getBlock( xCoord + x, yCoord, zCoord + z ), xCoord + x, yCoord, zCoord + z, worldObj ) && gas.amount > 1 ) {
									gas.amount /= 2;
									moveToOffset( x, 0, z );
								}
								
								r++;
								if( r == 4 ) r = 0;
							}
						}
					} else if( gas.amount > 8 ) {
						if( worldObj.isAirBlock( xCoord, yCoord + 1, zCoord ) ) {
							splitToOffset( 0, 1, 0 );
						} else if( thisBlock.canDestroyBlock( worldObj.getBlock( xCoord, yCoord + 1, zCoord ), xCoord, yCoord + 1, zCoord, worldObj ) ) {
							gas.amount /= 2;
							splitToOffset( 0, 1, 0 );
						} else {
							int x, z;
							boolean done = false;
							int r = rand.nextInt( 4 );
							
							
							for( int i = 0; i < 4 && !done; i++ ) {
								x = pos[r].x();
								z = pos[r].y();
								
								if( worldObj.isAirBlock( xCoord + x, yCoord, zCoord + z ) ) {
									splitToOffset( x, 0, z );
									done = true;
								} else if( thisBlock.canDestroyBlock( worldObj.getBlock( xCoord + x, yCoord, zCoord + z ), xCoord + x, yCoord, zCoord + z, worldObj ) ) {
									gas.amount /= 2;
									splitToOffset( x, 0, z );
									done = true;
								}
								
								
								r++;
								if( r == 4 ) r = 0;
							}
							
							if( !done && worldObj.isAirBlock( xCoord, yCoord - 1, zCoord ) ) {
								splitToOffset( 0, -1, 0 );
							}
						}
					}
					count = 0;
				} else {
					count++;
				}
			}
		}
		
	}
	
	public boolean canDis( int n ) {
		boolean result = true;
		
		int i = 0;
		
		while( result && i < n ) {
			result = worldObj.isAirBlock( xCoord, yCoord + i + 1, zCoord );
			
			i++;
		}
		
		return result;
	}
	
	public void moveToOffset( int x, int y, int z ) {
		worldObj.setBlock( xCoord + x, yCoord + y, zCoord + z, gas.getFluid().getBlock(), this.blockMetadata, 4 );
		TileEntity t = worldObj.getTileEntity( xCoord + x, yCoord + y, zCoord + z );
		if( t != null && t instanceof TileGas ) {
			( ( TileGas ) t ).setGasAmount( gas.amount );
		}
		
		worldObj.setBlockToAir( xCoord, yCoord, zCoord );
		worldObj.removeTileEntity( xCoord, yCoord, zCoord );
	}
	
	public void splitToOffset( int x, int y, int z ) {
		int vol;
		int meta;
		worldObj.setBlock( xCoord + x, yCoord + y, zCoord + z, gas.getFluid().getBlock() );
		TileEntity t = worldObj.getTileEntity( xCoord + x, yCoord + y, zCoord + z );
		if( t != null && t instanceof TileGas ) {
			TileGas tg = ( TileGas ) t;
			
			tg.setGasAmount( gas.amount / 2 );

			vol = tg.getGasAmount();
			meta = ( vol * 15 ) / TileGas.VOLUME;
			worldObj.setBlockMetadataWithNotify( x + xCoord, y + yCoord, z + zCoord, meta, 4 );
		}
		
		gas.amount /= 2;
		
		vol = gas.amount;
		meta = ( vol * 15 ) / TileGas.VOLUME;
		worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, meta, 4 );
	}
	
	public int getGasAmount() {
		return gas.amount;
	}
	
	public void setGasAmount( int newAmount ) {
		gas.amount = newAmount;
	}
	
	public int getExplosionSize() {
		return 4;
	}
	
	public void setGasAmount( int newAmount, World world, int x, int y, int z ) {
		gas.amount = newAmount;
		world.setBlockMetadataWithNotify( x, y, z, entityToBlock( newAmount ), 2 );
	}
	
	public FluidStack getGas() {
		return gas;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );

		if( data.hasKey( "Amount" ) ) {
			gas = FluidStack.loadFluidStackFromNBT( data );
		}
	}

	@Override
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		gas.writeToNBT( data );
	}

	public void fizz( World world, int x, int y, int z ) {
		world.playSoundEffect( ( double ) x + 0.5D, ( double ) y + 0.5D, ( double ) z + 0.5D, "random.fizz", 1.0F, worldObj.rand.nextFloat() * 0.4F + 0.8F );
		for( int i = 0; i < 10; i++ ) {
			world.spawnParticle( "smoke", ( double ) x + worldObj.rand.nextDouble() - 0.5, y + worldObj.rand.nextDouble() - 0.5, z + worldObj.rand.nextDouble() - 0.5, 0, 0, 0 );
		}
	}

	public static int entityToBlock( int entAmount ) {
		return ( entAmount * 15 ) / TileGas.VOLUME;
	}

	
}
