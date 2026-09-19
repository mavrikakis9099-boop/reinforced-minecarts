package atonkish.reinfcore.screen;

import atonkish.reinfcore.ReinforcedCoreMod;
import atonkish.reinfcore.mixin.SlotAccessor;
import atonkish.reinfcore.util.ReinforcedStorageScreenModel;
import atonkish.reinfcore.util.ReinforcedStorageScreenType;
import atonkish.reinfcore.util.ReinforcingMaterial;
import atonkish.reinfcore.util.math.Point2i;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Arbitrary-size reinforced storage menu with the upstream Single and Scroll presentations.
 * Slot order and container indices never change; Scroll only changes client-facing coordinates.
 */
public final class ReinforcedStorageMenu extends AbstractContainerMenu implements ReinforcedStorageScreenHandler {
    private static final int SLOT_SIZE = 18;
    private static final int HIDDEN_SLOT_COORDINATE = Integer.MIN_VALUE;
    private static final int PLAYER_HOTBAR_SIZE = 9;
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;

    private final ReinforcingMaterial material;
    private final Container container;
    private final boolean doubleBlock;
    private final ReinforcedStorageScreenModel screenModel;
    private final int columns;
    private final int visibleRows;

    /** Client constructor used by the registered MenuType. */
    public ReinforcedStorageMenu(MenuType<?> menuType, ReinforcingMaterial material, boolean doubleBlock, int containerId, Inventory inventory) {
        this(menuType, material, doubleBlock, containerId, inventory,
                new SimpleContainer(ReinforcedStorageScreenModel.getContainerInventorySize(material, doubleBlock)));
    }

    /** Server constructor used by reinforced block entities. */
    public ReinforcedStorageMenu(
            MenuType<?> menuType,
            ReinforcingMaterial material,
            boolean doubleBlock,
            int containerId,
            Inventory playerInventory,
            Container container
    ) {
        super(menuType, containerId);
        this.material = material;
        this.container = container;
        this.doubleBlock = doubleBlock;

        int expectedSize = ReinforcedStorageScreenModel.getContainerInventorySize(material, doubleBlock);
        if (container.getContainerSize() != expectedSize) {
            throw new IllegalArgumentException(
                    "Container size " + container.getContainerSize() + " does not match reinforced tier "
                            + material.getName() + " (" + expectedSize + ")"
            );
        }

        container.startOpen(playerInventory.player);

        this.screenModel = new ReinforcedStorageScreenModel(material, doubleBlock);
        Point2i containerPoint = screenModel.getContainerInventoryPoint();
        Point2i playerPoint = screenModel.getPlayerInventoryPoint();
        int containerSize = expectedSize;
        this.columns = ReinforcedStorageScreenModel.getContainerInventoryColumns(containerSize);
        this.visibleRows = ReinforcedStorageScreenModel.getContainerInventoryRows(containerSize, columns);
        int rows = (containerSize + columns - 1) / columns;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int index = column + row * columns;
                if (index >= containerSize) {
                    break;
                }

                int x = containerPoint.getX() + column * SLOT_SIZE;
                int y = containerPoint.getY() + row * SLOT_SIZE;
                if (isScrollMode() && row >= visibleRows) {
                    x = HIDDEN_SLOT_COORDINATE;
                    y = HIDDEN_SLOT_COORDINATE;
                }
                addSlot(new Slot(container, index, x, y));
            }
        }

        // Player main inventory.
        for (int row = 0; row < PLAYER_INVENTORY_ROWS; row++) {
            for (int column = 0; column < PLAYER_INVENTORY_COLUMNS; column++) {
                addSlot(new Slot(playerInventory, column + row * PLAYER_INVENTORY_COLUMNS + PLAYER_HOTBAR_SIZE,
                        playerPoint.getX() + column * SLOT_SIZE,
                        playerPoint.getY() + row * SLOT_SIZE));
            }
        }

        // Hotbar.
        int hotbarY = playerPoint.getY() + 58;
        for (int column = 0; column < PLAYER_HOTBAR_SIZE; column++) {
            addSlot(new Slot(playerInventory, column,
                    playerPoint.getX() + column * SLOT_SIZE,
                    hotbarY));
        }
    }

    public ReinforcingMaterial getMaterial() {
        return material;
    }

    /** Underlying world container; required by the vanilla-style opener counters. */
    public Container getContainer() {
        return container;
    }

    public boolean isDoubleBlock() {
        return doubleBlock;
    }

    public int getColumns() {
        return columns;
    }

    public int getVisibleRows() {
        return visibleRows;
    }

    /** Move the existing storage slots to the requested visible row window. */
    public void scrollItems(float position) {
        if (!isScrollMode()) {
            return;
        }

        int startRow = Math.round(position * getMaximumScrollRows());
        Point2i containerPoint = screenModel.getContainerInventoryPoint();
        for (int index = 0; index < container.getContainerSize(); index++) {
            int column = index % columns;
            int row = index / columns;
            int x = containerPoint.getX() + column * SLOT_SIZE;
            int y = containerPoint.getY() + (row - startRow) * SLOT_SIZE;
            if (row < startRow || row >= startRow + visibleRows) {
                x = HIDDEN_SLOT_COORDINATE;
                y = HIDDEN_SLOT_COORDINATE;
            }

            SlotAccessor slot = (SlotAccessor) slots.get(index);
            slot.reinfcore$setX(x);
            slot.reinfcore$setY(y);
        }
    }

    public boolean shouldShowScrollbar() {
        return isScrollMode() && getMaximumScrollRows() > 0;
    }

    private boolean isScrollMode() {
        return ReinforcedCoreMod.CONFIG.screenType == ReinforcedStorageScreenType.SCROLL;
    }

    private int getMaximumScrollRows() {
        int totalRows = (container.getContainerSize() + columns - 1) / columns;
        return Math.max(0, totalRows - visibleRows);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) {
            return result;
        }

        ItemStack source = slot.getItem();
        result = source.copy();
        int containerSlots = container.getContainerSize();
        int inventoryEnd = containerSlots + Inventory.INVENTORY_SIZE;

        if (slotIndex < containerSlots) {
            if (!moveItemStackTo(source, containerSlots, inventoryEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(source, 0, containerSlots, false)) {
            return ItemStack.EMPTY;
        }

        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }
}
