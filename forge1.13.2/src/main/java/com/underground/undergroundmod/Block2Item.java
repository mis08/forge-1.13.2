package com.underground.undergroundmod;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;


public final class Block2Item {
	
	public static Item set(Block block) {
		return new ItemBlock(block, new Item.Properties().group(UnderGroundMod.tabUnder)).setRegistryName(block.getRegistryName());
	}

}
