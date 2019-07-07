package com.underground.undergroundmod.irecipe;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public class MoreRecipeHolder {
	public static Map<ResourceLocation, JsonObject> more = new HashMap<>();
	
	public  static void setMore(ResourceLocation rl,JsonObject jo) {
		more.put(rl, jo);
	}
	
	public  static JsonObject getJsonObject(ResourceLocation rl) {
		return more.get(rl);
	}
	
}
