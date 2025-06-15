package de.j3ramy.edomui.components.text;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import net.minecraft.client.Minecraft;

public final class Reference extends Text {
    private final IAction clickAction;

    public Reference(int x, int y, String title, FontSize fontSize, int textColor, int hoverColor, IAction clickAction) {
        super(x, y, title, fontSize, 0, textColor, hoverColor, textColor);

        this.clickAction = clickAction;
        this.setHoverable(true);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        var font = Minecraft.getInstance().font;
        float scale = getFontSizeScale();
        float ratio = 1 / scale;

        boolean hovered = isHoverable() && isMouseOver();
        int color = !isEnabled() ? getDisabledTextColor() : hovered ? getHoverTextColor() : getTextColor();

        String rawText = getString().toString();
        String displayText = hovered ? "§n" + rawText + "§r" : rawText;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        font.draw(poseStack, displayText, getLeftPos() * ratio, getTopPos() * ratio, color);
        poseStack.popPose();
    }



    @Override
    public void onClick(int mouseButton) {
        if (clickAction != null && isMouseOver()) {
            clickAction.execute();
        }
    }
}
