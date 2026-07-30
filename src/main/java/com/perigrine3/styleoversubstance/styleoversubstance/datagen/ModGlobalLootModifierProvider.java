package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import com.perigrine3.styleoversubstance.styleoversubstance.loot.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Styleoversubstance.MODID);
    }

    @Override
    protected void start() {
        addTrialChambers();
    }

    private void addTrialChambers() {
        //this.add("trial_chamber_vault_qpu",
        //        new AddItemModifier(new LootItemCondition[]{
        //                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("vault/ominous_trial_chamber")).build(),
        //                LootItemRandomChanceCondition.randomChance(0.33f).build()}, ModItems.EXAMPLE_ITEM.get()));
    }
}
