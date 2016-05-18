package de.maefvfis.gameoverlay.client.kadconmap;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.ChunkData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CopyOfKadconMapHolder2 {
	
	public static ArrayList<MapTile> MapTiles = new ArrayList<MapTile>();
	public static ArrayList<MapTile> processingMapTiles = new ArrayList<MapTile>();
	public static ArrayList<MapTile> queMapTiles = new ArrayList<MapTile>();
	public static ArrayList<MapTile> allMapTiles = new ArrayList<MapTile>();
	public static List<Future> futures = new ArrayList<Future>();
	public static ExecutorService executor = Executors.newFixedThreadPool(1);
	public static int maxTileCountInBuffer = 1000;
	
	public synchronized static void reset() {
		System.out.println("Resett des Grafikspeichers");
		for(MapTile ChunkD: MapTiles) {
			TextureUtil.deleteTexture(ChunkD.img);
		}
		MapTiles.clear();
		processingMapTiles.clear();
		queMapTiles.clear();
		allMapTiles.clear();
	}
	public synchronized static int getImage(int xChoord,int yChoord) {
		
		if(MapTiles.size() > maxTileCountInBuffer) { reset(); }

		processQue();

		MapTile mT = new MapTile(xChoord,yChoord,0);
		if(MapTiles.contains(mT)) {
			return MapTiles.get(MapTiles.indexOf(mT)).img;
		}
		if(processingMapTiles.contains(mT)) { return -1; }
		if(allMapTiles.contains(mT)) { return -1; }
		int xTileStart = c4X(xChoord) * 4;
		int yTileStart = c4Y(yChoord) * -4;
		allMapTiles.add(new MapTile(xChoord,yChoord,0));
		for(int x = 0; x<4;x++) {
			for(int y = 0; y<4;y++) {
				allMapTiles.add(new MapTile(xTileStart + x,yTileStart - y,0));
				//System.out.println("Chunk added:" + (xTileStart + x) + ":" + (yTileStart - y) + "for: " + xChoord + ":" + yChoord);
			}
		}
		
		processingMapTiles.add(mT);
		addTask(xChoord,yChoord);
		
		
		//System.out.println("Nicht gefunden" + MapTiles.size());
		return -1;
	}
	
	public static void addTask(int x, int y) {
		 futures.add(executor.submit(new LeechTask(x,y)));
	}
	
	public synchronized static void addMapTileToQue(MapTile mapTile) {
		if(!queMapTiles.contains(mapTile)) {
			queMapTiles.add(mapTile);
		}
	}
	
	public static int uploadTexture(BufferedImage img) {
		return TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), img);
	}
	
	public synchronized static void processQue() {
		if(queMapTiles.size() == 0) return;
		int textureId = -1;
		for(MapTile mT:queMapTiles) {
			if(mT.texture != null) {
				slice(mT.texture,mT.x,mT.y);
			}
		}
		queMapTiles.clear();
	}
	
	public synchronized static BufferedImage resize(BufferedImage src) {
		  BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		  int x, y;
		  int ww = src.getWidth();
		  int hh = src.getHeight();
		  for (x = 0; x < 16; x++) {
		    for (y = 0; y < 16; y++) {
		      int col = src.getRGB(x * ww / 16, y * hh / 16);
		      img.setRGB(x, y, col);
		    }
		  }
		  return img;
	}
	
	public synchronized static void slice(BufferedImage image, int xChoord, int yChoord) {

	    int rows = 4;
	    int cols = 4;  
	    int chunks = rows * cols;  
	    
	    int xTileStart = c4X(xChoord) * 4;
		int yTileStart = c4Y(yChoord) * -4;
	  
	    int chunkWidth = image.getWidth() / cols;
	    int chunkHeight = image.getHeight() / rows;  
	    int count = 0;  
	    for (int x = 0; x < rows; x++) {  
	        for (int y = 0; y < cols; y++) {  
	            BufferedImage img = new BufferedImage(chunkWidth, chunkHeight, image.getType());  
	            Graphics2D gr = img.createGraphics();  
	            gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);  
	            gr.dispose();  
	            img = resize(img);
	            
	            
	            //System.out.println(""+(xChoord + x) + ":" + (yChoord - y));
	    		MapTiles.add(new MapTile(xTileStart + y,yTileStart - (cols - x),uploadTexture(img)));
	        }  
	    }  
	    if(MapTiles.size() > maxTileCountInBuffer) {
	    	
	    }
	}
	
	
	public static String buildUrl(int x, int y) {
		return 	"http://map"+ConfigurationHandler.myConfigMapType+".kadcon.de/tiles/world/gsf/"+c32X(x)+"_"+c32Y(y)+"/"+c4X(x)+"_"+c4Y(y)+ ".jpg";
	}
	
	
	public static int c32X(int x) {
		if(x>0) return (x-1)>>7;
	    if(x<0) return (((x*-1)>>7)+1)*-1;
	    return 0;
	}
	public static int c4X(int x) {
		if(x>0) return (x-1)>>2;
	    if(x<0) return (((x*-1)>>2)+1)*-1;
	    return 0;
	}
	public static int c32Y(int x) {
		if(x>0) return (((x*-1)-1)>>7);
	    if(x<0) return (((x)>>7)+1)*-1;
	    return -1;
	}
	public static int c4Y(int x) {
		if(x>0) return (((x*-1)-1)>>2);
	    if(x<0) return (((x)>>2)+1)*-1;
	    return -1;
	}

	
}
