package de.maefvfis.gameoverlay.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockScanner {
	
	private static int chunkX = 0;
	private static int chunkY = 0;
	private static String output = "";
	
	public static Map<Block,Integer> scan(Chunk chunk) {
		ArrayList<Blockinator> checkArr = parseConfig();
		//System.out.println(checkArr.size());
		Map<Block,Integer> counts = new HashMap<Block,Integer>();
		if(chunk == null) return counts;
		int count = 0;
		int chunkX = chunk.xPosition * 16;
	    int chunkZ = chunk.zPosition * 16;
	    
	    for (int i = 0; i < 16; ++i) {
	      for (int j = 0; j < 16; ++j) {
	        for (int k = 0; k < 256; ++k) {
	        
		        IBlockState	iblockstate = chunk.getBlockState(new BlockPos(chunkX + i, k, chunkZ + j));
		        if(iblockstate == null) continue;
		        Block block = iblockstate.getBlock();
		        if(block == null) continue;
		        if (block == Blocks.air) continue;
		        
		        int value = isThere(iblockstate,checkArr);
		        //System.out.println(value);
		        if (value != 0) {
		        	
		        	if(counts.containsKey(block)) {
		        		counts.put(block, counts.get(block) + value);
		        	} else {
		        		counts.put(block, value);
		        	}
		        }
	        }
	      }
	    }
	    return counts;
	}
	public static String analyze(Chunk chunk) {
		if(chunk.xPosition == chunkX && chunk.zPosition == chunkY) {
			return output;
		}
		String ret;
		Map<Block,Integer> counts = scan(chunk);
		int value = 0;
		for(Entry<Block,Integer> entry : counts.entrySet()) {
		    Block key = entry.getKey();
		    value += entry.getValue();
		}
		ret = "Chunk value: " + value;
		
		output = ret;
		chunkX = chunk.xPosition;
		chunkY = chunk.zPosition;
		return ret;
	}
	
	
	
	public static Object getBlock() {
		return null;
	}
	
	public static int isThere(IBlockState blockState,ArrayList<Blockinator> checkArr) {
		
		Block block = blockState.getBlock();
		for(Blockinator blocki:checkArr) {
			if(blocki.b == block) {
				if(!blocki.hasSub) { return blocki.price; }
				for(int i:blocki.subs) {
					if(i == block.getMetaFromState(blockState)) {  return blocki.price; }
				}
			}
		}
		return 0;
	}
	
    public static ArrayList<Blockinator> parseConfig() {
    	ArrayList<Blockinator> blocks = new ArrayList<Blockinator>();
    	for(String s:ConfigurationHandler.blocksToFind) {
    		if(s.isEmpty()) continue;
    		if(!s.contains(";")) continue;
    		if(s.split(";").length < 2) continue;
    		
    		int[] subs = new int[]{};
    		int blockID = -1;
    		int price = 0;
    		
    		int i = 0;
    		for(String split:s.split(";")) {
    			
    			if(i == 0) {
    				
    				
    				
    				if(split.contains(":")) {
    					// get Blocks with sub IDs
    					int ii = 0;
    					for(String split2:split.split(":")) {
    						if(ii == 0) {
    							blockID = getInt(split2);
    						} else {
    							int subID = getInt(split2);
    							if(subID != -1) { subs = ArrayUtils.add(subs, subID); }
    						}
    						ii++;
    					}
    				} else {
    					blockID = getInt(split);
    				}
    			} else if (i == 1) {
    				price = getInt(split);
    			}
    			
    			i++;
    		}
    		if(blockID != -1) { blocks.add(new Blockinator(Block.getBlockById(blockID),subs,price)); }
    	}
    	return blocks;
    }
	
	public static int getInt(String s) {
		int res = 0;
		boolean test = false;
		for (int i=0; i < s.length(); i++) {
		    char c = s.charAt(i);
		    if(c == ':') return -1;
		    if (c < '0' || c > '9') continue;
		    res = res * 10 + (c - '0');
		    test = true;
		}
		if(!test) return -1;
		return res;
	}
	

	 
}
