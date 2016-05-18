package de.maefvfis.gameoverlay.objects;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.WorldType;

public class ShopVergleich {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static int INDItem = 0;
	public static int INDVerkauf = 1;
	public static int INDAnkauf = 2;
	private static List<ShopVglPreis> Vergleichspreise = CsvFileReader.readCsvShopImportFile("ShopImport.csv");
	
	
	
	
	
	public static void ShowShopInfo(int Y,FontRenderer fontRender) {
		if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.objectMouseOver.getBlockPos() != null)
        {
            BlockPos blockpos = mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);

            if (mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD)
            {
                iblockstate = iblockstate.getBlock().getActualState(iblockstate, mc.theWorld, blockpos);
            }
            
            if(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())).equalsIgnoreCase("minecraft:wall_sign") || String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())).equalsIgnoreCase("minecraft:standing_sign")) {

            	
            	
            	
            	TileEntitySign Sign = (TileEntitySign) mc.theWorld.getTileEntity(blockpos);
            	
            	CSVSchildManager signmanager = new CSVSchildManager();
            	if(signmanager.getShopschildFromTiletentity(Sign) != null && Vergleichspreise != null) {
            		
            		ShopSchild ShopSchild = signmanager.getShopschildFromTiletentity(Sign);
            		
            		
            		if(getVglPreisByItem(ShopSchild.getItem()) != null) {
            		
	            		
	            		
	            		int color1 = 0;
	            		if(ShopSchild.getVerkauf() / ShopSchild.getAnzahl() > getVglPreisByItem(ShopSchild.getItem()).VKpreis) {
	            			
	            			// ROT
	            			color1 = 0xFF0000;
	            			
	            		} else if(ShopSchild.getVerkauf() / ShopSchild.getAnzahl() < getVglPreisByItem(ShopSchild.getItem()).AKpreis) {
	            			
	            			// GRÜN
	            			color1 = 0x00FF00;

	            		} else {
	            			
	            			// GELB
	            			color1 = 0xFFFF00;
	            		}
	            		
	            		
	            		
	            		int color2 = 0;
	            		if(ShopSchild.getAnkauf() / ShopSchild.getAnzahl() < getVglPreisByItem(ShopSchild.getItem()).AKpreis) {
	            			
	            			// ROT
	            			color2 = 0xFF0000;
	            			
	            		} else if(ShopSchild.getAnkauf() / ShopSchild.getAnzahl() > getVglPreisByItem(ShopSchild.getItem()).VKpreis) {
	            			
	            			// GRÜN
	            			color2 = 0x00FF00;

	            		} else {
	            			
	            			// GELB
	            			color2 = 0xFFFF00;
	            		}
	            		
	         
	            		
	            		String string1 = String.valueOf(getVglPreisByItem(ShopSchild.getItem()).VKpreis * ShopSchild.getAnzahl());
	            		String string2 = String.valueOf(getVglPreisByItem(ShopSchild.getItem()).AKpreis * ShopSchild.getAnzahl());
	            		
	            		fontRender.drawStringWithShadow(string1,5, Y, color1);
	            		fontRender.drawStringWithShadow(" : ",5 + fontRender.getStringWidth(string1), Y, 0xFFFFFF);
	            		fontRender.drawStringWithShadow(string2,5 + fontRender.getStringWidth(string1 + " : "), Y, color2);
	            		fontRender.drawStringWithShadow("",5, Y, 0xFFFFFF);
            		}
            		

            	}
            	
            	
            }
		
        }
	}
	
	
	
	

	
	public static void addVergleichspreis(String item, float VKpreis, float AKpreis) {
		Vergleichspreise.add(new ShopVglPreis(item,VKpreis, AKpreis));
	}
	
	
	public static ShopVglPreis getVglPreisByItem(String Item) {
		
		for (ShopVglPreis o : Vergleichspreise) {
			
			if(o.item.equalsIgnoreCase(Item))
				return o;
		}
		return null;
	}
	
	
	
	
	

}
