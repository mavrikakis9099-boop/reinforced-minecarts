package mavrikakis.reinfminecart.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import atonkish.reinfcore.util.ReinforcingMaterial;

import mavrikakis.reinfminecart.ReinforcedMinecartsMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/** Entity types with the same physical and tracking properties as a vanilla chest minecart. */
public final class ModEntityTypes {
    public static final Map<ReinforcingMaterial, EntityType<ReinforcedMinecart>>
            REINFORCED_MINECART_MAP = new LinkedHashMap<>();

    private ModEntityTypes() {
    }

    public static EntityType<ReinforcedMinecart> registerMaterial(ReinforcingMaterial material) {
        return REINFORCED_MINECART_MAP.computeIfAbsent(material, key -> {
            String path = key.getName() + "_chest_minecart";
            Identifier id = Identifier.fromNamespaceAndPath(ReinforcedMinecartsMod.MOD_ID, path);
            ResourceKey<EntityType<?>> entityKey = ResourceKey.create(Registries.ENTITY_TYPE, id);

            EntityType<ReinforcedMinecart> entityType = EntityType.Builder
                    .<ReinforcedMinecart>of(
                            (type, level) -> new ReinforcedMinecart(type, level, key),
                            MobCategory.MISC
                    )
                    .noLootTable()
                    .sized(0.98F, 0.7F)
                    .passengerAttachments(0.1875F)
                    .clientTrackingRange(8)
                    .build(entityKey);

            return Registry.register(BuiltInRegistries.ENTITY_TYPE, entityKey, entityType);
        });
    }
}
