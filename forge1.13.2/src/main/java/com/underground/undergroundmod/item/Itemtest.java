package com.underground.undergroundmod.item;

import com.underground.undergroundmod.UnderGroundMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
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
	
	public EntityType sky = UnderGroundMod.EntitySkyRoamer;
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		
		return super.onItemRightClick(worldIn, playerIn, handIn);

	}


	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		// TODO 自動生成されたメソッド・スタブ
		
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		
		

	}
}

