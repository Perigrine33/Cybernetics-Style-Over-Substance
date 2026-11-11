package com.perigrine3.styleoversubstance.styleoversubstance.mixin;

import com.perigrine3.createcybernetics.common.capabilities.EntityCyberwareData;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentModules;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityCyberwareData.class, remap = false)
public abstract class EntityEnergyCapacityMixin {
    @Inject(method = "getTotalEnergyCapacity", at = @At("RETURN"), cancellable = true)
    private void styleoversubstance$addBatteries(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() + GarmentModules.extraEnergyCapacity(entity));
    }
}
