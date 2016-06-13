package de.maefvfis.gameoverlay.client.mapcolors;

import java.awt.Color;
import java.io.File;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class ColorObj {
	
	private boolean notLoadedTextures = true; 
	public BlockColours blockColours = null;
	public TextureMap tM = null;
	
	public void reloadBlockColours()
	   {
		 this.blockColours = null;
	     BlockColours bc = new BlockColours();
	     BlockColourGen.genBlockColours(bc,tM);
	     this.blockColours = bc;
	   }
   @SubscribeEvent
   public void onTextureStitchEventPost(TextureStitchEvent.Post event)
   {
	   tM = event.getMap();
       System.out.println("awawdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!!!!!!!!!!!!");
       reloadBlockColours();
     
   }
   
   @SubscribeEvent
      public void onGuiOpenEvent(GuiOpenEvent event)
      {
        if (((event.getGui() instanceof GuiMainMenu)) && notLoadedTextures)
        {
          reloadBlockColours();
          notLoadedTextures = false;
        }
   }
}
