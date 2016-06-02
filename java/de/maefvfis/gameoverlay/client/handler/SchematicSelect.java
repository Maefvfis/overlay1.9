package de.maefvfis.gameoverlay.client.handler;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.maefvfis.gameoverlay.client.gui.ModGuiConfig;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.utility.LogHelper;

public class SchematicSelect  {
	
	public Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onMouseClick(MouseEvent event){
    	if(!ConfigurationHandler.SchematicActive) { return; }
    	if(!event.isButtonstate()) { return; }
    	if(!mc.inGameHasFocus) { return; }
    	if(mc.thePlayer.inventory.getCurrentItem() == null) { return; }
    	if(mc.thePlayer.inventory.getCurrentItem().getItem() == null) { return; }
    	if(mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK || mc.objectMouseOver.getBlockPos() == null) { return; }
    	if(mc.thePlayer.inventory.getCurrentItem().getItem() != Items.STONE_AXE) { return; }
    	
    	BlockPos blockpos = mc.objectMouseOver.getBlockPos();
    	String value = String.valueOf(blockpos.getX()) + "," + String.valueOf(blockpos.getY()) + "," + String.valueOf(blockpos.getZ());
    	
    	if(event.getButton() == 0) {
    		setConfigStringValue("Schematic Pos.1",value);
    		ConfigurationHandler.SchematicPos1 = value;
    		LogHelper.info(ConfigurationHandler.SchematicPos1);
    		event.setCanceled(true);
    	}
    	if(event.getButton() == 1) {
    		setConfigStringValue("Schematic Pos.2",value);
    		ConfigurationHandler.SchematicPos2 = value;
    		LogHelper.info(ConfigurationHandler.SchematicPos2);
    		event.setCanceled(true);
    	}
    }
    
    
    public void setConfigStringValue(String name, String value) {
    	ModGuiConfig modConfig = new ModGuiConfig(Minecraft.getMinecraft().currentScreen);
    	List<IConfigElement> ConfigElements = modConfig.configElements;
    	for(IConfigElement element:ConfigElements) {
    		for(IConfigElement element2:element.getChildElements()) {

	    		if(element2.getName().equals(name)) {
	    			element2.set(value);
	    			mc.thePlayer.addChatComponentMessage(new TextComponentString(name + " set to: " + value));
	    			return;
	    		}
    		}
			if(element.getName().equals(name)) {
				element.set(value);
				mc.thePlayer.addChatComponentMessage(new TextComponentString(name + " set to: " + value));
				return;
			}
    	}
    }
    
    
}
