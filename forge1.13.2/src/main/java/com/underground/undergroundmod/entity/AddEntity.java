package com.underground.undergroundmod.entity;

import static com.underground.undergroundmod.ModIdHolder.MODID;
import static com.underground.undergroundmod.UnderGroundMod.EntitySkyRoamer;

import com.underground.undergroundmod.monster.entity.EntitySkyRoamer;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.PlainsBiome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;
import com.underground.undergroundmod.UnderGroundMod;


@ObjectHolder(MODID)
public class AddEntity {
	
	//Entity宣言


	//Entity登録

	
	public static void registerEntityTypes(final RegistryEvent.Register<EntityType<?>> event) {
			
		event.getRegistry().registerAll(
			
				
				);

	}
	
	public static void registerPlacementType(EntityType<?> type,EntitySpawnPlacementRegistry.SpawnPlacementType spawnType) {
		EntitySpawnPlacementRegistry.register(type, spawnType, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,null);
	}
	
	public static void registerPlacementTypes() {
		registerPlacementType(EntitySkyRoamer, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND);
	}
	
	private static void registerEntitySpawn(EntityType<? extends EntityLiving> type,EnumCreatureType creatureType,EntitySpawnPlacementRegistry.SpawnPlacementType spawnType,Biome[] biomes,int weight,int min,int max) {
		for(Biome biome : biomes) {
			if(biome != null) {
				biome.getSpawns(creatureType).add(new Biome.SpawnListEntry(type, weight, min, max));
			}
		}
	}
	
	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.SpawnPlacementType ON_GROUND = EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND;
		registerEntitySpawn((EntityType<? extends EntityLiving>) UnderGroundMod.EntitySkyRoamer,EnumCreatureType.CREATURE,ON_GROUND,new Biome[]{Biomes.PLAINS,Biomes.BADLANDS,Biomes.BEACH,Biomes.JUNGLE},100,4,4);

	}
	
	public static void registerEggs(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(makeSpawnEgg(EntitySkyRoamer,0xffffff,0xff66e2,"SkyRoamer"));
	}
	
	private static Item makeSpawnEgg(EntityType<?> type, int color1,int color2,String name) {
		return new ItemSpawnEgg(type,color1,color2,new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(MODID,name);
	}


	
}

