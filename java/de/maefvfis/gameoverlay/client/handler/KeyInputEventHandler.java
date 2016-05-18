package de.maefvfis.gameoverlay.client.handler;


import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import de.maefvfis.gameoverlay.client.gui.CreativeInv2;
import de.maefvfis.gameoverlay.client.gui.ModGuiConfig;
import de.maefvfis.gameoverlay.client.kadconmap.KadconMapHolder;
import de.maefvfis.gameoverlay.client.schematic.schematic.SchematicSave;
import de.maefvfis.gameoverlay.client.settings.Keybindings;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.CSVSchildManager;
import de.maefvfis.gameoverlay.reference.EntityGridOptions;
import de.maefvfis.gameoverlay.reference.Key;
import de.maefvfis.gameoverlay.utility.GammaBright;
import de.maefvfis.gameoverlay.utility.LogHelper;

public class KeyInputEventHandler {
	
	private static Minecraft  mc = Minecraft.getMinecraft();
    private static Key getPressedKeybinding()
    {
    	// Click geräusch bei gedrücktem KeyBind
    	for(KeyBinding k:Keybindings.getKeyBindListe()) { if(k.isKeyDown()) {  mc.thePlayer.playSound(SoundEvents.ui_button_click, 0.15F, 1.0F); break; } }
    	
    	// Aktionen
    	if(Keybindings.menu.isPressed()) mc.displayGuiScreen(new ModGuiConfig(mc.currentScreen));
        if(Keybindings.toggleGamma.isPressed()) GammaBright.toggle();
        if(Keybindings.creativinv.isPressed()) mc.displayGuiScreen(new CreativeInv2(mc.thePlayer));
        if(Keybindings.maptoggle.isPressed()) { EntityGridOptions.CycleActiveEntity(); }
        if(Keybindings.maptoggleback.isPressed()) { EntityGridOptions.CycleBackActiveEntity(); }
        if(Keybindings.mapTypetoggle.isPressed()) { EntityGridOptions.CycleMapModes(); }
        if(Keybindings.SaveChunk.isPressed()) { new SchematicSave(); }
        
        if (Keybindings.CsvSave.isPressed()) {
        	mc.thePlayer.addChatComponentMessage(new TextComponentString("INIT SHOPSCHILDER"));
        	CSVSchildManager.getShopschilder(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posZ));
        	CSVSchildManager.save_csv();
        }
        
        
        
        for(KeyBinding k:Keybindings.getKeyBindListe()) {
        	if(k.getKeyCategory().equals("Ingame Makros") && k.isPressed()) {
        		int delay = 0;
        		if(ConfigurationHandler.test_makros_function.get(k.getKeyDescription()) == null) break;
        		for(String s:ConfigurationHandler.test_makros_function.get(k.getKeyDescription())) {
        			sendFunction(delay,s);
        			delay = delay + ConfigurationHandler.test_makros_delay;
        		}
    			break;
        	}
        }
        
        
        
        return Key.UNKNOWN;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        //LogHelper.info(getPressedKeybinding());
    	getPressedKeybinding();
    }
    
    public static void sendFunction(int delay, final String text) {
        TimerTask action = new TimerTask() {
        	public void run() { mc.thePlayer.sendChatMessage(text); }
        };
        Timer caretaker = new Timer();
        caretaker.schedule(action, delay);
	 }
}