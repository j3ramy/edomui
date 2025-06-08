package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.Color;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;


public final class Grid extends Widget {
    private final ArrayList<ArrayList<Cell>> grid;
    private final int cellWidth, cellHeight, cellMargin, maxVisibleRows, maxVisibleCols;
    private final Point visibleRowRange;
    private final VerticalScrollbar scrollbar;

    public Grid(int x, int y, int width, int height, int cellWidth, int cellHeight, int cellMargin) {
        super(x, y, width, height);

        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellMargin = cellMargin;

        this.maxVisibleRows = this.getHeight() / (this.cellHeight + this.cellMargin);
        this.maxVisibleCols = this.getWidth() / (this.cellWidth + this.cellMargin);
        this.visibleRowRange =  new Point(0, this.maxVisibleRows - 1);

        this.grid = new ArrayList<>();
        this.grid.add(new ArrayList<>());

        this.scrollbar = new VerticalScrollbar(this, 0, this.maxVisibleRows);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!this.isHidden()) {
            super.render(poseStack);

            ArrayList<Tooltip> tooltipsToRender = new ArrayList<>();

            for (ArrayList<Cell> row : grid) {
                for (Cell cell : row) {
                    if (cell != null) {
                        cell.render(poseStack);
                        if (cell.tooltip != null && !cell.tooltip.isHidden()) {
                            tooltipsToRender.add(cell.tooltip);
                        }
                    }
                }
            }

            for (Tooltip tooltip : tooltipsToRender) {
                tooltip.render(poseStack);
            }

            if (this.needsScrolling()) {
                this.scrollbar.render(poseStack);
            }
        }
    }

    @Override
    public void update(int x, int y) {
        if (!this.isHidden()) {
            super.update(x, y);

            int leftPos = this.getLeftPos();
            int topPos = this.getTopPos();
            int mouseX = this.getMousePosition().x;
            int mouseY = this.getMousePosition().y;

            int counter = 0;
            for (int i = 0; i < this.grid.size(); i++) {
                boolean isRowInVisibleRange = this.isIndexInVisibleRange(i);

                for (int j = 0; j < this.grid.get(i).size(); j++) {
                    Cell cell = this.grid.get(i).get(j);
                    if (cell == null) {
                        continue;
                    }

                    cell.setHidden(true);

                    if (isRowInVisibleRange) {
                        cell.setHidden(false);
                        cell.updateCell(counter, j, leftPos, topPos);
                    }

                    cell.update(mouseX, mouseY);
                }

                if (counter < this.maxVisibleRows && isRowInVisibleRange) {
                    counter++;
                }
            }
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if(!this.isHidden()){
            Cell hoveredCell = this.getHoveredCell();
            if(hoveredCell != null){
                hoveredCell.onClick(mouseButton);
            }
        }
    }

    @Override
    public void onScroll(double delta) {
        if(!this.isHidden() && this.isMouseOver() && this.needsScrolling()){
            int maxScrollIndex = this.grid.size() - 1;

            if(this.visibleRowRange.y < maxScrollIndex && delta == -1){
                this.visibleRowRange.x++;
                this.visibleRowRange.y++;
            }

            if(this.visibleRowRange.x > 0 && delta == 1){
                this.visibleRowRange.x--;
                this.visibleRowRange.y--;
            }

            this.scrollbar.updateScrollIndex(this.visibleRowRange.x);
        }
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = this.getTopPos() - topPos;
        this.scrollbar.setTopPos(this.scrollbar.getTopPos() - deltaPos);

        for(ArrayList<Cell> list : this.grid){
            for(Cell cell : list){
                cell.setTopPos(cell.getTopPos() - deltaPos);
            }
        }

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = this.getLeftPos() - leftPos;
        this.scrollbar.setLeftPos(this.scrollbar.getLeftPos() - deltaPos);

        for(ArrayList<Cell> list : this.grid){
            for(Cell cell : list){
                cell.setLeftPos(cell.getLeftPos() - deltaPos);
            }
        }

        super.setLeftPos(leftPos);
    }

    public void add(String label, int labelColor, int backgroundColor, IAction leftClickAction) {
        Button button = new Button(0, 0, this.cellWidth, this.cellHeight, label, FontSize.XS, leftClickAction);
        button.getTitle().setTextColor(labelColor);
        button.setShowBackground(false);

        Cell cell = new Cell(this.cellWidth, this.cellHeight, button, "", Color.WHITE);
        cell.setBackgroundColor(backgroundColor);
        this.grid.get(this.getNextEmptyCell()).add(cell);

        this.scrollbar.updateContentSize(this.grid.size());
        this.update(this.getMousePosition().x, this.getMousePosition().y);
    }

    public void add(ResourceLocation image, int uOffset, int vOffset, int textureWidth, int textureHeight, IAction leftClickAction, IAction rightClickAction,
                    String label, int labelColor){
        ImageButton imageButton = new ImageButton(0, 0, this.cellWidth, this.cellHeight, uOffset, vOffset, textureWidth, textureHeight, 0,
                image, leftClickAction, rightClickAction);
        imageButton.setShowBackground(false);

        Cell cell = new Cell(this.cellWidth, this.cellHeight, imageButton, label, labelColor);
        cell.setBackgroundColor(Color.WHITE);
        this.grid.get(this.getNextEmptyCell()).add(cell);

        this.scrollbar.updateContentSize(this.grid.size());
        this.update(this.getMousePosition().x, this.getMousePosition().y);
    }

    public void add(ResourceLocation image, IAction leftClickAction, IAction rightClickAction, String label, int labelColor){
        this.add(image, 0, 0, this.cellWidth, this.cellHeight, leftClickAction, rightClickAction, label, labelColor);
    }

    public void add(ResourceLocation image, String label, int labelColor){
        this.add(image, null, null, label, labelColor);
    }

    public void add(ResourceLocation image){
        this.add(image, null, null, "", -1);
    }

    public void replace(int rowIndex, int columnIndex, ImageButton imageButton, String label, int labelColor){
        if(imageButton != null && rowIndex < this.grid.size() && columnIndex < this.grid.get(rowIndex).size()){
            imageButton.setShowBackground(false);

            Cell cell = new Cell(this.cellWidth, this.cellHeight, imageButton, label, labelColor);
            cell.setBackgroundColor(Color.WHITE);
            this.grid.get(rowIndex).set(columnIndex, cell);
        }
    }

    public void replace(int rowIndex, int columnIndex, ImageButton imageButton){
        this.replace(rowIndex, columnIndex, imageButton, "", -1);
    }

    public void remove(int rowIndex, int columnIndex){
        if(rowIndex < this.grid.size() && columnIndex < this.grid.get(rowIndex).size()){
            this.grid.get(rowIndex).remove(columnIndex);
        }
    }

    public void removeLast(){
        if(!this.grid.isEmpty())
            this.grid.get(this.grid.size() - 1).remove(this.getLastCell());
    }

    public void resetScrollProgress(){
        this.visibleRowRange.x = 0;
        this.visibleRowRange.y = this.maxVisibleRows - 1;
    }

    public void clear(){
        this.grid.clear();
    }

    public int getCellCount(){
        int count = 0;
        for(ArrayList<Cell> row : this.grid){
            count += row.size();
        }

        return count;
    }

    @Nullable
    public Cell getHoveredCell(){
        for(ArrayList<Cell> row : this.grid){
            for(Cell cell : row){
                if(cell != null && !cell.isHidden()){
                    java.awt.Rectangle rectangle = new java.awt.Rectangle(cell.getLeftPos(), cell.getTopPos(), cell.getWidth(), cell.getHeight());
                    if(rectangle.contains(new Point(this.getMousePosition().x, this.getMousePosition().y))){
                        return cell;
                    }
                }
            }
        }

        return null;
    }

    public boolean isMouseOverCell(){
        return this.getHoveredCell() != null;
    }

    private int getNextEmptyCell(){
        for(int rowIndex = 0; rowIndex < this.grid.size(); rowIndex++){
            if (!this.isRowFull(rowIndex)) {
                return rowIndex;
            }
            else if(rowIndex == this.grid.size() - 1){
                this.grid.add(new ArrayList<>());
                return this.grid.size() - 1;
            }
        }

        if(this.grid.isEmpty())
            this.grid.add(new ArrayList<>());

        return 0;
    }

    private Cell getLastCell(){
        ArrayList<Cell> row = this.grid.get(this.grid.size() - 1);
        return row.get(row.size() - 1);
    }

    private boolean isRowFull(int rowIndex){
        return this.grid.get(rowIndex).size() >= this.maxVisibleCols;
    }

    private boolean needsScrolling(){
        return this.getCellCount() > this.maxVisibleRows * this.maxVisibleCols;
    }

    private boolean isIndexInVisibleRange(int index){
        return index >= this.visibleRowRange.x && index <= this.visibleRowRange.y;
    }

    public class Cell extends Widget {
        private final Button button;

        private HorizontalCenteredText label;
        private Tooltip tooltip;

        public String getLabel() {
            return label.getText().toString();
        }

        public Cell(int width, int height, @Nullable Button button, String label, int textColor) {
            super(0, 0, width, height);

            this.button = button;

            if(this.button instanceof ImageButton){
                this.label = new HorizontalCenteredText(this.toRect(), this.getTopPos() + this.getHeight() + 2, label,
                        FontSize.XXS, this.getWidth(), textColor);
            }
            else if(this.button != null){
                this.button.enableTooltip();
            }

            this.setHoverable(true);
            this.noBorder();
        }

        @Override
        public void render(PoseStack poseStack) {
            if (!this.isHidden()) {
                super.render(poseStack);

                if (this.button != null) {
                    this.button.render(poseStack);
                }

                if(this.label != null){
                    this.label.render(poseStack);
                }

                if (this.tooltip != null) {
                    this.tooltip.render(poseStack);
                }
            }
        }

        @Override
        public void update(int x, int y) {
            super.update(x, y);

            if (this.button != null) {
                this.button.update(x, y);
            }

            if(this.label != null){
                this.label.update(x, y);
            }

            if (this.tooltip != null) {
                this.tooltip.update(x, y);
            }
        }

        @Override
        public void onClick(int mouseButton) {
            super.onClick(mouseButton);

            if(this.button != null){
                this.button.onClick(mouseButton);
            }
        }

        public void updateCell(int rowIndex, int colIndex, int leftPos, int topPos) {
            if (!this.isHidden()) {
                this.setLeftPos(leftPos + cellMargin + (cellMargin * colIndex + cellWidth * colIndex));
                this.setTopPos(topPos + cellMargin + (cellMargin * rowIndex + cellHeight * rowIndex));

                if (this.button != null) {
                    this.button.update(leftPos, topPos);

                    this.tooltip = new Tooltip(this.button.getTitle().getText().toString(), this.toRect());
                }

                if (this.label != null) {
                    this.label.centerHorizontally(this.toRect());
                    this.label.setTopPos(this.getTopPos() + this.getHeight() + 2);

                    this.tooltip = new Tooltip(label.getText().toString(), this.toRect());
                }
            }
        }

        @Override
        public void setTopPos(int topPos) {
            int deltaPos = this.getTopPos() - topPos;

            if (this.button != null) {
                this.button.setTopPos(this.button.getTopPos() - deltaPos);
            }

            if (this.label != null) {
                this.label.setTopPos(this.label.getTopPos() - deltaPos + 2);
            }

            super.setTopPos(topPos);
        }

        @Override
        public void setLeftPos(int leftPos) {
            int deltaPos = this.getLeftPos() - leftPos;

            if (this.button != null) {
                this.button.setLeftPos(this.button.getLeftPos() - deltaPos);
            }

            if(this.label != null){
                this.label.centerHorizontally(this.toRect());
            }

            super.setLeftPos(leftPos);
        }
    }
}
