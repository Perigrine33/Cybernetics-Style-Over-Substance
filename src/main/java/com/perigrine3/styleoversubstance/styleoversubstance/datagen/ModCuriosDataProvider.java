package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class ModCuriosDataProvider extends CuriosDataProvider {

    public ModCuriosDataProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(Styleoversubstance.MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        createEntities("player")
                .addPlayer()
                .addSlots(
                        "head",
                        "necklace",
                        "back",
                        "bracelet",
                        "body",
                        "bundle",
                        "quiver",
                        "hands",
                        "ring",
                        "belt",
                        "charm",
                        "pants",
                        "feet"
                );

        createSlot("head")
                .size(1)
                .order(40)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_head_slot"));

        createSlot("necklace")
                .size(1)
                .order(50)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_necklace_slot"));

        createSlot("back")
                .size(1)
                .order(60)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_back_slot"));

        createSlot("body")
                .size(1)
                .order(70)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_body_slot"));

        createSlot("hands")
                .size(1)
                .order(80)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_hands_slot"));

        createSlot("bracelet")
                .size(1)
                .order(90)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_bracelet_slot"));

        createSlot("ring")
                .size(2)
                .order(100)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_ring_slot"));

        createSlot("belt")
                .size(1)
                .order(110)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_belt_slot"));

        createSlot("charm")
                .size(1)
                .order(120)
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/empty_charm_slot"));

        createSlot("pants")
                .size(1)
                .order(130)
                .icon(ResourceLocation.fromNamespaceAndPath("styleoversubstance", "slot/empty_pants_slot"));


        createSlot("feet")
                .size(1)
                .order(140)
                .icon(ResourceLocation.fromNamespaceAndPath("styleoversubstance", "slot/empty_feet_slot"));
    }
}