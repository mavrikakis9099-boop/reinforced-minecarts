package atonkish.reinfcore.integration.modmenu;

import atonkish.reinfcore.ReinforcedCoreConfig;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/** Restores the upstream Reinforced Core configuration entry in Mod Menu. */
@Environment(EnvType.CLIENT)
public final class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfigClient.getConfigScreen(ReinforcedCoreConfig.class, parent).get();
    }
}
