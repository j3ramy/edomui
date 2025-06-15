package de.j3ramy.edomui.components.presentation;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.components.basic.VerticalLine;
import de.j3ramy.edomui.components.text.CenteredText;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.text.Tooltip;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.view.View;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Table extends ScrollableList {
    private static final int DEFAULT_PADDING = 3;

    private final int headerHeight;
    private TableRow headerRow;
    private int headerBackgroundColor;
    private boolean isHeaderFixed;

    public Table(int x, int y, int width, int height, int rowHeight, int selectedColor) {
        super(x, y, width, height, rowHeight, selectedColor);
        this.headerHeight = rowHeight;
    }

    @Override
    public void render(PoseStack stack) {
        if (isHidden()) return;

        super.render(stack);

        // Render fixed header
        if (isHeaderFixed && headerRow != null) {
            headerRow.setTopPos(this.getTopPos());
            headerRow.renderWithoutTooltips(stack);
        }

        // Render all tooltips on top
        renderAllTooltips(stack);
    }

    private void renderAllTooltips(PoseStack stack) {
        // Header tooltips have priority
        if (isHeaderFixed && headerRow != null && headerRow.isMouseOver()) {
            headerRow.renderTooltips(stack);
            return;
        }

        // Render tooltips from visible rows
        for (Button button : getVisibleButtons()) {
            if (button instanceof TableRow row) {
                // Skip tooltip rendering for rows that are overlapped by fixed header
                if (isHeaderFixed && headerRow != null && isRowOverlappedByHeader(row)) {
                    continue;
                }

                row.renderTooltips(stack);
            }
        }
    }

    // Helper method to check if a row is overlapped by the fixed header
    private boolean isRowOverlappedByHeader(TableRow row) {
        if (!isHeaderFixed || headerRow == null) return false;

        int headerBottom = headerRow.getTopPos() + headerRow.getHeight();
        int rowTop = row.getTopPos();

        return rowTop < headerBottom;
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;

        super.update(x, y);

        if (isHeaderFixed && headerRow != null) {
            headerRow.update(x, y);
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden() || !isMouseOver() || !isEnabled()) return;

        if (isHeaderFixed && headerRow != null && headerRow.isMouseOver()) {
            return;
        }

        super.onClick(mouseButton);

        if (headerRow != null) {
            headerRow.getStyle().setBackgroundColor(headerBackgroundColor);
        }
    }

    @Override
    public boolean hasSelection() {
        int currentIndex = super.getSelectedIndex();
        return currentIndex >= 0 && (!hasHeader() || currentIndex > 0);
    }

    @Override
    public int getSelectedIndex() {
        int baseIndex = super.getSelectedIndex();
        if (hasHeader()) {
            return baseIndex <= 0 ? -1 : baseIndex - 1;
        }
        return baseIndex;
    }

    public List<String> getSelectedRowData() {
        if (!hasSelection()) return new ArrayList<>();

        int actualIndex = super.getSelectedIndex();
        if (actualIndex >= 0 && actualIndex < content.size()) {
            Button selectedButton = content.get(actualIndex);
            if (selectedButton instanceof TableRow) {
                return ((TableRow) selectedButton).getColumnData();
            }
        }
        return new ArrayList<>();
    }

    public void addHeader(List<String> headers, int textColor) {
        addHeader(headers, this.getStyle().getBackgroundColor(), textColor, true, true);
    }

    public void addHeader(List<String> headers, int backgroundColor, int textColor) {
        addHeader(headers, backgroundColor, textColor, true, true);
    }

    public void addHeader(List<String> headers, int backgroundColor, int textColor, boolean isFixed, boolean renderTooltip) {
        this.headerBackgroundColor = backgroundColor;
        this.isHeaderFixed = isFixed;

        this.headerRow = new TableRow(
                getLeftPos(), getTopPos(), getContentWidth(), headerHeight,
                new ArrayList<>(headers), textColor, renderTooltip, true
        );

        headerRow.getStyle().setBackgroundColor(backgroundColor);
        headerRow.setHoverable(false);

        content.add(0, headerRow);
        scrollbar.updateContentSize(content.size());
        layoutButtons();
    }

    public void addRow(List<String> rowData, int textColor, boolean renderTooltip) {
        TableRow row = new TableRow(
                getLeftPos(), calculateRowPosition(content.size()), getContentWidth(), elementHeight,
                new ArrayList<>(rowData), textColor, renderTooltip, false
        );

        row.getStyle().setHoverBackgroundColor(selectedColor);

        content.add(row);
        scrollbar.updateContentSize(content.size());
        layoutButtons();
    }

    public void addRow(List<String> rowData, int textColor) {
        addRow(rowData, textColor, true);
    }

    public void addRow(ArrayList<String> rowData, int textColor, boolean renderTooltip) {
        addRow((List<String>) rowData, textColor, renderTooltip);
    }

    public void addRow(ArrayList<String> rowData, int textColor) {
        addRow((List<String>) rowData, textColor);
    }

    public List<List<String>> getTableData() {
        List<List<String>> data = new ArrayList<>();
        int startIndex = hasHeader() ? 1 : 0;

        for (int i = startIndex; i < content.size(); i++) {
            Button row = content.get(i);
            if (row instanceof TableRow) {
                data.add(((TableRow) row).getColumnData());
            }
        }
        return data;
    }

    public List<String> getHeaderData() {
        return headerRow != null ? headerRow.getColumnData() : new ArrayList<>();
    }

    public void clearData() {
        if (hasHeader()) {
            content.subList(1, content.size()).clear();
        } else {
            content.clear();
        }
        unselect();
        scrollbar.updateContentSize(content.size());
        layoutButtons();
    }

    @Override
    public void clear() {
        super.clear();
        headerRow = null;
        isHeaderFixed = false;
    }

    private boolean hasHeader() {
        return headerRow != null;
    }

    private int getContentWidth() {
        return getWidth() - (needsScrolling() ? GuiPresets.SCROLLBAR_TRACK_WIDTH : 0);
    }

    private int calculateRowPosition(int rowIndex) {
        int basePosition = getTopPos();
        if (isHeaderFixed && hasHeader()) {
            basePosition += headerHeight;
        }
        return basePosition + rowIndex * elementHeight;
    }

    private boolean needsScrolling() {
        int availableHeight = getHeight();
        if (isHeaderFixed && hasHeader()) {
            availableHeight -= headerHeight;
        }
        return content.size() * elementHeight > availableHeight;
    }

    // ================================
    // INNER CLASS: TableRow
    // ================================

    public final class TableRow extends Button {
        private final View columnView = new View();
        private final List<String> columnData;
        private final boolean isHeader;
        private int[] columnWidths;

        public TableRow(int x, int y, int width, int height, List<String> columnData,
                        int textColor, boolean renderTooltip, boolean isHeader) {
            super(x, y, width, height, "", GuiPresets.SCROLLABLE_LIST_FONT_SIZE,
                    null, null, ButtonType.TEXT_FIELD);

            this.columnData = new ArrayList<>(columnData);
            this.isHeader = isHeader;
            this.noBorder();

            if (!isHeader) {
                this.getStyle().setHoverBackgroundColor(selectedColor);
            }

            calculateColumnWidths();
            buildColumns(textColor, renderTooltip);
        }

        @Override
        public void render(PoseStack poseStack) {
            renderWithoutTooltips(poseStack);
        }

        public void renderWithoutTooltips(PoseStack poseStack) {
            if (isShowBackground()) {
                renderBackground(poseStack);
            }

            for (Widget widget : columnView.getWidgets()) {
                if (!(widget instanceof Tooltip)) {
                    widget.render(poseStack);
                }
            }
        }

        public void renderTooltips(PoseStack poseStack) {
            for (Widget widget : columnView.getWidgets()) {
                if (widget instanceof Tooltip) {
                    widget.render(poseStack);
                }
            }
        }

        @Override
        public void update(int x, int y) {
            super.update(x, y);
            columnView.update(x, y);
        }

        @Override
        public void setTopPos(int topPos) {
            int delta = getTopPos() - topPos;
            updateWidgetPositions(0, -delta);
            super.setTopPos(topPos);
        }

        @Override
        public void setLeftPos(int leftPos) {
            int delta = getLeftPos() - leftPos;
            updateWidgetPositions(-delta, 0);
            super.setLeftPos(leftPos);
        }

        @Override
        public void setWidth(int width) {
            super.setWidth(width);
            calculateColumnWidths();
            buildColumns(Color.WHITE, true);
        }

        private void updateWidgetPositions(int deltaX, int deltaY) {
            for (Widget widget : columnView.getWidgets()) {
                if (deltaX != 0) widget.setLeftPos(widget.getLeftPos() + deltaX);
                if (deltaY != 0) widget.setTopPos(widget.getTopPos() + deltaY);
            }
        }

        private void calculateColumnWidths() {
            if (columnData.isEmpty()) {
                columnWidths = new int[0];
                return;
            }

            columnWidths = new int[columnData.size()];
            int baseWidth = getWidth() / columnData.size();
            Arrays.fill(columnWidths, baseWidth);
        }

        private void buildColumns(int textColor, boolean renderTooltip) {
            columnView.clear();

            if (columnData.isEmpty()) return;

            int currentX = getLeftPos();

            for (int i = 0; i < columnData.size(); i++) {
                String text = columnData.get(i);
                int columnWidth = columnWidths[i];

                // Create text widget
                Rectangle textRect = new Rectangle(
                        currentX + DEFAULT_PADDING, getTopPos(),
                        columnWidth - 2 * DEFAULT_PADDING, getHeight()
                );

                CenteredText columnText = new CenteredText(
                        textRect, text, GuiPresets.TABLE_COLUMN_FONT_SIZE,
                        textRect.width, textColor, selectedColor, -1
                );
                columnView.addWidget(columnText);

                // Add tooltip
                if (renderTooltip && !text.trim().isEmpty()) {
                    Tooltip cellTooltip = new Tooltip(text, columnText);
                    columnView.addWidget(cellTooltip);
                }

                // Add column separator
                if (i < columnData.size() - 1) {
                    VerticalLine separator = new VerticalLine(
                            currentX + columnWidth - 1, getTopPos(), 1, getHeight(), Color.WHITE
                    );
                    columnView.addWidget(separator);
                }

                currentX += columnWidth;
            }
        }

        public List<String> getColumnData() {
            return new ArrayList<>(columnData);
        }

        public boolean isHeaderRow() {
            return isHeader;
        }
    }
}