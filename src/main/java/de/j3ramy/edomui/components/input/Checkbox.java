package de.j3ramy.edomui.components.input;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.text.Text;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class Checkbox extends Button {
    private boolean checked;
    private final Text label;

    private static final int LABEL_PADDING = 5;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Checkbox(int x, int y, int width, int height, String title, boolean isChecked) {
        super(x, y, width, height, "", GuiPresets.CHECKBOX_FONT_SIZE, null);

        this.checked = isChecked;
        this. setLeftClickAction(() -> this.checked = !this.checked);

        int labelX = x + this.getWidth() + LABEL_PADDING;
        this.label = new Text(labelX, y + 1, title, FontSize.BASE, Color.WHITE);
    }

    public Checkbox(int x, int y, int width, int height, String title) {
        this(x, y, width, height, title, false);
    }

    @Override
    public void onClick(int mouseButton) {
        if (!isMouseOver() || !isEnabled()) return;

        this.checked = !this.checked;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        int boxX = getLeftPos();
        int boxY = getTopPos();
        int boxSize = getWidth();

        AbstractContainerScreen.fill(
                poseStack,
                boxX, boxY,
                boxX + boxSize, boxY + boxSize,
                isMouseOver() ? this.getStyle().getHoverBackgroundColor() : this.getStyle().getBackgroundColor()
        );

        if (checked) {
            AbstractContainerScreen.fill(
                    poseStack,
                    boxX + 1, boxY + 1,
                    boxX + boxSize - 1, boxY + boxSize - 1,
                    GuiPresets.CHECKBOX_CHECK_COLOR
            );
        }

        label.render(poseStack);
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        label.update(mouseX, mouseY);
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
}