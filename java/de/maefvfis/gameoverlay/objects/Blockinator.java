package de.maefvfis.gameoverlay.objects;

import net.minecraft.block.Block;

import org.apache.commons.lang3.ArrayUtils;

public class Blockinator {
	Block b;
	int[] subs = new int[]{};
	int price;
	boolean hasSub;
	
	Blockinator(Block block, int[] s, int p) {
		b = block;
		price = p;
		subs = ArrayUtils.addAll(subs, s);
		if(s.length == 0) {
			hasSub = false;
		} else {
			hasSub = true;
		}
	}
}