package mavrikakis.reinfminecart.item;

import java.util.LinkedHashMap;
import java.util.Map;

import atonkish.reinfcore.item.ModCreativeModeTabs;
import atonkish.reinfcore.util.ReinforcingMaterial;

import mavrikakis.reinfminecart.ReinforcedMinecartsMod;
import mavrikakis.reinfminecart.entity.ReinforcedMinecart;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MinecartItem;

/** Tiered minecart items using vanilla placement behaviour. */
public final class ModItems {
    public static final Map<ReinforcingMaterial, Item> REINFORCED_MINECART_MAP =
            new LinkedHashMap<>();

    private ModItems() {
    }

    public static Item registerMaterial(
            ReinforcingMaterial material,
            EntityType<ReinforcedMinecart> entityType
    ) {
        return REINFORCED_MINECART_MAP.computeIfAbsent(material, key -> {
            String path = key.getName() + "_chest_minecart";
            Identifier id = Identifier.fromNamespaceAndPath(ReinforcedMinecartsMod.MOD_ID, path);
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

            Item item = new MinecartItem(
                    entityType,
                    new Item.Properties().stacksTo(1).setId(itemKey)
            );
            Registry.register(BuiltInRegistries.ITEM, itemKey, item);

            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                    .register(output -> output.accept(item));
            CreativeModeTabEvents.modifyOutputEvent(ModCreativeModeTabs.REINFORCED_STORAGE_KEY)
                    .register(output -> output.accept(item));
            return item;
        });
    }
}
