package com.underground.undergroundmod.monster.render;

import com.underground.undergroundmod.monster.model.ModelSkyRoamer;
import com.underground.undergroundmod.monster.entity.*;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPhantom;
import net.minecraft.client.renderer.entity.layers.LayerPhantomEyes;
import net.minecraft.client.renderer.entity.model.ModelPhantom;
import net.minecraft.entity.monster.EntityPhantom;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSkyRoamer extends RenderLiving<EntitySkyRoamer>{
	   private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("undergroundmod:textures/entity/skyroamer.png");

	   public RenderSkyRoamer(RenderManager p_i48829_1_) {
	      super(p_i48829_1_, new ModelSkyRoamer(), 0.75F);
	      
	      this.addLayer(new LayerSkyRoamer(this));
	   }
	   

	   /**
	    * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	    */
	   protected ResourceLocation getEntityTexture(EntityPhantom entity) {
	      return PHANTOM_LOCATION;
	   }

	   /**
	    * Allows the render to do state modifications necessary before the model is rendered.
	    */
	   protected void preRenderCallback(EntitySkyRoamer entitylivingbaseIn, float partialTickTime) {
	      int i = entitylivingbaseIn.getPhantomSize();
	      float f = 1.0F + 0.15F * (float)i;
	      GlStateManager.scalef(f, f, f);
	      GlStateManager.translatef(0.0F, 1.3125F, 0.1875F);
	   }

	   protected void applyRotations(EntitySkyRoamer entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
	      super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
	      GlStateManager.rotatef(entityLiving.rotationPitch, 1.0F, 0.0F, 0.0F);
	   }


	@Override
	protected ResourceLocation getEntityTexture(EntitySkyRoamer entity) {
		// TODO 自動生成されたメソッド・スタブ
		return PHANTOM_LOCATION;
	}
}
