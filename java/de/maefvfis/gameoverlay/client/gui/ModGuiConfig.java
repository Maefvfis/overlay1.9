package de.maefvfis.gameoverlay.client.gui;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.reference.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfigEntries.BooleanEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.SelectValueEntry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ModGuiConfig extends GuiConfig{

    public ModGuiConfig(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
    }
    
    private static List<IConfigElement> getConfigElements()
    {
    	List<IConfigElement> allgemein = new ConfigElement(ConfigurationHandler.configuration.getCategory("allgemein")).getChildElements();
    	List<IConfigElement> shopschilder = new ConfigElement(ConfigurationHandler.configuration.getCategory("shopschilder")).getChildElements();
    	List<IConfigElement> schematic = new ConfigElement(ConfigurationHandler.configuration.getCategory("schematic")).getChildElements();
    	List<IConfigElement> lightoverlay = new ConfigElement(ConfigurationHandler.configuration.getCategory("lightoverlay")).getChildElements();
    	List<IConfigElement> minimap = new ConfigElement(ConfigurationHandler.configuration.getCategory("minimap")).getChildElements();
    	List<IConfigElement> test_makros = new ConfigElement(ConfigurationHandler.configuration.getCategory("test_makro")).getChildElements();
    	List<IConfigElement> test_makros_list = new ConfigElement(ConfigurationHandler.configuration.getCategory("test_makro_functions")).getChildElements();
    	test_makros.add(new DummyCategoryElement("Config Makros", Reference.MOD_ID,test_makros_list));
    	
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new DummyCategoryElement("Gerneral", Reference.MOD_ID, allgemein));
        list.add(new DummyCategoryElement("Minimap", Reference.MOD_ID, minimap));
        list.add(new DummyCategoryElement("Shop Export", Reference.MOD_ID, shopschilder));
        list.add(new DummyCategoryElement("Schematic Export", Reference.MOD_ID, schematic));
        list.add(new DummyCategoryElement("Light Overlay", Reference.MOD_ID, lightoverlay));
        list.add(new DummyCategoryElement("Makros", Reference.MOD_ID, test_makros));
        return list;
    }
}
