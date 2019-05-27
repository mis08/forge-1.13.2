package com.underground.undergroundmod.model;

import com.underground.undergroundmod.ModIdHolder;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

public class ModelBullet extends ModelBase {
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ModIdHolder.MODID,"textures/entity/bulletmodel.png");
    public ModelRenderer shape1;

    public ModelBullet() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
    }

    public void renderer() { 
        this.shape1.render(0.0625F);
    }

}
