package mavrikakis.reinfminecart;

import mavrikakis.reinfminecart.client.render.ReinforcedMinecartRenderer;
import mavrikakis.reinfminecart.entity.ModEntityTypes;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.renderer.entity.EntityRenderers;

/** Registers one tier-aware renderer for each reinforced minecart entity type. */
public final class ReinforcedMinecartsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityTypes.REINFORCED_MINECART_MAP.forEach((material, entityType) ->
                EntityRenderers.register(
                        entityType,
                        context -> new ReinforcedMinecartRenderer(context, material)
                )
        );
    }
}
