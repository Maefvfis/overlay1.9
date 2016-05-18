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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class KadconMapHolder {
	
	public static final Minecraft mc = Minecraft.getMinecraft();

	public static ArrayList<MapTile> MapTiles = new ArrayList<MapTile>();
	public static ArrayList<MapTile> queMapTiles = new ArrayList<MapTile>();
	public static ArrayList<MapTile> allMapTiles = new ArrayList<MapTile>();
	public static List<Future> futures = new ArrayList<Future>();
	public final static ExecutorService executor = Executors.newFixedThreadPool(1);
	public final static int maxTileCountInBuffer = 1000;
	public final static int range = 6;
	
	public synchronized static void reset(boolean typeSwitch) {
		System.out.println("reset...");
		if(typeSwitch) {
			//full reset
			for(MapTile ChunkD: MapTiles) {
				TextureUtil.deleteTexture(ChunkD.img);
			}
			MapTiles.clear();
			queMapTiles.clear();
			allMapTiles.clear();
		} else {
			// keep visible part
			int X = MathHelper.floor_double(mc.thePlayer.posX)>>4;
			int Y = MathHelper.floor_double(mc.thePlayer.posZ)>>4;
			ArrayList<MapTile> newMapTiles = new ArrayList<MapTile>();
			int removed = 0;
			for(MapTile tile: MapTiles) {
				if(tile.x < X-range || tile.x > X+range || tile.y < Y-range || tile.y > Y+range) { 
					TextureUtil.deleteTexture(tile.img);
					removed++;
				} else {
					newMapTiles.add(tile);
				}
			}
			MapTiles.clear();
			queMapTiles.clear();
			allMapTiles.clear();
			MapTiles.addAll(newMapTiles);
			allMapTiles.addAll(newMapTiles);
		}
	}
	
	public synchronized static int getImage(int xChoord,int yChoord) {
		//System.out.println(MapTiles.size());
		if(MapTiles.size() > maxTileCountInBuffer) { reset(false); }

		processQue();

		MapTile mT = new MapTile(xChoord,yChoord,0);
		if(MapTiles.contains(mT)) {
			return MapTiles.get(MapTiles.indexOf(mT)).img;
		}

		if(allMapTiles.contains(mT)) { return -1; }
		int xTileStart = c4X(xChoord) * 4;
		int yTileStart = c4Y(yChoord) * -4;
		allMapTiles.add(new MapTile(xChoord,yChoord,0));
		for(int x = 0; x<4;x++) {
			for(int y = 0; y<4;y++) {
				allMapTiles.add(new MapTile(xTileStart + x,yTileStart - y,0));
			}
		}
		addTask(xChoord,yChoord);
		return -1;
	}
	
	public static void addTask(int x, int y) {
		 futures.add(executor.submit(new LeechTask(x,y)));
	}
	
	public synchronized static void addMapTileToQue(MapTile mapTile) {
		if(!MapTiles.contains(mapTile) && !queMapTiles.contains(mapTile)) {
			queMapTiles.add(mapTile);
		}
	}
	
	public synchronized static void processQue() {
		if(queMapTiles.size() == 0) return;
		MapTiles.add(new MapTile(queMapTiles.get(0).x,queMapTiles.get(0).y,TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), queMapTiles.get(0).texture)));
		queMapTiles.remove(0);
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