package com.underground.undergroundmod.item;

import com.underground.undergroundmod.entity.EntityLaser;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PowerCell extends Item{

	public PowerCell(Properties builder) {
		super(builder);
		
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public EntityLaser createLaser(World world,EntityLivingBase player) {
		EntityLaser laser=new EntityLaser(world,(EntityPlayer)player);
		return laser;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
