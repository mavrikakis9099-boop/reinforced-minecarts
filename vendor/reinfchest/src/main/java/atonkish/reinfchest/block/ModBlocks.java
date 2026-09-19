package atonkish.reinfchest.block;

import java.util.LinkedHashMap;
import java.util.Map;

import atonkish.reinfchest.ReinforcedChestsMod;
import atonkish.reinfcore.item.ModCreativeModeTabs;
import atonkish.reinfcore.util.ReinforcingMaterial;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Exact legacy chest block/item IDs. */
public final class ModBlocks {
    public static final Map<ReinforcingMaterial, ReinforcedChestBlock> REINFORCED_CHEST_MAP = new LinkedHashMap<>();

    private ModBlocks() {
    }

    public static ReinforcedChestBlock registerMaterial(ReinforcingMaterial material) {
        return REINFORCED_CHEST_MAP.computeIfAbsent(material, key -> {
            String path = key.getName() + "_chest";
            Identifier id = Identifier.fromNamespaceAndPath(ReinforcedChestsMod.MOD_ID, path);
            ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

            BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST).setId(blockKey);
            ReinforcedChestBlock block = new ReinforcedChestBlock(key, properties);
            Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

            Item.Properties itemProperties = new Item.Properties().useBlockDescriptionPrefix().setId(itemKey);
            if ("netherite".equals(key.getName())) {
                itemProperties = itemProperties.fireResistant();
            }
            BlockItem blockItem = new BlockItem(block, itemProperties);
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
            ModCreativeModeTabs.addStorageItem(blockItem);
            return block;
        });
    }
}
