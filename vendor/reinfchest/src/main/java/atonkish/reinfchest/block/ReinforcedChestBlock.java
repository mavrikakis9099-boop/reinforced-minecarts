package atonkish.reinfchest.block;

import atonkish.reinfchest.block.entity.ModBlockEntityTypes;
import atonkish.reinfchest.block.entity.ReinforcedChestBlockEntity;
import atonkish.reinfchest.stat.ModStats;
import atonkish.reinfcore.screen.ReinforcedStorageMenu;
import atonkish.reinfcore.screen.ReinforcedStorageMenuTypes;
import atonkish.reinfcore.util.ReinforcingMaterial;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/**
 * Vanilla chest mechanics with the original reinforced tier identity and arbitrary-sized menu.
 * Vanilla ChestBlock still owns placement, pairing, lid state, blocking rules and comparator logic.
 */
public final class ReinforcedChestBlock extends ChestBlock {
    private final ReinforcingMaterial material;

    public ReinforcedChestBlock(ReinforcingMaterial material, BlockBehaviour.Properties properties) {
        super(
                () -> ModBlockEntityTypes.REINFORCED_CHEST_MAP.get(material),
                SoundEvents.CHEST_OPEN,
                SoundEvents.CHEST_CLOSE,
                properties
        );
        this.material = material;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReinforcedChestBlockEntity(material, pos, state);
    }

    /** Lets vanilla chest interaction award the exact legacy per-tier custom statistic. */
    @Override
    protected Stat<Identifier> getOpenChestStat() {
        return Stats.CUSTOM.get(ModStats.OPEN_REINFORCED_CHEST_MAP.get(material));
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        // Vanilla performs the hard part: blocked-chest checks, neighbor validation and combining
        // the two block entities into a CompoundContainer when this is a double chest.
        Container container = ChestBlock.getContainer(this, state, level, pos, false);
        if (container == null) {
            return null;
        }

        boolean doubleBlock = container.getContainerSize() == material.getSize() * 2;
        if (!doubleBlock && container.getContainerSize() != material.getSize()) {
            throw new IllegalStateException(
                    "Unexpected reinforced chest size " + container.getContainerSize() + " for " + material
            );
        }

        var menuType = doubleBlock
                ? ReinforcedStorageMenuTypes.DOUBLE_MAP.get(material)
                : ReinforcedStorageMenuTypes.SINGLE_MAP.get(material);
        if (menuType == null) {
            throw new IllegalStateException("Reinforced chest menu not registered for " + material);
        }

        Component title = resolveTitle(level, pos, doubleBlock);
        return new SimpleMenuProvider(
                (containerId, inventory, player) ->
                        new ReinforcedStorageMenu(menuType, material, doubleBlock, containerId, inventory, container),
                title
        );
    }

    private Component resolveTitle(Level level, BlockPos pos, boolean doubleBlock) {
        if (level.getBlockEntity(pos) instanceof ReinforcedChestBlockEntity chest && chest.hasCustomName()) {
            return chest.getDisplayName();
        }
        return Component.translatable(
                "container.reinfchest." + material.getName() + "Chest" + (doubleBlock ? "Double" : "")
        );
    }

    public ReinforcingMaterial getMaterial() {
        return material;
    }
}
