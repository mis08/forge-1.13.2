package com.underground.undergroundmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

public class BlockTemperedGlass extends BlockGlass{

	private ToolType harvestTool = ToolType.PICKAXE;
	private int harvestlevel = 4;
	
	public BlockTemperedGlass(Properties properties) {
		super(properties);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public int getHarvestLevel(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return harvestlevel;
	}
	
	@Override
	public ToolType getHarvestTool(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return harvestTool;
	}

}
