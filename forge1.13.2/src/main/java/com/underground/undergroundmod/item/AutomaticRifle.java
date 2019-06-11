package com.underground.undergroundmod.item;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.entity.EntityBullet;
import com.underground.undergroundmod.entity.EntityExpArrow;
import com.underground.undergroundmod.entity.EntityLaser;
import com.underground.undergroundmod.entity.EntityTippedExpArrow;
import com.underground.undergroundmod.item.Magazine;

import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
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
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public class AutomaticRifle extends Item{
	private int launchSpeed;


	public AutomaticRifle(Item.Properties builder) {
		super(builder);
		this.setDamage(getDefaultInstance(), getMaxDamage()-1);
	}


	//弾を飛ばす処理
	@Override
	public void onUsingTick(ItemStack itemRifle, EntityLivingBase entityLiving,int count) {
		if(entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			boolean flag = entityplayer.abilities.isCreativeMode;
			ItemStack itemstack = this.findAmmo(entityplayer);
			
			//弾があるか、クリエイティブモード
			if(!itemstack.isEmpty() || flag) {
				if(itemstack.isEmpty()) {
					itemstack= new ItemStack(UnderGroundMod.Magazine);
				}
				Magazine magazine =(Magazine)(itemstack.getItem() instanceof Magazine ? itemstack.getItem() : UnderGroundMod.Magazine);
				GunFire(entityplayer,magazine);
				itemstack.damageItem(1, entityplayer);
			}
			
			
			//Rifle側でdamage処理するコード、謎の不具合により凍結
			/*
			if(itemstack.isEmpty() && flag) {
				itemstack=new ItemStack(UnderGroundMod.Magazine);
			}
			Magazine magazine =(Magazine)(itemstack.getItem() instanceof Magazine ? itemstack.getItem() : UnderGroundMod.Magazine);

			
			if(itemRifle.getDamage()<380 || flag) {
				if(flag) {
					GunFire(entityplayer,magazine);
				}else{
					GunFire(entityplayer,magazine);
					RifleDamage(itemRifle, entityplayer);
				}
			}else if(!itemstack.isEmpty()) {
				itemRifle.setDamage(0);
				itemstack.setDamage(getMaxDamage()-1);
				itemstack.damageItem(1, entityplayer);
			}else {
				//なにもしない
			}
			*/

		}
		super.onUsingTick(itemRifle, entityLiving, count);
	}

	//銃を構える処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn,EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
	
		
		//銃を構える処理
		boolean flag = true;
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
		if (ret != null) return ret;

		if (!playerIn.abilities.isCreativeMode && !flag){
			return !flag ? new ActionResult(EnumActionResult.FAIL, itemstack) : new ActionResult(EnumActionResult.PASS, itemstack);
		}else{
			playerIn.setActiveHand(handIn);
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		}

	}

	public void GunFire(EntityPlayer playerIn,Magazine magazine) {
		//本来は２
		if(launchSpeed>=20) {
			World worldIn = playerIn.world;
//			EntityBullet bullet =magazine.createBullet(worldIn, playerIn);
			EntityLaser bullet =new EntityLaser(worldIn, playerIn);
			bullet.shoot(playerIn,playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3 * 3.0F, 1.0F);

			worldIn.spawnEntity(bullet);
			worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, UnderGroundMod.GunSound, SoundCategory.RECORDS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 3 * 0.5F);
			launchSpeed=0;
		}else {
			launchSpeed++;
		}
	}
	
	protected ItemStack findAmmo(EntityPlayer player) {
		if(this.isBullet(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		}else if(this.isBullet(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}else {
			for(int i=0;i<player.inventory.getSizeInventory();++i) {
				ItemStack itemStack=player.inventory.getStackInSlot(i);
				if(this.isBullet(itemStack)) {
					return itemStack;
				}
			}
			return ItemStack.EMPTY;
		}
	}
	
	protected boolean isBullet(ItemStack stack) {
		return stack.getItem() instanceof Magazine;
	}
	
	protected void RifleDamage(ItemStack stack,EntityPlayer player) {
		stack.damageItem(1, player);
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
