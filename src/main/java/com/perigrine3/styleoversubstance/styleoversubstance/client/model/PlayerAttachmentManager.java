package com.perigrine3.styleoversubstance.styleoversubstance.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.api.StyleClothingApi;
import com.perigrine3.styleoversubstance.styleoversubstance.client.model.attachments.*;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public final class PlayerAttachmentManager {

    private PlayerAttachmentManager() {}

    private static final Map<UUID, PlayerAttachmentState> STATES = new HashMap<>();

    // =========================
    // CAP
    // =========================
    public static final ResourceLocation CAP_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/cap.png");

    public static final ResourceLocation CAP_DYE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/cap_dye1.png");

    public static final ResourceLocation CAP_DYE2_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/cap_dye2.png");

    private static CapAttachmentModel CAP_MODEL;

    public static CapAttachmentModel capModel() {
        if (CAP_MODEL == null) {
            var baked = Minecraft.getInstance().getEntityModels().bakeLayer(CapAttachmentModel.LAYER);
            CAP_MODEL = new CapAttachmentModel(baked);
        }

        return CAP_MODEL;
    }

    // =========================
    // VISOR 1
    // =========================
    public static final ResourceLocation VISOR1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/visor1.png");

    public static final ResourceLocation VISOR1_DYE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/visor1_dye1.png");

    public static final ResourceLocation VISOR1_DYE2_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/visor1_dye2.png");

    private static Visor1AttachmentModel VISOR1_MODEL;

    public static Visor1AttachmentModel visor1Model() {
        if (VISOR1_MODEL == null) {
            var baked = Minecraft.getInstance().getEntityModels().bakeLayer(Visor1AttachmentModel.LAYER);
            VISOR1_MODEL = new Visor1AttachmentModel(baked);
        }

        return VISOR1_MODEL;
    }

    // =========================
    // GLASSES
    // =========================
    public static final ResourceLocation GLASSES1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/glasses1.png");

    public static final ResourceLocation GLASSES1_DYE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/glasses1_dye1.png");

    public static final ResourceLocation GLASSES2_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/glasses2.png");

    public static final ResourceLocation GLASSES2_DYE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/glasses2_dye1.png");

    private static final List<GlassesSource> GLASSES_SOURCES = List.of(
            new GlassesSource(ModItems.GLASSES1, "head", GLASSES1_TEXTURE, GLASSES1_DYE1_TEXTURE),
            new GlassesSource(ModItems.GLASSES2, "head", GLASSES2_TEXTURE, GLASSES2_DYE1_TEXTURE)
    );

    private static Glasses1AttachmentModel GLASSES_MODEL;

    public static Glasses1AttachmentModel glassesModel() {
        if (GLASSES_MODEL == null) {
            var baked = Minecraft.getInstance().getEntityModels().bakeLayer(Glasses1AttachmentModel.LAYER);
            GLASSES_MODEL = new Glasses1AttachmentModel(baked);
        }

        return GLASSES_MODEL;
    }

    // =========================
    // ONI MASK
    // =========================
    public static final ResourceLocation ONI_MASK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/oni_mask.png");

    public static final ResourceLocation ONI_MASK_DYE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/oni_mask_dye1.png");

    public static final ResourceLocation ONI_MASK_DYE2_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/oni_mask_dye2.png");

    private static OniMaskAttachmentModel ONI_MASK_MODEL;

    public static OniMaskAttachmentModel oniMaskModel() {
        if (ONI_MASK_MODEL == null) {
            var baked = Minecraft.getInstance().getEntityModels().bakeLayer(OniMaskAttachmentModel.LAYER);
            ONI_MASK_MODEL = new OniMaskAttachmentModel(baked);
        }

        return ONI_MASK_MODEL;
    }

    // =========================
    // FUNNEL COLLAR
    // =========================
    public static final ResourceLocation FUNNEL_COLLAR_EMTJACKET =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/funnel_collar_emtjacket.png");

    public static final ResourceLocation FUNNEL_COLLAR_EMTJACKET_HIGHLIGHT =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/funnel_collar_emtjacket_highlight.png");

    public static final ResourceLocation FUNNEL_COLLAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/funnel_collar.png");

    public static final ResourceLocation FUNNEL_COLLAR_DYE1_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/funnel_collar_dye1.png");

    public static final ResourceLocation FUNNEL_COLLAR_DYE2_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/entity/attachments/funnel_collar_dye2.png");

    private static final List<FunnelCollarSource> FUNNEL_COLLAR_SOURCES = List.of(
            new FunnelCollarSource(ModItems.EMT_JACKET, "back", FUNNEL_COLLAR_EMTJACKET, FUNNEL_COLLAR_EMTJACKET_HIGHLIGHT),
            new FunnelCollarSource(ModItems.LEATHER_JACKET1, "back", FUNNEL_COLLAR_TEXTURE, null)
    );

    private static FunnelCollarAttachmentModel FUNNEL_COLLAR_MODEL;

    public static FunnelCollarAttachmentModel funnelCollarModel() {
        if (FUNNEL_COLLAR_MODEL == null) {
            var baked = Minecraft.getInstance().getEntityModels().bakeLayer(FunnelCollarAttachmentModel.LAYER);
            FUNNEL_COLLAR_MODEL = new FunnelCollarAttachmentModel(baked);
        }

        return FUNNEL_COLLAR_MODEL;
    }

    // =========================
    // STATE BUILD
    // =========================
    public static PlayerAttachmentState getState(AbstractClientPlayer player) {
        if (player == null) return null;
        PlayerAttachmentState state = STATES.computeIfAbsent(player.getUUID(), id -> new PlayerAttachmentState());
        state.clear();

        ItemStack cap = StyleClothingApi.getSpecificItem(player, ModItems.CAP.get(), "head");
        if (!cap.isEmpty()) {
            state.add(new CapAttachment(cap));
        }

        ItemStack visor1 = StyleClothingApi.getSpecificItem(player, ModItems.VISOR1.get(), "head");
        if (!visor1.isEmpty()) {
            state.add(new Visor1Attachment(visor1));
        }

        ResolvedGlassesSource glassesSource = findGlassesSource(player);
        if (glassesSource != null) {
            state.add(new GlassesAttachment(glassesSource));
        }

        ItemStack oniMask = StyleClothingApi.getSpecificItem(player, ModItems.ONI_MASK.get(), "head");
        if (!oniMask.isEmpty()) {
            state.add(new OniMaskAttachment(oniMask));
        }

        ResolvedFunnelCollarSource funnelCollarSource = findFunnelCollarSource(player);
        if (funnelCollarSource != null) {
            state.add(new FunnelCollarAttachment(funnelCollarSource));
        }

        return state;
    }

    private static ResolvedGlassesSource findGlassesSource(AbstractClientPlayer player) {
        for (GlassesSource source : GLASSES_SOURCES) {
            ItemStack stack = StyleClothingApi.getSpecificItem(player, source.item().get(), source.slot());

            if (!stack.isEmpty()) {
                return new ResolvedGlassesSource(stack.copyWithCount(1), source.texture(), source.dye1Texture());
            }
        }

        return null;
    }

    private static ResolvedFunnelCollarSource findFunnelCollarSource(AbstractClientPlayer player) {
        for (FunnelCollarSource source : FUNNEL_COLLAR_SOURCES) {
            ItemStack stack = StyleClothingApi.getSpecificItem(player, source.item().get(), source.slot());

            if (!stack.isEmpty()) {
                return new ResolvedFunnelCollarSource(stack.copyWithCount(1), source.undyedPrimaryTexture(), source.undyedSecondaryHighlightTexture());
            }
        }

        return null;
    }

    // =========================
    // TRANSFORMS
    // =========================
    public static void applyCapTransform(PoseStack poseStack) {
        poseStack.translate(0.0F, 0.0F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
        poseStack.scale(1.0F, 1.0F, 1.0F);
    }

    public static void applyVisor1Transform(PoseStack poseStack) {
        poseStack.translate(0.0F, 0.0F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
        poseStack.scale(1.0F, 1.0F, 1.0F);
    }

    public static void applyGlassesTransform(PoseStack poseStack) {
        poseStack.translate(0.0F, 0.0F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
        poseStack.scale(1.25F, 1.25F, 1.25F);
    }

    public static void applyOniMaskTransform(PoseStack poseStack) {
        poseStack.translate(0.0F, 0.05F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
        poseStack.scale(1.0F, 1.0F, 1.0F);
    }

    public static void applyFunnelCollarTransform(PoseStack poseStack) {
        poseStack.translate(0.0F, 0.0F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
        poseStack.scale(1.0F, 1.0F, 1.0F);
    }

    // =========================
    // ATTACHMENTS
    // =========================
    private static final class CapAttachment implements PlayerAttachment {
        private final ItemStack garment;

        private CapAttachment(ItemStack garment) {
            this.garment = garment.copyWithCount(1);
        }

        @Override
        public AttachmentAnchor anchor() {
            return AttachmentAnchor.HEAD;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return capModel();
        }

        @Override
        public List<AttachmentRenderLayer> renderLayers(PlayerSkin.Model modelType) {
            List<AttachmentRenderLayer> layers = new ArrayList<>();

            layers.add(new AttachmentRenderLayer(CAP_TEXTURE, 0xFFFFFFFF));

            if (GarmentData.hasPrimaryColor(garment)) {
                int primaryColor = GarmentData.getPrimaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(CAP_DYE1_TEXTURE, primaryColor));
            }

            if (GarmentData.hasSecondaryColor(garment)) {
                int secondaryColor = GarmentData.getSecondaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(CAP_DYE2_TEXTURE, secondaryColor));
            }

            return layers;
        }

        @Override
        public boolean thirdPersonOnly() {
            return false;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            applyCapTransform(poseStack);
        }
    }

    private static final class Visor1Attachment implements PlayerAttachment {
        private final ItemStack garment;

        private Visor1Attachment(ItemStack garment) {
            this.garment = garment.copyWithCount(1);
        }

        @Override
        public AttachmentAnchor anchor() {
            return AttachmentAnchor.HEAD;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return visor1Model();
        }

        @Override
        public List<AttachmentRenderLayer> renderLayers(PlayerSkin.Model modelType) {
            List<AttachmentRenderLayer> layers = new ArrayList<>();

            layers.add(new AttachmentRenderLayer(VISOR1_TEXTURE, 0xFFFFFFFF));

            if (GarmentData.hasPrimaryColor(garment)) {
                int primaryColor = GarmentData.getPrimaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(VISOR1_DYE1_TEXTURE, primaryColor, AttachmentRenderLayer.RenderMode.STRONG_EMISSIVE));
            }

            if (GarmentData.hasSecondaryColor(garment)) {
                int secondaryColor = GarmentData.getSecondaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(VISOR1_DYE2_TEXTURE, secondaryColor));
            }

            return layers;
        }

        @Override
        public boolean thirdPersonOnly() {
            return false;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            applyVisor1Transform(poseStack);
        }
    }

    private static final class GlassesAttachment implements PlayerAttachment {
        private final ItemStack garment;
        private final ResourceLocation texture;
        private final @Nullable ResourceLocation dye1Texture;

        private GlassesAttachment(ResolvedGlassesSource source) {
            this.garment = source.garment();
            this.texture = source.texture();
            this.dye1Texture = source.dye1Texture();
        }

        @Override
        public AttachmentAnchor anchor() {
            return AttachmentAnchor.HEAD;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return glassesModel();
        }

        @Override
        public List<AttachmentRenderLayer> renderLayers(PlayerSkin.Model modelType) {
            List<AttachmentRenderLayer> layers = new ArrayList<>();

            layers.add(new AttachmentRenderLayer(texture, 0xFFFFFFFF));

            if (dye1Texture != null && GarmentData.hasPrimaryColor(garment)) {
                int primaryColor = GarmentData.getPrimaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(dye1Texture, primaryColor));
            }

            return layers;
        }

        @Override
        public boolean thirdPersonOnly() {
            return false;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            applyGlassesTransform(poseStack);
        }
    }

    private static final class OniMaskAttachment implements PlayerAttachment {
        private final ItemStack garment;

        private OniMaskAttachment(ItemStack garment) {
            this.garment = garment.copyWithCount(1);
        }

        @Override
        public AttachmentAnchor anchor() {
            return AttachmentAnchor.HEAD;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return oniMaskModel();
        }

        @Override
        public List<AttachmentRenderLayer> renderLayers(PlayerSkin.Model modelType) {
            List<AttachmentRenderLayer> layers = new ArrayList<>();

            layers.add(new AttachmentRenderLayer(ONI_MASK_TEXTURE, 0xFFFFFFFF));

            if (GarmentData.hasPrimaryColor(garment)) {
                int primaryColor = GarmentData.getPrimaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(ONI_MASK_DYE1_TEXTURE, primaryColor));
            }

            if (GarmentData.hasSecondaryColor(garment)) {
                int secondaryColor = GarmentData.getSecondaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(ONI_MASK_DYE2_TEXTURE, secondaryColor));
            }

            return layers;
        }

        @Override
        public boolean thirdPersonOnly() {
            return false;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            applyOniMaskTransform(poseStack);
        }
    }

    private static final class FunnelCollarAttachment implements PlayerAttachment {
        private final ItemStack garment;
        private final ResourceLocation undyedPrimaryTexture;
        private final @Nullable ResourceLocation undyedSecondaryHighlightTexture;

        private FunnelCollarAttachment(ResolvedFunnelCollarSource source) {
            this.garment = source.garment();
            this.undyedPrimaryTexture = source.undyedPrimaryTexture();
            this.undyedSecondaryHighlightTexture = source.undyedSecondaryHighlightTexture();
        }

        @Override
        public AttachmentAnchor anchor() {
            return AttachmentAnchor.BODY;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return funnelCollarModel();
        }

        @Override
        public List<AttachmentRenderLayer> renderLayers(PlayerSkin.Model modelType) {
            List<AttachmentRenderLayer> layers = new ArrayList<>();
            boolean hasPrimaryColor = GarmentData.hasPrimaryColor(garment);
            boolean hasSecondaryColor = GarmentData.hasSecondaryColor(garment);

            layers.add(new AttachmentRenderLayer(hasPrimaryColor ? FUNNEL_COLLAR_TEXTURE : undyedPrimaryTexture, 0xFFFFFFFF));

            if (hasPrimaryColor) {
                int primaryColor = GarmentData.getPrimaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(FUNNEL_COLLAR_DYE1_TEXTURE, primaryColor));
            }

            if (hasSecondaryColor) {
                int secondaryColor = GarmentData.getSecondaryColor(garment, 0xFFFFFFFF);
                layers.add(new AttachmentRenderLayer(FUNNEL_COLLAR_DYE2_TEXTURE, secondaryColor, AttachmentRenderLayer.RenderMode.STRONG_EMISSIVE));
            } else if (undyedSecondaryHighlightTexture != null) {
                layers.add(new AttachmentRenderLayer(undyedSecondaryHighlightTexture, 0xFFFFFFFF, AttachmentRenderLayer.RenderMode.STRONG_EMISSIVE));
            }

            return layers;
        }

        @Override
        public boolean thirdPersonOnly() {
            return false;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            applyFunnelCollarTransform(poseStack);
        }
    }

    private record GlassesSource(Supplier<? extends Item> item, String slot, ResourceLocation texture, @Nullable ResourceLocation dye1Texture) {}
    private record ResolvedGlassesSource(ItemStack garment, ResourceLocation texture, @Nullable ResourceLocation dye1Texture) {}
    private record FunnelCollarSource(Supplier<? extends Item> item, String slot, ResourceLocation undyedPrimaryTexture, @Nullable ResourceLocation undyedSecondaryHighlightTexture) {}
    private record ResolvedFunnelCollarSource(ItemStack garment, ResourceLocation undyedPrimaryTexture, @Nullable ResourceLocation undyedSecondaryHighlightTexture) {}
}