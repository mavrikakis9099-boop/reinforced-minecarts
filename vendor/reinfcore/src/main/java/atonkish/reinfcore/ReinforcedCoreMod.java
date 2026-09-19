package atonkish.reinfcore;

import atonkish.reinfcore.item.ModCreativeModeTabs;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

/** Shared compatibility layer for the Reinforced Storage 26.2 port. */
public final class ReinforcedCoreMod implements ModInitializer {
    public static final String MOD_ID = "reinfcore";
    public static ReinforcedCoreConfig CONFIG = new ReinforcedCoreConfig();

    @Override
    public void onInitialize() {
        AutoConfig.register(ReinforcedCoreConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ReinforcedCoreConfig.class).getConfig();
        ModCreativeModeTabs.init();
    }
}
