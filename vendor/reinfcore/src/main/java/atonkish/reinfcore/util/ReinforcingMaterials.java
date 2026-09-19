package atonkish.reinfcore.util;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.item.Item;

/** Stable insertion-ordered registry used by all three storage modules. */
public final class ReinforcingMaterials {
    public static final Map<String, ReinforcingMaterial> MAP = new LinkedHashMap<>();

    private ReinforcingMaterials() {
    }

    public static ReinforcingMaterial register(String name, int size, Item ingredient) {
        MAP.computeIfAbsent(name, key -> new ReinforcingMaterial(key, size, ingredient));
        return MAP.get(name);
    }
}
