package de.maefvfis.gameoverlay.handler;


import de.maefvfis.gameoverlay.client.renderer.HeadRenderer;
import de.maefvfis.gameoverlay.client.settings.Keybindings;
import de.maefvfis.gameoverlay.reference.EntityGridOptions;
import de.maefvfis.gameoverlay.reference.Reference;
import de.maefvfis.gameoverlay.utility.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

public class ConfigurationHandler {

    public static Configuration configuration;
    
    
    // Light Overlay Options
    public static final int COLOR_MIN = 0x00;
	public static final int COLOR_MAX = 0xFF;
	
	public static final int COLORDAYRED_DEFAULT = COLOR_MIN;
	public static final int COLORDAYGREEN_DEFAULT = COLOR_MAX;
	public static final int COLORDAYBLUE_DEFAULT = COLOR_MIN;
	public static final int COLORNIGHTRED_DEFAULT = COLOR_MIN;
	public static final int COLORNIGHTGREEN_DEFAULT = COLOR_MIN;
	public static final int COLORNIGHTBLUE_DEFAULT = COLOR_MAX;
	public static final int COLORBOTHRED_DEFAULT = COLOR_MAX;
	public static final int COLORBOTHGREEN_DEFAULT = COLOR_MIN;
	public static final int COLORBOTHBLUE_DEFAULT = COLOR_MIN;
	public static final int RENDERRANGEXZ_DEFAULT = 20;
	public static final int RENDERRANGEYBELLOW_DEFAULT = 4;
	public static final int RENDERRANGEYABOVE_DEFAULT = 0;
	public static final int UPDATERATE_DEFAULT = 5;
	public static final float GUIDELENGTH_DEFAULT = 1.0f;
	public static final boolean RENDERSPAWNS_DEFAULT = true;

	public static int colorDayRed = COLORDAYRED_DEFAULT;
	public static int colorDayGreen = COLORDAYGREEN_DEFAULT;
	public static int colorDayBlue = COLORDAYBLUE_DEFAULT;
	public static int colorNightRed = COLORNIGHTRED_DEFAULT;
	public static int colorNightGreen = COLORNIGHTGREEN_DEFAULT;
	public static int colorNightBlue = COLORNIGHTBLUE_DEFAULT;
	public static int colorBothRed = COLORBOTHRED_DEFAULT;
	public static int colorBothGreen = COLORBOTHGREEN_DEFAULT;
	public static int colorBothBlue = COLORBOTHBLUE_DEFAULT;
	public static int renderRangeXZ = RENDERRANGEXZ_DEFAULT;
	public static int renderRangeYBellow = RENDERRANGEYBELLOW_DEFAULT;
	public static int renderRangeYAbove = RENDERRANGEYABOVE_DEFAULT;
	public static int updateRate = UPDATERATE_DEFAULT;
	public static float guideLength = GUIDELENGTH_DEFAULT;
	public static boolean renderSpawns = RENDERSPAWNS_DEFAULT;

	public static Property propColorDayRed;
	public static Property propColorDayGreen;
	public static Property propColorDayBlue;
	public static Property propColorNightRed;
	public static Property propColorNightGreen;
	public static Property propColorNightBlue;
	public static Property propColorBothRed;
	public static Property propColorBothGreen;
	public static Property propColorBothBlue;
	public static Property propRenderRangeXZ;
	public static Property propRenderRangeYBellow;
	public static Property propRenderRangeYAbove;
	public static Property propUpdateRate;
	public static Property propGuideLength;
	

	public static void glColorDay() {
		GL11.glColor4ub((byte) colorDayRed, (byte) colorDayGreen, (byte) colorDayBlue, (byte) 79);
	}

	public static void glColorNight() {
		GL11.glColor4ub((byte) colorNightRed, (byte) colorNightGreen, (byte) colorNightBlue, (byte) 79);
	}

	public static void glColorBoth() {
		GL11.glColor4ub((byte) colorBothRed, (byte) colorBothGreen, (byte) colorBothBlue, (byte) 79);
	}
	
	// Minimap Options
	public static boolean EntityBlaze = true;
	public static boolean EntityCaveSpider = true;
	public static boolean EntityChicken = true;
	public static boolean EntityCow = true;
	public static boolean EntityCreeper = true;
	public static boolean EntityEnderman = true;
	public static boolean EntityEndermite = true;
	public static boolean EntityGhast = true;
	public static boolean EntityGuardian = true;
	public static boolean EntityIronGolem = true;
	public static boolean EntityMagmaCube = true;
	public static boolean EntityMooshroom = true;
	public static boolean EntityOcelot = true;
	public static boolean EntityPig = true;
	public static boolean EntityPigZombie = true;
	public static boolean EntityHorse = true;
	public static boolean EntityRabbit = true;
	public static boolean EntitySheep = true;
	public static boolean EntityShulker = true;
	public static boolean EntitySilverfish = true;
	public static boolean EntitySlime = true;
	public static boolean EntitySnowman = true;
	public static boolean EntitySpider = true;
	public static boolean EntitySquid = true;
	public static boolean EntityVillager = true;
	public static boolean EntityWitch = true;
	public static boolean EntityWolf = true;
	public static boolean EntityWither = true;
	public static boolean EntityZombie = true;
	public static boolean EntitySkelett = true;
	public static boolean EntityWitherSkelett = true;
	
	// Light Overlay Options ENde
	public static boolean myConfigLogShops = false;
	public static boolean myConfigHighLightSpawner = false;
    public static boolean myConfigShowGrid = true;
    public static boolean myConfigShowInfoIngameGui = true;
    public static boolean myConfigShowItemUsage = true;
    public static boolean myConfigShowEntityPosition = true;
    public static String myConfigGridType = "Mobs";
    
    public static int myConfigMapType = 0;
    
    public static int myConfigGridColor = 100;
    public static String myGridSize = "8";
    public static String PlayerGridWhitelist = "";
    
    
    public static boolean SchematicActive = true;
    public static String SchematicName = "";
    public static String SchematicPos1 = "";
    public static String SchematicPos2 = "";
    
    public static String ShopWarp = "";
    public static String ShopServer = "";
    
    
    public static String myAngler = "0";
    public static String[] blocksToFind = new String[]{"0","1:1"};
    public static boolean findBlocks = true;
    
    public static String[] test_makros_name = new String[]{};
    public static int test_makros_delay = 50;
    public static Map<String,String[]> test_makros_function = new HashMap<String,String[]>();
    
    public static void init(File configFile)
    {
    	LogHelper.info("Init");
        // Create the configuration object from the given configuration file
        if (configuration == null)
        {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
        EntityGridOptions.init();
        
    }

    private static void loadConfiguration()
    {
    	
    	// Minimap Options
    	EntityBlaze = configuration.getBoolean("Blaze", "minimap", EntityBlaze, "Show on Minimap?");
    	EntityCaveSpider = configuration.getBoolean("HöhlenSpinne", "minimap", EntityCaveSpider, "Show on Minimap?");
    	EntityChicken = configuration.getBoolean("Huhn", "minimap", EntityChicken, "Show on Minimap?");
    	EntityCow = configuration.getBoolean("Kuh", "minimap", EntityCow, "Show on Minimap?");
    	EntityCreeper = configuration.getBoolean("Creeper", "minimap", EntityCreeper, "Show on Minimap?");
    	EntityEnderman = configuration.getBoolean("Enderman", "minimap", EntityEnderman, "Show on Minimap?");
    	EntityEndermite = configuration.getBoolean("Endermite", "minimap", EntityEndermite, "Show on Minimap?");
    	EntityGhast = configuration.getBoolean("Ghast", "minimap", EntityGhast, "Show on Minimap?");
    	EntityGuardian = configuration.getBoolean("Guardian", "minimap", EntityGuardian, "Show on Minimap?");
    	EntityIronGolem = configuration.getBoolean("Eisen Golem", "minimap", EntityIronGolem, "Show on Minimap?");
    	EntityMagmaCube = configuration.getBoolean("MagmaCube", "minimap", EntityMagmaCube, "Show on Minimap?");
    	EntityMooshroom = configuration.getBoolean("PilzKuh", "minimap", EntityMooshroom, "Show on Minimap?");
    	EntityOcelot = configuration.getBoolean("Ocelot", "minimap", EntityOcelot, "Show on Minimap?");
    	EntityPig = configuration.getBoolean("Schwein", "minimap", EntityPig, "Show on Minimap?");
    	EntityPigZombie = configuration.getBoolean("PigZombie", "minimap", EntityPigZombie, "Show on Minimap?");
    	EntityHorse = configuration.getBoolean("Pferd", "minimap", EntityHorse, "Show on Minimap?");
    	EntityRabbit = configuration.getBoolean("Hase", "minimap", EntityRabbit, "Show on Minimap?");
    	EntitySheep = configuration.getBoolean("Schaf", "minimap", EntitySheep, "Show on Minimap?");
    	EntityShulker = configuration.getBoolean("Shulker", "minimap", EntityShulker, "Show on Minimap?");
    	EntitySilverfish = configuration.getBoolean("Silberfisch", "minimap", EntitySilverfish, "Show on Minimap?");
    	EntitySlime = configuration.getBoolean("Slime", "minimap", EntitySlime, "Show on Minimap?");
    	EntitySnowman = configuration.getBoolean("Schneeman", "minimap", EntitySnowman, "Show on Minimap?");
    	EntitySpider = configuration.getBoolean("Spinne", "minimap", EntitySpider, "Show on Minimap?");
    	EntitySquid = configuration.getBoolean("Tintenfisch", "minimap", EntitySquid, "Show on Minimap?");
    	EntityVillager = configuration.getBoolean("Villager", "minimap", EntityVillager, "Show on Minimap?");
    	EntityWitch = configuration.getBoolean("Hexe", "minimap", EntityWitch, "Show on Minimap?");
    	EntityWolf = configuration.getBoolean("Wolf", "minimap", EntityWolf, "Show on Minimap?");
    	EntityWither = configuration.getBoolean("WitherBoss", "minimap", EntityWither, "Show on Minimap?");
    	EntityZombie = configuration.getBoolean("Zombie", "minimap", EntityZombie, "Show on Minimap?");
    	EntitySkelett = configuration.getBoolean("Skelett", "minimap", EntitySkelett, "Show on Minimap?");
    	EntityWitherSkelett = configuration.getBoolean("WitherSkelett", "minimap", EntityWitherSkelett, "Show on Minimap?");

    	
    	
    	
    	
    	// Makros test
    	test_makros_name = configuration.getStringList("add / remove Makros", "test_makro", new String[] { "Makro1"}, "");
    	test_makros_delay = configuration.getInt("Makro delay", "test_makro", 50, 0, 10000, "Delay between chat functions");
    	for(String makro_name:test_makros_name) {
    		test_makros_function.put(makro_name, configuration.getStringList(makro_name, "test_makro_functions", new String[] { "/home"}, ""));
    	}
    	ConfigCategory cat = configuration.getCategory("test_makro_functions");
    	for(Entry<String, Property> e : cat.entrySet()) {
    		boolean found = false;
    		for(String s:test_makros_name) {
    			if(e.getKey().equals(s)) { found = true; break; }
    		}
    		if(!found) { cat.remove(e.getKey()); }
    	}
    	
    	
    	// shopschilder
    	ShopWarp = configuration.getString("ShopWarp", "shopschilder", ShopWarp, "ShopWarp");
        ShopServer = configuration.getString("ShopServer", "shopschilder", ShopServer, "ShopServer");

        // schematic
        SchematicActive = configuration.getBoolean("Activate Schematic func", "schematic", SchematicActive, "If true: StoneAxe zum auswhählen");
        SchematicPos1 = configuration.getString("Schematic Pos.1", "schematic", SchematicPos1, "coords of the 1. position. 'xx,yy,zz'");
        SchematicPos2 = configuration.getString("Schematic Pos.2", "schematic", SchematicPos2, "coords of the 2. position. 'xx,yy,zz'");
        SchematicName = configuration.getString("Schematic Name", "schematic", SchematicName, "name of the created file");
        
        //block render blocksToFind
        blocksToFind = configuration.getStringList("Blocks to find", "allgemein", blocksToFind, "Format: 'BLOCKID<:TYPE>;VALUE' all values must be integers!");
        findBlocks = configuration.getBoolean("Get Chunk Value", "allgemein", findBlocks, "If true: Shows Value in Top left corner");
        
        
        // light Overlay
        renderRangeXZ = configuration.get("lightoverlay", "LightOverlayRender width:", renderRangeXZ,"LightOverlayRender width", 5, 50 ).setConfigEntryClass(getSliderClass()).getInt();
        updateRate = configuration.get("lightoverlay", "LightOverlayRender UpdateRate:", updateRate,"LightOverlayRender UpdateRate", 5, 10 ).setConfigEntryClass(getSliderClass()).getInt();
        renderSpawns = configuration.getBoolean("Show LightOverlayRender", "lightoverlay", renderSpawns, "If true: Shows LightOverlay");
        
         //block render blocksToFind
        
        // minimap
        myConfigShowGrid = configuration.getBoolean("Show MiniMap", "allgemein", myConfigShowGrid, "If true: Shows the MiniMap grid");
        
        // Info Ingame
        myConfigShowInfoIngameGui = configuration.getBoolean("Show chunk info", "allgemein", myConfigShowInfoIngameGui, "If true: Shows the Chunk info");
        
        // show itemusage
        myConfigShowItemUsage = configuration.getBoolean("Show item usage", "allgemein", myConfigShowItemUsage, "If true: Shows the item usage");
        
        // Highlight spawner
        myConfigHighLightSpawner = configuration.getBoolean("Highlight Spawner", "allgemein", myConfigHighLightSpawner, "If true: Highlights Spawners");
        
        // Angelbot status
        myAngler = configuration.getString("Angelbot status", "allgemein", myGridSize, "Sets the size of the grid",new String[] { "Off", "Assist", "Auto"});
       
        // Player whitelist
        PlayerGridWhitelist = configuration.getString("Grid ignore player", "allgemein", PlayerGridWhitelist, "Players in this list will not be shown. Seperate by ','");
        
        if (configuration.hasChanged())
            configuration.save();
    }
    
    public static Class<? extends IConfigEntry> getSliderClass()
	{
		return NumberSliderEntry.class;
	}
    public void refreshConfig() {
    	
    }
    
    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {	
    	if (configuration.hasChanged()) { configuration.save(); }
        if (event.getModID().equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        	EntityGridOptions.SetActiveEntity(this.myConfigGridType);
        	HeadRenderer.headMap = HeadRenderer.generateHeadMap();
        }
        Keybindings.writeNewMakros();
    }

}
