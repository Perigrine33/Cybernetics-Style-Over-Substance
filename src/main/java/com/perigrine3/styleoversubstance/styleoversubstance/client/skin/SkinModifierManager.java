package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.compat.ModCompats;
import com.perigrine3.createcybernetics.compat.curios.CuriosCompat;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
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
    private static final ResourceLocation MERMOD_SEA_NECKLACE_ID =
            ResourceLocation.fromNamespaceAndPath("mermod", "sea_necklace");

    private static DataComponentType<?> MERMOD_NECKLACE_MODIFIERS_COMPONENT;
    private static boolean MERMOD_REFLECTION_READY = false;
    private static boolean MERMOD_REFLECTION_FAILED = false;
    private static Method MERMOD_COMPONENT_MODIFIERS_METHOD;
    private static Method MERMOD_MODIFIER_ID_METHOD;




    //INTERCHANGEABLES
    private static final ResourceLocation MISSING_SKIN_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/playermuscles_wide.png");

    //WIDE VARIANTS
    private static final ResourceLocation LEFT_CYBERARM_TEXTURE_WIDE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/left_cyberarm_wide.png");

    //SLIM VARIANTS
    private static final ResourceLocation LEFT_CYBERARM_TEXTURE_SLIM =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/left_cyberarm_slim.png");




    public static SkinModifierState getPlayerSkinState(AbstractClientPlayer player) {
        PlayerCyberwareData data = PlayerCyberwareData.getForVisual(player, player.registryAccess());
        if (data == null) {
            data = tryBuildCyberwareDataFromSnapshot(player);
        }
        if (data == null) return null;

        UUID playerId = player.getUUID();
        SkinModifierState state = PLAYER_STATES.computeIfAbsent(playerId, k -> new SkinModifierState());
        state.clearModifiers();

        EnumSet<SkinModifier.HideVanilla> FULL_OUTER_HIDE = EnumSet.allOf(SkinModifier.HideVanilla.class);
        EnumSet<SkinModifier.HideVanilla> BODY_ONLY_OUTER_HIDE = EnumSet.of(
                SkinModifier.HideVanilla.JACKET,
                SkinModifier.HideVanilla.LEFT_SLEEVE,
                SkinModifier.HideVanilla.RIGHT_SLEEVE,
                SkinModifier.HideVanilla.LEFT_PANTS,
                SkinModifier.HideVanilla.RIGHT_PANTS
        );


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



        // Put skin overlays here.



        return state;
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