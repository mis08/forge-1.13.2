package com.underground.undergroundmod;


import static com.underground.undergroundmod.ModIdHolder.MODID;

import net.minecraft.block.Block;
import net.minecraft.client.audio.Sound;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.structure.StructureIO;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import com.underground.undergroundmod.render.*;
import com.underground.undergroundmod.structures.AddBiomeFeature;
import com.underground.undergroundmod.item.*;
import com.underground.undergroundmod.monster.*;
import com.underground.undergroundmod.monster.render.RenderSkyRoamer;
import com.underground.undergroundmod.monster.entity.*;
import com.google.common.base.Joiner.MapJoiner;
import com.underground.undergroundmod.entity.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.xml.pull.EntityReplacementMap;

import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;
import javax.xml.stream.events.EntityReference;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("undergroundmod")
public class UnderGroundMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public UnderGroundMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        
        
    }
    
       //tab登録
    public static ItemGroup tabUnder = (new ItemGroup("tabUnder") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.IRON_PICKAXE);
        }
    }); 
    
    //Entity作成
    public static EntityType<?> EntityExpArrow =EntityType.Builder.create(EntityExpArrow.class,EntityExpArrow::new).tracker(60, 5, true).build(MODID+":entityexparrow").setRegistryName(new ResourceLocation( MODID,"entityexparrow"));
	public static EntityType<?> EntityTippedExpArrow = EntityType.Builder.create(EntityTippedExpArrow.class,EntityTippedExpArrow::new).tracker(60,5,true).build(MODID+":entitytippedexparrow").setRegistryName(new ResourceLocation(MODID,"entitytippedexparrow"));
	public static EntityType<?> EntitySkyRoamer = EntityType.Builder.create(EntitySkyRoamer.class,EntitySkyRoamer::new).tracker(40, 5, true).build(MODID+":entityskyroamer").setRegistryName(new ResourceLocation(MODID,"entityskyroamer"));
	public static EntityType<?> EntityBullet =EntityType.Builder.create(EntityBullet.class,EntityBullet::new).tracker(60, 1, true).build(MODID+":entitybullet").setRegistryName(new ResourceLocation(MODID,"entitybullet"));
	public static EntityType<?> EntitySupRob = EntityType.Builder.create(EntitySupRob.class,EntitySupRob::new).tracker(60,1,true).build(MODID+":entitysuprob").setRegistryName(new ResourceLocation(MODID,"entitysuprob"));
	
	
	public static EntityType<EntitySupRob> SUPROB = EntityType.register("suprob",EntityType.Builder.create(EntitySupRob.class, EntitySupRob::new));
    
    //Itemプロパティ作成
    public static Item test = new Itemtest(new Item.Properties().group(tabUnder).maxStackSize(64)).setRegistryName(new ResourceLocation(MODID, "itemtest"));
    public static Item ExpArrow =new ExpArrow(new Item.Properties().group(tabUnder).maxStackSize(64)).setRegistryName(new ResourceLocation(MODID, "exparrow"));
    public static Item ExpBow =new ExpBow(new Item.Properties().group(tabUnder).defaultMaxDamage(384)).setRegistryName(new ResourceLocation(MODID, "expbow"));
    public static Item SpawnSkyRoamer =new SpawnSkyRoamer(new Item.Properties().group(tabUnder).defaultMaxDamage(384)).setRegistryName(new ResourceLocation(MODID, "spawnskyroamer"));
    public static Item AutomaticRifle =new AutomaticRifle(new Item.Properties().group(tabUnder).defaultMaxDamage(384)).setRegistryName(new ResourceLocation(MODID, "automaticrifle"));
    public static Item Magazine =new Magazine(new Item.Properties().group(tabUnder).defaultMaxDamage(384)).setRegistryName(new ResourceLocation(MODID,"magazine"));
    public static Item SpawnSupRob =new ItemSpawnEgg(SUPROB,9999999,9999999,(new Item.Properties().group(tabUnder).defaultMaxDamage(384))).setRegistryName(new ResourceLocation(MODID, "spawnsuprob"));
    public static Item Circuit =new Circuit(new Item.Properties().group(tabUnder).maxStackSize(64)).setRegistryName(new ResourceLocation(MODID,"circuit"));
    public static Item RobotConnecter =new RobotConnecter(new Item.Properties().group(tabUnder).defaultMaxDamage(384)).setRegistryName(new ResourceLocation(MODID,"robotconnecter"));
    
    
    //Entity変更
	public static final ResourceLocation ENTITIES_SKYROAMER = LootTableList.register(new ResourceLocation(ModIdHolder.MODID,"inject/skyroamer"));
	public static final ResourceLocation ENTITIES_CREEPER = LootTableList.register(new ResourceLocation(ModIdHolder.MODID,"inject/more_creeper"));
	public static final ResourceLocation SPAWN_BONUS_CHEST = LootTableList.register(new ResourceLocation(ModIdHolder.MODID,"inject/spawn_bonus_chest"));

	
	//Sound作成
	public static final SoundEvent GunSound = new SoundEvent(new ResourceLocation(MODID,"gunsound")).setRegistryName(new ResourceLocation(MODID,"gunsound"));
	public static final SoundEvent R2D2flat = new SoundEvent(new ResourceLocation(MODID,"r2d2_flat")).setRegistryName(new ResourceLocation(MODID,"r2d2_flat"));
	public static final SoundEvent R2D2Scream = new SoundEvent(new ResourceLocation(MODID,"r2d2_scream")).setRegistryName(new ResourceLocation(MODID,"r2d2_scream"));
	public static final SoundEvent R2D2Beap = new SoundEvent(new ResourceLocation(MODID,"r2d2_beap")).setRegistryName(new ResourceLocation(MODID,"r2d2_beap"));
    
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        
    	//MobEntity設定
        AddEntity.registerPlacementTypes();
        AddEntity.registerEntitySpawns();
        
        AddBiomeFeature.add();
        
        
        

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        
        //EntityRender登録
        AddRender.registerRenders();
        

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    

    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegister) {
        	//Item登録
        	itemRegister.getRegistry().registerAll(
        			//test,
        			ExpArrow,
        			ExpBow,
        			SpawnSkyRoamer,
        			AutomaticRifle,
        			Magazine,
        			SpawnSupRob,
        			Circuit,
        			RobotConnecter
        	);
        }
       
        @SubscribeEvent
        public static void onEntityTypesRegistry(final RegistryEvent.Register<EntityType<?>> entityRegister) {
        	//Entity登録
        	entityRegister.getRegistry().registerAll(
        			EntityExpArrow,
        			EntityTippedExpArrow,
        			EntitySkyRoamer,
        			EntityBullet,
        			EntitySupRob
        			);
        	//EntityMonsterタイプ登録
        	AddEntity.entityTypeRegister();
        } 
        
        @SubscribeEvent
        public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> soundRegister) {
        	//Sound登録
        	//resources/assets/undergroundmod/ にあるsounds.json　にも追加記載すること
        	soundRegister.getRegistry().registerAll(
        			GunSound,
        			R2D2flat,
        			R2D2Scream,
        			R2D2Beap
        			);
        }
   
    }
}

