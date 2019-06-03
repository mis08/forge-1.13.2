package com.underground.undergroundmod.item;

import com.underground.undergroundmod.entity.EntityBullet;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Magazine extends Item{
	public Magazine(Item.Properties builder) {
		super(builder);
	}
	
	
	public EntityBullet createBullet(World worldIn,EntityLivingBase playerIn) {
		EntityBullet bullet = new EntityBullet(worldIn,(EntityPlayer)playerIn);
		return bullet;
	}

}
