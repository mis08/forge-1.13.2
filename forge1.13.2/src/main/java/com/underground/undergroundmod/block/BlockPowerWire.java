package com.underground.undergroundmod.block;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.underground.undergroundmod.tileentity.TileEntityPowerWire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSixWay;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class BlockPowerWire extends BlockSixWay implements ITileEntityProvider{

	private static final Logger PRIVATE_LOGGER = LogManager.getLogger();

	protected static final VoxelShape voxel = Block.makeCuboidShape(5.0D, 8.0D, 5.0D,11.0D,2.0D,11.0D);

	public BlockPowerWire(Properties properties) {
		super(0.3125F,properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(UP, Boolean.valueOf(false)).with(DOWN, Boolean.valueOf(false)));

		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO 自動生成されたメソッド・スタブ
		return voxel;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return EnumBlockRenderType.MODEL;
	}


	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		// TODO 自動生成されたメソッド・スタブ
		return new TileEntityPowerWire();
	}

	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			super.onReplaced(state, worldIn, pos, newState, isMoving);
			worldIn.removeTileEntity(pos);
		}
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		if (te instanceof INameable && ((INameable)te).hasCustomName()) {
			player.addStat(StatList.BLOCK_MINED.get(this));
			player.addExhaustion(0.005F);
			if (worldIn.isRemote) {
				PRIVATE_LOGGER.debug("Never going to hit this!");
				return;
			}

			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			Item item = this.getItemDropped(state, worldIn, pos, i).asItem();
			if (item == Items.AIR) {
				return;
			}

			ItemStack itemstack = new ItemStack(item, this.quantityDropped(state, worldIn.rand));
			itemstack.setDisplayName(((INameable)te).getCustomName());
			spawnAsEntity(worldIn, pos, itemstack);
		} else {
			super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
		}

	}

	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

}
