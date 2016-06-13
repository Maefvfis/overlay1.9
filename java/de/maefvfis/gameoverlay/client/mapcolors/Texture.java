package de.maefvfis.gameoverlay.client.mapcolors;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.lwjgl.opengl.GL11;



public class Texture
{
  private int id;
  public final int w;
  public final int h;
  private final IntBuffer pixelBuf;
  
  public Texture(int w, int h, int fillColour, int minFilter, int maxFilter, int textureWrap)
  {
    this.id = GlStateManager.generateTexture();
    this.w = w;
    this.h = h;
    this.pixelBuf = allocateDirectIntBuffer(w * h);
    fillRect(0, 0, w, h, fillColour);
    this.pixelBuf.position(0);
    bind();
    GL11.glTexImage2D(3553, 0, 32856, w, h, 0, 32993, 5121, this.pixelBuf);
    setTexParameters(minFilter, maxFilter, textureWrap);
  }
  
  public Texture(int w, int h, int fillColour)
  {
    this(w, h, fillColour, 9729, 9728, 33071);
  }

  public Texture(int id)
  {
    this.id = id;
    bind();
    this.w = Render.getTextureWidth();
    this.h = Render.getTextureHeight();
    
	    this.pixelBuf = allocateDirectIntBuffer(this.w * this.h);
	if(this.w != 0 && this.h != 0) { 
	    getPixelsFromExistingTexture();
	    //Logging.log("created new MwTexture from GL texture id %d (%dx%d) (%d pixels)", new Object[] { Integer.valueOf(this.id), Integer.valueOf(this.w), Integer.valueOf(this.h), Integer.valueOf(this.pixelBuf.limit()) });
	    System.out.println("Texture id: "+Integer.valueOf(this.id)+" w,h:"+Integer.valueOf(this.w)+","+Integer.valueOf(this.h));
    }
  }
  public static IntBuffer allocateDirectIntBuffer(int size)
   {
       return ByteBuffer.allocateDirect(size * 4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
   }

  public synchronized void close()
  {
    if (this.id != 0)
    {
      try
      {
        GlStateManager.deleteTexture(this.id);
      }
      catch (NullPointerException e)
      {
      }
      this.id = 0;
    }
  }
  
  public void setPixelBufPosition(int i)
  {
    this.pixelBuf.position(i);
  }
  
  public void pixelBufPut(int pixel)
  {
    this.pixelBuf.put(pixel);
  }
  
  public synchronized void fillRect(int x, int y, int w, int h, int colour)
  {
    int offset = y * this.w + x;
    for (int j = 0; j < h; j++)
    {
      this.pixelBuf.position(offset + j * this.w);
      for (int i = 0; i < w; i++)
      {
        this.pixelBuf.put(colour);
      }
    }
  }
  


  public synchronized void getRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize, TextureAtlasSprite icon)
  {
    int bufOffset = y * this.w + x;
    for (int i = 0; i < h; i++)
    {
      try
      {
        this.pixelBuf.position(bufOffset + i * this.w);
        this.pixelBuf.get(pixels, offset + i * scanSize, w);
      }
      catch (IllegalArgumentException e)
      {

      }
    }
  }
  



  public synchronized void setRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize)
  {
    int bufOffset = y * this.w + x;
    for (int i = 0; i < h; i++)
    {
      this.pixelBuf.position(bufOffset + i * this.w);
      this.pixelBuf.put(pixels, offset + i * scanSize, w);
    }
  }
  
  public synchronized void setRGB(int x, int y, int colour)
  {
    this.pixelBuf.put(y * this.w + x, colour);
  }
  
  public synchronized int getRGB(int x, int y)
  {
    return this.pixelBuf.get(y * this.w + x);
  }
  
  public void bind()
  {
	  GlStateManager.bindTexture(this.id);
    //GlStateManager.func_179144_i(this.id);
  }
  

  public void setTexParameters(int minFilter, int maxFilter, int textureWrap)
  {
    bind();
    GL11.glTexParameteri(3553, 10242, textureWrap);
    GL11.glTexParameteri(3553, 10243, textureWrap);
    GL11.glTexParameteri(3553, 10241, minFilter);
    GL11.glTexParameteri(3553, 10240, maxFilter);
  }
  
  public void setLinearScaling(boolean enabled)
  {
    bind();
    if (enabled)
    {
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
    }
    else
    {
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
    }
  }
  

  public synchronized void updateTextureArea(int x, int y, int w, int h)
  {
    try
    {
      bind();
      GL11.glPixelStorei(3314, this.w);
      this.pixelBuf.position(y * this.w + x);
      GL11.glTexSubImage2D(3553, 0, x, y, w, h, 32993, 5121, this.pixelBuf);
      GL11.glPixelStorei(3314, 0);
    }
    catch (NullPointerException e)
    {
    }
  }
  
  public synchronized void updateTexture()
  {
    bind();
    this.pixelBuf.position(0);
    GL11.glTexImage2D(3553, 0, 32856, this.w, this.h, 0, 32993, 5121, this.pixelBuf);
  }
  

  private synchronized void getPixelsFromExistingTexture()
  {
    try
    {
      bind();
      this.pixelBuf.clear();
      GL11.glGetTexImage(3553, 0, 32993, 5121, this.pixelBuf);
      


      this.pixelBuf.limit(this.w * this.h);
    }
    catch (NullPointerException e)
    {
    }
  }
}
