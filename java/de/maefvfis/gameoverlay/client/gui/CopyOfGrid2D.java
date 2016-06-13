package de.maefvfis.gameoverlay.client.gui;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.server.FMLServerHandler;

import org.lwjgl.opengl.GL11;

import sun.security.ssl.Debug;

import com.mojang.authlib.GameProfile;

import de.maefvfis.gameoverlay.client.gui.ChunckViewer.choords;
import de.maefvfis.gameoverlay.client.kadconmap.KadconMapHolder;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.ChunkImage;
import de.maefvfis.gameoverlay.objects.DayTime;
import de.maefvfis.gameoverlay.objects.Mondphasen;
import de.maefvfis.gameoverlay.reference.EntityGridOptions;

public class CopyOfGrid2D  extends GuiScreen {
	
	public int gridsize = Integer.valueOf(ConfigurationHandler.myGridSize);
	public int blocksize = 1;
	public int colorAlpha = 0xFF000000;
	public int scaleFactor = 16;
	public int texturesize = 314;
	public int texturecenterX = 173;
	public int texturecenterY = 186;
	public int mapTopOffset = 20;
	public int padding = 0;
	public int chunksize = blocksize * 16;
	public Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fontRender =  mc.fontRendererObj;
	public ScaledResolution scaledresolution = new ScaledResolution(mc);
	public int screenwidth = scaledresolution.getScaledWidth();
	public int screenheight = scaledresolution.getScaledHeight();
	public int X = MathHelper.floor_double(mc.thePlayer.posX);
	public int Z = MathHelper.floor_double(mc.thePlayer.posZ);
	public int Y = MathHelper.floor_double(mc.thePlayer.posY);
	public int xRel = Integer.valueOf(X & 15);
	public int zRel = Integer.valueOf(Z & 15);
	public float xFine = (float) ((mc.thePlayer.posX - X));
	public float zFine = (float) ((mc.thePlayer.posZ - Z));
	public int fullgridsizex = (chunksize*gridsize) + chunksize;
	public int radius = (fullgridsizex/2);
	public float transfX = screenwidth - radius - padding;
	public float transfY = radius + padding + mapTopOffset;
	public Chunk chunk;
	public int chunkX;
	public int chunkY;
	public boolean fullyInCircle;
	public int blockY;
	public boolean asd = false;
	public ChunckViewer CViewer = new ChunckViewer();
	public List<choords> choordsList = new ArrayList<choords>();
	
	public ResourceLocation mobmarkerNorm = new ResourceLocation("gameoverlay","textures/mobmarker.png");
	public ResourceLocation mobmarkerHigher = new ResourceLocation("gameoverlay","textures/mobmarker2.png");
	public ResourceLocation mobmarkerLower = new ResourceLocation("gameoverlay","textures/mobmarker3.png");
	
	
	public CopyOfGrid2D() {
		render_map();
	}
	
	public void render_map() {
		
		GlStateManager.pushMatrix();
		
			GlStateManager.translate(transfX, transfY, 0);

			GlStateManager.pushMatrix();
			
				GlStateManager.rotate(-this.mc.thePlayer.rotationYaw + 180, 0, 0, 1);
				//GlStateManager.enableAlpha();
			    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
				mc.renderEngine.bindTexture(new ResourceLocation("gameoverlay","textures/mask9.png"));
				drawTexturedModalRect(-radius+9, -radius+9, 0, 0, 16*(gridsize), 16*(gridsize));
				
				GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_ALPHA,GL11.GL_DST_ALPHA);
				//GlStateManager.blendFunc(772,773);
				
				GlStateManager.scale(0.0625F,0.0625F,0.0625F);
				// generate Map
			    for (int i1 = 0; i1 <= gridsize; i1++)
				{
					for (int i2 = 0; i2 <= gridsize; i2++)
					{
						chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(X + Offset(i1),0, Z + Offset(i2)));
						chunkX = ((i1*chunksize))-radius;
						chunkY = (i2*chunksize)-radius;
						
						if(EntityGridOptions.ActiveEntity.EntityName.equals("Mobs")) {
							choordsList.addAll(CViewer.ListLivingEntitys(chunk));
						} else {
							// Add EntityPositions to List
							choordsList.addAll(CViewer.ListEntitysOnChunkChoords(EntityGridOptions.ActiveEntity.EntityClass, chunk, EntityGridOptions.ActiveEntity.WitherSkelett,EntityGridOptions.ActiveEntity.IsTile));
						}
						int textureID = 0;
						if(ConfigurationHandler.myConfigMapType == 0) {
							textureID = ChunkImage.getMapImage(chunk,X + Offset(i1),Z + Offset(i2));
						} else {
						    textureID = KadconMapHolder.getImage((X + Offset(i1))>>4,(Z + Offset(i2))>>4);
						}
						if(textureID != -1) {
							GlStateManager.bindTexture(textureID);
							

							
							drawTexturedModalRect(((float)((i1*chunksize)-radius - xRel - xFine + 8) * scaleFactor), (((i2*chunksize)-radius - zRel - zFine + 8) * scaleFactor) , 0, 0, 16*scaleFactor, 16*scaleFactor);
						}	
						//break;
					}
					//break;
				}
			    
			    drawRect(0,0,1,1,0xFFFFFFFF);
			    GlStateManager.disableAlpha();
			GlStateManager.popMatrix(); 
			
			    
		    GlStateManager.enableBlend();
			
			
		    GlStateManager.pushMatrix();
			    GlStateManager.scale(0.03125F,0.03125F,0.03125F);
			    
				 // TEST
			    ArrayList<choords> playerChoords = new ArrayList<choords>();
			    ArrayList<choords> otherChoords = new ArrayList<choords>();
		    
			    for(choords choord: choordsList) {
				    double angle = (-MathHelper.wrapDegrees(mc.thePlayer.rotationYaw) / (180 / Math.PI));
				    double x2 = ((X+xFine-choord.x) * Math.cos(angle) - (Z+zFine-choord.z) * Math.sin(angle));
				    double z2 = ((X+xFine-choord.x) * Math.sin(angle) + (Z+zFine-choord.z) * Math.cos(angle));
			    	if(!in_radius2(0, 0, 64, (int)x2, (int)z2)) { continue; }
			    	
			    	if(choord.resourcelocation == null) {
	    				otherChoords.add(choord);
	    				continue;
			    	}
			    	
			    	if(choord.isPlayer) {
			    		choord.x = x2;
	    				choord.z = z2;
	    				playerChoords.add(choord);
	    				continue;
			    	}
			    	
		    		mc.renderEngine.bindTexture(choord.resourcelocation);
		    		GlStateManager.pushMatrix();
		    				GlStateManager.scale(0.8F, 0.8F, 1F);
		    				double x = ((x2 - 4) * scaleFactor * 2) * 1.25;
		    				double z = ((z2 - 4) * scaleFactor * 2) * 1.25;
		    				drawTexturedModalRect((int)x ,(int)z, 0, 0, scaleFactor*16 ,  scaleFactor*16);
		    		GlStateManager.popMatrix();
		    	}
			    
			    for(choords choord: playerChoords) {
		    		mc.renderEngine.bindTexture(choord.resourcelocation);
		    		GlStateManager.pushMatrix();
		    			GlStateManager.scale(8F, 8F, 4F);
		    			drawTexturedModalRect((int)(((choord.x - 4) * scaleFactor * 2) / 8), (int)(((choord.z - 4) * scaleFactor * 2) / 8), 32, 32, 8 * scaleFactor / 4, 8 * scaleFactor / 4);
		    		GlStateManager.popMatrix();
			    }
			    
			    for(choords choord: otherChoords) {
			    	double angle = (-MathHelper.wrapDegrees(mc.thePlayer.rotationYaw) / (180 / Math.PI));
			    	double x2 = ((X+xFine-choord.x) * Math.cos(angle) - (Z+zFine-choord.z) * Math.sin(angle));
			    	double z2 = ((X+xFine-choord.x) * Math.sin(angle) + (Z+zFine-choord.z) * Math.cos(angle));
			    	if(Y > choord.y + 1) { mc.renderEngine.bindTexture(mobmarkerLower); }
			    	if(Y < choord.y + 1) { mc.renderEngine.bindTexture(mobmarkerHigher); }
			    	if(Y == choord.y || Y == choord.y + 1 || Y == choord.y + 2) { mc.renderEngine.bindTexture(mobmarkerNorm); }
			    	drawTexturedModalRect((int)((x2 - 4) * scaleFactor * 2), (int)((z2 - 4) * scaleFactor * 2) , 0, 0, 16 * scaleFactor, 16* scaleFactor);
			    }
				 
		    GlStateManager.popMatrix();
		   
		    //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    
		    mc.renderEngine.bindTexture(new ResourceLocation("gameoverlay","textures/minimap.png"));
			drawTexturedModalRect(-(texturecenterX / 2) -1 , (-texturecenterY / 2) - 1 , 0, 0, 200, 200);
			
			// Uhrzeit
			String DayTimeString = DayTime.getTimeString(Minecraft.getMinecraft().theWorld.getWorldInfo().getWorldTime());
			fontRender.drawString(DayTimeString,- fontRender.getStringWidth(DayTimeString) / 2, -81, 0xffffff);
			
			// Mond
			mc.renderEngine.bindTexture(new ResourceLocation("gameoverlay","textures/moon_phases.png"));
		    int[] MoonTextOff = Mondphasen.getPhase_iconoffset(Minecraft.getMinecraft().theWorld.getMoonPhase());
		    drawTexturedModalRect(-81, -52, MoonTextOff[0] / 2, MoonTextOff[1] / 2, 50, 50);
		    
		    // Weather
		    int weatheroffset = 0;
		    
		    if (!DayTime.getDaytime(Minecraft.getMinecraft().theWorld.getWorldInfo().getWorldTime())) {
	            weatheroffset = 96;
	        }
		    if(mc.theWorld.isThundering()) {
		    	weatheroffset = 96*3;
		    } else if (mc.theWorld.isRaining()) {
		    	weatheroffset = 96 * 2;
		    }
		    
		    drawTexturedModalRect(-61 , -74, weatheroffset / 2, (96*2) / 2, 50, 50);
		    
		    //Grid Type
		    mc.renderEngine.bindTexture(new ResourceLocation("gameoverlay","textures/entityradar.png"));
		    GlStateManager.pushMatrix();
		    	GlStateManager.scale(1F,0.0625F,1F);
		    	drawTexturedModalRect(41, -140 * 8, ((EntityGridOptions.getActiveEntityIndex() * 32) / 2),0 , 16, 256);
		    GlStateManager.popMatrix();
		    
		    
		    //Directions North, South etc...
		    GlStateManager.pushMatrix();
		    	GlStateManager.rotate(-mc.thePlayer.rotationYaw + 180, 0, 0, 1);
		    	mc.renderEngine.bindTexture(new ResourceLocation("gameoverlay","textures/minimap_directions.png"));
		    	drawTexturedModalRect(-(texturecenterX / 2) -1 , (-texturecenterY / 2) - 1 , 0, 0, 200, 200);
		    GlStateManager.popMatrix();
		    
		    
			
		    GlStateManager.disableBlend();
		    
		GlStateManager.popMatrix();
	}
	
	
	
	public static boolean in_radius( int c_x, int c_y, int r, int x, int y) {
		return Math.hypot(c_x-x, c_y-y) <= r;
	}
	
	public static boolean in_radius2( int c_x, int c_y, int r, int x, int y) {
		int a = Math.abs(c_x-x);
		int b = Math.abs(c_y-y);
		if(a > r || b > r) { return false; }
		if(a+b <= r) { return true; }
		if((a*a) + (b*b) < r*r) { return true; }
		return false;
	}

	public int Offset(int count) {
		if(count < gridsize / 2) return ((gridsize / 2) - count) * -16;
		if(count > gridsize / 2) return (count - (gridsize / 2)) * 16;
		return 0;
	}

}
