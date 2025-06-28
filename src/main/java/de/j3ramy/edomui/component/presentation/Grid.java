package de.j3ramy.edomui.component.presentation;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.component.basic.VerticalScrollbar;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.button.SpriteImageButton;
import de.j3ramy.edomui.component.presentation.contextmenu.ContextMenu;
import de.j3ramy.edomui.component.presentation.contextmenu.DynamicContextMenuBuilder;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IContextMenuProvider;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.presentation.GridStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Grid extends Widget {
    private final List<List<Button>> grid;
    private final GridConfig config;
    private final VisibleRange visibleRange;
    private final VerticalScrollbar scrollbar;
    private final GridStyle gridStyle;

    private ContextMenu contextMenu;
    private IContextMenuProvider IContextMenuProvider;
    private DynamicContextMenuBuilder menuBuilder;
    private boolean dynamicMenuEnabled = false;
    private boolean needsUpdate = true;

    public Grid(int x, int y, int width, int height, int cellWidth, int cellHeight, int cellMargin) {
        super(x, y, width, height);

        this.gridStyle = new GridStyle(ThemeManager.getDefaultGridStyle());
        this.setStyle(gridStyle);

        this.config = new GridConfig(cellWidth, cellHeight, cellMargin, width, height, this.gridStyle.getPadding());
        this.visibleRange = new VisibleRange(config.maxVisibleRows);
        this.grid = new ArrayList<>();
        this.grid.add(new ArrayList<>());

        this.scrollbar = new VerticalScrollbar(this, 0, config.maxVisibleRows);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        if (needsScrolling()) {
            scrollbar.render(poseStack);
        }

        renderVisibleCells(poseStack);

        if (contextMenu != null && !contextMenu.isHidden()) {
            contextMenu.render(poseStack);
            return;
        }

        renderTooltips(poseStack);
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;

        super.update(x, y);
        updateCellPositions();

        if (contextMenu != null && !contextMenu.isHidden()) {
            if (needsScrolling()) {
                scrollbar.updateContentSize(grid.size());
            }
            contextMenu.update(x, y);
            return;
        }

        updateCellStates(x, y);

        if (needsScrolling()) {
            scrollbar.updateContentSize(grid.size());
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden()) return;

        if (contextMenu != null && !contextMenu.isHidden()) {
            if (contextMenu.isMouseOver()) {
                contextMenu.onClick(mouseButton);
                return;
            }

            if (mouseButton == 1) {
                Button hoveredCell = getHoveredCell();
                if (hoveredCell != null) {
                    showContextMenu(hoveredCell);
                    return;
                }
            }

            contextMenu.hide();
            return;
        }

        Button hoveredCell = getHoveredCell();
        if (hoveredCell != null) {
            if (mouseButton == 1 && IContextMenuProvider != null) {
                showContextMenu(hoveredCell);
            } else if (mouseButton == 0) {
                hoveredCell.onClick(mouseButton);
            }
        }
    }

    @Override
    public void onScroll(double delta) {
        if (isHidden() || !isMouseOver() || !needsScrolling()) return;

        handleScrolling(delta);
    }

    @Override
    public GridStyle getStyle() {
        return this.gridStyle;
    }

    private void updateContextMenuProvider() {
        if (menuBuilder != null && dynamicMenuEnabled) {
            this.IContextMenuProvider = (menu, elementIndex, elementTitle) -> {
                Button hoveredCell = getHoveredCell();
                if (hoveredCell != null) {
                    menuBuilder.build().populateContextMenu(menu, 0, hoveredCell.getTitle().getString().toString());
                }
            };
        }
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = getTopPos() - topPos;

        scrollbar.setTopPos(scrollbar.getTopPos() - deltaPos);
        grid.stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .forEach(cell -> cell.setTopPos(cell.getTopPos() - deltaPos));

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = getLeftPos() - leftPos;

        scrollbar.setLeftPos(scrollbar.getLeftPos() - deltaPos);
        grid.stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .forEach(cell -> cell.setLeftPos(cell.getLeftPos() - deltaPos));

        super.setLeftPos(leftPos);
    }

    private void showContextMenu(Button cell) {
        if (IContextMenuProvider == null || contextMenu == null) return;

        contextMenu.clear();

        GridPosition position = findCellPosition(cell);
        if (position == null) return;

        IContextMenuProvider.populateContextMenu(contextMenu,
                position.row * config.maxVisibleCols + position.col,
                cell.getTitle().getString().toString());

        contextMenu.show(this.getMousePosition().x, this.getMousePosition().y);
    }

    private void compactGrid() {
        List<Button> allCells = new ArrayList<>();

        for (List<Button> row : grid) {
            allCells.addAll(row);
        }

        grid.clear();
        grid.add(new ArrayList<>());

        for (Button cell : allCells) {
            if (cell != null) {
                addCellToGridDirect(cell);
            }
        }
    }

    private void addCellToGridDirect(Button cell) {
        int targetRow = findNextAvailableRow();
        grid.get(targetRow).add(cell);
    }

    private GridPosition findCellPosition(Button targetCell) {
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.get(row).size(); col++) {
                if (grid.get(row).get(col) == targetCell) {
                    return new GridPosition(row, col);
                }
            }
        }
        return null;
    }

    private void renderVisibleCells(PoseStack poseStack) {
        for (int i = 0; i < grid.size(); i++) {
            if (!visibleRange.isInRange(i)) continue;

            for (Button cell : grid.get(i)) {
                if (cell != null) {
                    renderButtonWithoutTooltip(cell, poseStack);
                }
            }
        }
    }

    private void renderButtonWithoutTooltip(Button button, PoseStack poseStack) {
        if (button.isHidden()) return;

        if (button.isShowBackground()) {
            button.renderBackground(poseStack);
        }

        button.getTitle().render(poseStack);
    }

    private void renderTooltips(PoseStack poseStack) {
        for (int i = 0; i < grid.size(); i++) {
            if (!visibleRange.isInRange(i)) continue;

            for (Button cell : grid.get(i)) {
                if (cell != null && cell.isTooltipEnabled() &&
                        cell.getTooltip() != null && cell.isMouseOver()) {
                    cell.getTooltip().render(poseStack);
                }
            }
        }
    }

    private void updateCellPositions() {
        if (!needsUpdate) return;

        int visibleRowCounter = 0;

        for (int i = 0; i < grid.size(); i++) {
            boolean isRowVisible = visibleRange.isInRange(i);

            for (int j = 0; j < grid.get(i).size(); j++) {
                Button cell = grid.get(i).get(j);
                if (cell == null) continue;

                cell.setHidden(!isRowVisible);

                if (isRowVisible) {
                    int newLeft = this.getLeftPos() + config.cellMargin + (config.cellMargin * j + config.cellWidth * j);
                    int newTop = this.getTopPos() + config.cellMargin + (config.cellMargin * visibleRowCounter + config.cellHeight * visibleRowCounter);

                    cell.setLeftPos(newLeft);
                    cell.setTopPos(newTop);
                }
            }

            if (isRowVisible && visibleRowCounter < config.maxVisibleRows) {
                visibleRowCounter++;
            }
        }

        needsUpdate = false;
    }

    private void updateCellStates(int mouseX, int mouseY) {
        grid.stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .forEach(cell -> cell.update(mouseX, mouseY));
    }

    private void handleScrolling(double delta) {
        int totalRows = grid.size();

        if (delta < 0 && visibleRange.end < totalRows - 1) {
            visibleRange.scrollDown();
            markForUpdate();
        } else if (delta > 0 && visibleRange.canScrollUp()) {
            visibleRange.scrollUp();
            markForUpdate();
        }

        scrollbar.updateScrollIndex(visibleRange.start);
    }

    private void addCellToGrid(Button cell) {
        int targetRow = findNextAvailableRow();
        grid.get(targetRow).add(cell);

        scrollbar.updateContentSize(grid.size());
        markForUpdate();
        update(getMousePosition().x, getMousePosition().y);
    }

    private int findNextAvailableRow() {
        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i).size() < config.maxVisibleCols) {
                return i;
            }
        }

        grid.add(new ArrayList<>());
        return grid.size() - 1;
    }

    private boolean isValidPosition(int rowIndex, int columnIndex) {
        return rowIndex >= 0 && rowIndex < grid.size() &&
                columnIndex >= 0 && columnIndex < grid.get(rowIndex).size();
    }

    private boolean needsScrolling() {
        return getCellCount() > config.maxVisibleRows * config.maxVisibleCols;
    }

    private void markForUpdate() {
        this.needsUpdate = true;
    }

    public boolean removeLastCell() {
        if (grid.isEmpty() || grid.get(grid.size() - 1).isEmpty()) {
            return false;
        }

        List<Button> lastRow = grid.get(grid.size() - 1);
        lastRow.remove(lastRow.size() - 1);
        compactGrid();
        markForUpdate();

        if (contextMenu != null) {
            contextMenu.hide();
        }

        return true;
    }

    public void clear() {
        grid.clear();
        grid.add(new ArrayList<>());
        resetScrollProgress();
        markForUpdate();

        if (contextMenu != null) {
            contextMenu.hide();
        }
    }

    public void resetScrollProgress() {
        visibleRange.reset();
        scrollbar.updateScrollIndex(0);
    }

    public int getCellCount() {
        return grid.stream().mapToInt(List::size).sum();
    }

    @Nullable
    public Button getHoveredCell() {
        return grid.stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .filter(cell -> !cell.isHidden())
                .filter(Widget::isMouseOver)
                .findFirst()
                .orElse(null);
    }

    public boolean isMouseOverCell() {
        return getHoveredCell() != null;
    }

    public void addButton(Button button) {
        addCellToGrid(button);
    }

    public void add(String label, Color backgroundColor, Color backgroundHoverColor, IAction leftClickAction) {
        Button button = new Button(0, 0, config.cellWidth, config.cellHeight, label, leftClickAction, ButtonType.DEFAULT, this.gridStyle.getPadding());
        button.setTitle(label);
        button.getStyle().setBackgroundColor(backgroundColor);
        button.getStyle().setHoverBackgroundColor(backgroundHoverColor);
        button.noBorder();
        button.enableTooltip();

        addButton(button);
    }

    public void add(String label, Color backgroundColor, IAction leftClickAction) {
        this.add(label, backgroundColor, backgroundColor, leftClickAction);
    }

    public void add(ResourceLocation resourceLocation, int textureX, int textureY, int textureWidth, int textureHeight,
                    IAction leftClickAction, Color backgroundColor) {

        SpriteImageButton imageButton = new SpriteImageButton(0, 0, config.cellWidth, config.cellHeight, textureX, textureY, textureWidth, textureHeight,
                resourceLocation, leftClickAction
        );

        imageButton.getStyle().setBackgroundColor(backgroundColor);
        imageButton.getStyle().setHoverBackgroundColor(this.gridStyle.getHoverBackgroundColor());
        imageButton.noBorder();

        addButton(imageButton);
    }

    @Nullable
    public Cell getHoveredCellInfo() {
        Button hoveredButton = getHoveredCell();
        if (hoveredButton == null) return null;

        String label;
        if (hoveredButton instanceof SpriteImageButton) {
            label = hoveredButton.getTooltip() != null ? hoveredButton.getTooltip().getText() : "";
        } else {
            label = hoveredButton.getTitle().getString().toString();
        }

        return new Cell(label, hoveredButton);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.grid.stream()
                .flatMap(List::stream)
                .forEach(button -> button.setEnabled(enabled));
    }

    public boolean removeCell(int rowIndex, int columnIndex) {
        if (!isValidPosition(rowIndex, columnIndex)) {
            return false;
        }

        grid.get(rowIndex).remove(columnIndex);
        compactGrid();
        markForUpdate();

        if (contextMenu != null) {
            contextMenu.hide();
        }

        return true;
    }

    public void removeCell(Button cell) {
        GridPosition position = findCellPosition(cell);
        if (position != null) {
            grid.get(position.row).remove(position.col);
            compactGrid();
            markForUpdate();

            if (contextMenu != null) {
                contextMenu.hide();
            }

        }
    }

    public Grid enableDynamicContextMenu() {
        return this.enableDynamicContextMenu(getStyle().getBackgroundColor());
    }

    public Grid enableDynamicContextMenu(Color selectionColor) {
        this.contextMenu = new ContextMenu(selectionColor);
        this.menuBuilder = DynamicContextMenuBuilder.create();
        this.dynamicMenuEnabled = true;
        return this;
    }

    public Grid addContextAction(String label, IAction action) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addAction(label, action);
        updateContextMenuProvider();
        return this;
    }

    public Grid addContextDeleteAction() {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addAction("Delete Cell", () -> {
            Button hoveredCell = getHoveredCell();
            if (hoveredCell != null) {
                removeCell(hoveredCell);
            }
        });
        updateContextMenuProvider();
        return this;
    }

    public Grid addContextEditAction(Function<Button, IAction> editFunction) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addAction("Edit Cell", () -> {
            Button hoveredCell = getHoveredCell();
            if (hoveredCell != null) {
                editFunction.apply(hoveredCell).execute();
            }
        });
        updateContextMenuProvider();
        return this;
    }

    public Grid addContextActionIf(String label, IAction action, Predicate<Button> condition) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addAction(label, () -> {
            Button hoveredCell = getHoveredCell();
            if (hoveredCell != null && condition.test(hoveredCell)) {
                action.execute();
            }
        });
        updateContextMenuProvider();
        return this;
    }

    public Grid addContextTitleAction(String label, Function<String, IAction> actionFactory) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addAction(label, () -> {
            Button hoveredCell = getHoveredCell();
            if (hoveredCell != null) {
                String cellText = hoveredCell.getTitle().getString().toString();
                actionFactory.apply(cellText).execute();
            }
        });
        updateContextMenuProvider();
        return this;
    }

    public DynamicContextMenuBuilder getMenuBuilder() {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        return menuBuilder;
    }

    public void setContextMenuProvider(IContextMenuProvider provider, Color selectedColor) {
        if (contextMenu == null) {
            this.contextMenu = new ContextMenu(selectedColor);
        }
        this.IContextMenuProvider = provider;
        this.dynamicMenuEnabled = false;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public VerticalScrollbar getScrollbar() {
        return scrollbar;
    }

    record GridPosition(int row, int col) { }

    static class GridConfig {
        final int cellWidth, cellHeight, cellMargin, cellPadding;
        final int maxVisibleRows, maxVisibleCols;

        GridConfig(int cellWidth, int cellHeight, int cellMargin, int gridWidth, int gridHeight, int cellPadding) {
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            this.cellMargin = cellMargin;
            this.maxVisibleRows = gridHeight / (cellHeight + cellMargin);
            this.maxVisibleCols = gridWidth / (cellWidth + cellMargin);
            this.cellPadding = cellPadding;
        }
    }

    static class VisibleRange {
        int start, end;
        private final int maxVisibleRows;

        VisibleRange(int maxVisibleRows) {
            this.maxVisibleRows = maxVisibleRows;
            this.start = 0;
            this.end = maxVisibleRows - 1;
        }

        boolean isInRange(int index) {
            return index >= start && index <= end;
        }

        boolean canScrollUp() {
            return start > 0;
        }

        void scrollUp() {
            if (canScrollUp()) {
                start--;
                end--;
            }
        }

        void scrollDown() {
            start++;
            end++;
        }

        void reset() {
            start = 0;
            end = maxVisibleRows - 1;
        }
    }

    public record Cell(String label, Button button) { }
}