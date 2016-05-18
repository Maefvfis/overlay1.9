package de.maefvfis.gameoverlay.client.kadconmap;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.world.chunk.Chunk;

public class LeechTask implements Callable {

	public int x;
	public int y;
	
	public LeechTask(int xCoord, int yCoord) {
		x = xCoord;
		y = yCoord;
	}
	
	@Override
	public Object call() throws Exception {
		
		URL imageURL = null;
		BufferedImage img = null;
		
		try {
			imageURL = new URL(KadconMapHolder.buildUrl(x, y));
		} catch (MalformedURLException e1) {
			return null; 
		}

		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			return null; 
		}

		slice(img,x,y);
		return null;	
	}
	
	public static void slice(BufferedImage image, int xChoord, int yChoord) {

	    int tiles = 4;

	    int xTileStart = KadconMapHolder.c4X(xChoord) * 4;
		int yTileStart = KadconMapHolder.c4Y(yChoord) * -4;
	  
	    int chunkWidth = image.getWidth() / tiles;
	    int chunkHeight = image.getHeight() / tiles;  

	    for (int x = 0; x < tiles; x++) {  
	        for (int y = 0; y < tiles; y++) {  
	            BufferedImage img = new BufferedImage(chunkWidth, chunkHeight, image.getType());  
	            Graphics2D gr = img.createGraphics();  
	            gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);  
	            gr.dispose();  
	            KadconMapHolder.addMapTileToQue(new MapTile(xTileStart + y,yTileStart - (tiles - x),resize(img)));
	        }  
	    }  
	}
	
	public static BufferedImage resize(BufferedImage src) {
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
	
}