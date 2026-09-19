package atonkish.reinfcore.client.gui.screen;

import atonkish.reinfcore.screen.ReinforcedStorageMenu;
import atonkish.reinfcore.util.ReinforcedStorageScreenModel;
import atonkish.reinfcore.util.math.Point2i;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

/**
 * Resource-pack-aware 26.2 screen for reinforced barrels and chests.
 *
 * <p>Uses the standard generic_54 texture layout: a 17-pixel header, a 9 by 6 grid of
 * 18-pixel cells, and the fixed player panel at v=126. Whole cell regions and frame strips
 * are tiled, never enlarged from a single pixel. The screen owns slot BACKGROUNDS;
 * AbstractContainerScreen owns items, hover effects, tooltips and input. All draws use the
 * currently resolved texture, including resource-pack replacements after a resource reload.</p>
 */
public final class ReinforcedStorageScreen extends AbstractContainerScreen<ReinforcedStorageMenu> {
    private static final int SLOT_SIZE = 18;
    private static final int VANILLA_TEXTURE_SIZE = 256;
    private static final int VANILLA_CHEST_WIDTH = 176;
    private static final int VANILLA_GRID_X = 7;
    private static final int VANILLA_GRID_Y = 17;
    private static final int VANILLA_GRID_WIDTH = 9 * SLOT_SIZE;
    private static final int VANILLA_GRID_HEIGHT = 6 * SLOT_SIZE;
    private static final int VANILLA_ITEM_X = VANILLA_GRID_X + 1;
    private static final int VANILLA_ITEM_Y = VANILLA_GRID_Y + 1;
    private static final int VANILLA_PLAYER_SECTION_TEXTURE_Y = 126;
    private static final int VANILLA_PLAYER_SECTION_HEIGHT = 96;
    private static final int VANILLA_PLAYER_ITEM_Y = 14;
    private static final int SHOULDER_BORDER_HEIGHT = 3;
    private static final int SCROLLBAR_GAP = 4;
    private static final int SCROLLBAR_WIDTH = 14;
    private static final int SCROLLBAR_SOURCE_X = 174;
    private static final int SCROLLBAR_SOURCE_Y = 17;
    private static final int SCROLLBAR_SOURCE_HEIGHT = 112;
    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final Identifier VANILLA_CHEST_TEXTURE =
            Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final Identifier SCROLLBAR_BACKGROUND_TEXTURE =
            Identifier.withDefaultNamespace("textures/gui/container/creative_inventory/tab_items.png");
    private static final Identifier SCROLLER_TEXTURE =
            Identifier.withDefaultNamespace("container/creative_inventory/scroller");

    private float scrollPosition;
    private boolean scrolling;

    public ReinforcedStorageScreen(ReinforcedStorageMenu menu, Inventory inventory, Component title) {
        // Minecraft 26.2 makes imageWidth/imageHeight final. Compute the legacy-compatible
        // dimensions before construction and pass them through the new five-argument overload.
        super(menu, inventory, title, getImageWidth(menu), getImageHeight(menu));

        Point2i playerPoint = new ReinforcedStorageScreenModel(
                menu.getMaterial(), menu.isDoubleBlock()).getPlayerInventoryPoint();

        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = playerPoint.getX();
        this.inventoryLabelY = playerPoint.getY() - 11;
        this.scrollPosition = 0.0f;
        this.scrolling = false;
    }

    private static int getImageWidth(ReinforcedStorageMenu menu) {
        int width = Math.max(176, 14 + menu.getColumns() * SLOT_SIZE);
        return menu.shouldShowScrollbar() ? width + SCROLLBAR_GAP + SCROLLBAR_WIDTH : width;
    }

    private static int getImageHeight(ReinforcedStorageMenu menu) {
        return menu.getVisibleRows() * SLOT_SIZE + 114;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);

        drawVanillaCompatibleBackground(graphics);
        if (menu.shouldShowScrollbar()) {
            drawScrollbar(graphics);
        }
    }

    @Override
    protected void init() {
        super.init();
        alignNemoPlayerButtons();
    }

    /**
     * Nemo's default storage-inventory buttons are positioned from the screen's full
     * imageWidth. The 176-pixel player panel is centered under the storage grid, and Scroll
     * mode adds a scrollbar gutter, so the automatic lower row must follow that panel instead
     * of the container edge.
     *
     * <p>Only Nemo's four default right-offset positions are moved. An explicit Nemo x/y
     * configuration remains authoritative, and container-row buttons keep their normal
     * container-relative placement.</p>
     */
    private void alignNemoPlayerButtons() {
        int columns = menu.getColumns();
        int centeredPanelOffset = (columns - 9) * SLOT_SIZE / 2;
        int playerButtonShift = imageWidth - VANILLA_CHEST_WIDTH - centeredPanelOffset;
        if (playerButtonShift <= 0) {
            return;
        }

        int defaultPlayerButtonY = topPos + inventoryLabelY - 2;
        for (var child : children()) {
            if (!(child instanceof AbstractWidget widget)
                    || widget.getY() != defaultPlayerButtonY
                    || !isNemoDefaultInventoryButton(widget.getX())) {
                continue;
            }
            widget.setX(widget.getX() - playerButtonShift);
        }
    }

    private boolean isNemoDefaultInventoryButton(int widgetX) {
        int relativeX = widgetX - leftPos - imageWidth;
        return relativeX == -61 || relativeX == -47 || relativeX == -33 || relativeX == -19;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        // 26.2 no longer exposes the old titleLabelColor/inventoryLabelColor fields. Emit the
        // labels explicitly to retain the white labels accepted in the previous test builds.
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFFFF, false);
        graphics.text(
                this.font,
                this.playerInventoryTitle,
                this.inventoryLabelX,
                this.inventoryLabelY,
                0xFFFFFFFF,
                false
        );
    }

    private void drawVanillaCompatibleBackground(GuiGraphicsExtractor graphics) {
        int columns = menu.getColumns();
        int rows = menu.getVisibleRows();
        ReinforcedStorageScreenModel model = new ReinforcedStorageScreenModel(
                menu.getMaterial(), menu.isDoubleBlock());
        Point2i containerPoint = model.getContainerInventoryPoint();
        Point2i playerPoint = model.getPlayerInventoryPoint();

        // Anchor the painted cell INTERIORS to the actual legacy item coordinates, not to
        // a guessed screen origin. This leaves the 1-pixel bevel outside the 16-pixel item.
        int panelX = leftPos + containerPoint.getX() - VANILLA_ITEM_X;
        int panelY = topPos + containerPoint.getY() - VANILLA_ITEM_Y;
        int gridWidth = columns * SLOT_SIZE;
        int gridHeight = rows * SLOT_SIZE;
        int panelWidth = 2 * VANILLA_GRID_X + gridWidth;
        int bodyY = panelY + VANILLA_GRID_Y;
        int bodyBottom = bodyY + gridHeight;
        int playerPanelX = leftPos + playerPoint.getX() - VANILLA_ITEM_X;
        int playerPanelY = topPos + playerPoint.getY() - VANILLA_PLAYER_ITEM_Y;

        // Preserve both halves of the title/header artwork; insert repeatable center strips
        // only when wider than vanilla. Do not repeat the title plaque along the extension.
        drawHorizontalStrip(graphics, panelX, panelY, panelWidth, 0, VANILLA_GRID_Y, 88);

        // Every cell comes from generic_54 itself, including its bevel and spacing. Tiling
        // the full 9x6 region preserves native variation; partial tiles end on whole cells.
        // No separate container/slot sprite is overlaid, and no inheritance assumption is made.
        blitTiled(graphics, panelX + VANILLA_GRID_X, bodyY, gridWidth, gridHeight,
                VANILLA_GRID_X, VANILLA_GRID_Y, VANILLA_GRID_WIDTH, VANILLA_GRID_HEIGHT);
        blitTiled(graphics, panelX, bodyY, VANILLA_GRID_X, gridHeight,
                0, VANILLA_GRID_Y, VANILLA_GRID_X, VANILLA_GRID_HEIGHT);
        blitTiled(graphics, panelX + VANILLA_GRID_X + gridWidth, bodyY,
                VANILLA_GRID_X, gridHeight, VANILLA_CHEST_WIDTH - VANILLA_GRID_X,
                VANILLA_GRID_Y, VANILLA_GRID_X, VANILLA_GRID_HEIGHT);

        // Keep the vanilla seam above the centered player panel. Only the exposed shoulders
        // need a closing edge. Mirror the pack's top three frame rows at those edges, without
        // sampling a color or painting a full-width rectangle below the storage grid.
        blitRegion(graphics, playerPanelX, bodyBottom, 0, 125, VANILLA_CHEST_WIDTH, 1);
        int shoulderWidth = playerPanelX - panelX;
        for (int row = 0; row < SHOULDER_BORDER_HEIGHT && shoulderWidth > 0; row++) {
            int sourceY = SHOULDER_BORDER_HEIGHT - row - 1;
            int cornerWidth = Math.min(VANILLA_GRID_X, shoulderWidth);
            blitRegion(graphics, panelX, bodyBottom + row, 0, sourceY, cornerWidth, 1);
            blitTiled(graphics, panelX + cornerWidth, bodyBottom + row,
                    shoulderWidth - cornerWidth, 1,
                    VANILLA_GRID_X, sourceY, VANILLA_GRID_WIDTH, 1);
            // Anchor the right corner to the right edge; intermediate pieces use the
            // middle of the top frame, not another copy of either corner.
            int rightX = playerPanelX + VANILLA_CHEST_WIDTH;
            blitTiled(graphics, rightX, bodyBottom + row, shoulderWidth - cornerWidth, 1,
                    VANILLA_GRID_X, sourceY, VANILLA_GRID_WIDTH, 1);
            blitRegion(graphics, rightX + shoulderWidth - cornerWidth, bodyBottom + row,
                    VANILLA_CHEST_WIDTH - cornerWidth, sourceY, cornerWidth, 1);
        }

        // The player panel is copied intact once, including all 27 main and 9 hotbar cells.
        // Nothing is painted alongside it after the storage's three-pixel closing edge.
        blitRegion(graphics, playerPanelX, playerPanelY, 0, VANILLA_PLAYER_SECTION_TEXTURE_Y,
                VANILLA_CHEST_WIDTH, VANILLA_PLAYER_SECTION_HEIGHT);
    }

    private void drawScrollbar(GuiGraphicsExtractor graphics) {
        int trackX = leftPos + 7 + menu.getColumns() * SLOT_SIZE + SCROLLBAR_GAP;
        int trackY = topPos + 17;
        int trackHeight = menu.getVisibleRows() * SLOT_SIZE;
        int middleHeight = SCROLLBAR_SOURCE_HEIGHT - 2;
        int fullMiddleTiles = (trackHeight - 2) / middleHeight;
        int remainingMiddleHeight = (trackHeight - 2) % middleHeight;

        blitRegion(graphics, SCROLLBAR_BACKGROUND_TEXTURE, trackX, trackY,
                SCROLLBAR_SOURCE_X, SCROLLBAR_SOURCE_Y, SCROLLBAR_WIDTH, 1);
        for (int tile = 0; tile < fullMiddleTiles; tile++) {
            blitRegion(graphics, SCROLLBAR_BACKGROUND_TEXTURE,
                    trackX, trackY + 1 + tile * middleHeight,
                    SCROLLBAR_SOURCE_X, SCROLLBAR_SOURCE_Y + 1,
                    SCROLLBAR_WIDTH, middleHeight);
        }
        blitRegion(graphics, SCROLLBAR_BACKGROUND_TEXTURE,
                trackX, trackY + 1 + fullMiddleTiles * middleHeight,
                SCROLLBAR_SOURCE_X, SCROLLBAR_SOURCE_Y + 1,
                SCROLLBAR_WIDTH, remainingMiddleHeight);
        blitRegion(graphics, SCROLLBAR_BACKGROUND_TEXTURE,
                trackX, trackY + trackHeight - 1,
                SCROLLBAR_SOURCE_X, SCROLLBAR_SOURCE_Y + SCROLLBAR_SOURCE_HEIGHT - 1,
                SCROLLBAR_WIDTH, 1);

        int thumbX = trackX + 1;
        int thumbY = trackY + 1
                + (int) ((trackHeight - SCROLLER_HEIGHT - 2) * scrollPosition);
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                SCROLLER_TEXTURE,
                thumbX,
                thumbY,
                SCROLLER_WIDTH,
                SCROLLER_HEIGHT
        );
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount
    ) {
        if (!menu.shouldShowScrollbar()) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        int totalRows = (menu.getContainer().getContainerSize() + menu.getColumns() - 1)
                / menu.getColumns();
        int scrollRows = totalRows - menu.getVisibleRows();
        scrollPosition = Mth.clamp(
                scrollPosition - (float) (verticalAmount / scrollRows),
                0.0f,
                1.0f
        );
        menu.scrollItems(scrollPosition);
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        if (menu.shouldShowScrollbar() && event.button() == 0
                && isClickInScrollbar(event.x(), event.y())) {
            scrolling = true;
            return true;
        }
        return super.mouseClicked(event, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0) {
            scrolling = false;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double offsetX, double offsetY) {
        if (menu.shouldShowScrollbar() && scrolling) {
            int trackY = topPos + 18;
            int trackHeight = menu.getVisibleRows() * SLOT_SIZE;
            scrollPosition = Mth.clamp(
                    ((float) event.y() - trackY - (float) SCROLLER_HEIGHT / 2.0f)
                            / (trackHeight - SCROLLER_HEIGHT),
                    0.0f,
                    1.0f
            );
            menu.scrollItems(scrollPosition);
            return true;
        }
        return super.mouseDragged(event, offsetX, offsetY);
    }

    private boolean isClickInScrollbar(double mouseX, double mouseY) {
        int x = leftPos + 8 + menu.getColumns() * SLOT_SIZE + SCROLLBAR_GAP;
        int y = topPos + 18;
        return mouseX >= x
                && mouseX < x + SCROLLER_WIDTH + 1
                && mouseY >= y
                && mouseY < y + menu.getVisibleRows() * SLOT_SIZE;
    }

    private void drawHorizontalStrip(GuiGraphicsExtractor graphics, int x, int y, int width,
            int sourceY, int height, int capWidth) {
        blitRegion(graphics, x, y, 0, sourceY, capWidth, height);
        blitTiled(graphics, x + capWidth, y, width - 2 * capWidth, height,
                VANILLA_CHEST_WIDTH / 2 - SLOT_SIZE, sourceY, SLOT_SIZE, height);
        blitRegion(graphics, x + width - capWidth, y,
                VANILLA_CHEST_WIDTH - capWidth, sourceY, capWidth, height);
    }

    private void blitTiled(GuiGraphicsExtractor graphics, int x, int y, int width, int height,
            int sourceU, int sourceV, int tileWidth, int tileHeight) {
        for (int dy = 0; dy < height; dy += tileHeight) {
            int partHeight = Math.min(tileHeight, height - dy);
            for (int dx = 0; dx < width; dx += tileWidth) {
                int partWidth = Math.min(tileWidth, width - dx);
                blitRegion(graphics, x + dx, y + dy, sourceU, sourceV, partWidth, partHeight);
            }
        }
    }

    private void blitRegion(GuiGraphicsExtractor graphics, int x, int y,
            int sourceU, int sourceV, int width, int height) {
        blitRegion(graphics, VANILLA_CHEST_TEXTURE, x, y, sourceU, sourceV, width, height);
    }

    private void blitRegion(GuiGraphicsExtractor graphics, Identifier texture, int x, int y,
            int sourceU, int sourceV, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                sourceU,
                sourceV,
                width,
                height,
                width,
                height,
                VANILLA_TEXTURE_SIZE,
                VANILLA_TEXTURE_SIZE
        );
    }
}
