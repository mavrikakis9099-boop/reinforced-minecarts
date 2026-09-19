package atonkish.reinfchest.util;

import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.ReinforcingMaterials;

import net.minecraft.world.item.Items;

/** Tier definitions recovered from Reinforced Chests 4.0.0-beta+1.21.11. */
public enum ReinforcingMaterialSettings {
    COPPER(ReinforcingMaterials.register("copper", 45, Items.COPPER_INGOT)),
    IRON(ReinforcingMaterials.register("iron", 54, Items.IRON_INGOT)),
    GOLD(ReinforcingMaterials.register("gold", 81, Items.GOLD_INGOT)),
    DIAMOND(ReinforcingMaterials.register("diamond", 108, Items.DIAMOND)),
    NETHERITE(ReinforcingMaterials.register("netherite", 108, Items.NETHERITE_INGOT));

    private final ReinforcingMaterial material;

    ReinforcingMaterialSettings(ReinforcingMaterial material) {
        this.material = material;
    }

    public ReinforcingMaterial getMaterial() {
        return material;
    }
}
