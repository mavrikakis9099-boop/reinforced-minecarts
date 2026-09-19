package atonkish.reinfchest;

import atonkish.reinfchest.block.ModBlocks;
import atonkish.reinfchest.block.entity.ModBlockEntityTypes;
import atonkish.reinfchest.stat.ModStats;
import atonkish.reinfchest.util.ReinforcingMaterialSettings;
import atonkish.reinfcore.screen.ReinforcedStorageMenuTypes;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Compatibility-first Fabric 26.2 port of Aton-Kish Reinforced Chests. */
public final class ReinforcedChestsMod implements ModInitializer {
    public static final String MOD_ID = "reinfchest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        for (ReinforcingMaterialSettings settings : ReinforcingMaterialSettings.values()) {
            var material = settings.getMaterial();
            ModStats.registerMaterialOpen(MOD_ID, material);
            ReinforcedStorageMenuTypes.registerSingle(material);
            ReinforcedStorageMenuTypes.registerDouble(material);
            ModBlocks.registerMaterial(material);
            ModBlockEntityTypes.registerMaterial(material);
        }
        LOGGER.info("Registered {} reinforced chest tiers for Minecraft 26.2 compatibility", ReinforcingMaterialSettings.values().length);
    }
}
