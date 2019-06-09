package com.underground.undergroundmod.structures;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.WithChance;

public class AddBiomeFeature {
	
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


	
	public static void add() {
		for(Biome b : AllBiome) {
			b.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createCompositeFeature(new Artifact(),IFeatureConfig.NO_FEATURE_CONFIG,Biome.WITH_CHANCE,new ChanceConfig(200)));
		}
	}
}
