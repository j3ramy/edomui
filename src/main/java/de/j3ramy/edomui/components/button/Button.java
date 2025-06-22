package de.j3ramy.edomui.components.button;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.ButtonStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.components.*;
import de.j3ramy.edomui.components.text.CenteredText;
import de.j3ramy.edomui.components.text.Text;
import de.j3ramy.edomui.components.text.Tooltip;
import de.j3ramy.edomui.components.text.VerticalCenteredText;

public class Button extends Widget {
    private final IAction rightClickAction;
    private final ButtonStyle buttonStyle;

    private int originalTextColor;
    private IAction leftClickAction;
    public Tooltip tooltip;
    private boolean tooltipEnabled = false;

    protected Text title;

    public Text getTitle() {
        return title;
    }

    protected void setLeftClickAction(IAction leftClickAction) {
        this.leftClickAction = leftClickAction;
    }

    public boolean isTooltipEnabled() {
        return tooltipEnabled;
    }

    public Button(int x, int y, int width, int height, String text, IAction leftClickAction, IAction rightClickAction, ButtonType type) {
        super(x, y, width, height);
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;

        this.buttonStyle = ThemeManager.getDefaultButtonStyle();
        this.setStyle(this.buttonStyle);

        this.setHoverable(true);
        this.title = createTitle(type, text, 0);

        if(title != null) {
            this.originalTextColor = this.title.getTextColor();
        }
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction) {
        this(x, y, width, height, title, leftClickAction, null, ButtonType.DEFAULT);
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction, ButtonType type) {
        this(x, y, width, height, title, leftClickAction, null, type);
    }

    protected Text createTitle(ButtonType type, String text, int leftPadding) {
        int left = this.getLeftPos();
        int width = this.getWidth();
        return switch (type) {
            case DROPDOWN -> new VerticalCenteredText(
                    this.toRect(),
                    left + leftPadding,
                    text,
                    this.buttonStyle.getFontSize(),
                    width - 2 * leftPadding - 16,
                    this.buttonStyle.getTextColor(),
                    this.buttonStyle.getTextHoverColor(),
                    this.buttonStyle.getTextDisabledColor()
            );
            case TEXT_FIELD -> new VerticalCenteredText(
                    this.toRect(),
                    left + leftPadding,
                    text,
                    this.buttonStyle.getFontSize(),
                    width - 2 * leftPadding,
                    this.buttonStyle.getTextColor(),
                    this.buttonStyle.getTextHoverColor(),
                    this.buttonStyle.getTextDisabledColor()
            );
            case IMAGE -> null;
            default -> new CenteredText(
                    this.toRect(),
                    text,
                    this.buttonStyle.getFontSize(),
                    this.buttonStyle.getTextColor(),
                    this.buttonStyle.getTextHoverColor(),
                    this.buttonStyle.getTextDisabledColor()
            );
        };
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

        if(title != null){
            if (isShowBackground() && isHoverable() && isMouseOver() ) {
                title.setTextColor(Color.getContrastColor(getStyle().getHoverBackgroundColor()));
            } else {
                title.setTextColor(originalTextColor);
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

    public void setTitle(String text) {
        if(title != null){
            title.setText(text);
            title.setTextColor(originalTextColor);
        }
    }

    public void enableTooltip() {
        if (tooltip == null) {
            tooltip = new Tooltip(title.getString().toString(), this);
        }
        tooltipEnabled = true;
    }
}