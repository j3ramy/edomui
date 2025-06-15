package de.j3ramy.edomui.components.button;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.util.style.WidgetStyle;
import de.j3ramy.edomui.components.*;
import de.j3ramy.edomui.components.text.CenteredText;
import de.j3ramy.edomui.components.text.Text;
import de.j3ramy.edomui.components.text.Tooltip;
import de.j3ramy.edomui.components.text.VerticalCenteredText;

public class Button extends Widget {
    private final IAction rightClickAction;
    private final Text title;
    private final int originalTextColor;

    private IAction leftClickAction;
    public Tooltip tooltip;
    private boolean tooltipEnabled = false;

    public Text getTitle() {
        return title;
    }

    protected void setLeftClickAction(IAction leftClickAction) {
        this.leftClickAction = leftClickAction;
    }

    public boolean isTooltipEnabled() {
        return tooltipEnabled;
    }

    public Button(int x, int y, int width, int height, String text, FontSize fontSize, IAction leftClickAction, IAction rightClickAction, ButtonType type) {
        super(x, y, width, height);
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;

        this.setHoverable(true);
        this.setStyle(new WidgetStyle(GuiPresets.DEFAULT_STYLE));
        this.title = createTitle(type, text, fontSize);
        this.originalTextColor = this.title.getTextColor();
    }

    public Button(int x, int y, int width, int height, String title, FontSize fontSize, IAction leftClickAction) {
        this(x, y, width, height, title, fontSize, leftClickAction, null, ButtonType.DEFAULT);
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction) {
        this(x, y, width, height, title, FontSize.BASE, leftClickAction);
    }

    public Button(int x, int y, int width, int height, String title, FontSize fontSize, IAction leftClickAction, ButtonType type) {
        this(x, y, width, height, title, fontSize, leftClickAction, null, type);
    }

    private Text createTitle(ButtonType type, String text, FontSize fontSize) {
        int left = this.getLeftPos();
        int width = this.getWidth();
        return switch (type) {
            case DROPDOWN -> new VerticalCenteredText(
                    this.toRect(),
                    left + GuiPresets.INPUT_LABEL_LEFT_MARGIN,
                    text,
                    fontSize,
                    width - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN - 15,
                    GuiPresets.BUTTON_TEXT,
                    GuiPresets.BUTTON_TEXT_HOVER,
                    GuiPresets.BUTTON_TEXT_DISABLED
            );
            case CHECKBOX -> new VerticalCenteredText(
                    this.toRect(),
                    left + width + GuiPresets.CHECKBOX_LABEL_LEFT_MARGIN,
                    text,
                    fontSize,
                    0,
                    GuiPresets.BUTTON_TEXT,
                    GuiPresets.BUTTON_TEXT_HOVER,
                    GuiPresets.BUTTON_TEXT_DISABLED
            );
            case TEXT_FIELD -> new VerticalCenteredText(
                    this.toRect(),
                    left + GuiPresets.INPUT_LABEL_LEFT_MARGIN,
                    text,
                    fontSize,
                    width - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN,
                    GuiPresets.BUTTON_TEXT,
                    GuiPresets.BUTTON_TEXT_HOVER,
                    GuiPresets.BUTTON_TEXT_DISABLED
            );
            default -> new CenteredText(
                    this.toRect(),
                    text,
                    fontSize,
                    GuiPresets.BUTTON_TEXT,
                    GuiPresets.BUTTON_TEXT_HOVER,
                    GuiPresets.BUTTON_TEXT_DISABLED
            );
        };
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;
        super.render(poseStack);
        title.render(poseStack);

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

        if (isShowBackground() && isHoverable() && isMouseOver()) {
            title.setTextColor(Color.getContrastColor(getStyle().getHoverBackgroundColor()));
        } else {
            title.setTextColor(originalTextColor);
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
        title.setEnabled(enabled);
    }

    @Override
    public void setLeftPos(int newLeftPos) {
        int delta = this.getLeftPos() - newLeftPos;
        title.setLeftPos(title.getLeftPos() - delta);
        super.setLeftPos(newLeftPos);
    }

    @Override
    public void setTopPos(int newTopPos) {
        int delta = this.getTopPos() - newTopPos;
        title.setTopPos(title.getTopPos() - delta);
        if (tooltip != null) tooltip.setTopPos(tooltip.getTopPos() - delta);
        super.setTopPos(newTopPos);
    }

    public void setTitle(String text) {
        title.setText(text);
        title.setTextColor(originalTextColor);
    }

    public void enableTooltip() {
        if (tooltip == null) {
            tooltip = new Tooltip(title.getString().toString(), this);
        }
        tooltipEnabled = true;
    }
}