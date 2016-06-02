package de.maefvfis.gameoverlay;

import java.io.File;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import de.maefvfis.gameoverlay.client.handler.GuiOpenHandler;
import de.maefvfis.gameoverlay.client.handler.KeyInputEventHandler;
import de.maefvfis.gameoverlay.client.handler.MainTick;
import de.maefvfis.gameoverlay.client.handler.OverlayEventHandler;
import de.maefvfis.gameoverlay.client.handler.SchematicSelect;
import de.maefvfis.gameoverlay.client.handler.SoundHandler;
import de.maefvfis.gameoverlay.client.mapcolors.ColorObj;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.CraftingBenchManager;
import de.maefvfis.gameoverlay.objects.CraftingBenchManagerNeu;
import de.maefvfis.gameoverlay.objects.WireSphere;
import de.maefvfis.gameoverlay.proxy.IProxy;
import de.maefvfis.gameoverlay.reference.Reference;
import de.maefvfis.gameoverlay.utility.LogHelper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME,version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class GameOverlay {
	
	public CraftingBenchManagerNeu BenchManager = new CraftingBenchManagerNeu();
	
    @Mod.Instance(Reference.MOD_ID)
    public static GameOverlay instance;
    public static ColorObj colorObj = new ColorObj();
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        proxy.registerKeyBindings();
        MinecraftForge.EVENT_BUS.register(new MainTick());
        
        MinecraftForge.EVENT_BUS.register(BenchManager);
        
        
        MinecraftForge.EVENT_BUS.register(new GuiOpenHandler());
        LogHelper.info("PRE ist durch .........................................");
        
        FMLCommonHandler.instance().bus().register(new SoundHandler());
        //MinecraftForge.EVENT_BUS.register(new SoundHandler());
        
        
        MinecraftForge.EVENT_BUS.register(new OverlayEventHandler());
        
       
        
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	
    	WireSphere.initSphere();
    	
        FMLCommonHandler.instance().bus().register(new KeyInputEventHandler());
        //MinecraftForge.EVENT_BUS.register(new LightOverlayHandler());

        FMLCommonHandler.instance().bus().register(new SchematicSelect());
        MinecraftForge.EVENT_BUS.register(new SchematicSelect());
        
        LogHelper.info("INIT ist durch .........................................");
        MinecraftForge.EVENT_BUS.register(colorObj);
        
        //MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }
    

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LogHelper.info("POST ist durch .........................................");
        
        
        //MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }
}
