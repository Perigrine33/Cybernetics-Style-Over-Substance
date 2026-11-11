package com.perigrine3.styleoversubstance.styleoversubstance.client.colors;

import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.component.ModDataComponents;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class StyleItemColors {

    private StyleItemColors() {
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
                return color != null ? 0xFF000000 | color.rgb() : 0x00FFFFFF;
            }

            if (tintIndex == 2) {
                DyedItemColor color = stack.get(ModDataComponents.SECONDARY_DYE_COLOR.get());
                return color != null ? 0xFF000000 | color.rgb() : 0x00FFFFFF;
            }

            return 0xFFFFFFFF;
        },
                ModItems.BALACLAVA.get(),
                ModItems.CAP.get(),
                ModItems.VISOR1.get(),
                ModItems.GLASSES1.get(),
                ModItems.GLASSES2.get(),
                ModItems.MONOVISOR.get(),
                ModItems.MAXTAC_HELMET.get(),
                ModItems.TRAUMA_TEAM_HELMET.get(),
                ModItems.ONI_MASK.get(),

                ModItems.LEATHER_JACKET1.get(),
                ModItems.EMT_JACKET.get(),
                ModItems.HIGH_CROPPED_LEATHER_JACKET.get(),
                ModItems.LABCOAT1.get(),
                ModItems.LABCOAT2.get(),
                ModItems.BAGGY_SWEATSHIRT.get(),

                ModItems.NETRUNNING_BODYSUIT.get(),
                ModItems.NETRUNNING_UNITARD.get(),
                ModItems.MAXTAC_BODYSUIT.get(),
                ModItems.TRAUMA_TEAM_PADDED_ARMOR.get(),

                ModItems.MESSY_TIE.get(),
                ModItems.TIE.get(),

                ModItems.CAMO_PANTS.get()
        );
    }
}