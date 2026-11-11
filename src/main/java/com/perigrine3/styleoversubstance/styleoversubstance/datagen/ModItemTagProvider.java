package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import com.perigrine3.styleoversubstance.styleoversubstance.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Styleoversubstance.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ModTags.Items.FABRICS)
                .add(ModItems.BASIC_FABRIC.get())
                .add(ModItems.LEATHER_FABRIC.get())
                .add(ModItems.SYNTHWEAVE_FABRIC.get());

        tag(ModTags.Items.CLOTHING_MODS)
                .add(ModItems.MODULE_LEDS.get())
                .add(ModItems.MODULE_BATTERY.get())
                .add(ModItems.MODULE_ARMOREDPLATING.get())
                .add(ModItems.MODULE_FIREPROOFGEL.get())
                .add(ModItems.MODULE_FARADAYTHREADING.get());



        tag(ModTags.Items.HEAD)
                .add(ModItems.BALACLAVA.get())
                .add(ModItems.CAP.get())
                .add(ModItems.VISOR1.get())
                .add(ModItems.GLASSES1.get())
                .add(ModItems.GLASSES2.get())
                .add(ModItems.MONOVISOR.get())
                .add(ModItems.GM08.get())
                .add(ModItems.TB3.get())
                .add(ModItems.MAXTAC_HELMET.get())
                .add(ModItems.TRAUMA_TEAM_HELMET.get())
                .add(ModItems.ONI_MASK.get());

        tag(ModTags.Items.NECKLACE)
                .add(ModItems.MESSY_TIE.get())
                .add(ModItems.TIE.get());


        tag(ModTags.Items.BACK)
                .add(ModItems.LEATHER_JACKET1.get())
                .add(ModItems.EMT_JACKET.get())
                .add(ModItems.HIGH_CROPPED_LEATHER_JACKET.get())
                .add(ModItems.LABCOAT1.get())
                .add(ModItems.LABCOAT2.get())
                .add(ModItems.BAGGY_SWEATSHIRT.get());

        tag(ModTags.Items.BANGLE);

        tag(ModTags.Items.BODY)
                .add(ModItems.NETRUNNING_BODYSUIT.get())
                .add(ModItems.NETRUNNING_UNITARD.get())
                .add(ModItems.MAXTAC_BODYSUIT.get())
                .add(ModItems.TRAUMA_TEAM_PADDED_ARMOR.get());

        tag(ModTags.Items.BUNDLE);

        tag(ModTags.Items.QUIVER);

        tag(ModTags.Items.HANDS);

        tag(ModTags.Items.RING);

        tag(ModTags.Items.BELT);

        tag(ModTags.Items.CHARM);

        tag(ModTags.Items.PANTS)
                .add(ModItems.CAMO_PANTS.get());

        tag(ModTags.Items.FEET);




        tag(com.perigrine3.createcybernetics.util.ModTags.Items.CYBERWARE_ITEM)
                .add(ModItems.SKINUPGRADES_PLASTICCASING.get())
                .add(ModItems.SKINUPGRADES_TECHHAIR.get())
                .add(ModItems.SKINUPGRADES_SKINWATCH.get())
                .add(ModItems.SKINUPGRADES_CHEMSKIN.get());

        tag(com.perigrine3.createcybernetics.util.ModTags.Items.SCAVENGED_CYBERWARE)
                .add(ModItems.SCAVENGED_PLASTICCASING.get())
                .add(ModItems.SCAVENGED_TECHHAIR.get())
                .add(ModItems.SCAVENGED_SKINWATCH.get())
                .add(ModItems.SCAVENGED_CHEMSKIN.get());

        tag(com.perigrine3.createcybernetics.util.ModTags.Items.SKIN_ITEMS)
                .add(ModItems.SKINUPGRADES_PLASTICCASING.get())
                .add(ModItems.SKINUPGRADES_TECHHAIR.get())
                .add(ModItems.SKINUPGRADES_SKINWATCH.get())
                .add(ModItems.SKINUPGRADES_CHEMSKIN.get());

        tag(com.perigrine3.createcybernetics.util.ModTags.Items.SKIN_UPGRADES)
                .add(ModItems.SKINUPGRADES_PLASTICCASING.get())
                .add(ModItems.SKINUPGRADES_TECHHAIR.get())
                .add(ModItems.SKINUPGRADES_SKINWATCH.get())
                .add(ModItems.SKINUPGRADES_CHEMSKIN.get());
    }
}