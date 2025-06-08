package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.utils.GuiPresets;
import net.minecraft.client.Minecraft;

public final class Tooltip extends Widget{
    private final Widget widgetRef;
    private final Text text;

    public Text getText() {
        return text;
    }

    public Tooltip(String text, java.awt.Rectangle hoverArea){
        this(text, new EmptyWidget(hoverArea.x, hoverArea.y, hoverArea.width, hoverArea.height));
    }

    public Tooltip(String text, Widget widget){
        super(widget.getLeftPos(), widget.getTopPos(), widget.getWidth(), widget.getHeight());

        this.widgetRef = widget;

        this.text = new Text(-1000, 0, text, GuiPresets.TOOLTIP_FONT_SIZE, GuiPresets.TOOLTIP_TEXT);
        this.text.setShowBackground(true);
        this.text.setBackgroundColor(GuiPresets.TOOLTIP_BACKGROUND);
        this.text.setBorderColor(GuiPresets.TOOLTIP_BACKGROUND);

        this.setHidden(true);
    }

    @Override
    public void setShowBackground(boolean showBackground) {
        super.setShowBackground(showBackground);

        this.text.setShowBackground(false);
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = this.getTopPos() - topPos;
        this.widgetRef.setTopPos(this.widgetRef.getTopPos() - deltaPos);

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = this.getLeftPos() - leftPos;
        this.widgetRef.setLeftPos(this.widgetRef.getLeftPos() - deltaPos);

        super.setLeftPos(leftPos);
    }

    @Override
    public void render(PoseStack poseStack){
        if(this.isHidden()){
            return;
        }

        this.text.render(poseStack);
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);

        this.widgetRef.update(x, y);
        this.setHidden(this.widgetRef.isHidden() || !this.widgetRef.isMouseOver());

        if(!this.isHidden()){
            this.text.setLeftPos(this.fitHorizontal(x) ? x : x - this.text.getWidth());
            this.text.setTopPos(y - this.text.getHeight());
        }
    }

    private boolean fitHorizontal(int xPos){
        if(Minecraft.getInstance().screen != null){
            return xPos + this.text.getTextWidth() < Minecraft.getInstance().screen.width;
        }

        return false;
    }
}
