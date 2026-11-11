package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.compat.ModCompats;
import com.perigrine3.createcybernetics.compat.curios.CuriosCompat;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.api.StyleClothingApi;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Central manager for all skin modifications in the game.
 * This class determines which skin modifiers should be active for each player.
 *
 * How to Add New Skin Modifications:
 * 1. Define your texture constant:
 *    private static final ResourceLocation YOUR_SKIN_TEXTURE = 
 *        new ResourceLocation(CreateCybernetics.MODID, "textures/entity/your_texture.png");
 *
 * 2. Create a SkinModifier instance:
 *    private static final SkinModifier YOUR_MODIFIER = new SkinModifier(
 *        YOUR_SKIN_TEXTURE,
 *        FastColor.ARGB32.color(255, r, g, b), // Color tint (optional)
 *        hideVanillaLayers // true/false
 *    );
 *
 * 3. Add your condition in getPlayerSkinState:
 *    // For cyberware-based modifications:
 *    if (data.hasAnyTagged(ModTags.Items.YOUR_ITEMS, CyberwareSlot.YOUR_SLOT)) {
 *        state.addModifier(YOUR_MODIFIER);
 *    }
 *
 *    // For effect-based modifications:
 *    if (player.hasEffect(ModEffects.YOUR_EFFECT)) {
 *        state.addModifier(YOUR_MODIFIER);
 *    }
 *
 *    // For conditional modifications:
 *    if (shouldApplyCondition(player)) {
 *        state.addModifier(YOUR_MODIFIER);
 *    }
 *
 * Example Implementations:
 * 1. Metallic Skin Overlay:
 *    private static final ResourceLocation METALLIC_SKIN = 
 *        new ResourceLocation(CreateCybernetics.MODID, "textures/entity/metallic_skin.png");
 *    private static final SkinModifier METALLIC_MODIFIER = new SkinModifier(
 *        METALLIC_SKIN,
 *        FastColor.ARGB32.color(255, 192, 192, 192),
 *        false
 *    );
 *
 * 2. Glowing Circuits:
 *    private static final ResourceLocation CIRCUIT_PATTERN = 
 *        new ResourceLocation(CreateCybernetics.MODID, "textures/entity/circuit_pattern.png");
 *    private static final SkinModifier CIRCUIT_MODIFIER = new SkinModifier(
 *        CIRCUIT_PATTERN,
 *        FastColor.ARGB32.color(255, 0, 255, 255),
 *        false
 *    );
 *
 * Important Notes:
 * - Modifiers are rendered in the order they're added to the state
 * - Later modifiers will render on top of earlier ones
 * - Use alpha channels in textures for partial transparency
 * - Color tints can be used to create variations of the same texture
 * - Consider performance when adding multiple modifiers
 */
public class SkinModifierManager {
    private static final Map<UUID, SkinModifierState> PLAYER_STATES = new HashMap<>();

    // ---- MERMOD COMPONENT-BASED MODIFIER DETECTION (soft compat) ----
    private static final ResourceLocation MERMOD_SEA_NECKLACE_ID = ResourceLocation.fromNamespaceAndPath("mermod", "sea_necklace");

    private static DataComponentType<?> MERMOD_NECKLACE_MODIFIERS_COMPONENT;
    private static boolean MERMOD_REFLECTION_READY = false;
    private static boolean MERMOD_REFLECTION_FAILED = false;
    private static Method MERMOD_COMPONENT_MODIFIERS_METHOD;
    private static Method MERMOD_MODIFIER_ID_METHOD;



    //INTERCHANGEABLES
    private static final ResourceLocation BALACLAVA =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/balaclava.png");
    private static final ResourceLocation BALACLAVA_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/balaclava_dye1.png");
    private static final ResourceLocation BALACLAVA_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/balaclava_dye2.png");
    private static final ResourceLocation GLASSES =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/glasses.png");
    private static final ResourceLocation MONOVISOR =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/monovisor.png");
    private static final ResourceLocation MONOVISOR_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/monovisor_dye1.png");
    private static final ResourceLocation MONOVISOR_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/monovisor_dye2.png");
    private static final ResourceLocation GM08 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/gm08.png");
    private static final ResourceLocation TB3 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/tb3.png");
    private static final ResourceLocation NET_UNITARD =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_unitard.png");
    private static final ResourceLocation NET_UNITARD_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_unitard_dye1.png");
    private static final ResourceLocation NET_UNITARD_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_unitard_dye2.png");
    private static final ResourceLocation MESSY_TIE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/messy_tie.png");
    private static final ResourceLocation TIE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/tie.png");
    private static final ResourceLocation MAXTAC_HELMET =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_helmet.png");
    private static final ResourceLocation MAXTAC_HELMET_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_helmet_dye1.png");
    private static final ResourceLocation MAXTAC_HELMET_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_helmet_dye2.png");
    private static final ResourceLocation TRAUMA_TEAM_HELMET =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_helmet.png");
    private static final ResourceLocation TRAUMA_TEAM_HELMET_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_helmet_dye1.png");
    private static final ResourceLocation TRAUMA_TEAM_HELMET_HIGHLIGHT =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_helmet_highlight.png");
    private static final ResourceLocation CAMO_PANTS =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/camo_pants.png");
    private static final ResourceLocation CAMO_PANTS_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/camo_pants_dye1.png");
    private static final ResourceLocation CAMO_PANTS_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/camo_pants_dye2.png");
    private static final ResourceLocation ONI_MASK =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/oni_mask.png");
    private static final ResourceLocation ONI_MASK_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/oni_mask_dye1.png");
    private static final ResourceLocation ONI_MASK_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/oni_mask_dye2.png");


    //WIDE VARIANTS
    private static final ResourceLocation LEATHER_JACKET1_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/leather_jacket1_wide.png");
    private static final ResourceLocation LEATHER_JACKET1_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/leather_jacket1_wide_dye1.png");
    private static final ResourceLocation LEATHER_JACKET1_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/leather_jacket1_wide_dye2.png");
    private static final ResourceLocation EMT_JACKET_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/emt_jacket_wide.png");
    private static final ResourceLocation EMT_JACKET_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/emt_jacket_wide_dye1.png");
    private static final ResourceLocation EMT_JACKET_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/emt_jacket_wide_dye2.png");
    private static final ResourceLocation NETRUNNING_BODYSUIT_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_bodysuit_wide.png");
    private static final ResourceLocation NETRUNNING_BODYSUIT_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_bodysuit_wide_dye1.png");
    private static final ResourceLocation NETRUNNING_BODYSUIT_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_bodysuit_wide_dye2.png");
    private static final ResourceLocation HIGH_CROPPED_LEATHER_JACKET_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/high_cropped_leather_jacket_wide.png");
    private static final ResourceLocation HIGH_CROPPED_LEATHER_JACKET_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/high_cropped_leather_jacket_wide_dye1.png");
    private static final ResourceLocation HIGH_CROPPED_LEATHER_JACKET_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/high_cropped_leather_jacket_wide_dye2.png");
    private static final ResourceLocation LABCOAT1_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat1_wide.png");
    private static final ResourceLocation LABCOAT1_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat1_wide_dye1.png");
    private static final ResourceLocation LABCOAT1_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat1_wide_dye2.png");
    private static final ResourceLocation LABCOAT2_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat2_wide.png");
    private static final ResourceLocation LABCOAT2_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat2_wide_dye1.png");
    private static final ResourceLocation LABCOAT2_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat2_wide_dye2.png");
    private static final ResourceLocation MAXTAC_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_wide.png");
    private static final ResourceLocation MAXTAC_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_wide_dye1.png");
    private static final ResourceLocation TRAUMA_TEAM_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_padded_armor_wide.png");
    private static final ResourceLocation TRAUMA_TEAM_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_padded_armor_wide_dye1.png");
    private static final ResourceLocation TRAUMA_TEAM_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_padded_armor_wide_dye2.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_wide.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_WIDE_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_wide_dye1.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_WIDE_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_wide_dye2.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_WIDE_HIGHLIGHT =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_wide_highlight.png");

    //SLIM VARIANTS
    private static final ResourceLocation LEATHER_JACKET1_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/leather_jacket1_slim.png");
    private static final ResourceLocation LEATHER_JACKET1_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/leather_jacket1_slim_dye1.png");
    private static final ResourceLocation LEATHER_JACKET1_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/leather_jacket1_slim_dye2.png");
    private static final ResourceLocation EMT_JACKET_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/emt_jacket_slim.png");
    private static final ResourceLocation EMT_JACKET_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/emt_jacket_slim_dye1.png");
    private static final ResourceLocation EMT_JACKET_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/emt_jacket_slim_dye2.png");
    private static final ResourceLocation NETRUNNING_BODYSUIT_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_bodysuit_slim.png");
    private static final ResourceLocation NETRUNNING_BODYSUIT_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_bodysuit_slim_dye1.png");
    private static final ResourceLocation NETRUNNING_BODYSUIT_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/netrunning_bodysuit_slim_dye2.png");
    private static final ResourceLocation HIGH_CROPPED_LEATHER_JACKET_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/high_cropped_leather_jacket_slim.png");
    private static final ResourceLocation HIGH_CROPPED_LEATHER_JACKET_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/high_cropped_leather_jacket_slim_dye1.png");
    private static final ResourceLocation HIGH_CROPPED_LEATHER_JACKET_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/high_cropped_leather_jacket_slim_dye2.png");
    private static final ResourceLocation LABCOAT1_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat1_slim.png");
    private static final ResourceLocation LABCOAT1_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat1_slim_dye1.png");
    private static final ResourceLocation LABCOAT1_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat1_slim_dye2.png");
    private static final ResourceLocation LABCOAT2_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat2_slim.png");
    private static final ResourceLocation LABCOAT2_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat2_slim_dye1.png");
    private static final ResourceLocation LABCOAT2_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/labcoat2_slim_dye2.png");
    private static final ResourceLocation MAXTAC_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_slim.png");
    private static final ResourceLocation MAXTAC_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/maxtac_slim_dye1.png");
    private static final ResourceLocation TRAUMA_TEAM_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_padded_armor_slim.png");
    private static final ResourceLocation TRAUMA_TEAM_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_padded_armor_slim_dye1.png");
    private static final ResourceLocation TRAUMA_TEAM_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/trauma_team_padded_armor_slim_dye2.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_slim.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_SLIM_DYE1 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_slim_dye1.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_SLIM_DYE2 =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_slim_dye2.png");
    private static final ResourceLocation BAGGY_SWEATSHIRT_SLIM_HIGHLIGHT =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/overlays/baggy_sweatshirt_slim_highlight.png");






    /* ---------------------------------------------------------------------------------------------------------------------------------- */

    public static SkinModifierState getPlayerSkinState(AbstractClientPlayer player) {
        if (player == null) return null;

        PlayerCyberwareData data = PlayerCyberwareData.getForVisual(player, player.registryAccess());
        if (data == null) {
            data = tryBuildCyberwareDataFromSnapshot(player);
        }

        UUID playerId = player.getUUID();
        SkinModifierState state = PLAYER_STATES.computeIfAbsent(playerId, k -> new SkinModifierState());
        state.clearModifiers();

        EnumSet<SkinModifier.HideVanilla> FULL_OUTER_HIDE = EnumSet.allOf(SkinModifier.HideVanilla.class);
        EnumSet<SkinModifier.HideVanilla> BODY_ONLY_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.JACKET, SkinModifier.HideVanilla.LEFT_SLEEVE, SkinModifier.HideVanilla.RIGHT_SLEEVE, SkinModifier.HideVanilla.LEFT_PANTS, SkinModifier.HideVanilla.RIGHT_PANTS);

        EnumSet<SkinModifier.HideVanilla> HEAD_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.HAT);
        EnumSet<SkinModifier.HideVanilla> TORSO_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.JACKET);
        EnumSet<SkinModifier.HideVanilla> JACKET_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.JACKET, SkinModifier.HideVanilla.LEFT_SLEEVE, SkinModifier.HideVanilla.RIGHT_SLEEVE);
        EnumSet<SkinModifier.HideVanilla> PANTS_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS, SkinModifier.HideVanilla.RIGHT_PANTS);

        EnumSet<SkinModifier.HideVanilla> LARM_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE);
        EnumSet<SkinModifier.HideVanilla> RARM_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE);
        EnumSet<SkinModifier.HideVanilla> LLEG_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS);
        EnumSet<SkinModifier.HideVanilla> RLEG_OUTER_HIDE = EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS);


// MERMOD TAIL
        boolean mermodTailActive = false;
        if (ModCompats.isInstalled("mermod")) {

            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack necklaceCurio = CuriosCompat.findFirstCurio(player, "necklace", st -> {
                        ResourceLocation k = BuiltInRegistries.ITEM.getKey(st.getItem());return MERMOD_SEA_NECKLACE_ID.equals(k);
                    })
                    .orElse(ItemStack.EMPTY);

            boolean hasNecklaceEquipped = false;
            ItemStack necklaceStack = ItemStack.EMPTY;

            if (!chest.isEmpty()) {
                ResourceLocation key = BuiltInRegistries.ITEM.getKey(chest.getItem());
                if (MERMOD_SEA_NECKLACE_ID.equals(key)) {
                    hasNecklaceEquipped = true;
                    necklaceStack = chest;
                }
            }

            if (!hasNecklaceEquipped && !necklaceCurio.isEmpty()) {
                hasNecklaceEquipped = true;
                necklaceStack = necklaceCurio;
            }

            if (hasNecklaceEquipped) {
                boolean inWater = player.isInWaterOrBubble();

                var moisturizerTag = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                        ResourceLocation.fromNamespaceAndPath("mermod", "tail_moisturizer_modifier"));

                boolean hasMoisturizerByItemTag = necklaceStack.getTags().anyMatch(t -> t.equals(moisturizerTag));
                boolean hasMoisturizerByComponent = hasMermodMoisturizerComponent(necklaceStack);

                mermodTailActive = inWater || hasMoisturizerByItemTag || hasMoisturizerByComponent;
            }
        }

        /* ---------------------------------------------------------------------------------------------------------------------------------- */



// HEAD
        if (StyleClothingApi.hasSpecificItem(player, ModItems.BALACLAVA.get(), "head")) {
            ItemStack balaclava = StyleClothingApi.getSpecificItem(player, ModItems.BALACLAVA.get(), "head");
            int primaryTint = GarmentData.getPrimaryColor(balaclava, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(balaclava, 0xFFFFFFFF);


            state.addModifier(new SkinModifier(BALACLAVA, BALACLAVA,
                    0xFFFFFFFF, true, HEAD_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(balaclava)) {
                state.addModifier(new SkinModifier(BALACLAVA_DYE1, BALACLAVA_DYE1,
                        primaryTint, true, HEAD_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(balaclava)) {
                state.addModifier(new SkinModifier(BALACLAVA_DYE2, BALACLAVA_DYE2,
                        secondaryTint, true, HEAD_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.GLASSES1.get(), "head") ||
                StyleClothingApi.hasSpecificItem(player, ModItems.GLASSES2.get(), "head")) {

            state.addModifier(new SkinModifier(GLASSES, GLASSES,
                    0xFFFFFFFF, false));
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.MONOVISOR.get(), "head")) {
            ItemStack monovisor = StyleClothingApi.getSpecificItem(player, ModItems.MONOVISOR.get(), "head");
            int primaryTint = GarmentData.getPrimaryColor(monovisor, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(monovisor, 0xFFFFFFFF);


            state.addModifier(new SkinModifier(MONOVISOR, MONOVISOR,
                    0xFFFFFFFF, false));

            if (GarmentData.hasPrimaryColor(monovisor)) {
                state.addModifier(new SkinModifier(MONOVISOR_DYE1, MONOVISOR_DYE1,
                        primaryTint, false));
            }

            if (GarmentData.hasSecondaryColor(monovisor)) {
                state.addModifier(new SkinModifier(MONOVISOR_DYE2, MONOVISOR_DYE2,
                        secondaryTint, false));
            }

            state.addHighlight(new SkinHighlight(MONOVISOR_DYE1, MONOVISOR_DYE1,
                    primaryTint, true, true));
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.GM08.get(), "head")) {

            state.addModifier(new SkinModifier(GM08, GM08,
                    0xFFFFFFFF, true, HEAD_OUTER_HIDE));
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.TB3.get(), "head")) {

            state.addModifier(new SkinModifier(TB3, TB3,
                    0xFFFFFFFF, true, HEAD_OUTER_HIDE));
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.MAXTAC_HELMET.get(), "head")) {
            ItemStack maxTacHelmet = StyleClothingApi.getSpecificItem(player, ModItems.MAXTAC_HELMET.get(), "body");
            int primaryTint = GarmentData.getPrimaryColor(maxTacHelmet, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(maxTacHelmet, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(MAXTAC_HELMET, MAXTAC_HELMET,
                    0xFFFFFFFF, true, HEAD_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(maxTacHelmet)) {
                state.addModifier(new SkinModifier(MAXTAC_HELMET_DYE1, MAXTAC_HELMET_DYE1,
                        primaryTint, true, HEAD_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(maxTacHelmet)) {
                state.addModifier(new SkinModifier(MAXTAC_HELMET_DYE2, MAXTAC_HELMET_DYE2,
                        secondaryTint, true, HEAD_OUTER_HIDE));
            }

            state.addHighlight(new SkinHighlight(MAXTAC_HELMET_DYE2, MAXTAC_HELMET_DYE2,
                    secondaryTint, true, true));
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.TRAUMA_TEAM_HELMET.get(), "head")) {
            ItemStack traumaTeamHelmet = StyleClothingApi.getSpecificItem(player, ModItems.TRAUMA_TEAM_HELMET.get(), "head");
            int primaryTint = GarmentData.getPrimaryColor(traumaTeamHelmet, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(TRAUMA_TEAM_HELMET, TRAUMA_TEAM_HELMET,
                    0xFFFFFFFF, true, HEAD_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(traumaTeamHelmet)) {
                state.addModifier(new SkinModifier(TRAUMA_TEAM_HELMET_DYE1, TRAUMA_TEAM_HELMET_DYE1,
                        primaryTint, true, HEAD_OUTER_HIDE));

                state.addHighlight(new SkinHighlight(TRAUMA_TEAM_HELMET_DYE1, TRAUMA_TEAM_HELMET_DYE1,
                        primaryTint, true, true));
            } else {
                state.addHighlight(new SkinHighlight(TRAUMA_TEAM_HELMET_HIGHLIGHT, TRAUMA_TEAM_HELMET_HIGHLIGHT,
                        0xFFFFFFFF, true, true));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.ONI_MASK.get(), "head")) {
            ItemStack oniMask = StyleClothingApi.getSpecificItem(player, ModItems.ONI_MASK.get(), "head");
            int primaryTint = GarmentData.getPrimaryColor(oniMask, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(oniMask, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(ONI_MASK, ONI_MASK,
                    0xFFFFFFFF, false));

            if (GarmentData.hasPrimaryColor(oniMask)) {
                state.addModifier(new SkinModifier(ONI_MASK_DYE1, ONI_MASK_DYE1,
                        primaryTint, false));
            }

            if (GarmentData.hasSecondaryColor(oniMask)) {
                state.addModifier(new SkinModifier(ONI_MASK_DYE2, ONI_MASK_DYE2,
                        secondaryTint, false));
            }
        }

// BODY
        if (StyleClothingApi.hasSpecificItem(player, ModItems.NETRUNNING_BODYSUIT.get(), "body")) {
            ItemStack netrunningBodysuit = StyleClothingApi.getSpecificItem(player, ModItems.NETRUNNING_BODYSUIT.get(), "body");
            int primaryTint = GarmentData.getPrimaryColor(netrunningBodysuit, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(netrunningBodysuit, 0xFFFFFFFF);


            state.addModifier(new SkinModifier(NETRUNNING_BODYSUIT_WIDE, NETRUNNING_BODYSUIT_SLIM,
                    0xFFFFFFFF, true, BODY_ONLY_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(netrunningBodysuit)) {
                state.addModifier(new SkinModifier(NETRUNNING_BODYSUIT_WIDE_DYE1, NETRUNNING_BODYSUIT_SLIM_DYE1,
                        primaryTint, true, BODY_ONLY_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(netrunningBodysuit)) {
                state.addModifier(new SkinModifier(NETRUNNING_BODYSUIT_WIDE_DYE2, NETRUNNING_BODYSUIT_SLIM_DYE2,
                        secondaryTint, true, BODY_ONLY_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.NETRUNNING_UNITARD.get(), "body")) {
            ItemStack netrunningUnitard = StyleClothingApi.getSpecificItem(player, ModItems.NETRUNNING_UNITARD.get(), "body");
            int primaryTint = GarmentData.getPrimaryColor(netrunningUnitard, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(netrunningUnitard, 0xFFFFFFFF);


            state.addModifier(new SkinModifier(NET_UNITARD, NET_UNITARD,
                    0xFFFFFFFF, true, BODY_ONLY_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(netrunningUnitard)) {
                state.addModifier(new SkinModifier(NET_UNITARD_DYE1, NET_UNITARD_DYE1,
                        primaryTint, true, BODY_ONLY_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(netrunningUnitard)) {
                state.addModifier(new SkinModifier(NET_UNITARD_DYE2, NET_UNITARD_DYE2,
                        secondaryTint, true, BODY_ONLY_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.MAXTAC_BODYSUIT.get(), "body")) {
            ItemStack maxTacSuit = StyleClothingApi.getSpecificItem(player, ModItems.MAXTAC_BODYSUIT.get(), "body");
            int primaryTint = GarmentData.getPrimaryColor(maxTacSuit, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(maxTacSuit, 0xFFFFFFFF);


            state.addModifier(new SkinModifier(MAXTAC_WIDE, MAXTAC_SLIM,
                    0xFFFFFFFF, true, BODY_ONLY_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(maxTacSuit)) {
                state.addModifier(new SkinModifier(MAXTAC_WIDE_DYE1, MAXTAC_SLIM_DYE1,
                        primaryTint, true, BODY_ONLY_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.TRAUMA_TEAM_PADDED_ARMOR.get(), "body")) {
            ItemStack traumaTeamArmor = StyleClothingApi.getSpecificItem(player, ModItems.TRAUMA_TEAM_PADDED_ARMOR.get(), "body");
            int primaryTint = GarmentData.getPrimaryColor(traumaTeamArmor, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(traumaTeamArmor, 0xFFFFFFFF);


            state.addModifier(new SkinModifier(TRAUMA_TEAM_WIDE, TRAUMA_TEAM_SLIM,
                    0xFFFFFFFF, true, BODY_ONLY_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(traumaTeamArmor)) {
                state.addModifier(new SkinModifier(TRAUMA_TEAM_WIDE_DYE1, TRAUMA_TEAM_SLIM_DYE1,
                        primaryTint, true, BODY_ONLY_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(traumaTeamArmor)) {
                state.addModifier(new SkinModifier(TRAUMA_TEAM_WIDE_DYE2, TRAUMA_TEAM_SLIM_DYE2,
                        secondaryTint, true, BODY_ONLY_OUTER_HIDE));
            }
        }

// NECKLACE
        if (StyleClothingApi.hasSpecificItem(player, ModItems.MESSY_TIE.get(), "necklace")) {
            ItemStack messyTie = StyleClothingApi.getSpecificItem(player, ModItems.MESSY_TIE.get(), "necklace");
            int primaryTint = GarmentData.getPrimaryColor(messyTie, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(MESSY_TIE, MESSY_TIE,
                    0xFFFFFFFF, false));

            if (GarmentData.hasPrimaryColor(messyTie)) {
                state.addModifier(new SkinModifier(MESSY_TIE, MESSY_TIE,
                        primaryTint, false));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.TIE.get(), "necklace")) {
            ItemStack neckTie = StyleClothingApi.getSpecificItem(player, ModItems.TIE.get(), "necklace");
            int primaryTint = GarmentData.getPrimaryColor(neckTie, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(TIE, TIE,
                    0xFFFFFFFF, false));

            if (GarmentData.hasPrimaryColor(neckTie)) {
                state.addModifier(new SkinModifier(TIE, TIE,
                        primaryTint, false));
            }
        }

// PANTS
        if (StyleClothingApi.hasSpecificItem(player, ModItems.CAMO_PANTS.get(), "pants")) {
            ItemStack camoPants = StyleClothingApi.getSpecificItem(player, ModItems.CAMO_PANTS.get(), "pants");
            int primaryTint = GarmentData.getPrimaryColor(camoPants, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(camoPants, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(CAMO_PANTS, CAMO_PANTS,
                    0xFFFFFFFF, true, PANTS_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(camoPants)) {
                state.addModifier(new SkinModifier(CAMO_PANTS_DYE1, CAMO_PANTS_DYE1,
                        primaryTint, true, PANTS_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(camoPants)) {
                state.addModifier(new SkinModifier(CAMO_PANTS_DYE2, CAMO_PANTS_DYE2,
                        secondaryTint, true, PANTS_OUTER_HIDE));
            }
        }

// BACK
        if (StyleClothingApi.hasSpecificItem(player, ModItems.LEATHER_JACKET1.get(), "back")) {
            ItemStack leatherJacket1 = StyleClothingApi.getSpecificItem(player, ModItems.LEATHER_JACKET1.get(), "back");
            int primaryTint = GarmentData.getPrimaryColor(leatherJacket1, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(leatherJacket1, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(LEATHER_JACKET1_WIDE, LEATHER_JACKET1_SLIM,
                    0xFFFFFFFF, true, JACKET_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(leatherJacket1)) {
                state.addModifier(new SkinModifier(LEATHER_JACKET1_WIDE_DYE1, LEATHER_JACKET1_SLIM_DYE1,
                        primaryTint, true, JACKET_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(leatherJacket1)) {
                state.addModifier(new SkinModifier(LEATHER_JACKET1_WIDE_DYE2, LEATHER_JACKET1_SLIM_DYE2,
                        secondaryTint, true, JACKET_OUTER_HIDE));
            }

            state.addHighlight(new SkinHighlight(LEATHER_JACKET1_WIDE_DYE2, LEATHER_JACKET1_SLIM_DYE2,
                    secondaryTint, true, true));

        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.EMT_JACKET.get(), "back")) {
            ItemStack emtJacket = StyleClothingApi.getSpecificItem(player, ModItems.EMT_JACKET.get(), "back");
            int primaryTint = GarmentData.getPrimaryColor(emtJacket, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(emtJacket, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(EMT_JACKET_WIDE, EMT_JACKET_SLIM,
                    0xFFFFFFFF, true, JACKET_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(emtJacket)) {
                state.addModifier(new SkinModifier(EMT_JACKET_WIDE_DYE1, EMT_JACKET_SLIM_DYE1,
                        primaryTint, true, JACKET_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(emtJacket)) {
                state.addModifier(new SkinModifier(EMT_JACKET_WIDE_DYE2, EMT_JACKET_SLIM_DYE2,
                        secondaryTint, true, JACKET_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.HIGH_CROPPED_LEATHER_JACKET.get(), "back")) {
            ItemStack highCropJacket = StyleClothingApi.getSpecificItem(player, ModItems.HIGH_CROPPED_LEATHER_JACKET.get(), "back");
            int primaryTint = GarmentData.getPrimaryColor(highCropJacket, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(highCropJacket, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(HIGH_CROPPED_LEATHER_JACKET_WIDE, HIGH_CROPPED_LEATHER_JACKET_SLIM,
                    0xFFFFFFFF, true, JACKET_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(highCropJacket)) {
                state.addModifier(new SkinModifier(HIGH_CROPPED_LEATHER_JACKET_WIDE_DYE1, HIGH_CROPPED_LEATHER_JACKET_SLIM_DYE1,
                        primaryTint, true, JACKET_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(highCropJacket)) {
                state.addModifier(new SkinModifier(HIGH_CROPPED_LEATHER_JACKET_WIDE_DYE2, HIGH_CROPPED_LEATHER_JACKET_SLIM_DYE2,
                        secondaryTint, true, JACKET_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.LABCOAT1.get(), "back")) {
            ItemStack labcoat1 = StyleClothingApi.getSpecificItem(player, ModItems.LABCOAT1.get(), "back");
            int primaryTint = GarmentData.getPrimaryColor(labcoat1, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(labcoat1, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(LABCOAT1_WIDE, LABCOAT1_SLIM,
                    0xFFFFFFFF, true, JACKET_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(labcoat1)) {
                state.addModifier(new SkinModifier(LABCOAT1_WIDE_DYE1, LABCOAT1_SLIM_DYE1,
                        primaryTint, true, JACKET_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(labcoat1)) {
                state.addModifier(new SkinModifier(LABCOAT1_WIDE_DYE2, LABCOAT1_SLIM_DYE2,
                        secondaryTint, true, JACKET_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.LABCOAT2.get(), "back")) {
            ItemStack labcoat2 = StyleClothingApi.getSpecificItem(player, ModItems.LABCOAT2.get(), "back");
            int primaryTint = GarmentData.getPrimaryColor(labcoat2, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(labcoat2, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(LABCOAT2_WIDE, LABCOAT2_SLIM,
                    0xFFFFFFFF, true, JACKET_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(labcoat2)) {
                state.addModifier(new SkinModifier(LABCOAT2_WIDE_DYE1, LABCOAT2_SLIM_DYE1,
                        primaryTint, true, JACKET_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(labcoat2)) {
                state.addModifier(new SkinModifier(LABCOAT2_WIDE_DYE2, LABCOAT2_SLIM_DYE2,
                        secondaryTint, true, JACKET_OUTER_HIDE));
            }
        }
        if (StyleClothingApi.hasSpecificItem(player, ModItems.LABCOAT2.get(), "back")) {
            ItemStack baggySweatshirt = StyleClothingApi.getSpecificItem(player, ModItems.LABCOAT2.get(), "back");
            int primaryTint = GarmentData.getPrimaryColor(baggySweatshirt, 0xFFFFFFFF);
            int secondaryTint = GarmentData.getSecondaryColor(baggySweatshirt, 0xFFFFFFFF);

            state.addModifier(new SkinModifier(BAGGY_SWEATSHIRT_WIDE, BAGGY_SWEATSHIRT_SLIM,
                    0xFFFFFFFF, true, JACKET_OUTER_HIDE));

            if (GarmentData.hasPrimaryColor(baggySweatshirt)) {
                state.addModifier(new SkinModifier(BAGGY_SWEATSHIRT_WIDE_DYE1, BAGGY_SWEATSHIRT_SLIM_DYE1,
                        primaryTint, true, JACKET_OUTER_HIDE));
            }

            if (GarmentData.hasSecondaryColor(baggySweatshirt)) {
                state.addModifier(new SkinModifier(BAGGY_SWEATSHIRT_WIDE_DYE2, BAGGY_SWEATSHIRT_SLIM_DYE2,
                        secondaryTint, true, JACKET_OUTER_HIDE));

                state.addHighlight(new SkinHighlight(BAGGY_SWEATSHIRT_WIDE_DYE2, BAGGY_SWEATSHIRT_SLIM_DYE2,
                        secondaryTint, true, true));
            } else {
                state.addHighlight(new SkinHighlight(BAGGY_SWEATSHIRT_WIDE_HIGHLIGHT, BAGGY_SWEATSHIRT_SLIM_HIGHLIGHT,
                        0xFFFFFFFF, true, true));

            }
        }








        return state;
    }



    /* ---------------------------------------------------------------------------------------------------------------------------------- */

    private static boolean isItem(ItemStack stack, ResourceLocation itemId) {
        if (stack == null || stack.isEmpty() || itemId == null) return false;

        ResourceLocation stackId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return itemId.equals(stackId);
    }

    private static boolean hasMermodMoisturizerComponent(ItemStack necklace) {
        DataComponentType<?> type = getMermodNecklaceModifiersComponentType();
        if (type == null) return false;
        Object componentValue = necklace.get(type);
        if (componentValue == null) return false;
        return componentContainsMoisturizer(componentValue);
    }
    private static DataComponentType<?> getMermodNecklaceModifiersComponentType() {
        if (MERMOD_NECKLACE_MODIFIERS_COMPONENT != null) return MERMOD_NECKLACE_MODIFIERS_COMPONENT;
        DataComponentType<?> t;

        t = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath("mermod", "necklace_modifiers"));
        if (t != null) return MERMOD_NECKLACE_MODIFIERS_COMPONENT = t;

        t = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath("mermod", "necklace_modifiers_component_type"));
        if (t != null) return MERMOD_NECKLACE_MODIFIERS_COMPONENT = t;

        t = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath("mermod", "necklace_modifiers_component"));
        if (t != null) return MERMOD_NECKLACE_MODIFIERS_COMPONENT = t;

        DataComponentType<?> best = null;
        int bestScore = -1;

        for (ResourceLocation id : BuiltInRegistries.DATA_COMPONENT_TYPE.keySet()) {
            if (!"mermod".equals(id.getNamespace())) continue;
            String p = id.getPath();

            int score = 0;
            if (p.contains("necklace")) score += 3;
            if (p.contains("modifier")) score += 3;
            if (p.contains("modifiers")) score += 2;
            if (p.contains("component")) score += 1;

            if (score > bestScore) {
                DataComponentType<?> candidate = BuiltInRegistries.DATA_COMPONENT_TYPE.get(id);
                if (candidate != null) {
                    best = candidate;
                    bestScore = score;
                }
            }
        }

        MERMOD_NECKLACE_MODIFIERS_COMPONENT = best;
        return best;
    }
    private static boolean componentContainsMoisturizer(Object necklaceModifiersComponent) {
        if (MERMOD_REFLECTION_FAILED) return false;
        if (!MERMOD_REFLECTION_READY) {
            try {
                MERMOD_COMPONENT_MODIFIERS_METHOD = necklaceModifiersComponent.getClass().getMethod("modifiers");

                Class<?> necklaceModifierClass = Class.forName("io.github.thatpreston.mermod.item.modifier.NecklaceModifier");
                MERMOD_MODIFIER_ID_METHOD = necklaceModifierClass.getMethod("id");
                MERMOD_REFLECTION_READY = true;
            } catch (Throwable t) {
                MERMOD_REFLECTION_FAILED = true;
                return false;
            }
        }

        try {
            Object mapObj = MERMOD_COMPONENT_MODIFIERS_METHOD.invoke(necklaceModifiersComponent);
            if (!(mapObj instanceof Map<?, ?> map)) return false;

            Collection<?> values = map.values();
            for (Object mod : values) {
                if (mod == null) continue;

                Object idObj = MERMOD_MODIFIER_ID_METHOD.invoke(mod);
                if (!(idObj instanceof String rawId)) continue;

                if (isMoisturizerId(rawId)) return true;
            }
        } catch (Throwable ignored) {}

        return false;
    }
    private static boolean isMoisturizerId(String rawId) {
        if (rawId == null || rawId.isBlank()) return false;
        String s = rawId;

        int colon = s.indexOf(':');
        if (colon >= 0 && colon + 1 < s.length()) s = s.substring(colon + 1);

        if (s.endsWith("_modifier")) s = s.substring(0, s.length() - "_modifier".length());

        return "tail_moisturizer".equals(s);
    }

    private static PlayerCyberwareData tryBuildCyberwareDataFromSnapshot(AbstractClientPlayer player) {
        if (player == null) {
            return null;
        }

        CompoundTag pd = player.getPersistentData();

        if (!pd.contains(PlayerCyberwareData.HOLO_SNAPSHOT_CYBERWARE, net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            return null;
        }

        CompoundTag snap = pd.getCompound(PlayerCyberwareData.HOLO_SNAPSHOT_CYBERWARE);
        if (snap.isEmpty()) {
            return null;
        }

        return PlayerCyberwareData.fromSnapshotTag(snap, player.registryAccess());
    }
}