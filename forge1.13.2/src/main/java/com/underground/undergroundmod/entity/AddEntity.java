package com.underground.undergroundmod.entity;

import static com.underground.undergroundmod.ModIdHolder.MODID;
import static com.underground.undergroundmod.UnderGroundMod.EntitySkyRoamer;
import static com.underground.undergroundmod.UnderGroundMod.EntitySupRob;

import com.underground.undergroundmod.monster.entity.EntitySkyRoamer;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.EntityType;
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
	
	//AllBiomes
	//Not END,MUSHROOM,NETHER
	public static final Biome[] AllBiome= {Biomes.BADLANDS,Biomes.BADLANDS_PLATEAU,Biomes.BEACH,Biomes.BIRCH_FOREST,
			Biomes.BIRCH_FOREST_HILLS,Biomes.COLD_OCEAN,Biomes.DARK_FOREST,Biomes.DARK_FOREST_HILLS,Biomes.DEEP_COLD_OCEAN,Biomes.DEEP_FROZEN_OCEAN,
			Biomes.DEEP_LUKEWARM_OCEAN,Biomes.DEEP_OCEAN,Biomes.DEEP_WARM_OCEAN,Biomes.DEFAULT,Biomes.DESERT,Biomes.DESERT_HILLS,Biomes.DESERT_LAKES,
			Biomes.ERODED_BADLANDS,Biomes.FLOWER_FOREST,Biomes.FOREST,Biomes.FROZEN_OCEAN,Biomes.FROZEN_RIVER,Biomes.GIANT_SPRUCE_TAIGA,
			Biomes.GIANT_SPRUCE_TAIGA_HILLS,Biomes.GIANT_TREE_TAIGA,Biomes.GIANT_TREE_TAIGA_HILLS,Biomes.GRAVELLY_MOUNTAINS,Biomes.ICE_SPIKES,
			Biomes.JUNGLE,Biomes.JUNGLE_EDGE,Biomes.JUNGLE_HILLS,Biomes.LUKEWARM_OCEAN,Biomes.MODIFIED_BADLANDS_PLATEAU,Biomes.MODIFIED_GRAVELLY_MOUNTAINS,
			Biomes.MODIFIED_JUNGLE,Biomes.MODIFIED_JUNGLE_EDGE,Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU,Biomes.MOUNTAIN_EDGE,Biomes.MOUNTAINS,
			Biomes.OCEAN,Biomes.PLAINS,Biomes.RIVER,Biomes.SAVANNA,Biomes.SAVANNA_PLATEAU,Biomes.SHATTERED_SAVANNA,Biomes.SHATTERED_SAVANNA_PLATEAU,
			Biomes.SNOWY_BEACH,Biomes.SNOWY_MOUNTAINS,Biomes.SNOWY_TAIGA,Biomes.SNOWY_TAIGA_HILLS,Biomes.SNOWY_TAIGA_MOUNTAINS,Biomes.SNOWY_TUNDRA,
			Biomes.STONE_SHORE,Biomes.SUNFLOWER_PLAINS,Biomes.SWAMP,Biomes.SWAMP_HILLS,Biomes.TAIGA,Biomes.TAIGA_HILLS,Biomes.TAIGA_MOUNTAINS,
			Biomes.TALL_BIRCH_FOREST,Biomes.TALL_BIRCH_HILLS,Biomes.WARM_OCEAN,Biomes.WOODED_BADLANDS_PLATEAU,Biomes.WOODED_HILLS,Biomes.WOODED_MOUNTAINS
			};
	
	//Entity宣言
	public static  EntityType<EntitySkyRoamer> SKYROAMER;
	public static EntityType<EntitySupRob> SUPROB = EntityType.register("suprob",EntityType.Builder.create(EntitySupRob.class, EntitySupRob::new));
	
		//Entity登録
	public static void entityTypeRegister() {
		SKYROAMER = EntityType.register("skyroamer",EntityType.Builder.create(EntitySkyRoamer.class, EntitySkyRoamer::new));

	}
	
	
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
		registerEntitySpawn((EntityType<? extends EntityLiving>) UnderGroundMod.EntitySkyRoamer,EnumCreatureType.MONSTER,ON_GROUND,AllBiome,50,4,4);

	}
	
	public static void registerEggs(final RegistryEvent.Register<Item> event) {
	//	event.getRegistry().register(makeSpawnEgg(EntitySkyRoamer,0xffffff,0xff66e2,"SkyRoamer"));
		event.getRegistry().register(makeSpawnEgg(EntitySupRob,0xffffff,0xff66e2,"SpawnSupRob"));
		
	}
	
	private static Item makeSpawnEgg(EntityType<?> type, int color1,int color2,String name) {
		return new ItemSpawnEgg(type,color1,color2,new Item.Properties().group(UnderGroundMod.tabUnder)).setRegistryName(MODID,name);
	}


	
}

