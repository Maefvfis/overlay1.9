package de.maefvfis.gameoverlay.objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;
import de.maefvfis.gameoverlay.client.gui.ChunckViewer.choords;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;

public class CSVSchildManager {
	
	static String CsvFileName = "ShopExport.csv";
	static List<ShopSchild> shopschilder = new ArrayList(); // CSV LOAD
	static Minecraft mc = Minecraft.getMinecraft();
	static int SchilderFound = 0;
	static int SchilderAdded = 0;
	static int SchilderImprotedFromCSV = 0;
	
	
	public static void add_shopschild(ShopSchild shopschild,boolean count) {
		//Debug.println("","awdawdawd");
		
		ShopSchild testschild;
		boolean isin = false;
		for(int i = 0; i < shopschilder.size(); i++) {
			testschild = shopschilder.get(i);
			if(testschild.isequal(shopschild)) {
				isin = true;
			}
		}
		if(!isin) {
			if(count)
				SchilderAdded++;
			shopschilder.add(shopschild);
		}
	}
	
	
	public static void getShopschilder(int X, int Z) {
		
		List<ShopSchild> shopschilder = new ArrayList(); // CSV LOAD
		
		Chunk chunk;
		CSVSchildManager CViewer = new CSVSchildManager();
		SchilderFound = 0;
		SchilderImprotedFromCSV = 0;
		SchilderAdded = 0;
		
		if(CsvFileReader.readCsvFile(CsvFileName) != null) {
			
			List<ShopSchild> ShopSchilderCSV = CsvFileReader.readCsvFile(CsvFileName);
			for(ShopSchild shopschild: ShopSchilderCSV) {
				add_shopschild(shopschild, false);
				SchilderImprotedFromCSV++;
			}
			
		}
		
		
		for (int i1 = 0; i1 <= Integer.valueOf(ConfigurationHandler.myGridSize); i1++)
		{
			for (int i2 = 0; i2 <= Integer.valueOf(ConfigurationHandler.myGridSize); i2++)
			{
				chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(X + Offset(i1),0, Z + Offset(i2)));
				CViewer.addShopSchilderinMapRadiusToList(TileEntitySign.class,chunk);
			}
		}
		
		mc.thePlayer.addChatComponentMessage(new TextComponentString("--------------------- CSV EXPORT ---------------------"));
		mc.thePlayer.addChatComponentMessage(new TextComponentString("Shops aus CSV Geladen: " + String.valueOf(SchilderImprotedFromCSV)));
		mc.thePlayer.addChatComponentMessage(new TextComponentString("Shops im Umkreis Gefunden: " + String.valueOf(SchilderFound)));
		mc.thePlayer.addChatComponentMessage(new TextComponentString("neue Shops in CSV geschrieben: " + String.valueOf(SchilderAdded)));
		mc.thePlayer.addChatComponentMessage(new TextComponentString("------------------------------------------------------"));
	}
	
	
	public static void save_csv() {
		
		CsvFileWriter.writeCsvFile(CsvFileName, shopschilder);
		
	}
	
	
	public static int Offset(int count) {
		if(count < Integer.valueOf(ConfigurationHandler.myGridSize) / 2) return ((Integer.valueOf(ConfigurationHandler.myGridSize) / 2) - count) * -16;
		if(count > Integer.valueOf(ConfigurationHandler.myGridSize) / 2) return (count - (Integer.valueOf(ConfigurationHandler.myGridSize) / 2)) * 16;
		return 0;
	}
	
	
	
	
	
	public void addShopSchilderinMapRadiusToList(Class<?> instance, Chunk chunk) {
		List<choords> result = new ArrayList<choords>();
		HashMap chunkTileEntityMap = (HashMap) chunk.getTileEntityMap();
		Iterator iterator = chunkTileEntityMap.values().iterator();
		TileEntitySign Sign;
        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            if (instance.isInstance(tileentity)) {
            	
            	Sign = (TileEntitySign) tileentity;

            	if(isShopSign(Sign)) {
            		SchilderFound++;
            		String ShopInhaber = Sign.signText[0].getUnformattedText();
            		int Anzahl = Integer.parseInt(Sign.signText[1].getUnformattedText());
            		float Ankauf = 0;
            		float Verkauf = 0;
            		String Item = Sign.signText[3].getUnformattedText();
            		int X = Sign.getPos().getX();
            		int Y = Sign.getPos().getY();
            		int Z = Sign.getPos().getZ();
            		String preiszeile = Sign.signText[2].getUnformattedText();
            		
            		
            		if(preiszeile.contains(":")) {
            			String[]preise = preiszeile.split(":");
            			
            			if(preise[0].contains("B")) {
            				Verkauf = extractprice(preise[0]);
            			}
            			if(preise[0].contains("S")) {
            				Ankauf = extractprice(preise[0]);
            			}
            			
            			if(preise[1].contains("B")) {
            				Verkauf = extractprice(preise[1]);
            			}
            			if(preise[1].contains("S")) {
            				Ankauf = extractprice(preise[1]);
            			}
            			
            			
            		} else {
            			
            			if(preiszeile.contains("B")) {
            				Verkauf = extractprice(preiszeile);
            			}
            			if(preiszeile.contains("S")) {
            				Ankauf = extractprice(preiszeile);
            			}
            				
            		}
            		//Debug.println("","" + new ShopSchild(ShopInhaber, Anzahl, Ankauf, Verkauf, Item, X, Y, Z).toString());
            		CSVSchildManager.add_shopschild(new ShopSchild(ShopInhaber, Anzahl, Ankauf, Verkauf, Item, X, Y, Z,ConfigurationHandler.ShopWarp,ConfigurationHandler.ShopServer), true);
            		
            	}
            	//result.add(new choords(tileentity.getPos().getX(),tileentity.getPos().getZ(),tileentity.getPos().getY(),null));
            }
        }
 
	}
	
	
	
	
	
	
	
	public ShopSchild getShopschildFromTiletentity(TileEntitySign Sign) {


            	if(isShopSign(Sign)) {
            		SchilderFound++;
            		String ShopInhaber = Sign.signText[0].getUnformattedText();
            		int Anzahl = Integer.parseInt(Sign.signText[1].getUnformattedText());
            		float Ankauf = 0;
            		float Verkauf = 0;
            		String Item = Sign.signText[3].getUnformattedText();
            		int X = Sign.getPos().getX();
            		int Y = Sign.getPos().getY();
            		int Z = Sign.getPos().getZ();
            		String preiszeile = Sign.signText[2].getUnformattedText();
            		
            		
            		if(preiszeile.contains(":")) {
            			String[]preise = preiszeile.split(":");
            			
            			if(preise[0].contains("B")) {
            				Verkauf = extractprice(preise[0]);
            			}
            			if(preise[0].contains("S")) {
            				Ankauf = extractprice(preise[0]);
            			}
            			
            			if(preise[1].contains("B")) {
            				Verkauf = extractprice(preise[1]);
            			}
            			if(preise[1].contains("S")) {
            				Ankauf = extractprice(preise[1]);
            			}
            			
            			
            		} else {
            			
            			if(preiszeile.contains("B")) {
            				Verkauf = extractprice(preiszeile);
            			}
            			if(preiszeile.contains("S")) {
            				Ankauf = extractprice(preiszeile);
            			}
            				
            		}
            		//Debug.println("","" + new ShopSchild(ShopInhaber, Anzahl, Ankauf, Verkauf, Item, X, Y, Z).toString());
            		return new ShopSchild(ShopInhaber, Anzahl, Ankauf, Verkauf, Item, X, Y, Z,ConfigurationHandler.ShopWarp,ConfigurationHandler.ShopServer);
            		
            	}
            	return null;

        
 
	}
	
	
	
	public float extractprice(String string) {
		string = string.replaceAll("B", "");
		string = string.replaceAll("S", "");
		string = string.replaceAll(" ", "");
		//string = string.replaceAll(".", ",");
		//Debug.println("",string);
		return Float.valueOf(string);
	}
	

	public boolean isShopSign(TileEntitySign Sign) {
		
		
		if(Sign.signText[0] == null || Sign.signText[1] == null || Sign.signText[2] == null || Sign.signText[3] == null) 
			return false;
		
		
		if(Sign.signText[0].getUnformattedText().replaceAll(" ", "").equalsIgnoreCase(""))
			return false;
		
		if(Sign.signText[3].getUnformattedText().replaceAll(" ", "").equalsIgnoreCase(""))
			return false;
		
		if(!isInteger(Sign.signText[1].getUnformattedText()))
			return false;
		
		String preiszeile = Sign.signText[2].getUnformattedText();
		
		if(preiszeile.contains(",")) 
			return false;
		
		if(preiszeile.contains(":")) {
			
			
			
			if(preiszeile.contains(" : ")) {
				String[]preise = preiszeile.split(" : ");
			} else if (preiszeile.contains(" : ")) {
				String[]preise = preiszeile.split(": ");
			} else if (preiszeile.contains(" :")) {
				String[]preise = preiszeile.split(" :");
			} else if (preiszeile.contains(":")) {
				String[]preise = preiszeile.split(":");
			}
				
			
			
			
			String[]preise = preiszeile.split(":");
			if(!istPreis(preise[0]) || !istPreis(preise[1])) 
				
				return false;
			
			
			//Debug.println("","" + preise[1]);
		} else {
			if(!istPreis(preiszeile)) 
				return false;
		}
		
		
		
		
		
		return true;
		
	}
	
	
	
	public boolean istPreis(String string) {
		string = string.trim();
		if(string.matches("B ([0-9]+).([0-9]+)|S ([0-9]+).([0-9]+)|B ([0-9]+)|S ([0-9]+)|([0-9]+).([0-9]+) B|([0-9]+).([0-9]+) S|([0-9]+) S|([0-9]+) B")) {
			return true;
		}
		
		
		return false;
		
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
}
