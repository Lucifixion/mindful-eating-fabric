//package com.minecraftabnormals.mindful_eating.core;
//
//import com.minecraftabnormals.mindful_eating.client.ClientSetup;
//import com.minecraftabnormals.mindful_eating.compat.AppleskinCompat;
//import fuzs.forgeconfigapiport.api.config.v3.ForgeConfigRegistry;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.fml.ModList;
//import net.minecraftforge.fml.ModLoadingContext;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.config.ModConfig;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//public class MindfulEating
//{
//    public static final String MODID = "mindful_eating";
//
//    public static final Logger LOGGER = LogManager.getLogger(MODID);
//
//
//
//    public MindfulEating() {
//
//        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
//        bus.addListener(ClientSetup::init);
//
//        MinecraftForge.EVENT_BUS.register(this);
//
//        if (ModList.get().isLoaded("appleskin")) {
//            MinecraftForge.EVENT_BUS.register(AppleskinCompat.class);
//        }
//
//
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MEConfig.COMMON_SPEC);
//    }
//}
