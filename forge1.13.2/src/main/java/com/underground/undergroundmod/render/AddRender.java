package com.underground.undergroundmod.render;

import com.underground.undergroundmod.entity.EntityBullet;
import com.underground.undergroundmod.entity.EntityExpArrow;
import com.underground.undergroundmod.entity.EntitySupRob;
import com.underground.undergroundmod.entity.EntityTippedExpArrow;
import com.underground.undergroundmod.monster.entity.EntitySkyRoamer;
import com.underground.undergroundmod.monster.render.RenderSkyRoamer;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class AddRender {

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityExpArrow.class, RenderExpArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTippedExpArrow.class, RenderTippedExpArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySkyRoamer.class, RenderSkyRoamer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, RenderBullet::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySupRob.class, RenderSupRob::new);
	}
} 
