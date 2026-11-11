package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Styleoversubstance.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.BASIC_FABRIC.get());
        basicItem(ModItems.LEATHER_FABRIC.get());
        basicItem(ModItems.SYNTHWEAVE_FABRIC.get());

        basicItem(ModItems.MODULE_LEDS.get());
        basicItem(ModItems.MODULE_BATTERY.get());
        basicItem(ModItems.MODULE_ARMOREDPLATING.get());
        basicItem(ModItems.MODULE_FIREPROOFGEL.get());
        basicItem(ModItems.MODULE_FARADAYTHREADING.get());

        basicItem(ModItems.GM08.get());
        basicItem(ModItems.TB3.get());





        basicItem(ModItems.SKINUPGRADES_PLASTICCASING.get());
        basicItem(ModItems.SKINUPGRADES_TECHHAIR.get());
        basicItem(ModItems.SKINUPGRADES_SKINWATCH.get());
        basicItem(ModItems.SKINUPGRADES_CHEMSKIN.get());

        basicItem(ModItems.SCAVENGED_PLASTICCASING.get());
        basicItem(ModItems.SCAVENGED_TECHHAIR.get());
        basicItem(ModItems.SCAVENGED_SKINWATCH.get());
        basicItem(ModItems.SCAVENGED_CHEMSKIN.get());

        //withExistingParent(ModItems.RIPPER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}
