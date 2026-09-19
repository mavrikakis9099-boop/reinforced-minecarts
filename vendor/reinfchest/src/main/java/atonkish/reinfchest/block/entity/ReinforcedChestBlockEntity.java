package atonkish.reinfchest.block.entity;

import java.util.List;

import atonkish.reinfcore.screen.ReinforcedStorageMenu;
import atonkish.reinfcore.screen.ReinforcedStorageMenuTypes;
import atonkish.reinfcore.util.ReinforcingMaterial;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

/** Migration-compatible reinforced chest backed by vanilla chest serialization. */
public final class ReinforcedChestBlockEntity extends ChestBlockEntity {
    private final ReinforcingMaterial cachedMaterial;

    /**
     * Mirrors the legacy 4.0.0-beta counter so single and compound reinforced chest menus keep
     * vanilla lid animation, viewer counts and open/close events alive with the custom menu class.
     */
    private final ContainerOpenersCounter reinforcedOpenersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            playChestSound(level, pos, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            playChestSound(level, pos, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int previous, int current) {
            signalOpenCount(level, pos, state, previous, current);
        }

        @Override
        public boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ReinforcedStorageMenu menu)) {
                return false;
            }

            Container container = menu.getContainer();
            if (container == ReinforcedChestBlockEntity.this) {
                return true;
            }
            return container instanceof CompoundContainer compound
                    && compound.contains(ReinforcedChestBlockEntity.this);
        }
    };

    public ReinforcedChestBlockEntity(ReinforcingMaterial material, BlockPos pos, BlockState state) {
        super(requireType(material), pos, state);
        this.cachedMaterial = material;

        // Matches 1.21.11: establish the tier capacity before vanilla loads saved Items.
        setItems(NonNullList.withSize(material.getSize(), ItemStack.EMPTY));
    }

    private static net.minecraft.world.level.block.entity.BlockEntityType<?> requireType(ReinforcingMaterial material) {
        var type = ModBlockEntityTypes.REINFORCED_CHEST_MAP.get(material);
        if (type == null) {
            throw new IllegalStateException("Reinforced chest block entity type not registered for " + material);
        }
        return type;
    }

    @Override
    public int getContainerSize() {
        return cachedMaterial.getSize();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.reinfchest." + cachedMaterial.getName() + "Chest");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        var menuType = ReinforcedStorageMenuTypes.SINGLE_MAP.get(cachedMaterial);
        if (menuType == null) {
            throw new IllegalStateException("Single chest menu not registered for " + cachedMaterial);
        }
        return new ReinforcedStorageMenu(menuType, cachedMaterial, false, containerId, inventory, this);
    }

    @Override
    public void startOpen(ContainerUser containerUser) {
        if (isRemoved()) {
            return;
        }
        var living = containerUser.getLivingEntity();
        if (living.isSpectator() || level == null) {
            return;
        }
        reinforcedOpenersCounter.incrementOpeners(
                living, level, worldPosition, getBlockState(), containerUser.getContainerInteractionRange());
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        if (isRemoved()) {
            return;
        }
        var living = containerUser.getLivingEntity();
        if (living.isSpectator() || level == null) {
            return;
        }
        reinforcedOpenersCounter.decrementOpeners(living, level, worldPosition, getBlockState());
    }

    @Override
    public List<ContainerUser> getEntitiesWithContainerOpen() {
        if (level == null) {
            return List.of();
        }
        return reinforcedOpenersCounter.getEntitiesWithContainerOpen(level, worldPosition);
    }

    @Override
    public void recheckOpen() {
        if (!isRemoved() && level != null) {
            reinforcedOpenersCounter.recheckOpeners(level, worldPosition, getBlockState());
        }
    }

    private static void playChestSound(Level level, BlockPos pos, net.minecraft.sounds.SoundEvent sound) {
        // Position-centred sound is intentionally conservative for the migration build; the legacy
        // half-chest positional offset is cosmetic and can be restored after data integrity passes.
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    public ReinforcingMaterial getMaterial() {
        return cachedMaterial;
    }
}
