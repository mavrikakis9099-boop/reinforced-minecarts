package atonkish.reinfcore;

import atonkish.reinfcore.util.ReinforcedStorageScreenType;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

/** User-facing Reinforced Core settings, restored from upstream 4.0.9. */
@Config(name = ReinforcedCoreMod.MOD_ID)
public final class ReinforcedCoreConfig implements ConfigData {
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ReinforcedStorageScreenType screenType = ReinforcedStorageScreenType.SINGLE;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public ScrollScreen scrollScreen = new ScrollScreen();

    public static final class ScrollScreen {
        @ConfigEntry.BoundedDiscrete(min = 6, max = 9)
        public int rows = 6;
    }
}
