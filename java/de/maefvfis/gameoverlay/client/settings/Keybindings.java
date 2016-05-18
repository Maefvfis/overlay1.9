package de.maefvfis.gameoverlay.client.settings;


import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.Map.Entry;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.reference.Names;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	
    public static KeyBinding menu = new KeyBinding(Names.Keys.MENU, Keyboard.KEY_K, Names.Keys.CATEGORY);
    public static KeyBinding SaveChunk = new KeyBinding(Names.Keys.SaveChunk, Keyboard.KEY_I, Names.Keys.CATEGORY);
    public static KeyBinding creativinv = new KeyBinding(Names.Keys.CREATIVEINV, Keyboard.KEY_L, Names.Keys.CATEGORY);
    public static KeyBinding maptoggle = new KeyBinding(Names.Keys.MAPTOGGLE, Keyboard.KEY_ADD, Names.Keys.CATEGORY);
    public static KeyBinding maptoggleback = new KeyBinding(Names.Keys.MAPTOGGLEBACK, Keyboard.KEY_SUBTRACT, Names.Keys.CATEGORY);
    public static KeyBinding CsvSave = new KeyBinding(Names.Keys.CSVSAVE, Keyboard.KEY_O, Names.Keys.CATEGORY);
    public static KeyBinding toggleGamma = new KeyBinding(Names.Keys.TOGGLEGAMMA, Keyboard.KEY_G, Names.Keys.CATEGORY);
    public static KeyBinding mapTypetoggle = new KeyBinding(Names.Keys.TYPETOGGLE, Keyboard.KEY_UP, Names.Keys.CATEGORY);
    public static ArrayList<KeyBinding> makro_array = load_config_makros();
    
    
    public static ArrayList<KeyBinding> getKeyBindListe() {
    	ArrayList<KeyBinding> keyBindListe = new ArrayList<KeyBinding>();
    	keyBindListe.add(menu);
    	keyBindListe.add(SaveChunk);
    	keyBindListe.add(creativinv);
    	keyBindListe.add(maptoggle);
    	keyBindListe.add(maptoggleback);
    	keyBindListe.add(CsvSave);
    	keyBindListe.add(toggleGamma);
    	keyBindListe.add(mapTypetoggle);
    	keyBindListe.addAll(makro_array);
    	return keyBindListe;
    }
    
    public static ArrayList<KeyBinding> load_config_makros() {
    	ArrayList<KeyBinding> ret = new ArrayList<KeyBinding>();
    	for(Entry<String,String[]> e:ConfigurationHandler.test_makros_function.entrySet()) {
    		ret.add(new KeyBinding(e.getKey(), Keyboard.CHAR_NONE, "Ingame Makros"));
    	}
    	return ret;
    }
    
    public static void writeNewMakros() {
    	ArrayList<KeyBinding> new_makro_array = load_config_makros();
    	KeyBinding[] Keys = Minecraft.getMinecraft().gameSettings.keyBindings;
    	KeyBinding[] newList = new KeyBinding[]{};
    	ArrayList<String> alreadyLoaded = new ArrayList<String>();
    	for(KeyBinding key:Keys) {
    		if(key.getKeyCategory().equals("Ingame Makros")) { alreadyLoaded.add(key.getKeyDescription()); }
    	}
    	for(KeyBinding k:new_makro_array) {
    		if(!alreadyLoaded.contains(k.getKeyDescription())) {
    			newList =  ArrayUtils.add(newList, k);
    			makro_array.add(k);
    		}
    	}
    	Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(Keys,newList);
    }
    
}
