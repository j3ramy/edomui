package de.j3ramy.edomui.component.text;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.theme.text.TooltipStyle;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.util.style.GuiUtils;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public final class Tooltip extends Widget {
    private final Widget target;
    private final String text;
    private final TooltipStyle tooltipStyle;

    private String[] wrappedLines;

    public Tooltip(String text, Widget target) {
        super(target.getLeftPos(), target.getTopPos(), 0, 0);
        this.target = target;
        this.text = text.replaceAll(GuiPresets.TEXT_AREA_DELIMITER, "");

        this.tooltipStyle = new TooltipStyle(ThemeManager.getDefaultTooltipStyle());
        this.setStyle(tooltipStyle);

        this.setWidth(this.tooltipStyle.getMinWidth());
        this.setHeight(this.tooltipStyle.getMinHeight());

        calculateSize();
        setHidden(true);
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);

        boolean shouldShow = !target.isHidden() && target.isMouseOver();
        setHidden(!shouldShow);

        if (!isHidden()) {
            positionTooltip(x, y);
        }
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!isHidden()) {
            super.render(poseStack);

            renderText(poseStack);
        }
    }

    private void calculateSize() {
        Minecraft minecraft = Minecraft.getInstance();
        float fontScale = GuiUtils.getFontScale(this.tooltipStyle.getFontSize());

        // Check if text fits in one line
        int singleLineWidth = (int)(minecraft.font.width(text) * fontScale);
        int padding = this.tooltipStyle.getPadding();

        if (singleLineWidth + padding <= this.tooltipStyle.getMaxWidth()) {
            // Single line - use exact width needed
            this.wrappedLines = new String[]{text};
            this.setWidth(Math.max(this.tooltipStyle.getMinWidth(), singleLineWidth + 2 * padding));
        } else {
            // Multi-line - wrap text
            this.wrappedLines = wrapText(text, this.tooltipStyle.getMaxWidth() -  2 * padding);

            // Calculate actual width needed for wrapped text
            int maxLineWidth = 0;
            for (String line : wrappedLines) {
                int lineWidth = (int)(minecraft.font.width(line) * fontScale);
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
            }
            this.setWidth(Math.max(this.tooltipStyle.getMinWidth(), maxLineWidth + 2 * padding));
        }

        // Calculate height
        int lineHeight = (int)(GuiPresets.LETTER_HEIGHT * fontScale);
        int lineSpacing = this.tooltipStyle.getLineSpacing();

        int totalHeight;
        if (wrappedLines.length == 1) {
            // Single line - no line spacing needed
            totalHeight = lineHeight + 2 * padding;
        } else {
            // Multi-line - with line spacing
            totalHeight = wrappedLines.length * (lineHeight + lineSpacing) - lineSpacing + 2 * padding;
        }

        int minHeight = lineHeight + 2 * padding; // Minimum für single line

        this.setHeight(Math.max(minHeight, totalHeight));
    }

    private String[] wrapText(String text, int maxWidth) {
        Minecraft minecraft = Minecraft.getInstance();
        float fontScale = GuiUtils.getFontScale(this.tooltipStyle.getFontSize());

        String[] words = text.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            int testWidth = (int)(minecraft.font.width(testLine) * fontScale);

            if (testWidth <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    lines.add(word);
                }
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }

    private void positionTooltip(int mouseX, int mouseY) {
        int tooltipWidth = this.getWidth();
        int tooltipHeight = this.getHeight();

        Minecraft minecraft = Minecraft.getInstance();
        int screenWidth = minecraft.screen != null ? minecraft.screen.width : Integer.MAX_VALUE;
        int screenHeight = minecraft.screen != null ? minecraft.screen.height : Integer.MAX_VALUE;

        int posX = mouseX;
        if (posX + tooltipWidth > screenWidth) {
            posX = mouseX - tooltipWidth;
        }

        int posY = mouseY - tooltipHeight;
        if (posY < 0) {
            posY = mouseY;
        }

        posX = Math.max(2, Math.min(posX, screenWidth - tooltipWidth - 2));
        posY = Math.max(2, Math.min(posY, screenHeight - tooltipHeight - 2));

        this.setLeftPos(posX);
        this.setTopPos(posY);
    }

    private void renderText(PoseStack poseStack) {
        if (wrappedLines == null || wrappedLines.length == 0) return;

        Minecraft minecraft = Minecraft.getInstance();
        float fontScale = GuiUtils.getFontScale(this.tooltipStyle.getFontSize());

        int padding = this.tooltipStyle.getPadding();
        int x = this.getLeftPos() + padding;
        int y = this.getTopPos() + padding;

        int lineHeight = (int)(GuiPresets.LETTER_HEIGHT * fontScale);
        int lineSpacing = this.tooltipStyle.getLineSpacing();

        for (int i = 0; i < wrappedLines.length; i++) {
            String line = wrappedLines[i];
            int lineY = y + i * (lineHeight + lineSpacing);

            poseStack.pushPose();
            poseStack.scale(fontScale, fontScale, 1.0f);

            int scaledX = (int)(x / fontScale);
            int scaledY = (int)(lineY / fontScale);

            minecraft.font.draw(poseStack, line, scaledX, scaledY, this.tooltipStyle.getTextColor().getRGB());
            poseStack.popPose();
        }
    }

    public String getText() {
        return text;
    }

    public Widget getTarget() {
        return target;
    }
}