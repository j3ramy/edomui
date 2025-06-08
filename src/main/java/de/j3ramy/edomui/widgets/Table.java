package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.View;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;

import java.util.ArrayList;

public final class Table extends List {
    private int attributeRowBackgroundColor;
    private boolean isAttributeRowFixed;

    public Table(int x, int y, int width, int height, int rowHeight, int selectedColor){
        super(new ArrayList<>(), x, y, width, height, rowHeight, selectedColor);
    }

    @Override
    public void onClick(int mouseButton) {
        if(this.isMouseOver() && mouseButton == 0 && this.isEnabled()){
            super.onClick(mouseButton);

            if(this.getAttributeRow() != null){
                this.getAttributeRow().setBackgroundColor(this.attributeRowBackgroundColor);
            }
        }
    }

    @Override
    protected void initList(int startIndex) {
        super.initList(startIndex);

        if(this.isAttributeRowFixed){
            this.visibleContent.set(0, this.getAttributeRow());
        }
    }

    @Override
    public boolean hasSelection() {
        return this.getSelectedIndex() > -1;
    }

    @Override
    public int getSelectedIndex() {
        return super.getSelectedIndex() - 1;
    }

    public void addAttributeRow(ArrayList<String> attributes, int backgroundColor, int textColor){
        this.addAttributeRow(attributes, backgroundColor, textColor, true, true);
    }

    public ArrayList<ArrayList<String>> getContent(){
        ArrayList<ArrayList<String>> content = new ArrayList<>();

        for(Button row : this.content){
            if(row instanceof TableRow){
                ArrayList<String> data = new ArrayList<>();
                for(Widget widget : ((TableRow) row).getView().getWidgets()){
                    if(widget instanceof Text){
                        data.add(((Text) widget).getText().toString());
                    }
                }
                content.add(data);
            }
        }

        return content;
    }

    public void addAttributeRow(ArrayList<String> attributes, int backgroundColor, int textColor, boolean isFixed, boolean renderTooltip){
        this.attributeRowBackgroundColor = backgroundColor;
        this.isAttributeRowFixed = isFixed;

        TableRow row = new TableRow(this.getLeftPos(), this.getTopPos() + this.content.size() * this.elementHeight, this.getWidth(),
                this.elementHeight, attributes, textColor, renderTooltip);
        row.setBackgroundColor(backgroundColor);
        row.setHoverable(false);

        this.content.add(row);
        this.initList(0);
    }

    public void addRow(ArrayList<String> rowContents, int backgroundColor, int textColor, boolean renderTooltip){
        TableRow row = new TableRow(this.getLeftPos(), this.getTopPos() + this.content.size() * this.elementHeight, this.getWidth(),
                this.elementHeight, rowContents, textColor, renderTooltip);
        row.setBackgroundColor(backgroundColor);

        this.content.add(row);
        this.scrollbar.updateContentSize(this.content.size());
        this.initList(0);
    }

    public void addRow(ArrayList<String> rowContents, int backgroundColor, int textColor){
        this.addRow(rowContents, backgroundColor, textColor, true);
    }

    public ArrayList<String> getSelectedRow(){
        if(this.hasSelection()){
            TableRow row = (TableRow) this.content.get(this.selectedIndex);
            return row.getColumnContents();
        }

        return new ArrayList<>();
    }

    private TableRow getAttributeRow(){
        if(!this.content.isEmpty() && this.content.get(0) instanceof TableRow){
            return (TableRow) this.content.get(0);
        }

        return null;
    }

    final class TableRow extends Button {
        private final View view = new View();
        private final ArrayList<String> columnContents = new ArrayList<>();

        public ArrayList<String> getColumnContents() {
            return columnContents;
        }

        public View getView() {
            return view;
        }

        public TableRow(int x, int y, int width, int height, ArrayList<String> columnContents, int textColor, boolean renderTooltip) {
            super(x, y, width - GuiPresets.SCROLLBAR_TRACK_WIDTH, height, "", null);

            this.columnContents.addAll(columnContents);
            this.noBorder();
            this.setHoverBackgroundColor(selectedColor);

            final int columnWidth = width / columnContents.size();
            final int paddingX = 3;
            for(int i = 0; i < columnContents.size(); i++){
                java.awt.Rectangle rectangle = new java.awt.Rectangle(this.getLeftPos() + i * columnWidth + paddingX - 1, this.getTopPos(),
                        columnWidth - 2 * paddingX, this.getHeight());

                this.view.addWidget(new CenteredText(rectangle, columnContents.get(i), GuiPresets.TABLE_COLUMN_FONT_SIZE, rectangle.width, textColor, selectedColor, -1));

                rectangle.x = this.getLeftPos() + i * columnWidth;
                rectangle.width = columnWidth;
                if(i < columnContents.size() - 1){
                    this.view.addWidget(new VerticalLine(this.getLeftPos() + columnWidth + columnWidth * i - 1, this.getTopPos(), 1,
                            this.getHeight(), Color.WHITE));
                }

                if(renderTooltip){
                    this.view.addWidget(new Tooltip(columnContents.get(i), rectangle));
                }
            }
        }

        @Override
        public void render(PoseStack poseStack) {
            super.render(poseStack);

            this.view.render(poseStack);
        }

        @Override
        public void update(int x, int y) {
            super.update(x, y);

            this.view.update(x, y);
        }

        @Override
        public void setTopPos(int topPos) {
            for(Widget widget : this.view.getWidgets()){
                int deltaPos = this.getTopPos() - topPos;
                widget.setTopPos(widget.getTopPos() - deltaPos);
            }

            super.setTopPos(topPos);
        }

        @Override
        public void setLeftPos(int leftPos) {
            for(Widget widget : this.view.getWidgets()){
                int deltaPos = this.getLeftPos() - leftPos;
                widget.setLeftPos(widget.getLeftPos() - deltaPos);
            }

            super.setLeftPos(leftPos);
        }
    }
}