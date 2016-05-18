package de.maefvfis.gameoverlay.proxy;


import de.maefvfis.gameoverlay.client.handler.SoundHandler;
import de.maefvfis.gameoverlay.client.renderer.Events;
import de.maefvfis.gameoverlay.client.renderer.Renderer;
import de.maefvfis.gameoverlay.client.renderer.SpawnerHighlight;
import de.maefvfis.gameoverlay.client.settings.Keybindings;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerKeyBindings() {
    	
    	for(KeyBinding k:Keybindings.getKeyBindListe()) {
    		ClientRegistry.registerKeyBinding(k);
    	}
        
        MinecraftForge.EVENT_BUS.register(new Renderer(Minecraft.getMinecraft()));
        FMLCommonHandler.instance().bus().register(new Events());
        
        
        FMLCommonHandler.instance().bus().register(new SpawnerHighlight());
        MinecraftForge.EVENT_BUS.register(new SpawnerHighlight());
        
    }
    
    
    
    
}
