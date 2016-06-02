package de.maefvfis.gameoverlay.client.mapcolors;

 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.FileReader;
 import java.io.IOException;
 import java.io.OutputStreamWriter;
 import java.io.PrintStream;
 import java.io.RandomAccessFile;
 import java.io.Writer;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Scanner;

 import net.minecraft.block.Block;
 import net.minecraft.block.material.MapColor;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
 import net.minecraftforge.fml.common.registry.RegistryDelegate;
 
 public class BlockColours
 {
   private LinkedHashMap<String, BiomeData> biomeMap = new LinkedHashMap();
   private LinkedHashMap<String, BlockData> bcMap = new LinkedHashMap();
   
   public static enum BlockType
   {
     NORMAL, 
     GRASS, 
     LEAVES, 
     FOLIAGE, 
     WATER, 
     OPAQUE;
     
 
     private BlockType() {}
   }
   
 
   public String CombineBlockMeta(String BlockName, int meta)
   {
     return BlockName + " " + meta;
   }
   
   public String CombineBlockMeta(String BlockName, String meta)
   {
     return BlockName + " " + meta;
   }
   
   public int getColour(String BlockName, int meta)
   {
     String BlockAndMeta = CombineBlockMeta(BlockName, meta);
     String BlockAndWildcard = CombineBlockMeta(BlockName, "*");
     
     BlockData data = new BlockData();
     
     if (this.bcMap.containsKey(BlockAndMeta))
     {
       data = (BlockData)this.bcMap.get(BlockAndMeta);
       
     }
     else if (this.bcMap.containsKey(BlockAndWildcard))
     {
       data = (BlockData)this.bcMap.get(BlockAndWildcard);
     }
     
     return data.color;
   }
   
   public int getColour(IBlockState BlockState)
   {
     Block block = BlockState.getBlock();
     int meta = block.getMetaFromState(BlockState);
     
     if (block.delegate == null)
     {
    	 System.out.println("Delegate was Null when getting colour, Block in:" + block.toString());
       return 0;
     }
     if (block.delegate.name() == null)
     {
    	 System.out.println("Block Name was Null when getting colour, Block in:" + block.toString());
       return 0;
     }
     return getColour(block.delegate.name().toString(), meta);
   }
   
   public void setColour(String BlockName, String meta, int colour)
   {
     String BlockAndMeta = CombineBlockMeta(BlockName, meta);
     
     if (meta.equals("*"))
     {
       for (int i = 0; i < 16; i++)
       {
         setColour(BlockName, String.valueOf(i), colour);
       }
     }
     
     if (this.bcMap.containsKey(BlockAndMeta))
     {
       BlockData data = (BlockData)this.bcMap.get(BlockAndMeta);
       data.color = colour;
     }
     else
     {
       BlockData data = new BlockData();
       data.color = colour;
       this.bcMap.put(BlockAndMeta, data);
     }
   }
   
   private int getGrassColourMultiplier(String biomeName)
   {
     BiomeData data = (BiomeData)this.biomeMap.get(biomeName);
     return data != null ? data.grassMultiplier : 16777215;
   }
   
   private int getWaterColourMultiplier(String biomeName)
   {
     BiomeData data = (BiomeData)this.biomeMap.get(biomeName);
     return data != null ? data.waterMultiplier : 16777215;
   }
   
   private int getFoliageColourMultiplier(String biomeName)
   {
     BiomeData data = (BiomeData)this.biomeMap.get(biomeName);
     return data != null ? data.foliageMultiplier : 16777215;
   }
   
   public int getBiomeColour(String BlockName, int meta, String biomeName)
   {
     int colourMultiplier = 16777215;
     //System.out.println(bcMap.keySet().toString());
     if (this.bcMap.containsKey(CombineBlockMeta(BlockName, meta)))
     {
       switch (((BlockData)this.bcMap.get(CombineBlockMeta(BlockName, meta))).type)
       {
       case GRASS: 
         colourMultiplier = getGrassColourMultiplier(biomeName);
         //System.out.println("awdawdawd");
         break;
       case LEAVES: 
    	   //colourMultiplier = getFoliageColourMultiplier(biomeName);
       case FOLIAGE: 
    	   //System.out.println("awdawdawd");
         colourMultiplier = getFoliageColourMultiplier(biomeName);
         break;
       case WATER: 
    	   //System.out.println("WASSER");
    	   //colourMultiplier = 0xCC0000FF;
    	   colourMultiplier = getWaterColourMultiplier(biomeName);
         break;
       default: 
    	  // System.out.println(((BlockData)this.bcMap.get(CombineBlockMeta(BlockName, meta))).type);
         colourMultiplier = 16777215;
       }
       
     }
     return colourMultiplier;
   }
   
   public int getBiomeColour(IBlockState BlockState, int biomeId)
   {
     String biomeName = "";
     Biome biome = Biome.getBiome(biomeId);
     if (biome != null)
     {
       biomeName = biome.getBiomeName();
     }
     
     Block block = BlockState.getBlock();
     int meta = block.getMetaFromState(BlockState);
     
     return getBiomeColour(block.delegate.name().toString(), meta, biomeName);
   }
   
   public void setBiomeData(String biomeName, int waterShading, int grassShading, int foliageShading)
   {
     BiomeData data = new BiomeData();
     data.foliageMultiplier = foliageShading;
     data.grassMultiplier = grassShading;
     data.waterMultiplier = waterShading;
     System.out.println(biomeName+biomeMap.size() + " foliage:"+ foliageShading + " grass:"+ grassShading + " water:"+ waterShading);
     this.biomeMap.put(biomeName, data);
   }
   
   
   private static String getBlockTypeAsString(BlockType blockType)
   {
     String s = "normal";
     switch (blockType)
     {
     case NORMAL: 
       s = "normal";
       break;
     case GRASS: 
       s = "grass";
       break;
     case LEAVES: 
       s = "leaves";
       break;
     case FOLIAGE: 
       s = "foliage";
       break;
     case WATER: 
       s = "water";
       break;
     case OPAQUE: 
       s = "opaque";
     }
     
     return s;
   }
   
   public BlockType getBlockType(String BlockName, int meta)
   {
     String BlockAndMeta = CombineBlockMeta(BlockName, meta);
     String BlockAndWildcard = CombineBlockMeta(BlockName, "*");
     
     BlockData data = new BlockData();
     
     if (this.bcMap.containsKey(BlockAndMeta))
     {
       data = (BlockData)this.bcMap.get(BlockAndMeta);
     }
     else if (this.bcMap.containsKey(BlockAndWildcard))
     {
       data = (BlockData)this.bcMap.get(BlockAndWildcard);
     }
     return data.type;
   }
   
   public BlockType getBlockType(int BlockAndMeta)
   {
     Block block = Block.getBlockById(BlockAndMeta >> 4);
     int meta = BlockAndMeta & 0xF;
     return getBlockType(block.delegate.name().toString(), meta);
   }
   
   public void setBlockType(String BlockName, String meta, BlockType type)
   {
     String BlockAndMeta = CombineBlockMeta(BlockName, meta);
     
     if (meta.equals("*"))
     {
       for (int i = 0; i < 16; i++)
       {
         setBlockType(BlockName, String.valueOf(i), type);
       }
       return;
     }
     
     if (this.bcMap.containsKey(BlockAndMeta))
     {
       BlockData data = (BlockData)this.bcMap.get(BlockAndMeta);
       data.type = type;
       data.color = adjustBlockColourFromType(BlockName, meta, type, data.color);
     }
     else
     {
       BlockData data = new BlockData();
       data.type = type;
       this.bcMap.put(BlockAndMeta, data);
     }
   }
   
 
 
 
   private static int adjustBlockColourFromType(String BlockName, String meta, BlockType type, int blockColour)
   {
     Block block = Block.getBlockFromName(BlockName);
     
     switch (type)
     {
     case OPAQUE: 
       blockColour |= 0xFF000000;
     
 
     case NORMAL: 
       try
       {
		 int renderColour = block.getMapColor(block.getStateById(Integer.parseInt(meta) & 0xF)).colorValue;
         if (renderColour != 16777215)
         {
           blockColour = Render.multiplyColours(blockColour, 0xFF000000 | renderColour);
         }
       }
       catch (RuntimeException localRuntimeException) {}

     case LEAVES: 
       blockColour |= 0xFF000000;
       break;
     
 
     case GRASS: 
       blockColour = -6579301;
	default:
		break;
     }
     
     return blockColour;
   }
   
   
   public class BlockData
   {
     public int color = 0;
     public BlockColours.BlockType type = BlockColours.BlockType.NORMAL;
     
     public BlockData() {}
   }
   
   public class BiomeData { private int waterMultiplier = 0;
     private int grassMultiplier = 0;
     private int foliageMultiplier = 0;
     
     public BiomeData() {}
   }
 }
