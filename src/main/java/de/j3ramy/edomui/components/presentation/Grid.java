package de.j3ramy.edomui.components.presentation;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.components.basic.VerticalScrollbar;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Grid extends Widget {
    private final List<List<Button>> grid;
    private final GridConfig config;
    private final VisibleRange visibleRange;
    private final VerticalScrollbar scrollbar;

    private boolean needsUpdate = true;

    public Grid(int x, int y, int width, int height, int cellWidth, int cellHeight, int cellMargin) {
        super(x, y, width, height);

        this.config = new GridConfig(cellWidth, cellHeight, cellMargin, width, height);
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
        renderTooltips(poseStack);
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;

        super.update(x, y);
        updateCellPositions();
        updateCellStates(x, y);

        if (needsScrolling()) {
            scrollbar.updateContentSize(grid.size());
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden()) return;

        Button hoveredCell = getHoveredCell();
        if (hoveredCell != null) {
            hoveredCell.onClick(mouseButton);
        }
    }

    @Override
    public void onScroll(double delta) {
        if (isHidden() || !isMouseOver() || !needsScrolling()) return;

        handleScrolling(delta);
    }

    public void addButton(Button button) {
        addCellToGrid(button);
    }

    public void add(String label, int labelColor, int backgroundColor, IAction leftClickAction) {
        Button button = new Button(0, 0, config.cellWidth, config.cellHeight, label, FontSize.S, leftClickAction);
        button.getTitle().setFontSize(FontSize.S);
        button.setTitle(label);
        button.getTitle().setTextColor(labelColor);
        button.getStyle().setBackgroundColor(backgroundColor);
        button.noBorder();
        button.enableTooltip();

        addButton(button);
    }

    public boolean removeCell(int rowIndex, int columnIndex) {
        if (!isValidPosition(rowIndex, columnIndex)) {
            return false;
        }

        grid.get(rowIndex).remove(columnIndex);
        markForUpdate();
        return true;
    }

    public boolean removeLastCell() {
        if (grid.isEmpty() || grid.get(grid.size() - 1).isEmpty()) {
            return false;
        }

        List<Button> lastRow = grid.get(grid.size() - 1);
        lastRow.remove(lastRow.size() - 1);
        markForUpdate();
        return true;
    }

    public void clear() {
        grid.clear();
        grid.add(new ArrayList<>());
        resetScrollProgress();
        markForUpdate();
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
                        cell.tooltip != null && cell.isMouseOver()) {
                    cell.tooltip.render(poseStack);
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

    private static class GridConfig {
        final int cellWidth, cellHeight, cellMargin;
        final int maxVisibleRows, maxVisibleCols;

        GridConfig(int cellWidth, int cellHeight, int cellMargin, int gridWidth, int gridHeight) {
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            this.cellMargin = cellMargin;
            this.maxVisibleRows = gridHeight / (cellHeight + cellMargin);
            this.maxVisibleCols = gridWidth / (cellWidth + cellMargin);
        }
    }

    private static class VisibleRange {
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
}