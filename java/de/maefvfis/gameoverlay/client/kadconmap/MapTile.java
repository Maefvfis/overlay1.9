package de.maefvfis.gameoverlay.client.kadconmap;

import java.awt.image.BufferedImage;

import de.maefvfis.gameoverlay.objects.Output;
import scala.Equals;

public class MapTile {
	
	public int x;
	public int y;
	public int img;
	public BufferedImage texture;
	
	MapTile(int xCoord, int yCoord, int imageId, BufferedImage bufferedImage) {
		x = xCoord;
		y = yCoord;
		img = imageId;
		texture = bufferedImage;
	}
	MapTile(int xCoord, int yCoord, BufferedImage bufferedImage) {
		this(xCoord,yCoord,0,bufferedImage);
	}
	MapTile(int xCoord, int yCoord, int imageId) {
		this(xCoord,yCoord,imageId,null);
	}
	MapTile(int xCoord, int yCoord) {
		this(xCoord,yCoord,0,null);
	}

	
	public boolean equals (Object o) {
        // no need for (o instanceof Point) by design
		if(o == null) return false;
        return x == ((MapTile)o).x && y == ((MapTile)o).y;
    }
	
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + x;
	    result = prime * result + y;
	    return result;
	}
}
