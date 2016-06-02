	package de.maefvfis.gameoverlay.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.maefvfis.gameoverlay.client.gui.CraftingBench;
import de.maefvfis.gameoverlay.client.schematic.schematic.Names.Gui;


public class GuiOpenHandler {

	
    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onEvent(GuiOpenEvent event)
    {
    	//if(event.getGui() instanceof GuiCrafting && !(event.getGui() instanceof CraftingBench)) {
        //	event.setCanceled(true);
        //	CraftingBench blubb = new CraftingBench(Minecraft.getMinecraft().thePlayer.inventory, Minecraft.getMinecraft().theWorld, 0, 0, 0);
        //	net.minecraftforge.client.event.GuiOpenEvent event2 = new net.minecraftforge.client.event.GuiOpenEvent(blubb);
        //	Minecraft.getMinecraft().displayGuiScreen(blubb);
        //	net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event2);
        //} 
    }  
}
