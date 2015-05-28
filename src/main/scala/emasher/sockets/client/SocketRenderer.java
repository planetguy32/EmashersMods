package emasher.sockets.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SocketModule;
import emasher.sockets.BlockSocket;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import emasher.sockets.modules.ModPressurizer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

//import net.minecraft.client.renderer.RenderEngine;
//import net.minecraftforge.liquids.LiquidDictionary;
//import net.minecraftforge.liquids.LiquidStack;

@SideOnly( Side.CLIENT )
public class SocketRenderer extends TileEntitySpecialRenderer {
	public static final SocketRenderer instance = new SocketRenderer();
	private final Tessellator tessellator = Tessellator.instance;
	private final RenderBlocks blockRender = new RenderBlocks();
	
	@Override
	public void renderTileEntityAt( TileEntity t, double x, double y, double z, float counter ) {
		TileSocket ts = ( TileSocket ) t;
		BlockSocket b = ( BlockSocket ) SocketsMod.socket;
		SocketModule m;

		//Render the inside

		FMLClientHandler.instance().getClient().entityRenderer.disableLightmap( 1 );
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable( GL11.GL_LIGHTING );
		Minecraft.getMinecraft().renderEngine.bindTexture( TextureMap.locationBlocksTexture );
		CubeRenderBounds bounds = new CubeRenderBounds( 0.05, 0.05, 0.05, 0.95, 0.95, 0.95 );
		IIcon[] icons = new IIcon[6];
		for( int i = 0; i < 6; i++ ) {
			m = ts.getSide( ForgeDirection.getOrientation( i ) );
			icons[i] = SocketsMod.innerTextures.get( m.getInternalTexture( ts, ts.configs[i], ForgeDirection.getOrientation( i ) ) );
		}
		CubeRenderer.render( x, y, z, icons, bounds, false );

		FMLClientHandler.instance().getClient().entityRenderer.enableLightmap( ( double ) counter );
		RenderHelper.enableStandardItemLighting();

		Minecraft.getMinecraft().renderEngine.bindTexture( TextureMap.locationItemsTexture );

		bounds = new CubeRenderBounds( 0, 0, 0, 1, 1, 1 );
		for( int i = 0; i < 6; i++ ) {
			icons[i] = SocketsMod.blankSide.getIconFromDamage( 0 );
		}
		CubeRenderer.render( x, y, z, icons, bounds, true );
		GL11.glEnable( GL11.GL_LIGHTING );
		
		FMLClientHandler.instance().getClient().entityRenderer.disableLightmap( 1 );
		RenderHelper.disableStandardItemLighting();
		
		for( int side = 0; side < 6; side++ ) {
			m = ts.getSide( ForgeDirection.getOrientation( side ) );
			
			GL11.glPushMatrix();
			GL11.glPushAttrib( GL11.GL_ENABLE_BIT );
			GL11.glEnable( GL11.GL_CULL_FACE );
			GL11.glDisable( GL11.GL_LIGHTING );
			GL11.glEnable( GL11.GL_BLEND );
			GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
			
			
			GL11.glTranslatef( ( float ) x, ( float ) y, ( float ) z );
			
			
			switch(side) {
				case 0:
					GL11.glRotatef( 270, 1, 0, 0 );
					GL11.glRotatef( 180, 0, 0, 1 );
					GL11.glTranslatef( -1.0F, 0.0F, -0.001F );
					break;
				case 1:
					GL11.glRotatef( 90, 1, 0, 0 );
					GL11.glTranslatef( 0.0F, 0.0F, -1.001F );
					break;
				case 2:
					GL11.glRotatef( 180, 0, 0, 1 );
					GL11.glTranslatef( -1.0F, -1.0F, -0.001F );
					break;
				case 3:
					GL11.glRotatef( 180, 0, 0, 1 );
					GL11.glRotatef( 180, 0, 1, 0 );
					GL11.glTranslatef( 0.0F, -1.0F, -1.001F );
					break;
				case 4:
					GL11.glRotatef( 180, 0, 0, 1 );
					GL11.glRotatef( 270, 0, 1, 0 );
					GL11.glTranslatef( 0.0F, -1.0F, -0.001F );
					break;
				case 5:
					GL11.glRotatef( 180, 0, 0, 1 );
					GL11.glRotatef( 90, 0, 1, 0 );
					GL11.glTranslatef( -1.0F, -1.0F, -1.001F );
					break;
			}
			
			Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( "textures/atlas/blocks.png" ) );
			//Minecraft.getMinecraft().renderEngine.bindTexture(par1ResourceLocation)
			//bindTextureByName("/terrain.png");
			
			IIcon[] indIcons = new IIcon[4];
			
			if( m.hasIndicator( 0 ) ) indIcons[0] = b.tankIndicator[ts.tankIndicatorIndex( side )];
			if( m.hasIndicator( 1 ) ) indIcons[1] = b.inventoryIndicator[ts.inventoryIndicatorIndex( side )];
			if( m.hasIndicator( 2 ) ) indIcons[2] = b.rsIndicator[ts.rsIndicatorIndex( side )];
			if( m.hasIndicator( 3 ) ) indIcons[3] = b.latchIndicator[ts.latchIndicatorIndex( side )];
			
			if( !ts.sideLocked[side] ) for( int i = 0; i < 4; i++ ) {
				if( m.hasIndicator( i ) ) {
					tessellator.startDrawingQuads();
					
					tessellator.addVertexWithUV( 0, 1, 0, indIcons[i].getMinU(), indIcons[i].getMaxV() );
					tessellator.addVertexWithUV( 1, 1, 0, indIcons[i].getMaxU(), indIcons[i].getMaxV() );
					tessellator.addVertexWithUV( 1, 0, 0, indIcons[i].getMaxU(), indIcons[i].getMinV() );
					tessellator.addVertexWithUV( 0, 0, 0, indIcons[i].getMinU(), indIcons[i].getMinV() );
					
					tessellator.draw();
				}
			}
			

			IIcon[] additional = m.getAdditionalOverlays( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );
			//bindTextureByName("/terrain.png");
			Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( "textures/atlas/blocks.png" ) );
			
			for( int i = 0; i < additional.length; i++ ) {
				tessellator.startDrawingQuads();
				
				if( side != 0 || !m.flipBottomOverlay() ) {
					tessellator.addVertexWithUV( 0, 1, 0, additional[i].getMinU(), additional[i].getMaxV() );
					tessellator.addVertexWithUV( 1, 1, 0, additional[i].getMaxU(), additional[i].getMaxV() );
					tessellator.addVertexWithUV( 1, 0, 0, additional[i].getMaxU(), additional[i].getMinV() );
					tessellator.addVertexWithUV( 0, 0, 0, additional[i].getMinU(), additional[i].getMinV() );
				} else {
					tessellator.addVertexWithUV( 0, 1, 0, additional[i].getMaxU(), additional[i].getMaxV() );
					tessellator.addVertexWithUV( 1, 1, 0, additional[i].getMinU(), additional[i].getMaxV() );
					tessellator.addVertexWithUV( 1, 0, 0, additional[i].getMinU(), additional[i].getMinV() );
					tessellator.addVertexWithUV( 0, 0, 0, additional[i].getMaxU(), additional[i].getMinV() );
				}
				
				tessellator.draw();
			}


			switch(side) {
				case 0:
					GL11.glTranslatef( 0.0F, 0.0F, 0.026F );
					break;
				case 1:
					GL11.glTranslatef( 0.0F, 0.0F, 0.026F );
					break;
				case 2:
					GL11.glTranslatef( 0.0F, 0.0F, 0.026F );
					break;
				case 3:
					GL11.glTranslatef( 0.0F, 0.0F, 0.026F );
					break;
				case 4:
					GL11.glTranslatef( 0.0F, 0.0F, 0.026F );
					break;
				case 5:
					GL11.glTranslatef( 0.0F, 0.0F, 0.026F );
					break;
			}

			GL11.glDisable( GL11.GL_BLEND );

			int tankToRender = m.getTankToRender( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );

			if( tankToRender != -1 && ts.tanks[tankToRender].getFluid() != null ) {
				FluidStack ls = ts.tanks[tankToRender].getFluid();

				if( ls != null ) {
					int cap = 8000;

					for( int i = 0; i < 6; i++ ) {
						SocketModule m2 = ts.getSide( ForgeDirection.getOrientation( i ) );
						if( m2 instanceof ModPressurizer ) {
							cap = 32000;
							break;
						}
					}
					float scale = ts.tanks[tankToRender].getFluid().amount / ( float ) cap;
					Minecraft.getMinecraft().renderEngine.bindTexture( TextureMap.locationBlocksTexture );
					if( ls.getFluid() != null && ls.getFluid().getIcon() != null ) {
						IIcon fluidIcon = ls.getFluid().getIcon();

						GL11.glTranslatef( 0.25F, 0.25F, 0.0005F );
						GL11.glScalef( 0.5F, 0.5F, 0.5F );
						GL11.glTranslatef( 0.0F, 1.0F - scale, 0.0F );
						GL11.glScalef( 1.0F, scale, 1.0F );

						tessellator.startDrawingQuads();

						tessellator.addVertexWithUV( 0, 1, 0, fluidIcon.getMinU(), fluidIcon.getMaxV() );
						tessellator.addVertexWithUV( 1, 1, 0, fluidIcon.getMaxU(), fluidIcon.getMaxV() );
						tessellator.addVertexWithUV( 1, 0, 0, fluidIcon.getMaxU(), fluidIcon.getMinV() );
						tessellator.addVertexWithUV( 0, 0, 0, fluidIcon.getMinU(), fluidIcon.getMinV() );

						tessellator.draw();

						GL11.glScalef( 1.0F, 1.0F / scale, 1.0F );
						GL11.glTranslatef( 0.0F, -( 1.0F - scale ), 0.0F );
						GL11.glScalef( 2.0F, 2.0F, 2.0F );
						GL11.glTranslatef( -0.25F, -0.25F, -0.0005F );
					}
				}

			}

			GL11.glEnable( GL11.GL_BLEND );

			if( m.renderEnergyAmount() ) {
				float enScale = ts.configs[side].meta / 100.0f;

				GL11.glTranslatef( 0.25F, 0.25F, 0.0005F );
				GL11.glScalef( 0.5F, 0.5F, 0.5F );
				GL11.glTranslatef( 0.0F, 1.0F - enScale, 0.0F );
				GL11.glScalef( 1.0F, enScale, 1.0F );

				IIcon energyIcon = SocketsMod.innerTextures.get( "sockets:inner_blue_tile" );

				tessellator.startDrawingQuads();

				tessellator.addVertexWithUV( 0, 1, 0, energyIcon.getMinU(), energyIcon.getMaxV() );
				tessellator.addVertexWithUV( 1, 1, 0, energyIcon.getMaxU(), energyIcon.getMaxV() );
				tessellator.addVertexWithUV( 1, 0, 0, energyIcon.getMaxU(), energyIcon.getMinV() );
				tessellator.addVertexWithUV( 0, 0, 0, energyIcon.getMinU(), energyIcon.getMinV() );

				tessellator.draw();

				GL11.glScalef( 1.0F, 1.0F / enScale, 1.0F );
				GL11.glTranslatef( 0.0F, -( 1.0F - enScale ), 0.0F );
				GL11.glScalef( 2.0F, 2.0F, 2.0F );
				GL11.glTranslatef( -0.25F, -0.25F, -0.0005F );
			}

			String s = m.getTextToRender( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );

			GL11.glTranslatef( 0.25F, 0.25F, 0.0F );

			GL11.glScalef( 0.01F, 0.01F, 1 );

			if( s != null )
				Minecraft.getMinecraft().fontRenderer.drawString( s, -Minecraft.getMinecraft().fontRenderer.getStringWidth( s ) / 2, 2, -1 );

			GL11.glScalef( 100, 100, 1 );

			ItemStack theStack = m.getItemToRender( ts, ts.configs[side], ForgeDirection.getOrientation( side ) );
			
			if( theStack != null ) {
				Item theItem = theStack.getItem();
				
				GL11.glScalef( 0.5F, 0.5F, 0.5F );
				
				IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer( theStack, ItemRenderType.INVENTORY );
				
				boolean isBlock = false;
				
				if( Item.getIdFromItem( theStack.getItem() ) < 4096 && Block.getBlockFromItem( theStack.getItem() ) != null && !Block.getBlockFromItem( theStack.getItem() ).getUnlocalizedName().equals( "tile.ForgeFiller" ) )
					isBlock = true;
				
				if( isBlock && RenderBlocks.renderItemIn3d( Block.getBlockFromItem( theItem ).getRenderType() ) || customRenderer != null ) {
					Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( "textures/atlas/blocks.png" ) );
					
					boolean usesHelper = customRenderer == null ? true : customRenderer.shouldUseRenderHelper( ItemRenderType.INVENTORY, theStack, ItemRendererHelper.INVENTORY_BLOCK );
					
					int color = theItem.getColorFromItemStack( theStack, 0 );
					float r = ( float ) ( color >> 16 & 255 ) / 255.0F;
					float g = ( float ) ( color >> 8 & 255 ) / 255.0F;
					float bl = ( float ) ( color & 255 ) / 255.0F;
					GL11.glColor4f( r, g, bl, 1 );
					
					GL11.glTranslatef( 0.25F, 0.25F, 0.0F );
					GL11.glScalef( 0.5F, 0.5F, 0.5F );
					
					if( usesHelper ) {
						GL11.glScalef( 1, 1, 0.0001F );
						GL11.glTranslatef( 0.5F, 0.5F, 0 );
						GL11.glRotatef( 210, 1, 0, 0 );
						GL11.glRotatef( -45, 0, 1, 0 );
					} else {
						GL11.glTranslatef( 0, 0, -3 );
					}
					
					if( usesHelper ) blockRender.useInventoryTint = true;
					
					if( customRenderer != null ) {
						customRenderer.renderItem( ItemRenderType.INVENTORY, theStack, blockRender );
					} else {
						blockRender.renderBlockAsItem( Block.getBlockFromItem( theStack.getItem() ), theStack.getItemDamage(), 1 );
					}
				} else {
					Minecraft.getMinecraft().renderEngine.bindTexture( theStack.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture );

					if( theItem.requiresMultipleRenderPasses() ) {
						int passes = theItem.getRenderPasses( theStack.getItemDamage() );
						
						for( int i = 0; i < passes; i++ ) {
							Minecraft.getMinecraft().renderEngine.bindTexture( theStack.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture );
							IIcon itemIcon = theItem.getIconFromDamageForRenderPass( theStack.getItemDamage(), i );
							if( itemIcon != null ) {
								
								tessellator.startDrawingQuads();

								tessellator.addVertexWithUV( 0, 1, 0, itemIcon.getMinU(), itemIcon.getMaxV() );
								tessellator.addVertexWithUV( 1, 1, 0, itemIcon.getMaxU(), itemIcon.getMaxV() );
								tessellator.addVertexWithUV( 1, 0, 0, itemIcon.getMaxU(), itemIcon.getMinV() );
								tessellator.addVertexWithUV( 0, 0, 0, itemIcon.getMinU(), itemIcon.getMinV() );

								tessellator.draw();

							} else {
								// Log an error, But don't crash the game... Hopefully...
							}
						}
					} else {

						IIcon itemIcon = theItem.getIcon( theStack, 0 );
						
						tessellator.startDrawingQuads();
						
						tessellator.addVertexWithUV( 0, 1, 0, itemIcon.getMinU(), itemIcon.getMaxV() );
						tessellator.addVertexWithUV( 1, 1, 0, itemIcon.getMaxU(), itemIcon.getMaxV() );
						tessellator.addVertexWithUV( 1, 0, 0, itemIcon.getMaxU(), itemIcon.getMinV() );
						tessellator.addVertexWithUV( 0, 0, 0, itemIcon.getMinU(), itemIcon.getMinV() );
						
						tessellator.draw();
					}
				}
			}


			FMLClientHandler.instance().getClient().entityRenderer.enableLightmap( 1 );
			RenderHelper.enableStandardItemLighting();
			
			GL11.glDisable( GL11.GL_CULL_FACE );
			GL11.glEnable( GL11.GL_LIGHTING );
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		}
		
		FMLClientHandler.instance().getClient().entityRenderer.enableLightmap( ( double ) counter );
		RenderHelper.enableStandardItemLighting();
		
		//Do custom rendering for each module
		
		ForgeDirection d;
		SocketModule module;
		
		for( int i = 0; i < 6; i++ ) {
			d = ForgeDirection.getOrientation( i );
			module = ts.getSide( d );
			module.doCustomRendering( ts, ts.getConfigForSide( d ), d, tessellator, blockRender );
		}


	}

}
