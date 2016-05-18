package de.maefvfis.gameoverlay.client.kadconmap;

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

public class CopyOfLeechTask implements Callable {

	public int x;
	public int y;
	
	public CopyOfLeechTask(int xCoord, int yCoord) {
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
			//System.out.println(KadconMapHolder.buildUrl(x, y) + " - " + x + ":" + y);
			return null; 
		}

		try {
			img = ImageIO.read(imageURL);
		} catch (IOException e) {
			//System.out.println(KadconMapHolder.buildUrl(x, y) + " - " + x + ":" + y);
			return null; 
		}
		
		KadconMapHolder.addMapTileToQue(new MapTile(x,y,img));
		//KadconMapHolder.processingMapTiles.remove(new MapTile(x,y,0));
		return null;	
	}
}
