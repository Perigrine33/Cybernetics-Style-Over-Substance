package com.perigrine3.styleoversubstance.styleoversubstance.mixin;

import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.energy.EnergyController;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentModules;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnergyController.class, remap = false)
public abstract class GarmentBatteryChargingMixin {
    // Capacity and permission to store generated surplus are separate checks in Cybernetics.
    @Inject(method = "canAcceptGeneratedSurplus", at = @At("RETURN"), cancellable = true)
    private static void styleoversubstance$acceptGeneratedEnergy(Player player, PlayerCyberwareData data,
            CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && GarmentModules.extraEnergyCapacity(player) > 0) {
            cir.setReturnValue(true);
        }
    }
}
