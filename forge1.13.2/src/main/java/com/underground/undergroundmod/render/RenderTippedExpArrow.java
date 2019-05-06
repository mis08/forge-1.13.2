package com.underground.undergroundmod.render;

import javax.annotation.Nullable;

import com.underground.undergroundmod.entity.EntityExpArrow;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTippedExpArrow<T extends EntityExpArrow> extends RenderArrow<T> {
	private static final ResourceLocation TorchTextures = new ResourceLocation("undergroundmod:textures/entity/entitytippedexparrow.png");

	public RenderTippedExpArrow(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Nullable
	protected ResourceLocation getEntityTexture(T entity) {
		return TorchTextures;
	}
}