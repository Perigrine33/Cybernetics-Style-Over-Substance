package com.perigrine3.styleoversubstance.styleoversubstance.mixin;

import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentModules;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerCyberwareData.class, remap = false)
public abstract class PlayerEnergyCapacityMixin {
    @Inject(method = "getTotalEnergyCapacity", at = @At("RETURN"), cancellable = true)
    private void styleoversubstance$addBatteries(Player player, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() + GarmentModules.extraEnergyCapacity(player));
    }
}
