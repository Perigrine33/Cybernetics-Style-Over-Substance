package com.perigrine3.styleoversubstance.styleoversubstance.mixin;

import com.perigrine3.createcybernetics.network.handler.CastCyberdeckQuickhackHandler;
import com.perigrine3.createcybernetics.network.payload.CastCyberdeckQuickhackPayload;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentModules;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CastCyberdeckQuickhackHandler.class, remap = false)
public abstract class OutgoingQuickhackMixin {
    // The cast is validated and its cooldown applied before the actual quickhack is attempted.
    @Inject(method = "lambda$handle$0", at = @At(value = "INVOKE",
            target = "Lcom/perigrine3/createcybernetics/network/handler/CastCyberdeckQuickhackHandler;applyQuickhack(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Z"), cancellable = true)
    private static void styleoversubstance$faradayInterference(IPayloadContext context,
            CastCyberdeckQuickhackPayload payload, CallbackInfo ci) {
        if (context.player() instanceof ServerPlayer player && GarmentModules.blocksOutgoingQuickhack(player)) {
            player.displayClientMessage(Component.translatable("message.styleoversubstance.faraday_interference")
                    .withStyle(ChatFormatting.RED), true);
            ci.cancel();
        }
    }
}
