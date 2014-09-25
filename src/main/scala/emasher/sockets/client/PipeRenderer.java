package emasher.sockets.client;

import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.SocketsMod;
import emasher.sockets.pipes.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;


@SideOnly(Side.CLIENT)
public class PipeRenderer extends TileEntitySpecialRenderer
{
	public static final PipeRenderer instance = new PipeRenderer();
	private final Tessellator tessellator = Tessellator.instance;
	private final RenderBlocks blockRender = new RenderBlocks();
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float counter)
	{
		TilePipeBase p = (TilePipeBase)t;
        BlockFluidPipe bfp = (BlockFluidPipe)SocketsMod.blockFluidPipe;
		
		if(p.colour == -1) return;
		
		FMLClientHandler.instance().getClient().entityRenderer.disableLightmap(1);
		RenderHelper.disableStandardItemLighting();
		
		for(int side = 0; side < 6; side++)
		{	
			
			GL11.glPushMatrix();
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			
			GL11.glTranslatef((float)x, (float)y, (float)z);
			
			
			switch(side)
			{
			case 0:
				GL11.glRotatef(270, 1, 0, 0);
				GL11.glRotatef(180, 0, 0, 1);
				GL11.glTranslatef(-1.0F, 0.0F, -0.001F);
				break;
			case 1:
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0.0F, 0.0F, -1.001F);
				break;
			case 2:
				GL11.glRotatef(180, 0, 0, 1);
				GL11.glTranslatef(-1.0F, -1.0F, -0.001F);
				break;
			case 3:
				GL11.glRotatef(180, 0, 0, 1);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glTranslatef(0.0F, -1.0F, -1.001F);
				break;
			case 4:
				GL11.glRotatef(180, 0, 0, 1);
				GL11.glRotatef(270, 0, 1, 0);
				GL11.glTranslatef(0.0F, -1.0F, -0.001F);
				break;
			case 5:
				GL11.glRotatef(180, 0, 0, 1);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(-1.0F, -1.0F, -1.001F);
				break;
			}
			
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
			
			IIcon icon = bfp.getColIcon(p.colour);
			
			tessellator.startDrawingQuads();
			
			tessellator.addVertexWithUV(0, 1, 0, icon.getMinU(), icon.getMaxV());
			tessellator.addVertexWithUV(1, 1, 0, icon.getMaxU(), icon.getMaxV());
			tessellator.addVertexWithUV(1, 0, 0, icon.getMaxU(), icon.getMinV());
			tessellator.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
			
			tessellator.draw();
			
			FMLClientHandler.instance().getClient().entityRenderer.enableLightmap(1);
			RenderHelper.enableStandardItemLighting();
			
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		}
		
		FMLClientHandler.instance().getClient().entityRenderer.enableLightmap((double) counter);
		RenderHelper.enableStandardItemLighting();
		
	}

}