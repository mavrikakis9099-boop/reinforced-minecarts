package atonkish.reinfchest.stat;

import atonkish.reinfcore.util.ReinforcingMaterial;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

/** Legacy per-tier chest statistics, preserved under their exact 1.21.11 registry keys. */
public final class ModStats {
    public static final Map<ReinforcingMaterial, Identifier> OPEN_REINFORCED_CHEST_MAP =
            new LinkedHashMap<>();

    private ModStats() {
    }

    public static Identifier registerMaterialOpen(String namespace, ReinforcingMaterial material) {
        return OPEN_REINFORCED_CHEST_MAP.computeIfAbsent(
                material,
                key -> register(namespace, "open_" + key.getName() + "_chest", StatFormatter.DEFAULT)
        );
    }

    private static Identifier register(String namespace, String name, StatFormatter formatter) {
        Identifier identifier = Identifier.fromNamespaceAndPath(namespace, name);

        // Preserve the original minecraft:open_<tier>_chest registry keys stored in player stats.
        Registry.register(BuiltInRegistries.CUSTOM_STAT, name, identifier);
        Stats.CUSTOM.get(identifier, formatter);
        return identifier;
    }
}
