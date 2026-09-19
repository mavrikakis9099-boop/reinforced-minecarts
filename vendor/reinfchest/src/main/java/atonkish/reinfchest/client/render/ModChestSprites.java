package atonkish.reinfchest.client.render;

import atonkish.reinfcore.util.ReinforcingMaterial;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;

/** Resolves the original reinforced-chest atlas paths for single and double chests. */
public final class ModChestSprites {
    private ModChestSprites() {}

    public static SpriteId get(ReinforcingMaterial material, ChestType type) {
        String variant = switch (type) {
            case LEFT -> "left";
            case RIGHT -> "right";
            case SINGLE -> "single";
        };

        return new SpriteId(
                Sheets.CHEST_SHEET,
                Identifier.fromNamespaceAndPath(
                        "reinfchest",
                        "entity/chest/" + material.getName() + "/" + variant
                )
        );
    }
}
