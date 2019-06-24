package com.underground.undergroundmod.block;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockAlloy_Door extends BlockDoor{
	
	private final ToolType harvestTool = ToolType.PICKAXE;
	private final int  harvestlevel = 4;

	public BlockAlloy_Door(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH).with(OPEN, Boolean.valueOf(false)).with(HINGE, DoorHingeSide.LEFT).with(POWERED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER));
	}
	

	
	@Override
	public int getHarvestLevel(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return this.harvestlevel;
	}
	
	@Override
	public ToolType getHarvestTool(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return this.harvestTool;
	}

}
