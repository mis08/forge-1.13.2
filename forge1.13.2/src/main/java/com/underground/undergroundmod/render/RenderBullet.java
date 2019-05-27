package com.underground.undergroundmod.render;

import javax.annotation.Nullable;

import com.sun.jna.platform.win32.WinUser.FLASHWINFO;
import com.underground.undergroundmod.ModIdHolder;
import com.underground.undergroundmod.entity.EntityBullet;
import com.underground.undergroundmod.model.ModelBullet;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelBoat;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBullet extends Render<EntityBullet>{
	private static final ResourceLocation texture = new ResourceLocation(ModIdHolder.MODID+":textures/entity/bulletmodel.png");
	private final ModelBullet modelBullet = new ModelBullet();

	
	public RenderBullet(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(EntityBullet entity, double x, double y, double z,float entityYaw, float partialTicks) {
		this.bindEntityTexture(entity);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translatef((float)x, (float)y, (float)z);
		GlStateManager.rotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90.0F, 0.0F, 0.0F, 1.0F);
		this.modelBullet.renderer();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.enableLighting();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBullet entity) {
		return texture;
	}

	
}
