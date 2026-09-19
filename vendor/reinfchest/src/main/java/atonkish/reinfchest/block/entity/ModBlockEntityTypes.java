package atonkish.reinfchest.block.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfcore.util.ReinforcingMaterial;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Exact legacy block-entity IDs, one type per reinforcing tier. */
public final class ModBlockEntityTypes {
    public static final Map<ReinforcingMaterial, BlockEntityType<ReinforcedChestBlockEntity>> REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    private ModBlockEntityTypes() {
    }

    public static BlockEntityType<ReinforcedChestBlockEntity> registerMaterial(ReinforcingMaterial material) {
        return REINFORCED_CHEST_MAP.computeIfAbsent(material, key -> {
            Identifier id = Identifier.fromNamespaceAndPath(ReinforcedChestsMod.MOD_ID, key.getName() + "_chest");
            var block = ModBlocks.REINFORCED_CHEST_MAP.get(key);
            if (block == null) {
                throw new IllegalStateException("Block must be registered before its block entity: " + id);
            }
            BlockEntityType<ReinforcedChestBlockEntity> type = FabricBlockEntityTypeBuilder
                    .create((pos, state) -> new ReinforcedChestBlockEntity(key, pos, state), block)
                    .build();
            return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type);
        });
    }
}
