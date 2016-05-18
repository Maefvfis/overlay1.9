package de.maefvfis.gameoverlay.client.handler;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayEventHandler {
	
	@SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onEvent(RenderGameOverlayEvent event)
    {
		// Disable Render top Right Potioneffects
		if(event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS && ConfigurationHandler.myConfigShowGrid) {
			event.setCanceled(true);
		}
		
    }
}
