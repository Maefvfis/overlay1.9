package de.maefvfis.gameoverlay.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.BlockScanner;
import de.maefvfis.gameoverlay.objects.ShopVergleich;
import de.maefvfis.gameoverlay.utility.GammaBright;

public class InfoIngameGui extends GuiScreen {

	private final Minecraft mc = Minecraft.getMinecraft();
	private int X = MathHelper.floor_double(mc.thePlayer.posX); // j3
	private int Y = MathHelper.floor_double(mc.thePlayer.posY); // k3
	private int Z = MathHelper.floor_double(mc.thePlayer.posZ); // l3
	
	
	private final ScaledResolution scaledresolution = new ScaledResolution(mc);
	private final int screenwidth = scaledresolution.getScaledWidth();
	private final int screenheight = scaledresolution.getScaledHeight();
	
	private int currentY = 10;
	
	private final Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(X, 0, Z));
	
	public InfoIngameGui() {

		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//GL11.glScalef(0.8F,0.8F,0.8F);
		
		FontRenderer fontRender =  mc.fontRendererObj;
		ChunckViewer CViewer = new ChunckViewer();
		// Entity Counts
		fontRender.drawStringWithShadow("In Chunk (Ani/Vil/Gol/Mon): " + CViewer.countEntity(EntityAnimal.class, chunk) + "/" + CViewer.countEntity(EntityVillager.class, chunk) + "/" + CViewer.countEntity(EntityGolem.class, chunk)+ "/" + CViewer.countEntity(EntityMob.class, chunk), 5, currentY, 0xffffff);
		currentY += 10;
		
		// Chunk / ChunkBiome
		fontRender.drawStringWithShadow("Chunk: " + Integer.valueOf(X >> 4) + " (" + Integer.valueOf(X & 15) + "), " + Integer.valueOf(Z >> 4) + " (" + Integer.valueOf(Z & 15) + ")" + " | " + chunk.getBiome(new BlockPos(X & 0xf, X & 0xf,0), mc.theWorld.getBiomeProvider()).getBiomeName(), 5, currentY, 0xffffff);
		currentY += 10;
		
		// Chords
		fontRender.drawStringWithShadow(Integer.valueOf(X) + ", " + Integer.valueOf(Z) + " (" + Integer.valueOf(Y) + ")",5, currentY, 0xffffff);
		currentY += 10;
		
		// light level
		if (mc.theWorld != null && mc.theWorld.isBlockLoaded(new BlockPos(X, Y, Z)))
        {
			
			int lY = Y;
			if(lY<1) {
				lY = 1;
			} else if(lY > 255) {
				lY = 255;
			}
			fontRender.drawStringWithShadow("Light: " + chunk.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(X & 15, lY, Z & 15)) + " / " + chunk.getLightFor(EnumSkyBlock.SKY, new BlockPos(X & 15, lY, Z & 15)) + " | " + GammaBright.getGammaStatus(), 5,currentY, 0xffffff);
			currentY += 10;
        }
		
		// FPS
		String[] splits = mc.debug.split(",");
		fontRender.drawStringWithShadow(splits[0], 5, currentY,0xffffff);
		currentY += 10;
		if(ConfigurationHandler.findBlocks) {
			fontRender.drawStringWithShadow(BlockScanner.analyze(chunk),5, currentY, 0xffffff);
		}
		currentY += 10;
		// Shop Vergleich
		ShopVergleich.ShowShopInfo(currentY, fontRender);

		currentY += 10;
		

	}
	
   public static String getWorldName()
   {
		
     String worldName;
     if (Minecraft.getMinecraft().isSingleplayer())
     {
       IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
       worldName = server != null ? server.getWorldName() : "sp_world";
     } else { 
		 if (Minecraft.getMinecraft().isConnectedToRealms()) {
		  worldName = "Realms";
		   }
		   else
		   {
			   if(Minecraft.getMinecraft().getConnection() != null) {
				   SPacketPlayerListHeaderFooter test = new SPacketPlayerListHeaderFooter();
				   test.processPacket(Minecraft.getMinecraft().getConnection());
				   if(test.getHeader() != null) {
					   worldName = test.getHeader().getUnformattedText();
				   } else {
					   worldName = "Not loaded";
				   }
			   } else {
				   worldName = Minecraft.getMinecraft().getCurrentServerData().getNBTCompound().toString();
			   }
			   
		     //worldName = Minecraft.getMinecraft().getCurrentServerData()..getNBTCompound().toString();
		     if (worldName.indexOf(":") == -1)
			 {
				worldName = worldName + "_25565";
			 }
			 else
			 {
				 worldName = worldName.replace(":", "_");
		     }
		  }
     	}
     return worldName;
   }
   
}
