package com.perigrine3.styleoversubstance.styleoversubstance.block.entity;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Styleoversubstance.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GarmentTableBlockEntity>> GARMENT_TABLE = BLOCK_ENTITIES.register("garment_table",
            () -> BlockEntityType.Builder.of(GarmentTableBlockEntity::new, ModBlocks.GARMENT_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}