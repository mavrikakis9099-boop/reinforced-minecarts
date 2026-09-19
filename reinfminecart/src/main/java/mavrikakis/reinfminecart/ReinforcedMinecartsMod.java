package mavrikakis.reinfminecart;

import atonkish.reinfchest.util.ReinforcingMaterialSettings;

import mavrikakis.reinfminecart.entity.ModEntityTypes;
import mavrikakis.reinfminecart.item.ModItems;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Capacity-focused reinforced chest minecarts for Minecraft 26.2. */
public final class ReinforcedMinecartsMod implements ModInitializer {
    public static final String MOD_ID = "reinfminecart";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        for (ReinforcingMaterialSettings settings : ReinforcingMaterialSettings.values()) {
            var material = settings.getMaterial();
            var entityType = ModEntityTypes.registerMaterial(material);
            ModItems.registerMaterial(material, entityType);
        }

        LOGGER.info(
                "Registered {} reinforced minecart tiers",
                ReinforcingMaterialSettings.values().length
        );
    }
}
