package de.maefvfis.gameoverlay.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public class ChunkImage {
	
	public static final int normBlockHeight = 64;
	public static final int maxBlockHeight = 256;
	public static List<ChunkData> ChunkList = new ArrayList();
	public static int maxChunkBufferSize = 250;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static int colorAlpha = 0xFF000000;
	public static Chunk chunk;
	
	public static int LightSteps = 10;
	public static float MaxLight = 1F;
	
	public static int ShadowSteps = 10;
	public static float MaxShadow = 0.2F;
	
	public static 	int SaturationSteps = 30;
	public static	float MinSaturation = 0.1F;
	
	public static final  int default_map_color = 0xFFFFFF;
	public static int rX;
	public static int rZ;
	
	public static int y;
	
	public static int absX;
	public static int absZ;
	
	public static int mapcolor;
	public static MapColor MapColorObj;
	public static int meta;

	public static int getMapImage(Chunk chunk2, int absX2, int absZ2) {
		checkBufferSize();
		if(!chunk2.isLoaded()) { return -1;  }
		ChunkData cData = getChunkData(chunk2.xPosition, chunk2.zPosition);		
		if(cData == null) { 
			chunk = chunk2;
			absX = absX2;
			absZ = absZ2;
			CreateChunkData(); 
			return -1; 
		}
		return cData.TextureID;
		
	}
	

	
	public static void CreateChunkData() {
		
		BufferedImage Img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		
		for(rX = 0; rX <= 15; rX++) {
			for(rZ = 0; rZ <= 15; rZ++) {

				mapcolor = getMapColor();
				
				
				LightSteps = 10;
				ShadowSteps = 10;
				SaturationSteps = 30;
				
				mapcolor = addLightning(1,0);
				mapcolor = addLightning(0,1);
				mapcolor = addLightning(1,1);
				
				LightSteps = LightSteps * 2;
				ShadowSteps = ShadowSteps * 2;
				SaturationSteps = SaturationSteps * 2;
				
				mapcolor = addLightning(2,1);
				mapcolor = addLightning(1,2);
				
				LightSteps = LightSteps * 2;
				ShadowSteps = ShadowSteps * 2;
				SaturationSteps = SaturationSteps * 2;
				
				mapcolor = addLightning(2,0);
				mapcolor = addLightning(0,2);
				mapcolor = addLightning(2,2);
				
	

				Img.setRGB(rX, rZ, mapcolor);
			}
		}
		
		int TextureID = TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), Img);
		ChunkList.add(new ChunkData(chunk.xPosition,chunk.zPosition,TextureID));
	}
	
	public static int getMapColor() {
		
		int j4 = chunk.getHeightValue(rX, rZ) + 1;
        IBlockState iblockstate = Blocks.air.getDefaultState();

        if (j4 > 1)
        {
            do
            {
                --j4;
                iblockstate = chunk.getBlockState(new BlockPos(rX, j4, rZ));
            }
            while (iblockstate.getBlock().getMapColor(iblockstate) == MapColor.airColor && j4 > 0);

            if (j4 > 0 && iblockstate.getBlock().getMaterial(iblockstate).isLiquid())
            {
                int k4 = j4 - 1;
                Block block;
            }
        }

        y = j4 +1;
        return iblockstate.getBlock().getMapColor(iblockstate).colorValue;
	}
	
	private static class stoneColor {
		static MapColor MapColorObj2;
		static int getColorbyMeta(int meta) {
			if(meta == 1 || meta == 9) {
				return MapColorObj2.sandColor.colorValue;
			}
			if(meta == 15 || meta == 7) {
				return MapColorObj2.quartzColor.colorValue;
			}
			if(meta == 14 || meta == 12 || meta == 6 || meta == 4) {
				return MapColorObj2.netherrackColor.colorValue;
			}
			return MapColorObj2.stoneColor.colorValue;
		}
	}
	
	public static int addLightning(int x,int z) {
		
		ChunkCoordIntPair chunkChoord = chunk.getChunkCoordIntPair();
		BlockPos block = new BlockPos(chunkChoord.getXStart() + rX + x ,0, chunkChoord.getZStart() + rZ + z);
		Chunk chunk2 = mc.theWorld.getChunkFromBlockCoords(block);
		if(!chunk2.isLoaded()) { return mapcolor;  }
		
		int y2 = chunk2.getHeight(block);
		
		Color color = new Color(mapcolor);
		float hsbVals[] = Color.RGBtoHSB( color.getRed(),color.getGreen(),color.getBlue(), null );
		if(y < y2) { color = Color.getHSBColor( hsbVals[0], hsbVals[1],  getShadowBrightness(hsbVals[2],y2 - y)); } 
		if(y > y2) { color = Color.getHSBColor( hsbVals[0], getLightSaturation(hsbVals[1],y - y2),  getLightBrightness(hsbVals[2],y - y2)); } 
		
		return color.getRGB();
	}
	
	public static float getShadowBrightness(float color, int heigtDiff) {
		if(color < MaxShadow) { return color; }
		if(heigtDiff > ShadowSteps) heigtDiff = ShadowSteps;
		float color2 = (float) (color * (1 - (heigtDiff * (double) (1D / (double)ShadowSteps))));
		if(color2 < MaxShadow) { return color; }
		return  color2;
	}
	
	public static float getLightBrightness(float color, int heigtDiff) {
		if(color > MaxLight) { return color; }
		if(heigtDiff > LightSteps) heigtDiff = LightSteps;
		float color2 = (float) (color + ((heigtDiff * (double) (1D / (double)LightSteps)) * (1 - color)));
		if(color2 > MaxLight) { return color; }
		return  color2;	
	}
	
	public static float getLightSaturation(float color, int heigtDiff) {
		if(color < MinSaturation) { return color; }
		if(heigtDiff > SaturationSteps) heigtDiff = SaturationSteps;
		float color2 = (float) (color * (1 - (heigtDiff * (double) (1D / (double)SaturationSteps))));
		if(color2 < MinSaturation) { return color; }
		return  color2;	
	}
	
	
	
	public static void checkBufferSize() {
		if(ChunkList.size() > maxChunkBufferSize) {
			deleteBuffer();
		}
	}
	
	public static void deleteBuffer() {
		for(ChunkData ChunkD: ChunkList) {
			TextureUtil.deleteTexture(ChunkD.TextureID);
		}
		ChunkList.clear();
	}

	public static ChunkData getChunkData(int x, int z) {
		for(ChunkData TestChunk: ChunkList) {
			if(TestChunk.x == x && TestChunk.z == z ) {
				return TestChunk;
				
			}
		}
		return null;
	}

}
