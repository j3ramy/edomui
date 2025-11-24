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
import de.j3ramy.edomui.util.style.GuiPresets;

import java.awt.*;

public class Button extends CompositeWidget {
    private final IAction rightClickAction;
    private final ButtonStyle buttonStyle;
    protected final IAction leftClickAction;

    private Tooltip tooltip;
    private boolean tooltipEnabled = false;

    protected Text title;

    public Button(int x, int y, int width, int height, String text, IAction leftClickAction, IAction rightClickAction, ButtonType type) {
        super(x, y, width, height);
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;

        this.buttonStyle = new ButtonStyle(ThemeManager.getDefaultButtonStyle());
        this.setStyle(this.buttonStyle);

        this.setHoverable(true);
        this.title = createTitle(type, text, 0);

        this.syncChildStyles();
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

        int rightPadding = padding;
        if (type == ButtonType.DROPDOWN) {
            rightPadding = padding + GuiPresets.DROPDOWN_RIGHT_PADDING;
        }

        int maxTextWidth = width - padding - rightPadding;

        Text title = switch (type) {
            case DROPDOWN, TEXT_FIELD -> new VerticalCenteredText(this.toRect(), left + padding, text, this.buttonStyle.getFontSize(),
                    maxTextWidth, this.buttonStyle.getTextColor(), this.buttonStyle.getTextHoverColor(), this.buttonStyle.getTextDisabledColor()) {
                @Override
                public boolean isMouseOver() {
                    return Button.this.isMouseOver();
                }
            };

            default -> {
                Rectangle rect = this.toRect();
                rect.width -= padding + rightPadding;
                rect.x += padding;

                yield new CenteredText(rect, text, this.buttonStyle.getFontSize(), maxTextWidth, this.buttonStyle.getTextColor(),
                        this.buttonStyle.getTextHoverColor(), this.buttonStyle.getTextDisabledColor()) {
                    @Override
                    public boolean isMouseOver() {
                        return Button.this.isMouseOver();
                    }
                };
            }
        };

        title.setHoverable(true);
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

        if (tooltipEnabled && tooltip != null) {
            boolean isImageButton = this instanceof StaticImageButton;
            boolean showTooltip = isImageButton ? isMouseOver() : (title != null && title.isTruncated() && isMouseOver());

            tooltip.setHidden(!showTooltip);

            if (!tooltip.isHidden()) {
                tooltip.render(poseStack);
            }
        }
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;
        super.update(x, y);

        if (tooltipEnabled && tooltip != null) {
            boolean isImageButton = this instanceof StaticImageButton;
            boolean showTooltip = isImageButton ? isMouseOver() : (title != null && title.isTruncated() && isMouseOver());

            tooltip.setHidden(!showTooltip);

            if (!tooltip.isHidden()) {
                tooltip.update(x, y);
            }
        }

        if (this.title != null) {
            this.title.update(x, y);
        }
    }

    @Override
    protected void syncChildStyles() {
        if (this.title != null) {
            this.title.getStyle().setTextColor(this.buttonStyle.getTextColor());
            this.title.getStyle().setTextHoverColor(this.buttonStyle.getTextHoverColor());
            this.title.getStyle().setTextDisabledColor(this.buttonStyle.getTextDisabledColor());
            this.title.getStyle().setFontSize(this.buttonStyle.getFontSize());
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (!isMouseOver() || !isEnabled()){
            return;
        }

        if (mouseButton == 0 && leftClickAction != null) {
            leftClickAction.execute();
        }

        if (mouseButton == 1 && rightClickAction != null) {
            rightClickAction.execute();
        }
    }

    public void executeLeftClick() {
        if (leftClickAction != null) {
            leftClickAction.execute();
        }
    }

    public void executeRightClick() {
        if (rightClickAction != null) {
            rightClickAction.execute();
        }
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

    public void setTitle(String text, boolean replace, int padding) {
        if(replace){
            this.title = this.createTitle(ButtonType.DEFAULT, text, padding);
        } else if(title != null){
            title.setText(text);
            title.autoWidth();
        }
    }

    public void setTitle(String text) {
        this.setTitle(text, false, 0);
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

    public String getText(){
        return this.getTitle().getString().toString();
    }

    public boolean isTooltipEnabled() {
        return tooltipEnabled;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }
}