package de.maefvfis.gameoverlay.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.init.SoundEvents;

public class GammaBright {
	
	private static boolean isGamma = false;
	
	public static void toggle() {
		Minecraft mc = Minecraft.getMinecraft();

        setGammaState();
        
        if (getGammaState())     {
            mc.gameSettings.gammaSetting = 10000.0f;            
        } else {         
            mc.gameSettings.gammaSetting = 1.0f;
        }
    }
	

	public static void setGammaState()    
	{
	    isGamma =! isGamma;
	}

	public static boolean getGammaState()    
	{
	    return isGamma;
	}
	
	public static String getGammaStatus() {
		if(getGammaState()) {
			return "Gamma ON";
		} else {
			return "Gamma OFF";
		}
	}
}
