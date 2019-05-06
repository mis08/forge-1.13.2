package com.underground.undergroundmod;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import com.underground.undergroundmod.entity.EntityExpArrow;
import com.underground.undergroundmod.entity.EntityTippedExpArrow;
import com.underground.undergroundmod.render.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.underground.undergroundmod.item.Itemtest;

import java.util.stream.Collectors;

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
    
    //Entity登録
    public static EntityType EntityExpArrow;
    public static EntityType EntityTippedExpArrow;
    
    //Item登録
    public static Item test = new Itemtest(new Item.Properties().group(tabUnder).defaultMaxDamage(384)).setRegistryName(new ResourceLocation("undergroundmod", "itemtest"));
    public static Item ExpArrow =new Itemtest(new Item.Properties().group(tabUnder).maxStackSize(64)).setRegistryName(new ResourceLocation("undergroundmod", "exparrow"));
    public static Item ExpBow =new Itemtest(new Item.Properties().group(ItemGroup.COMBAT).defaultMaxDamage(384)).setRegistryName(new ResourceLocation("undergroundmod", "expbow"));

    
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
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
        	itemRegister.getRegistry().register(test);
        	itemRegister.getRegistry().register(ExpArrow);
        	itemRegister.getRegistry().register(ExpBow);
        }
        
        @SubscribeEvent
        public static void onEntityTypesRegistry(final RegistryEvent.Register<EntityType<?>> entityRegister) {
        	
        	EntityExpArrow = EntityType.Builder.create(EntityExpArrow.class,EntityExpArrow::new).tracker(60, 5, true).build("undergroundmod" + ":entityexparrow");
        	EntityExpArrow.setRegistryName(new ResourceLocation("undergroundmod", "entityexparrow"));
        	entityRegister.getRegistry().register(EntityExpArrow);
        	
        	EntityTippedExpArrow = EntityType.Builder.create(EntityTippedExpArrow.class,EntityTippedExpArrow::new).tracker(60, 5, true).build("undergroundmod" + ":entitytippedexparrow");
        	EntityTippedExpArrow.setRegistryName(new ResourceLocation("undergroundmod", "entitytippedexparrow"));
        	entityRegister.getRegistry().register(EntityTippedExpArrow);
        	
        }
    }
}

