package mavrikakis.reinfminecart.entity;

import atonkish.reinfcore.screen.ReinforcedStorageMenu;
import atonkish.reinfcore.screen.ReinforcedStorageMenuTypes;
import atonkish.reinfcore.util.ReinforcingMaterial;

import mavrikakis.reinfminecart.item.ModItems;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;

/**
 * A vanilla chest minecart whose only gameplay difference is tier-derived storage capacity.
 *
 * <p>Rail movement, collisions, hopper access, comparator output, persistence, loot-table support,
 * interaction, destruction and content drops remain owned by {@link MinecartChest}.</p>
 */
public final class ReinforcedMinecart extends MinecartChest {
    private final ReinforcingMaterial material;

    public ReinforcedMinecart(
            EntityType<? extends ReinforcedMinecart> type,
            Level level,
            ReinforcingMaterial material
    ) {
        super(type, level);
        this.material = material;

        // AbstractMinecartContainer creates a fixed bootstrap list. Resize it immediately so every
        // access path, including hoppers and world loading, sees the authoritative tier capacity.
        clearItemStacks();
    }

    @Override
    protected Item getDropItem() {
        Item item = ModItems.REINFORCED_MINECART_MAP.get(material);
        if (item == null) {
            throw new IllegalStateException("Reinforced minecart item not registered for " + material);
        }
        return item;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }

    @Override
    public int getContainerSize() {
        return material.getSize();
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        // The custom renderer replaces this vanilla chest model with the tier sprite. Retaining a
        // non-empty vanilla display state keeps the parent minecart rendering pipeline intact.
        return Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        var menuType = ReinforcedStorageMenuTypes.SINGLE_MAP.get(material);
        if (menuType == null) {
            throw new IllegalStateException("Reinforced storage menu not registered for " + material);
        }
        return new ReinforcedStorageMenu(
                menuType,
                material,
                false,
                containerId,
                inventory,
                this
        );
    }

    public ReinforcingMaterial getMaterial() {
        return material;
    }
}
