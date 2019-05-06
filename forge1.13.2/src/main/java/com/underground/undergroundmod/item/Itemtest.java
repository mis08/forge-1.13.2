package com.underground.undergroundmod.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Itemtest extends Item{

	public Itemtest(Properties properties) {
		super(properties);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		// プレイヤーが持っているItemStackは、プレイヤーから取得
		ItemStack stack = player.getHeldItem(hand);

		// 今回は使用すると自分を回復するアイテムにしてみる
		player.heal(20.0F); // ハート20個

		// 使用後にアイテムを減らす処理
		// このメソッドでは、数を減らした後に0個の場合EMPTYに差し替える処理などはなくてOK
		stack.shrink(1);

		// 結果を返す
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
	
	
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		// こちらは右クリックしたブロックが草ブロックだった場合に、草の小道ブロックに置き換えてみる

		//
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState state = world.getBlockState(pos);

			// IBlockStateは、その座標のBlockやメタデータ、Stateなどいろいろ確認できる
			// 今回は草ブロックかどうかの判定に使う
			if (state.getBlock() == Blocks.GRASS) {

				// 小道ブロック(GRASS_PATH)を設置
				// ブロックを置くときもIBlockStateが必要なので、ここではgetDefaultState()で初期設定を使用
				world.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());

				// シャベルで地面をならしたときの効果音を呼んでいる
				world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

		}
		return EnumActionResult.SUCCESS;
	}


}

