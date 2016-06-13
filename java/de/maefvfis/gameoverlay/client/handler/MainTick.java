package de.maefvfis.gameoverlay.client.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import de.maefvfis.gameoverlay.client.gui.CopyOfGrid2D;
import de.maefvfis.gameoverlay.client.gui.InfoIngameGui;
import de.maefvfis.gameoverlay.client.gui.ShowItemUsage;
import de.maefvfis.gameoverlay.client.kadconmap.LeechTask;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.ShopVergleich;
@SideOnly(Side.CLIENT)

public class MainTick {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	
	public void onRenderChunkViewer(RenderGameOverlayEvent.Text event) {
		if (mc.gameSettings.showDebugInfo || !mc.inGameHasFocus)
			return;
		// Render Chunk Info In game
		if(ConfigurationHandler.myConfigShowInfoIngameGui == true) {
			new InfoIngameGui();
		}
		// Render Chunk Info In game
		if(ConfigurationHandler.myConfigShowItemUsage == true) {
			ShowItemUsage.ShowUsage();
		}
        // Render Mob Grid
		if(ConfigurationHandler.myConfigShowGrid == true) {
			new CopyOfGrid2D();
		}
		
		
		
		
		
	}
}