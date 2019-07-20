package com.underground.undergroundmod.tileentity.gui;

import com.underground.undergroundmod.ModIdHolder;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGenerator extends GuiContainer{
	
	private static final ResourceLocation GENERATOR_GUI_TEXTURES = new ResourceLocation(ModIdHolder.MODID + ":textures/gui/generator_gui.png");

}
