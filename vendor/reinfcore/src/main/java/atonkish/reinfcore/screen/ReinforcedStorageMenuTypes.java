package atonkish.reinfcore.screen;

import java.util.LinkedHashMap;
import java.util.Map;

import atonkish.reinfcore.ReinforcedCoreMod;
import atonkish.reinfcore.util.ReinforcingMaterial;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

/** Registers the same menu identifiers used by Reinforced Core 4.0.9. */
public final class ReinforcedStorageMenuTypes {
    public static final Map<ReinforcingMaterial, MenuType<ReinforcedStorageMenu>> SINGLE_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, MenuType<ReinforcedStorageMenu>> DOUBLE_MAP = new LinkedHashMap<>();

    private ReinforcedStorageMenuTypes() {
    }

    public static MenuType<ReinforcedStorageMenu> registerSingle(ReinforcingMaterial material) {
        return SINGLE_MAP.computeIfAbsent(material, key -> {
            String path = "single_" + key.getName() + "_block";
            Identifier id = Identifier.fromNamespaceAndPath(ReinforcedCoreMod.MOD_ID, path);
            MenuType<ReinforcedStorageMenu> type = new MenuType<>(
                    (containerId, inventory) -> new ReinforcedStorageMenu(SINGLE_MAP.get(key), key, false, containerId, inventory),
                    FeatureFlagSet.of()
            );
            Registry.register(BuiltInRegistries.MENU, id, type);
            return type;
        });
    }

    public static MenuType<ReinforcedStorageMenu> registerDouble(ReinforcingMaterial material) {
        return DOUBLE_MAP.computeIfAbsent(material, key -> {
            String path = "double_" + key.getName() + "_block";
            Identifier id = Identifier.fromNamespaceAndPath(ReinforcedCoreMod.MOD_ID, path);
            MenuType<ReinforcedStorageMenu> type = new MenuType<>(
                    (containerId, inventory) -> new ReinforcedStorageMenu(DOUBLE_MAP.get(key), key, true, containerId, inventory),
                    FeatureFlagSet.of()
            );
            Registry.register(BuiltInRegistries.MENU, id, type);
            return type;
        });
    }
}
