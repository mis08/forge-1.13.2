package com.underground.undergroundmod.item;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.entity.EntityBullet;
import com.underground.undergroundmod.entity.EntityExpArrow;
import com.underground.undergroundmod.entity.EntityTippedExpArrow;

import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.event.world.NoteBlockEvent.Play;

public class AutomaticRifle extends Item{
	private int launchSpeed;


	public AutomaticRifle(Item.Properties builder) {
		super(builder);
	}


	//弾を飛ばす処理、発射速度要調整
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase playerIn,int count) {
		GunFire(playerIn);
		super.onUsingTick(stack, playerIn, count);
	}

	//銃を構える処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn,EntityPlayer playerIn, EnumHand handIn) {

		ItemStack itemstack = playerIn.getHeldItem(handIn);
		boolean flag = false;

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
		if (ret != null) return ret;

		if (!playerIn.abilities.isCreativeMode && !flag){
			return !flag ? new ActionResult(EnumActionResult.FAIL, itemstack) : new ActionResult(EnumActionResult.PASS, itemstack);
		}else{
			playerIn.setActiveHand(handIn);
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		}

	}

	public void GunFire(EntityLivingBase playerIn) {
		if(launchSpeed>3) {
			World worldIn = playerIn.world;
			EntityBullet bullet = new EntityBullet(worldIn,(EntityPlayer) playerIn);
			bullet.shoot(playerIn,playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3 * 3.0F, 1.0F);

			worldIn.spawnEntity(bullet);
			worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, UnderGroundMod.GunSound, SoundCategory.RECORDS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 3 * 0.5F);
			launchSpeed=0;
		}else {
			launchSpeed++;
		}
	}

//構える秒数
	@Override
	public int getUseDuration(ItemStack stack) {
		return 7200;
	}
//構えのタイプ
	@Override
	public EnumAction getUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}


}
