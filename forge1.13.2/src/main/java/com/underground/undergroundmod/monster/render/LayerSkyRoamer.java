package com.underground.undergroundmod.monster.render;

import com.underground.undergroundmod.monster.render.RenderSkyRoamer;
import com.underground.undergroundmod.monster.entity.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPhantom;
import net.minecraft.client.renderer.entity.layers.LayerPhantomEyes;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityPhantom;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerSkyRoamer implements LayerRenderer<EntitySkyRoamer>{
	   private static final ResourceLocation field_204248_a = new ResourceLocation("undergroundmod:textures/entity/skyroamer_eyes.png");
	   private final RenderSkyRoamer phantomRenderer;

	   public LayerSkyRoamer(RenderSkyRoamer phantomRenderer) {
		      this.phantomRenderer = phantomRenderer;
		   }

		   public void render(EntitySkyRoamer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		      this.phantomRenderer.bindTexture(field_204248_a);
		      GlStateManager.enableBlend();
		      GlStateManager.disableAlphaTest();
		      GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		      GlStateManager.disableLighting();
		      GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
		      int i = 61680;
		      int j = 61680;
		      int k = 0;
		      OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, 61680.0F, 0.0F);
		      GlStateManager.enableLighting();
		      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		      Minecraft.getInstance().entityRenderer.setupFogColor(true);
		      this.phantomRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		      Minecraft.getInstance().entityRenderer.setupFogColor(false);
		      this.phantomRenderer.setLightmap(entitylivingbaseIn);
		      GlStateManager.depthMask(true);
		      GlStateManager.disableBlend();
		      GlStateManager.enableAlphaTest();
		   }

		   public boolean shouldCombineTextures() {
		      return false;
		   }

}
