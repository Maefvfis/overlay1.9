package de.maefvfis.gameoverlay.client.mapcolors;

 

import java.awt.Color;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
 
 
 
 
 
 
 
 public class BlockColourGen
 {
   private static int getIconMapColour(TextureAtlasSprite icon, Texture terrainTexture)
   {
     int iconX = Math.round(terrainTexture.w * Math.min(icon.getMinU(), icon.getMaxU()));
     int iconY = Math.round(terrainTexture.h * Math.min(icon.getMinV(), icon.getMaxV()));
     int iconWidth = Math.round(terrainTexture.w * Math.abs(icon.getMaxU() - icon.getMinU()));
     int iconHeight = Math.round(terrainTexture.h * Math.abs(icon.getMaxV() - icon.getMinV()));
     
     int[] pixels = new int[iconWidth * iconHeight];
     
 
 
     terrainTexture.getRGB(iconX, iconY, iconWidth, iconHeight, pixels, 0, iconWidth, icon);
     
 
 
 
     return Render.getAverageColourOfArray(pixels);
   }
   
 
 
 
   private static void genBiomeColours(BlockColours bc)
   {
	

	         for(ResourceLocation rL: Biome.REGISTRY.getKeys()) {
	        	 Biome biome = Biome.REGISTRY.getObject(rL);
	             
	             if (biome != null)
	             {
	               double temp = MathHelper.clamp_double(biome.getTemperature(), 0.0F, 1.0F);
	               double rain = MathHelper.clamp_double(biome.getRainfall(), 0.0F, 1.0F);
	               int grasscolor = ColorizerGrass.getGrassColor(temp, rain);
	               int foliagecolor = ColorizerFoliage.getFoliageColor(temp, rain);
	               int watercolor = biome.getWaterColorMultiplier();
	               
	               bc.setBiomeData(biome.getBiomeName(), watercolor & 0xFFFFFF, grasscolor & 0xFFFFFF, foliagecolor & 0xFFFFFF);
	             }
	         }

   }
   
 
 
 
 
   public static void genBlockColours(BlockColours bc,TextureMap tM)
   {
	   
     //Logging.log("generating block map colours from textures", new Object[0]);
     
	   System.out.println("generating block map colours from textures");
 
 
	//int terrainTextureId = tM.getGlTextureId();
 
 	int terrainTextureId = Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).getGlTextureId();
 	
 	
   //int terrainTextureId = Minecraft.func_71410_x().field_71446_o.func_110581_b(TextureMap.field_110575_b).func_110552_b();
     
 
     if (terrainTextureId == 0)
     {
    	 System.out.println("error: could get terrain texture ID");
       //Logging.log("error: could get terrain texture ID", new Object[0]);
       return;
     }
     Texture terrainTexture = new Texture(terrainTextureId);
     
     if(terrainTexture.h == 0 || terrainTexture.w == 0) { return; }
     
     double u1Last = 0.0D;
     double u2Last = 0.0D;
     double v1Last = 0.0D;
     double v2Last = 0.0D;
     int blockColourLast = 0;
     int e_count = 0;
     int b_count = 0;
     int s_count = 0;
     
     for (Object oblock : Block.REGISTRY)
     {
       Block block = (Block)oblock;
       int blockID = Block.getIdFromBlock(block);
       
       for (int dv = 0; dv < 16; dv++)
       {
         int blockColour = 0;
         
         if ((block != null) && (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE))
         {
 
           TextureAtlasSprite icon = null;
           try
           {
					  icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getStateFromMeta(dv));
					  //icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(block.getStateFromMeta(dv)).getParticleTexture();
					  //icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(block.getStateFromMeta(dv))
             //icon = Minecraft.func_71410_x().func_175602_ab().func_175023_a().func_178122_a(block.func_176203_a(dv));
 
 
           }
           catch (Exception e)
           {
        	   //System.out.println(e.toString());
             e_count++;
           }
           
           if (icon != null)
           {
				double u1 = icon.getMinU();
				double u2 = icon.getMaxU();
				double v1 = icon.getMinV();
				double v2 = icon.getMaxV();
             
             if ((u1 == u1Last) && (u2 == u2Last) && (v1 == v1Last) && (v2 == v2Last))
             {
               blockColour = blockColourLast;
               s_count++;
             }
             else
             {
               blockColour = getIconMapColour(icon, terrainTexture);
 
               u1Last = u1;
               u2Last = u2;
               v1Last = v1;
               v2Last = v2;
               blockColourLast = blockColour;
               b_count++;
               //System.out.println("Processed block: " + block.delegate.name().toString() + "variant:" + String.valueOf(dv) + " color:" + blockColour);
             }
           }
         }
         
         if(block.delegate.name().toString().equals("minecraft:yellow_flower") || block.delegate.name().toString().equals("minecraft:red_flower")) {
        	 blockColour = (blockColour & 0x00FFFFFF) + 0xFF000000;
         }
         
         
         bc.setColour(block.delegate.name().toString(), String.valueOf(dv), blockColour);
         //block.isFoliage(Minecraft.getMinecraft().theWorld, block.getb)
         if(block.getMaterial(block.getDefaultState()) == Material.LEAVES) {
        	 bc.setBlockType(block.delegate.name().toString(), "*", BlockColours.BlockType.LEAVES);
         }
         
         if(block.getMaterial(block.getDefaultState()) == Material.WATER) {
        	 bc.setBlockType(block.delegate.name().toString(), "*", BlockColours.BlockType.WATER);
         }
         
         if(block.getMaterial(block.getDefaultState()) == Material.GRASS) {
        	 bc.setBlockType(block.delegate.name().toString(), "*", BlockColours.BlockType.GRASS);
         }
         if(block.delegate.name().toString().equals("minecraft:tallgrass")) {
        	 bc.setBlockType(block.delegate.name().toString(), "*", BlockColours.BlockType.GRASS);
         }

         
       }
     }
     //Logging.log("processed %d block textures, %d skipped, %d exceptions", new Object[] { Integer.valueOf(b_count), Integer.valueOf(s_count), Integer.valueOf(e_count) });
     System.out.println("processed " + Integer.valueOf(b_count) + " block textures skipped " + Integer.valueOf(s_count) +  " exceptions " + Integer.valueOf(e_count));
     genBiomeColours(bc);
   }
 }
