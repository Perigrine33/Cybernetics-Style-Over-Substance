package com.perigrine3.styleoversubstance.styleoversubstance;

import com.mojang.logging.LogUtils;
import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import com.perigrine3.styleoversubstance.styleoversubstance.block.entity.ModBlockEntities;
import com.perigrine3.styleoversubstance.styleoversubstance.client.colors.StyleItemColors;
import com.perigrine3.styleoversubstance.styleoversubstance.component.ModDataComponents;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.ModMenuTypes;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.screen.GarmentTableScreen;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModCreativeModeTabs;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.ModRecipes;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Styleoversubstance.MODID)
public class Styleoversubstance {

    public static final String MODID = "styleoversubstance";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Styleoversubstance(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModRecipes.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModDataComponents.register(eventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == com.perigrine3.createcybernetics.item.ModCreativeModeTabs.CREATE_CYBERNETICS_UPGRADES_TAB) {

            event.insertAfter(
                    com.perigrine3.createcybernetics.item.ModItems.SKINUPGRADES_SYNTHSKIN.get().getDefaultInstance(),
                    ModItems.SKINUPGRADES_PLASTICCASING.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );

            event.insertAfter(
                    ModItems.SKINUPGRADES_PLASTICCASING.get().getDefaultInstance(),
                    ModItems.SKINUPGRADES_TECHHAIR.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );

            event.insertAfter(
                    ModItems.SKINUPGRADES_TECHHAIR.get().getDefaultInstance(),
                    ModItems.SKINUPGRADES_SKINWATCH.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );

            event.insertAfter(
                    ModItems.SKINUPGRADES_SKINWATCH.get().getDefaultInstance(),
                    ModItems.SKINUPGRADES_CHEMSKIN.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }

        if (event.getTabKey() == com.perigrine3.createcybernetics.item.ModCreativeModeTabs.CREATE_CYBERNETICS_TAB) {

            event.insertAfter(
                    com.perigrine3.createcybernetics.item.ModItems.SCAVENGED_SYNTHSKIN.get().getDefaultInstance(),
                    ModItems.SCAVENGED_PLASTICCASING.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );

            event.insertAfter(
                    ModItems.SCAVENGED_PLASTICCASING.get().getDefaultInstance(),
                    ModItems.SCAVENGED_TECHHAIR.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );

            event.insertAfter(
                    ModItems.SCAVENGED_TECHHAIR.get().getDefaultInstance(),
                    ModItems.SCAVENGED_SKINWATCH.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );

            event.insertAfter(
                    ModItems.SCAVENGED_SKINWATCH.get().getDefaultInstance(),
                    ModItems.SCAVENGED_CHEMSKIN.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //EntityRenderers.register(ModEntities.NUGGET_PROJECTILE.get(), NuggetProjectileRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.GARMENT_TABLE.get(), GarmentTableScreen::new);
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            StyleItemColors.registerItemColors(event);
        }
    }
}