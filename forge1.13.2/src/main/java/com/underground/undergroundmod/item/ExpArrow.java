package com.underground.undergroundmod.item;


import com.underground.undergroundmod.entity.EntityTippedExpArrow;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExpArrow extends ItemArrow{

	public ExpArrow(Properties builder) {
		super(builder);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase base) {
		EntityTippedExpArrow entitytippedexparrow = new EntityTippedExpArrow(worldIn, base);
		entitytippedexparrow.setPotionEffect(stack);
		return entitytippedexparrow;
   }

}