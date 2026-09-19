package atonkish.reinfcore.item;

import atonkish.reinfcore.ReinforcedCoreMod;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

/** Restores the original Reinforced Storage creative tab and vanilla-tab membership. */
public final class ModCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> REINFORCED_STORAGE_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(ReinforcedCoreMod.MOD_ID, "reinforced_storage")
    );

    public static final CreativeModeTab REINFORCED_STORAGE = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(Items.CHEST))
            .title(Component.translatable("itemGroup.reinfcore.reinforced_storage"))
            .build();

    private static boolean initialized;

    private ModCreativeModeTabs() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, REINFORCED_STORAGE_KEY, REINFORCED_STORAGE);
        initialized = true;
    }

    /**
     * The 1.21.11 mods exposed every storage item in Functional Blocks, Redstone Blocks, and the
     * dedicated Reinforced Storage tab. Fabric 26.2 renamed this event API from item-group entries
     * to creative-tab output, but the behavior remains the same.
     */
    public static void addStorageItem(ItemLike item) {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register(output -> output.accept(item));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS)
                .register(output -> output.accept(item));
        CreativeModeTabEvents.modifyOutputEvent(REINFORCED_STORAGE_KEY)
                .register(output -> output.accept(item));
    }
}
