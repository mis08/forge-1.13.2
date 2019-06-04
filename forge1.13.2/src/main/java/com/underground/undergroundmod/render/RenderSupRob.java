package com.underground.undergroundmod.render;

import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.entity.EntitySupRob;
import com.underground.undergroundmod.model.ModelSupRob;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderSupRob extends RenderLiving<EntitySupRob>{
	private static final ResourceLocation SUPROB_TEXTURES = new ResourceLocation(ModIdHolder.MODID,"textures/entity/suprob.png");
	private static final ModelSupRob modelSupRob = new ModelSupRob();
	
	public RenderSupRob(RenderManager renderManager) {
		super(renderManager,modelSupRob,0.25F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntitySupRob entity) {
		return SUPROB_TEXTURES;
	}

}
