package com.underground.undergroundmod.structures;

import static com.underground.undergroundmod.ModIdHolder.MODID;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftPieces;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

public class Artifact extends Feature<NoFeatureConfig>{
    @Override
    public boolean func_212245_a(IWorld worldIn, IChunkGenerator<? extends IChunkGenSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int posX=pos.getX();
        int posY=30;
//        int posY=150+rand.nextInt(25)+rand.nextInt(25)+rand.nextInt(25);
        int posZ=pos.getZ();

        Template structureTemplate = worldIn.getSaveHandler().getStructureTemplateManager().getTemplateDefaulted(new ResourceLocation(MODID,"artifact"));
        structureTemplate.addBlocksToWorld(worldIn,new BlockPos(posX,posY,posZ),new PlacementSettings().setReplacedBlock(Blocks.STONE).setRotation(Rotation.NONE).setMirror(Mirror.NONE));
        structureTemplate.getSize();
        ArrayList<TileEntityChest> chests = new ArrayList<>();
        BlockPos size = structureTemplate.getSize();
        for(int x=0;x<=size.getX();x++){
            for(int y=0;y<=size.getY();y++){
                for(int z=0;z<=size.getZ();z++){
                    BlockPos tmp = new BlockPos(posX+x,posY+y,posZ+z);
                    if(worldIn.getTileEntity(tmp)!=null){
                        if(worldIn.getTileEntity(tmp) instanceof TileEntityChest){
                            chests.add((TileEntityChest) worldIn.getTileEntity(tmp));
                        }
                    }
                }
            }
        }
        //fill chests
        for(TileEntityChest chest:chests){
            ArrayList<ItemStack> rewards = new ArrayList<>();
//            rewards = RewardHelper.getCloudMiniTempleRewards(rand);
            for(int i = 0;i<=rewards.size();i++) {
//                chest.setInventorySlotContents(rand.nextInt(15), rewards.get(rand.nextInt(rewards.size())));
            }
        }
        return true;
    }

}
