package de.j3ramy.edomui.component.input;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.text.VerticalCenteredText;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.input.CheckboxStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.component.text.Text;
import de.j3ramy.edomui.util.style.GuiPresets;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class Checkbox extends Button {
    private final Text label;
    private final CheckboxStyle checkboxStyle;

    private boolean checked;

    public Checkbox(int x, int y, int width, int height, String title, boolean isChecked, IAction leftClickAction) {
        super(x, y, width, height, "", leftClickAction);

        this.checkboxStyle = new CheckboxStyle(ThemeManager.getDefaultCheckboxStyle());
        this.setStyle(this.checkboxStyle);

        this.checked = isChecked;

        int labelX = x + this.getWidth() + this.checkboxStyle.getLabelLeftMargin();
        java.awt.Rectangle labelArea = new java.awt.Rectangle(labelX, y, GuiPresets.MAX_TEXT_LENGTH, height);
        this.label = new VerticalCenteredText(labelArea, labelX, title, this.checkboxStyle.getFontSize(),
                this.checkboxStyle.getTextColor(), this.checkboxStyle.getTextHoverColor(), this.checkboxStyle.getTextDisabledColor());
    }

    public Checkbox(int x, int y, int width, int height, String title, boolean isChecked) {
        this(x, y, width, height, title, isChecked, null);
    }

    public Checkbox(int x, int y, int width, int height, String title) {
        this(x, y, width, height, title, false);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) {
            return;
        }

        super.render(poseStack);

        int boxX = getLeftPos();
        int boxY = getTopPos();
        int boxSize = getWidth();

        if (checked) {
            int checkMargin = this.checkboxStyle.getCheckMargin();
            AbstractContainerScreen.fill(poseStack, boxX + checkMargin, boxY + checkMargin, boxX + boxSize - checkMargin,
                    boxY + boxSize - checkMargin, this.checkboxStyle.getCheckColor().getRGB());
        }

        label.render(poseStack);
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        label.update(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseButton) {
        if(!this.isEnabled() || this.isHidden() || !this.isMouseOver()){
            return;
        }

        this.checked = !this.checked;
        super.onClick(mouseButton);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int dx = leftPos - getLeftPos();
        super.setLeftPos(leftPos);
        label.setLeftPos(label.getLeftPos() + dx);
    }

    @Override
    public void setTopPos(int topPos) {
        int dy = topPos - getTopPos();
        super.setTopPos(topPos);
        label.setTopPos(label.getTopPos() + dy);
    }

    @Override
    public CheckboxStyle getStyle() {
        return this.checkboxStyle;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Text getLabel() {
        return label;
    }
}