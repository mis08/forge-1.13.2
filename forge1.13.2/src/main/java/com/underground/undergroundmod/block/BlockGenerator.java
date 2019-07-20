package com.underground.undergroundmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.EnumFacing;

public class BlockGenerator extends Block{
	
	public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

	public BlockGenerator(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		// TODO 自動生成されたメソッド・スタブ
		builder.add(FACING);
	}

}
