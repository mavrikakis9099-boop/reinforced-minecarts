package atonkish.reinfcore;

import atonkish.reinfcore.client.gui.screen.ReinforcedStorageScreen;
import atonkish.reinfcore.screen.ReinforcedStorageMenuTypes;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

/** Registers migration-build screens after the common storage modules have registered their menus. */
public final class ReinforcedCoreClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ReinforcedStorageMenuTypes.SINGLE_MAP.values().forEach(
                type -> MenuScreens.register(type, ReinforcedStorageScreen::new));
        ReinforcedStorageMenuTypes.DOUBLE_MAP.values().forEach(
                type -> MenuScreens.register(type, ReinforcedStorageScreen::new));
    }
}
