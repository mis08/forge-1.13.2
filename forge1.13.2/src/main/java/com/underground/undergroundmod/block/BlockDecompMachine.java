package com.underground.undergroundmod.block;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.tileentity.TileEntityDecompMachine;
import com.underground.undergroundmod.tileentity.gui.GuiDecompMachine;
import com.underground.undergroundmod.tileentity.interactionobject.InteractionObjectDecompMachine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRedstone;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockDecompMachine extends BlockContainer{
	
	//ブロックの向き
	public static DirectionProperty FACING=BlockHorizontal.HORIZONTAL_FACING;
	public static final BooleanProperty DECOMP = BlockRedstoneTorch.LIT;

	public BlockDecompMachine(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn,
			BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		// TODO 自動生成されたメソッド・スタブ
		if(!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityDecompMachine) {
				TileEntityDecompMachine tiledm=(TileEntityDecompMachine)tileentity;
				NetworkHooks.openGui((EntityPlayerMP)player,new InteractionObjectDecompMachine(tiledm),pos);
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
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		// TODO 自動生成されたメソッド・スタブ
		return new TileEntityDecompMachine();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos,
			IBlockState newState, boolean isMoving) {
		// TODO 自動生成されたメソッド・スタブ
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityDecompMachine) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDecompMachine) tileentity);
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	


}
