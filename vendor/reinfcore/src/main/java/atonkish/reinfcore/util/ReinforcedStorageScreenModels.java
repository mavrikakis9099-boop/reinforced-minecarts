package atonkish.reinfcore.util;

import java.util.LinkedHashMap;
import java.util.Map;

/** Exact map/register-once semantics from Reinforced Core 4.0.9. */
public final class ReinforcedStorageScreenModels {
    public static final Map<ReinforcingMaterial, ReinforcedStorageScreenModel> SINGLE_MAP = new LinkedHashMap<>();
    public static final Map<ReinforcingMaterial, ReinforcedStorageScreenModel> DOUBLE_MAP = new LinkedHashMap<>();

    private ReinforcedStorageScreenModels() {
    }

    public static ReinforcedStorageScreenModel registerMaterialSingleBlock(ReinforcingMaterial material) {
        SINGLE_MAP.computeIfAbsent(material, key -> new ReinforcedStorageScreenModel(key, false));
        return SINGLE_MAP.get(material);
    }

    public static ReinforcedStorageScreenModel registerMaterialDoubleBlock(ReinforcingMaterial material) {
        DOUBLE_MAP.computeIfAbsent(material, key -> new ReinforcedStorageScreenModel(key, true));
        return DOUBLE_MAP.get(material);
    }
}
