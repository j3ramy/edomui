package de.j3ramy.edomui.component.presentation;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.component.basic.VerticalLine;
import de.j3ramy.edomui.component.text.CenteredText;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.text.Tooltip;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.theme.presentation.TableStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.view.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ScrollableTable extends ScrollableList {
    private final TableStyle tableStyle;

    private TableRow headerRow;

    public ScrollableTable(int x, int y, int width, int height, Color selectionColor) {
        super(x, y, width, height, selectionColor);

        this.tableStyle = new TableStyle(ThemeManager.getDefaultTableStyle());
        this.setStyle(this.tableStyle);

        this.tableStyle.setSelectionColor(this.listStyle.getSelectionColor());
        this.tableStyle.setBackgroundColor(this.listStyle.getBackgroundColor());
        this.tableStyle.setTextColor(this.listStyle.getTextColor());
        this.tableStyle.setTextHoverColor(this.listStyle.getTextHoverColor());
    }


    @Override
    public void render(PoseStack stack) {
        if (isHidden()) return;

        super.render(stack);

        // Render fixed header
        if (headerRow != null) {
            headerRow.setTopPos(this.getTopPos());
            headerRow.renderWithoutTooltips(stack);
        }

        // Render all tooltips on top
        renderAllTooltips(stack);
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;

        super.update(x, y);

        if (headerRow != null) {
            headerRow.update(x, y);
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden() || !isMouseOver() || !isEnabled()) return;

        if (headerRow != null && headerRow.isMouseOver()) {
            return;
        }

        super.onClick(mouseButton);
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

    @Override
    public TableStyle getStyle() {
        return tableStyle;
    }

    @Override
    public void clear() {
        super.clear();
        headerRow = null;
    }

    private void renderAllTooltips(PoseStack stack) {
        // Header tooltips have priority
        if (headerRow != null && headerRow.isMouseOver()) {
            headerRow.renderTooltips(stack);
            return;
        }

        // Render tooltips from visible rows
        for (Button button : getVisibleButtons()) {
            if (button instanceof TableRow row && row.isMouseOver()) {
                // Skip tooltip rendering for rows that are overlapped by fixed header
                if (headerRow != null && isRowOverlappedByHeader(row)) {
                    continue;
                }

                row.renderTooltips(stack);
                break;
            }
        }
    }

    private boolean isRowOverlappedByHeader(TableRow row) {
        if (headerRow == null) return false;

        int headerBottom = headerRow.getTopPos() + headerRow.getHeight();
        int rowTop = row.getTopPos();

        return rowTop < headerBottom;
    }

    private boolean hasHeader() {
        return headerRow != null;
    }

    private int getContentWidth() {
        return getWidth() - (needsScrolling() ? this.scrollbar.getWidth() : 0);
    }

    private int calculateRowPosition(int rowIndex) {
        int basePosition = getTopPos();
        if (hasHeader()) {
            basePosition += this.tableStyle.getElementHeight();
        }
        return basePosition + rowIndex * this.listStyle.getElementHeight();
    }

    private boolean needsScrolling() {
        int availableHeight = getHeight();
        if (hasHeader()) {
            availableHeight -= this.tableStyle.getElementHeight();
        }
        return content.size() * this.listStyle.getElementHeight() > availableHeight;
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

    public void addHeader(List<String> headers) {
        addHeader(headers, true);
    }

    public void addHeader(List<String> headers, boolean renderTooltip) {
        this.headerRow = new TableRow(
                getLeftPos(), getTopPos(), getContentWidth(), this.tableStyle.getElementHeight(),
                new ArrayList<>(headers), renderTooltip, true
        );

        headerRow.getStyle().setBackgroundColor(this.listStyle.getSelectionColor());
        headerRow.setHoverable(false);

        content.add(0, headerRow);
        scrollbar.updateContentSize(content.size());
        layoutButtons();
    }

    public void addRow(List<String> rowData, boolean renderTooltip) {
        TableRow row = new TableRow(
                getLeftPos(), calculateRowPosition(content.size()), getContentWidth(), this.listStyle.getElementHeight(),
                new ArrayList<>(rowData), renderTooltip, false
        );

        row.getStyle().setHoverBackgroundColor(this.listStyle.getSelectionColor());

        content.add(row);
        scrollbar.updateContentSize(content.size());
        layoutButtons();
    }

    public void addRow(List<String> rowData) {
        addRow(rowData, true);
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

    public int getTableRowCount() {
        return hasHeader() ? super.getContent().size() - 1 : super.getContent().size();
    }

    // ================================
    // INNER CLASS: TableRow
    // ================================
    public final class TableRow extends Button {
        private final List<CenteredText> columnTexts = new ArrayList<>();
        private final List<Tooltip> columnTooltips = new ArrayList<>();
        private final View columnView = new View();
        private final List<String> columnData;
        private final boolean isHeader;

        private int[] columnWidths;

        public TableRow(int x, int y, int width, int height, List<String> columnData, boolean renderTooltip, boolean isHeader) {
            super(x, y, width, height, "", null, null, ButtonType.TEXT_FIELD);

            this.columnData = new ArrayList<>(columnData);
            this.isHeader = isHeader;
            this.noBorder();

            if (!isHeader) {
                this.setHoverable(true);
                this.getStyle().setHoverBackgroundColor(listStyle.getSelectionColor());
            } else {
                this.setHoverable(false);
            }

            calculateColumnWidths();
            buildColumns(renderTooltip);
        }

        @Override
        public void render(PoseStack poseStack) {
            renderWithoutTooltips(poseStack);
        }

        @Override
        public void update(int x, int y) {
            super.update(x, y);

            if (!isHeader) {
                for (CenteredText textWidget : columnTexts) {
                    if (this.isMouseOver()) {
                        textWidget.getStyle().setTextColor(this.getStyle().getTextHoverColor());
                    } else {
                        textWidget.getStyle().setTextColor(this.getStyle().getTextColor());
                    }
                }
            }

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
            buildColumns(true);
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

        private void buildColumns(boolean renderTooltip) {
            columnView.clear();
            columnTexts.clear();
            columnTooltips.clear();

            if (columnData.isEmpty()) return;

            int currentX = getLeftPos();

            for (int i = 0; i < columnData.size(); i++) {
                String text = columnData.get(i);
                int columnWidth = columnWidths[i];

                Rectangle textRect = new Rectangle(currentX + tableStyle.getPadding(), getTopPos(), columnWidth - 2 * tableStyle.getPadding(), getHeight());

                Color textColor = this.getStyle().getTextColor();
                Color hoverTextColor = this.getStyle().getTextHoverColor();

                CenteredText columnText = new CenteredText(textRect, text, listStyle.getFontSize(), textRect.width, textColor,
                        tableStyle.getSelectionColor(), null);

                if (!isHeader) {
                    columnText.setHoverable(true);
                    columnText.getStyle().setTextHoverColor(hoverTextColor);
                } else {
                    columnText.setHoverable(false);
                }

                columnView.addWidget(columnText);
                columnTexts.add(columnText);

                // Add tooltip
                Tooltip cellTooltip = null;
                if (renderTooltip && !text.trim().isEmpty()) {
                    cellTooltip = new Tooltip(text, columnText);
                    columnView.addWidget(cellTooltip);
                }
                columnTooltips.add(cellTooltip);

                // Add column separator
                if (i < columnData.size() - 1) {
                    VerticalLine separator = new VerticalLine(currentX + columnWidth - 1, getTopPos(), 1, getHeight(), tableStyle.getBorderColor());
                    columnView.addWidget(separator);
                }

                currentX += columnWidth;
            }
        }

        public List<String> getColumnData() {
            return new ArrayList<>(columnData);
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
            for (int i = 0; i < columnTexts.size(); i++) {
                CenteredText columnText = columnTexts.get(i);
                if (columnText.isMouseOver() && i < columnTooltips.size()) {
                    Tooltip tooltip = columnTooltips.get(i);
                    if (tooltip != null) {
                        tooltip.render(poseStack);
                    }
                    break;
                }
            }
        }
    }
}