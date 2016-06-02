package de.maefvfis.gameoverlay.client.mapcolors;

import org.lwjgl.opengl.GL11;

public class Render {
	 public static int getAverageOfPixelQuad(int[] pixels, int offset, int scanSize)
	    {
	      int p00 = pixels[offset];
	      int p01 = pixels[(offset + 1)];
	      int p10 = pixels[(offset + scanSize)];
	      int p11 = pixels[(offset + scanSize + 1)];
	      
	  
	      int r = (p00 >> 16 & 0xFF) + (p01 >> 16 & 0xFF) + (p10 >> 16 & 0xFF) + (p11 >> 16 & 0xFF);
	      r >>= 2;
	      int g = (p00 >> 8 & 0xFF) + (p01 >> 8 & 0xFF) + (p10 >> 8 & 0xFF) + (p11 >> 8 & 0xFF);
	      g >>= 2;
	      int b = (p00 & 0xFF) + (p01 & 0xFF) + (p10 & 0xFF) + (p11 & 0xFF);
	      b >>= 2;
	      return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
	    }
	    
	    public static int getAverageColourOfArray(int[] pixels)
	    {
	      int count = 0;
	      double totalA = 0.0D;
	      double totalR = 0.0D;
	      double totalG = 0.0D;
	      double totalB = 0.0D;
	      for (int pixel : pixels)
	      {
	        double a = pixel >> 24 & 0xFF;
	        double r = pixel >> 16 & 0xFF;
	        double g = pixel >> 8 & 0xFF;
	        double b = pixel >> 0 & 0xFF;
	        
	        totalA += a;
	        totalR += r * a / 255.0D;
	        totalG += g * a / 255.0D;
	        totalB += b * a / 255.0D;
	        
	        count++;
	      }
	      
	      totalR = totalR * 255.0D / totalA;
	      totalG = totalG * 255.0D / totalA;
	      totalB = totalB * 255.0D / totalA;
	      totalA /= count;
	      
	      return ((int)totalA & 0xFF) << 24 | ((int)totalR & 0xFF) << 16 | ((int)totalG & 0xFF) << 8 | (int)totalB & 0xFF;
	    }
	    
	    public static int getTextureWidth()
	      {
	         return GL11.glGetTexLevelParameteri(3553, 0, 4096);
	       }
	       
	      public static int getTextureHeight()
	       {
	         return GL11.glGetTexLevelParameteri(3553, 0, 4097);
	     }
	      
	      public static int multiplyColours(int c1, int c2)
	         {
	           float c1A = c1 >> 24 & 0xFF;
	           float c1R = c1 >> 16 & 0xFF;
	           float c1G = c1 >> 8 & 0xFF;
	           float c1B = c1 >> 0 & 0xFF;
	           float c2A = c2 >> 24 & 0xFF;
	           float c2R = c2 >> 16 & 0xFF;
	           float c2G = c2 >> 8 & 0xFF;
	           float c2B = c2 >> 0 & 0xFF;
	           int r = (int)(c1R * c2R / 255.0F) & 0xFF;
	           int g = (int)(c1G * c2G / 255.0F) & 0xFF;
	           int b = (int)(c1B * c2B / 255.0F) & 0xFF;
	           int a = (int)(c1A * c2A / 255.0F) & 0xFF;
	           return a << 24 | r << 16 | g << 8 | b;
	         }
}
