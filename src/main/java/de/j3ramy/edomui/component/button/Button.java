package de.j3ramy.edomui.component.button;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.basic.ButtonStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.component.*;
import de.j3ramy.edomui.component.text.CenteredText;
import de.j3ramy.edomui.component.text.Text;
import de.j3ramy.edomui.component.text.Tooltip;
import de.j3ramy.edomui.component.text.VerticalCenteredText;

import java.awt.*;

public class Button extends Widget {
    private final IAction rightClickAction;
    private final ButtonStyle buttonStyle;

    private Tooltip tooltip;
    private boolean tooltipEnabled = false;

    protected Text title;
    protected IAction leftClickAction;

    public Button(int x, int y, int width, int height, String text, IAction leftClickAction, IAction rightClickAction, ButtonType type) {
        super(x, y, width, height);
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;

        this.buttonStyle = new ButtonStyle(ThemeManager.getDefaultButtonStyle());
        this.setStyle(this.buttonStyle);

        this.setHoverable(true);
        this.title = createTitle(type, text, 0);
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction) {
        this(x, y, width, height, title, leftClickAction, null, ButtonType.DEFAULT);
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction, ButtonType type, int padding) {
        this(x, y, width, height, title, leftClickAction, null, ButtonType.DEFAULT);

        this.title = this.createTitle(type, title, padding);
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction, ButtonType type) {
        this(x, y, width, height, title, leftClickAction, null, type);
    }

    protected Text createTitle(ButtonType type, String text, int padding) {
        int left = this.getLeftPos();
        int width = this.getWidth();

        Text title = switch (type) {
            case DROPDOWN -> new VerticalCenteredText(this.toRect(), left + padding, text, this.buttonStyle.getFontSize(),
                    width - 2 * padding - 20, this.buttonStyle.getTextColor(), this.buttonStyle.getTextHoverColor(),
                    this.buttonStyle.getTextDisabledColor()
            ){
                @Override
                public boolean isMouseOver() {
                    return Button.this.isMouseOver();
                }
            };
            case TEXT_FIELD -> new VerticalCenteredText(this.toRect(), left + padding, text, this.buttonStyle.getFontSize(),
                    width - 2 * padding, this.buttonStyle.getTextColor(), this.buttonStyle.getTextHoverColor(),
                    this.buttonStyle.getTextDisabledColor()
            ){
                @Override
                public boolean isMouseOver() {
                    return Button.this.isMouseOver();
                }
            };
            case IMAGE -> null;
            default -> {
                Rectangle rect = this.toRect();
                rect.width -= 2 * padding;
                rect.x += padding;

                yield new CenteredText(rect, text, this.buttonStyle.getFontSize(), this.buttonStyle.getTextColor(), this.buttonStyle.getTextHoverColor(),
                        this.buttonStyle.getTextDisabledColor()
                ){
                    @Override
                    public boolean isMouseOver() {
                        return Button.this.isMouseOver();
                    }
                };
            }
        };

        if (title != null) {
            title.setHoverable(true);
        }

        return title;
    }

    @Override
    public ButtonStyle getStyle() {
        return this.buttonStyle;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;
        super.render(poseStack);

        if(title != null){
            title.render(poseStack);
        }

        if (tooltipEnabled && tooltip != null && isMouseOver()) {
            tooltip.render(poseStack);
        }
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;
        super.update(x, y);

        if (tooltipEnabled && tooltip != null) {
            tooltip.update(x, y);
        }

        if (this.title != null) {
            this.title.update(x, y);

            if(this.titleColorsNeedUpdate()){
                this.title.getStyle().setTextColor(this.buttonStyle.getTextColor());
                this.title.getStyle().setTextHoverColor(this.buttonStyle.getTextHoverColor());
                this.title.getStyle().setTextDisabledColor(this.buttonStyle.getTextDisabledColor());
            }
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (!isMouseOver() || !isEnabled()) return;
        if (mouseButton == 0 && leftClickAction != null) leftClickAction.execute();
        if (mouseButton == 1 && rightClickAction != null) rightClickAction.execute();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if(this.title != null){
            title.setEnabled(enabled);
        }
    }

    @Override
    public void setLeftPos(int newLeftPos) {
        int delta = this.getLeftPos() - newLeftPos;

        if(title != null){
            title.setLeftPos(title.getLeftPos() - delta);
        }

        super.setLeftPos(newLeftPos);
    }

    @Override
    public void setTopPos(int newTopPos) {
        int delta = this.getTopPos() - newTopPos;

        if(title != null){
            title.setTopPos(title.getTopPos() - delta);
        }

        if (tooltip != null) tooltip.setTopPos(tooltip.getTopPos() - delta);
        super.setTopPos(newTopPos);
    }

    private boolean titleColorsNeedUpdate() {
        if (this.title == null) return false;

        return !this.title.getStyle().getTextColor().equals(this.buttonStyle.getTextColor()) ||
                !this.title.getStyle().getTextHoverColor().equals(this.buttonStyle.getTextHoverColor()) ||
                !this.title.getStyle().getTextDisabledColor().equals(this.buttonStyle.getTextDisabledColor());
    }

    public void setTitle(String text) {
        if(title != null){
            title.setText(text);
        }
    }

    public void enableTooltip() {
        if (tooltip == null) {
            tooltip = new Tooltip(title.getString().toString(), this);
        }
        tooltipEnabled = true;
    }

    public Text getTitle() {
        return title;
    }

    public boolean isTooltipEnabled() {
        return tooltipEnabled;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }
}