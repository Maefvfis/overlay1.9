package de.maefvfis.gameoverlay.client.mapcolors;


 import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
 

 
 public class ChunkRender
 {
   public static final byte FLAG_UNPROCESSED = 0;
   public static final byte FLAG_NON_OPAQUE = 1;
   public static final byte FLAG_OPAQUE = 2;
   public static final double brightenExponent = 0.35D;
   public static final double darkenExponent = 0.35D;
   public static final double brightenAmplitude = 0.7D;
   public static final double darkenAmplitude = 1.4D;
   
   public static double getHeightShading(int height, int heightW, int heightN)
   {
     int samples = 0;
     int heightDiff = 0;
     
     if ((heightW > 0) && (heightW < 255))
     {
       heightDiff += height - heightW;
       samples++;
     }
     
     if ((heightN > 0) && (heightN < 255))
     {
       heightDiff += height - heightN;
       samples++;
     }
     
     double heightDiffFactor = 0.0D;
     if (samples > 0)
     {
       heightDiffFactor = heightDiff / samples;
     }
     
     return heightDiffFactor >= 0.0D ? Math.pow(heightDiffFactor * 0.00392156862745098D, 0.35D) * 0.7D : -Math.pow(-(heightDiffFactor * 0.00392156862745098D), 0.35D) * 1.4D;
   }
   
   public static int getPixelColour(BlockColours bc, Chunk chunk, int x, int y, int z)
   {
     double a = 1.0D;
     double r = 0.0D;
     double g = 0.0D;
     double b = 0.0D;
     for (; y > 0; y--)
     {
       IBlockState blockState = chunk.getBlockState(x, y, z);
       int c1 = bc.getColour(blockState);
       int alpha = c1 >> 24 & 0xFF;
       
 
 
       if (c1 == -8650628)
       {
         alpha = 0;
       }
       
 
       if (alpha > 0)
       {
    	 int biome = Biome.getIdForBiome(chunk.getBiome(new BlockPos(x, 10, z),Minecraft.getMinecraft().theWorld.getBiomeProvider()));
         //int biome = chunk.getBiome(x, z);
         int c2 = bc.getBiomeColour(blockState, biome);
         
 
         double c1A = alpha / 255.0D;
         double c1R = (c1 >> 16 & 0xFF) / 255.0D;
         double c1G = (c1 >> 8 & 0xFF) / 255.0D;
         double c1B = (c1 >> 0 & 0xFF) / 255.0D;
         
 
         double c2R = (c2 >> 16 & 0xFF) / 255.0D;
         double c2G = (c2 >> 8 & 0xFF) / 255.0D;
         double c2B = (c2 >> 0 & 0xFF) / 255.0D;
         
 
         r += a * c1A * c1R * c2R;
         g += a * c1A * c1G * c2G;
         b += a * c1A * c1B * c2B;
         a *= (1.0D - c1A);
       }
       
       if (alpha == 255) {
         break;
       }
     }
     
     //double heightShading = getHeightShading(y, heightW, heightN);
     int lightValue = getLightValue(chunk,x, y + 1, z);
     double lightShading = lightValue / 15.0D;
     //double shading = (heightShading + 1.0D) * lightShading;
     double shading = ( 1.0D) * lightShading;
 
     r = Math.min(Math.max(0.0D, r * shading), 1.0D);
     g = Math.min(Math.max(0.0D, g * shading), 1.0D);
     b = Math.min(Math.max(0.0D, b * shading), 1.0D);
     
 
 
     return (y & 0xFF) << 24 | ((int)(r * 255.0D) & 0xFF) << 16 | ((int)(g * 255.0D) & 0xFF) << 8 | (int)(b * 255.0D) & 0xFF;
   }
 
   public static int getColumnColour(BlockColours bc, Chunk chunk, int x, int y, int z, int heightW, int heightN)
   {
     double a = 1.0D;
     double r = 0.0D;
     double g = 0.0D;
     double b = 0.0D;
     for (; y > 0; y--)
     {
       IBlockState blockState = chunk.getBlockState(x, y, z);
       int c1 = bc.getColour(blockState);
       int alpha = c1 >> 24 & 0xFF;
       
 
 
       if (c1 == -8650628)
       {
         alpha = 0;
       }
       
 
       if (alpha > 0)
       {
    	 int biome = Biome.getIdForBiome(chunk.getBiome(new BlockPos(chunk.xPosition, 0, chunk.zPosition),Minecraft.getMinecraft().theWorld.getBiomeProvider()));
         //int biome = chunk.getBiome(x, z);
         int c2 = bc.getBiomeColour(blockState, biome);
         
 
         double c1A = alpha / 255.0D;
         double c1R = (c1 >> 16 & 0xFF) / 255.0D;
         double c1G = (c1 >> 8 & 0xFF) / 255.0D;
         double c1B = (c1 >> 0 & 0xFF) / 255.0D;
         
 
         double c2R = (c2 >> 16 & 0xFF) / 255.0D;
         double c2G = (c2 >> 8 & 0xFF) / 255.0D;
         double c2B = (c2 >> 0 & 0xFF) / 255.0D;
         
 
         r += a * c1A * c1R * c2R;
         g += a * c1A * c1G * c2G;
         b += a * c1A * c1B * c2B;
         a *= (1.0D - c1A);
       }
       
       if (alpha == 255) {
         break;
       }
     }
     
     double heightShading = getHeightShading(y, heightW, heightN);
     int lightValue = getLightValue(chunk,x, y + 1, z);
     double lightShading = lightValue / 15.0D;
     double shading = (heightShading + 1.0D) * lightShading;
     
 
     r = Math.min(Math.max(0.0D, r * shading), 1.0D);
     g = Math.min(Math.max(0.0D, g * shading), 1.0D);
     b = Math.min(Math.max(0.0D, b * shading), 1.0D);
     
 
 
     return (y & 0xFF) << 24 | ((int)(r * 255.0D) & 0xFF) << 16 | ((int)(g * 255.0D) & 0xFF) << 8 | (int)(b * 255.0D) & 0xFF;
   }
   
   public static int getPixelHeightN(int[] pixels, int offset, int scanSize)
   {
     return offset >= scanSize ? pixels[(offset - scanSize)] >> 24 & 0xFF : -1;
   }
   
   public static int getPixelHeightW(int[] pixels, int offset, int scanSize)
   {
     return (offset & scanSize - 1) >= 1 ? pixels[(offset - 1)] >> 24 & 0xFF : -1;
   }
   
   public static void renderSurface(BlockColours bc, Chunk chunk, int[] pixels, int offset, int scanSize, boolean dimensionHasCeiling)
   {
     int chunkMaxY = getMaxY(chunk);
     for (int z = 0; z < 16; z++)
     {
       for (int x = 0; x < 16; x++)
       {
 
 
 
 
 
 
         if (dimensionHasCeiling)
         {
           for (int y = 127; y >= 0; y--)
           {
             IBlockState blockState = chunk.getBlockState(x, y, z);
             int color = bc.getColour(blockState);
             int alpha = color >> 24 & 0xFF;
             
             if (color == -8650628)
             {
               alpha = 0;
             }
             
             if (alpha != 255) {
               break;
             }
           }
         }
         
 
 
         int y = chunkMaxY - 1;
         
 
         int pixelOffset = offset + z * scanSize + x;
         pixels[pixelOffset] = getColumnColour(bc, chunk, x, y, z, getPixelHeightW(pixels, pixelOffset, scanSize), getPixelHeightN(pixels, pixelOffset, scanSize));
       }
     }
   }
   
   public static void renderUnderground(BlockColours bc, Chunk chunk, int[] pixels, int offset, int scanSize, int startY, byte[] mask)
   {
     startY = Math.min(Math.max(0, startY), 255);
     for (int z = 0; z < 16; z++)
     {
       for (int x = 0; x < 16; x++)
       {
 
 
 
         if ((mask == null) || (mask[(z * 16 + x)] == 1))
         {
 
 
 
 
 
 
           int lastNonTransparentY = startY;
           for (int y = startY; y < getMaxY(chunk); y++)
           {
             IBlockState blockState = chunk.getBlockState(x, y, z);
             int color = bc.getColour(blockState);
             int alpha = color >> 24 & 0xFF;
             
             if (color == -8650628)
             {
               alpha = 0;
             }
             
             if (alpha == 255) {
               break;
             }
             
             if (alpha > 0)
             {
               lastNonTransparentY = y;
             }
           }
           
           int pixelOffset = offset + z * scanSize + x;
           pixels[pixelOffset] = getColumnColour(bc, chunk, x, lastNonTransparentY, z, getPixelHeightW(pixels, pixelOffset, scanSize), getPixelHeightN(pixels, pixelOffset, scanSize));
         }
       }
     }
   }
   
   public static int getLightValue(Chunk chunk,int x,int y,int z) {
	   return 15;
	   
   }
   
   public static int getMaxY(Chunk chunk) {
	   int[] myArray = chunk.getHeightMap();
	   int max = Integer.MIN_VALUE;
	   for(int i = 0; i < myArray.length; i++) {
	         if(myArray[i] > max) {
	            max = myArray[i];
	         }
	   }
	   return max;
   }
 }

