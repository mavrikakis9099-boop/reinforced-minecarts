package atonkish.reinfchest;

import atonkish.reinfchest.block.entity.ModBlockEntityTypes;
import atonkish.reinfchest.client.render.blockentity.ReinforcedChestRenderer;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

/** Registers tier-aware 26.2 chest renderers for every legacy reinforced chest type. */
public final class ReinforcedChestsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlockEntityTypes.REINFORCED_CHEST_MAP.forEach((material, type) ->
                BlockEntityRenderers.register(
                        type,
                        context -> new ReinforcedChestRenderer(context, material)
                )
        );

        ReinforcedChestsMod.LOGGER.info(
                "Registered tier-aware renderers for {} reinforced chest tiers",
                ModBlockEntityTypes.REINFORCED_CHEST_MAP.size()
        );
    }
}
