package com.underground.undergroundmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;

public class BlockAlloy_stairs extends BlockStairs{

    Block block = new Block(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
	
	public BlockAlloy_stairs(IBlockState p_i48321_1_,
			Properties p_i48321_2_) {
		super(p_i48321_1_, p_i48321_2_);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
}