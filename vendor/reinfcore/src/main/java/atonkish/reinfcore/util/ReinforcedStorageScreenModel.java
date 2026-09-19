package atonkish.reinfcore.util;

import atonkish.reinfcore.ReinforcedCoreMod;
import atonkish.reinfcore.util.math.Point2i;

/**
 * Exact layout mathematics reconstructed from Reinforced Core 4.0.9+1.21.11 bytecode.
 */
public final class ReinforcedStorageScreenModel {
    private static final int SLOT_SIZE = 18;
    private static final int CONTAINER_INVENTORY_X = 7;
    private static final int CONTAINER_INVENTORY_Y = 17;
    private static final int GAP_BETWEEN_CONTAINER_INVENTORY_AND_PLAYER_INVENTORY = 14;
    private static final int SINGLE_SCREEN_THRESHOLD_SIZE = 81;
    private static final int SINGLE_SCREEN_DEFAULT_COLS = 9;
    private static final int SCROLL_SCREEN_COLS = 9;

    private final ReinforcingMaterial material;
    private final boolean isDoubleBlock;

    public ReinforcedStorageScreenModel(ReinforcingMaterial material, boolean isDoubleBlock) {
        this.material = material;
        this.isDoubleBlock = isDoubleBlock;
    }

    public ReinforcingMaterial getMaterial() {
        return material;
    }

    public boolean getIsDoubleBlock() {
        return isDoubleBlock;
    }

    public Point2i getContainerInventoryPoint() {
        return new Point2i(CONTAINER_INVENTORY_X, CONTAINER_INVENTORY_Y);
    }

    public Point2i getPlayerInventoryPoint() {
        int size = getContainerInventorySize(material, isDoubleBlock);
        int columns = getContainerInventoryColumns(size);
        int rows = getContainerInventoryRows(size, columns);
        int x = CONTAINER_INVENTORY_X + (columns - SINGLE_SCREEN_DEFAULT_COLS) * SLOT_SIZE / 2;
        int y = CONTAINER_INVENTORY_Y + rows * SLOT_SIZE
                + GAP_BETWEEN_CONTAINER_INVENTORY_AND_PLAYER_INVENTORY;
        return new Point2i(x, y);
    }

    public static int getContainerInventorySize(ReinforcingMaterial material, boolean isDoubleBlock) {
        return isDoubleBlock ? material.getSize() * 2 : material.getSize();
    }

    public static int getContainerInventoryColumns(int size) {
        if (ReinforcedCoreMod.CONFIG.screenType == ReinforcedStorageScreenType.SCROLL) {
            return SCROLL_SCREEN_COLS;
        }
        return size <= SINGLE_SCREEN_THRESHOLD_SIZE ? SINGLE_SCREEN_DEFAULT_COLS : size / 9;
    }

    public static int getContainerInventoryRows(int size, int columns) {
        int rows = size / columns;
        if (ReinforcedCoreMod.CONFIG.screenType == ReinforcedStorageScreenType.SCROLL
                && rows > ReinforcedCoreMod.CONFIG.scrollScreen.rows) {
            return ReinforcedCoreMod.CONFIG.scrollScreen.rows;
        }
        return rows;
    }
}
