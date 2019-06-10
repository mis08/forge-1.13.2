package com.underground.undergroundmod.block;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

public class BlockAlloy extends Block{
	
	private ToolType harvestTool = ToolType.PICKAXE;
	private int harvestlevel = 4;
	
	public BlockAlloy(Properties properties) {
		super(properties);
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
