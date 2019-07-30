package com.underground.undergroundmod.block;

import com.underground.undergroundmod.tileentity.TileEntityDecompMachine;
import com.underground.undergroundmod.tileentity.TileEntityGenerator;
import com.underground.undergroundmod.tileentity.interactionobject.InteractionObjectGenerator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockGenerator extends BlockContainer{

	public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

	public BlockGenerator(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn,
			BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		// TODO 自動生成されたメソッド・スタブ
		if(!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityGenerator) {
				TileEntityGenerator tilegen =(TileEntityGenerator)tileentity;
				NetworkHooks.openGui((EntityPlayerMP)player,new InteractionObjectGenerator(tilegen),pos);
				player.addStat(StatList.INTERACT_WITH_FURNACE);
			}
		}
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasTileEntity() {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}


	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		// TODO 自動生成されたメソッド・スタブ
		builder.add(FACING);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		// TODO 自動生成されたメソッド・スタブ
		return new TileEntityGenerator();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos,
			IBlockState newState, boolean isMoving) {
		// TODO 自動生成されたメソッド・スタブ
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityGenerator) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityGenerator) tileentity);
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	

}
