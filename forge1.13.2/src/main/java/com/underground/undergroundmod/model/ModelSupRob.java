package com.underground.undergroundmod.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSupRob extends ModelBase{
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer mainfod;
    public ModelRenderer rightfod;
    public ModelRenderer leftfod;
    public ModelRenderer shape21;
    public ModelRenderer shape23;
    public ModelRenderer shape25;

    public ModelSupRob() {
        this.textureWidth = 256;
        this.textureHeight = 128;
        this.head = new ModelRenderer(this, 40, 0);
        this.head.setRotationPoint(-0.2F, -0.6F, 0.0F);
        this.head.addBox(-4.4F, -7.4F, -4.5F, 9, 8, 9, 0.0F);
        this.shape21 = new ModelRenderer(this, 108, 0);
        this.shape21.setRotationPoint(0.1F, 2.9F, -0.3F);
        this.shape21.addBox(-2.3F, -0.1F, -4.6F, 5, 3, 6, 0.0F);
        this.setRotateAngle(shape21, 0.27314402793711257F, 0.0F, 0.0F);
        this.rightfod = new ModelRenderer(this, 76, 0);
        this.rightfod.setRotationPoint(-7.1F, 2.6F, -4.9F);
        this.rightfod.addBox(0.0F, 0.0F, 0.0F, 2, 17, 6, 0.0F);
        this.setRotateAngle(rightfod, 0.5462880558742251F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(-0.2F, 11.9F, 2.0F);
        this.body.addBox(-5.1F, 0.0F, -4.9F, 10, 15, 10, 0.0F);
        this.setRotateAngle(body, -0.27314402793711257F, 0.0F, 0.0F);
        this.leftfod = new ModelRenderer(this, 92, 0);
        this.leftfod.setRotationPoint(4.9F, 2.6F, -5.0F);
        this.leftfod.addBox(0.0F, 0.0F, 0.0F, 2, 17, 6, 0.0F);
        this.setRotateAngle(leftfod, 0.5462880558742251F, 0.0F, 0.0F);
        this.shape25 = new ModelRenderer(this, 152, 0);
        this.shape25.setRotationPoint(0.0F, 12.8F, 0.0F);
        this.shape25.addBox(0.0F, 0.0F, 0.0F, 3, 4, 8, 0.0F);
        this.setRotateAngle(shape25, -0.27314402793711257F, 0.0F, 0.0F);
        this.mainfod = new ModelRenderer(this, 0, 0);
        this.mainfod.setRotationPoint(-0.5F, 13.1F, -3.7F);
        this.mainfod.addBox(-1.3F, 0.0F, -0.8F, 3, 4, 2, 0.0F);
        this.shape23 = new ModelRenderer(this, 130, 0);
        this.shape23.setRotationPoint(-1.1F, 12.7F, 0.2F);
        this.shape23.addBox(0.0F, 0.0F, 0.0F, 3, 4, 8, 0.0F);
        this.setRotateAngle(shape23, -0.27314402793711257F, 0.0F, 0.0F);
        this.body.addChild(this.head);
        this.mainfod.addChild(this.shape21);
        this.body.addChild(this.rightfod);
        this.body.addChild(this.leftfod);
        this.leftfod.addChild(this.shape25);
        this.body.addChild(this.mainfod);
        this.rightfod.addChild(this.shape23);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.body.offsetX, this.body.offsetY, this.body.offsetZ);
        GlStateManager.translatef(this.body.rotationPointX * f5, this.body.rotationPointY * f5, this.body.rotationPointZ * f5);
        GlStateManager.scaled(0.7D, 0.7D, 0.7D);
        GlStateManager.translatef(-this.body.offsetX, -this.body.offsetY, -this.body.offsetZ);
        GlStateManager.translatef(-this.body.rotationPointX * f5, -this.body.rotationPointY * f5, -this.body.rotationPointZ * f5);
        this.body.render(f5);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
