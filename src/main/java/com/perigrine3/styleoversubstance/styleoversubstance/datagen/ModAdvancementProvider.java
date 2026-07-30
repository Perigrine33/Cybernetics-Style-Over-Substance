package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class ModAdvancementProvider extends AdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new AdvancementsProvider()));
    }

    private static final class AdvancementsProvider implements AdvancementGenerator {

        private static final ResourceLocation MACHINE_BG =
                ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/block/blankmachine.png");

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            //AdvancementHolder root = Advancement.Builder.advancement().display(ModItems.BASECYBERWARE_CYBEREYES,
            //                Component.translatable("advancement.cybernetics"),
            //                Component.translatable("advancement.cybernetics.desc"),
            //                MACHINE_BG, AdvancementType.TASK, true, true, false)
            //        .addCriterion("tick", net.minecraft.advancements.critereon.PlayerTrigger.TriggerInstance.tick())
            //        .save(saver, ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "cyberware/root"), existingFileHelper);

        }
    }
}
