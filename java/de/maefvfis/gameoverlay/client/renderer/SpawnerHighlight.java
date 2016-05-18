package de.maefvfis.gameoverlay.client.renderer;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import de.maefvfis.gameoverlay.client.gui.ChunckViewer;
import de.maefvfis.gameoverlay.client.gui.ChunckViewer.choords;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.WireSphere;
import de.maefvfis.gameoverlay.reference.EntityGridOptions;

public class SpawnerHighlight {
	
	
	private float alpha = 0.2F;
	private float linewidth = 2F;
	private Minecraft minecraft = Minecraft.getMinecraft();
	public int gridsize = Integer.valueOf(ConfigurationHandler.myGridSize);

	
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent event) {
		if(!ConfigurationHandler.myConfigShowGrid) return;
		if(!EntityGridOptions.IsActiveEntity(EntityGridOptions.Spawner)) return;
		if(!ConfigurationHandler.myConfigHighLightSpawner) return;
		if(this.minecraft == null) return;
		EntityPlayerSP player = this.minecraft.thePlayer;
		if (player == null) return;
		PlayerPosition.x = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks());
		PlayerPosition.y = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks());
		PlayerPosition.z = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks());
		drawSphere();
	}
	
	private void drawSphere()
	{
		GL11.glPushMatrix();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glLineWidth(linewidth);
		
		Chunk chunk;
		ChunckViewer CViewer = new ChunckViewer();
		int X = MathHelper.floor_double(minecraft.thePlayer.posX);
		int Z = MathHelper.floor_double(minecraft.thePlayer.posZ);
		int Y = MathHelper.floor_double(minecraft.thePlayer.posY);
		int i = 0;

		for (int i1 = 0; i1 <= gridsize; i1++)
		{
			for (int i2 = 0; i2 <= gridsize; i2++)
			{
				chunk = minecraft.theWorld.getChunkFromBlockCoords(new BlockPos(X + Offset(i1),0, Z + Offset(i2)));
				List<choords> EntityCount = CViewer.ListEntitysOnChunkChoords(TileEntityMobSpawner.class, chunk, false,true);
				
				for(choords choord: EntityCount) {
					
					GL11.glTranslatef(-PlayerPosition.x +0.5F + choord.x, -PlayerPosition.y +0.5F + choord.y, -PlayerPosition.z +0.5F + choord.z);
					GL11.glRotatef(90F, 1, 0, 0);
					switch(i%7) {
						case 0: GL11.glColor4f(1f,1f,1f,this.alpha);
							break;
						case 1: GL11.glColor4f(0f,1f,1f,this.alpha);
							break;
						case 2: GL11.glColor4f(1f,0f,1f,this.alpha);
							break;
						case 3: GL11.glColor4f(1f,1f,0f,this.alpha);
							break;
						case 4: GL11.glColor4f(0f,0f,1f,this.alpha);
							break;
						case 5: GL11.glColor4f(0f,1f,0f,this.alpha);
							break;
						case 6: GL11.glColor4f(1f,0f,0f,this.alpha);
							i=-1;
							break;
					}
					
				    GL11.glCallList(WireSphere.sphereIdOutside);
				    GL11.glRotatef(-90F, 1, 0, 0);
				    GL11.glTranslatef(-(-PlayerPosition.x +0.5F + choord.x), -(-PlayerPosition.y +0.5F + choord.y), -(-PlayerPosition.z +0.5F + choord.z));
				    i = i + 1;
				}
			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	    GL11.glPopMatrix();
	}
	
	public int Offset(int count) {
		if(count < gridsize / 2) return ((gridsize / 2) - count) * -16;
		if(count > gridsize / 2) return (count - (gridsize / 2)) * 16;
		return 0;
	}
}
