package com.underground.undergroundmod.item;

import com.underground.undergroundmod.UnderGroundMod;
import com.underground.undergroundmod.entity.EntityLaser;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class LaserGun extends AutomaticRifle{
	
	public boolean FireFlag;
	public LaserGun(Item.Properties builder) {
		super(builder);
	}
	
	@Override
	public void onUsingTick(ItemStack itemRifle, EntityLivingBase entityLiving,
			int count) {
		//なにもしない
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn,
			EntityPlayer playerIn, EnumHand handIn) {
		
		//二回呼び出されて二度Fireするの防止
		//二度目のFireはブロックを透明にするだけで消さない
		if(FireFlag) {
			
			boolean flag=playerIn.abilities.isCreativeMode;
			ItemStack itemstack =this.findAmmo(playerIn);
			
			if(!itemstack.isEmpty()||flag) {
				if(itemstack.isEmpty()) {
					itemstack=new ItemStack(UnderGroundMod.PowerCell);
				}
				PowerCell powercell =(PowerCell)(itemstack.getItem() instanceof PowerCell ? itemstack.getItem():UnderGroundMod.PowerCell);
				GunFire(playerIn,powercell);
				itemstack.damageItem(1, playerIn);
			}
			
		
			FireFlag=false;
		}else {
			FireFlag=true;
		}
		
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public void GunFire(EntityPlayer playerIn,PowerCell powerCell) {
			World worldIn = playerIn.world;
			EntityLaser laser =powerCell.createLaser(worldIn, playerIn);
			laser.shoot(playerIn,playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.7F, 1.0F);
			worldIn.spawnEntity(laser);
			worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, UnderGroundMod.LaserGunSound, SoundCategory.RECORDS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 3 * 0.5F);
	}
	
	@Override
	protected ItemStack findAmmo(EntityPlayer player) {
		if(this.isPowerCell(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		}else if(this.isPowerCell(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}else {
			for(int i=0;i<player.inventory.getSizeInventory();++i) {
				ItemStack itemStack=player.inventory.getStackInSlot(i);
				if(this.isPowerCell(itemStack)) {
					return itemStack;
				}
			}
			return ItemStack.EMPTY;
		}
	}
	
	protected boolean isPowerCell(ItemStack stack) {
		return stack.getItem() instanceof PowerCell;
	}

}
