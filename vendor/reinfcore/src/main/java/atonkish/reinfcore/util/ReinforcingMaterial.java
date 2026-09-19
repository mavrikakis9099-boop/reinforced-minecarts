package atonkish.reinfcore.util;

import net.minecraft.world.item.Item;

/** Reinforcing tier definition. Field semantics match Reinforced Core 4.0.9. */
public final class ReinforcingMaterial {
    private final String name;
    private final int size;
    private final Item ingredient;

    public ReinforcingMaterial(String name, int size, Item ingredient) {
        this.name = name;
        this.size = size;
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Item getIngredient() {
        return ingredient;
    }
}
