package de.j3ramy.edomui.component.input;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.component.basic.VerticalScrollbar;
import de.j3ramy.edomui.component.text.Text;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.theme.input.TextAreaStyle;
import de.j3ramy.edomui.theme.input.TextFieldStyle;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.util.style.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.*;

public class TextArea extends Widget {
    private final List<String> lines = new ArrayList<>();
    private final VerticalScrollbar scrollbar;
    private final IValueAction onTextChangeAction;
    private final Text placeholderRenderer;
    private final int lineHeight;
    private final int maxVisibleLines;
    private final TextAreaStyle textAreaStyle;
    private final Set<Character> forbiddenCharacters = new HashSet<>();

    // Cursor & Selection
    private int caretRow = 0;
    private int caretCol = 0;
    private int selectionStartRow = -1;
    private int selectionStartCol = -1;
    private boolean isManuallyScrolling = false;

    // Visual State
    private boolean focused = false;
    private boolean caretVisible = true;
    private int caretTickCounter = 0;
    private int scrollOffset = 0;

    // Multi-click handling
    private long lastClickTime = 0;
    private int lastClickRow = -1;
    private int lastClickCol = -1;
    private int clickCount = 0;

    // Configuration
    private String placeholder;
    private boolean wordWrap = true;
    private boolean touched = false;

    public TextArea(int x, int y, int width, int height) {
        this(x, y, width, height,"", null);
    }

    public TextArea(int x, int y, int width, int height, String placeholder) {
        this(x, y, width, height, placeholder, null);
    }

    public TextArea(int x, int y, int width, int height, @Nullable IValueAction onTextChange) {
        this(x, y, width, height, "", onTextChange);
    }

    public TextArea(int x, int y, int width, int height, String placeholder, @Nullable IValueAction onTextChange) {
        super(x, y, width, height);

        this.placeholder = placeholder;
        this.onTextChangeAction = onTextChange;

        this.textAreaStyle = new TextAreaStyle(ThemeManager.getDefaultTextAreaStyle());
        super.setStyle(this.textAreaStyle);

        // Initialize text renderers first
        int padding = this.textAreaStyle.getPadding();
        Text textRenderer = new Text(x + padding, y + padding, "", this.textAreaStyle.getFontSize(),
                this.textAreaStyle.getTextColor());
        textRenderer.disableTruncate();

        this.placeholderRenderer = new Text(x + padding, y + padding, placeholder, this.textAreaStyle.getFontSize(),
                this.textAreaStyle.getPlaceholderColor());

        // Calculate proper line height based on actual font rendering
        float fontScale = GuiUtils.getFontScale(this.textAreaStyle.getFontSize());
        int baseLineHeight = (int)(7 * fontScale);
        int lineSpacing = this.textAreaStyle.getLineSpacing();
        this.lineHeight = baseLineHeight + lineSpacing;

        int availableHeight = height - 2 * padding;
        if (availableHeight >= baseLineHeight) {
            int remainingHeight = availableHeight - baseLineHeight;
            this.maxVisibleLines = 1 + Math.max(0, remainingHeight / lineHeight);
        } else {
            this.maxVisibleLines = 1;
        }

        this.scrollbar = new VerticalScrollbar(this, 1, maxVisibleLines);

        lines.add("");
        updateScrollbarData();

        setHoverable(true);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        renderSelection(poseStack);
        renderTextContent(poseStack);
        renderCursor(poseStack);

        if (needsScrolling()) {
            scrollbar.render(poseStack);
        }

        renderPlaceholder(poseStack);
    }

    private void renderTextContent(PoseStack poseStack) {
        if (isEmpty() && !focused) return;

        // Render each line separately with consistent spacing
        int visibleLineCount = Math.min(maxVisibleLines, lines.size() - scrollOffset);

        for (int i = 0; i < visibleLineCount; i++) {
            int lineIndex = scrollOffset + i;
            if (lineIndex >= lines.size()) break;

            String line = lines.get(lineIndex);

            // Calculate position with consistent line height
            int lineY = getTopPos() + this.textAreaStyle.getPadding() + (i * lineHeight);

            // Create temporary text renderer for each line
            Text lineRenderer = new Text(
                    getLeftPos() + this.textAreaStyle.getPadding(),
                    lineY,
                    line,
                    this.textAreaStyle.getFontSize(),
                    this.isEmpty()  ? this.textAreaStyle.getPlaceholderColor() : this.textAreaStyle.getTextColor()
            );
            lineRenderer.disableTruncate();
            lineRenderer.render(poseStack);
        }
    }

    private void renderPlaceholder(PoseStack poseStack) {
        if (!isEmpty() || placeholder.isEmpty() || focused) return;

        placeholderRenderer.setLeftPos(getLeftPos() + this.textAreaStyle.getPadding());
        placeholderRenderer.setTopPos(getTopPos() + this.textAreaStyle.getPadding());
        placeholderRenderer.render(poseStack);
    }

    private void renderSelection(PoseStack poseStack) {
        if (!hasSelection()) return;

        int startRow = Math.min(caretRow, selectionStartRow);
        int endRow = Math.max(caretRow, selectionStartRow);
        int startCol = (caretRow < selectionStartRow || (caretRow == selectionStartRow && caretCol < selectionStartCol))
                ? caretCol : selectionStartCol;
        int endCol = (caretRow > selectionStartRow || (caretRow == selectionStartRow && caretCol > selectionStartCol))
                ? caretCol : selectionStartCol;

        float fontScale = GuiUtils.getFontScale(this.textAreaStyle.getFontSize());

        for (int row = startRow; row <= endRow; row++) {
            if (row < scrollOffset || row >= scrollOffset + maxVisibleLines) continue;
            if (row >= lines.size()) break;

            String line = lines.get(row);
            int lineStartCol = (row == startRow) ? startCol : 0;
            int lineEndCol = (row == endRow) ? endCol : line.length();

            if (lineStartCol < lineEndCol) {
                String beforeSelection = line.substring(0, lineStartCol);
                String selection = line.substring(lineStartCol, lineEndCol);

                int x1 = getLeftPos() + this.textAreaStyle.getPadding() + (int)(Minecraft.getInstance().font.width(beforeSelection) * fontScale);
                int x2 = x1 + (int)(Minecraft.getInstance().font.width(selection) * fontScale);
                int y1 = getTopPos() + this.textAreaStyle.getPadding() + (row - scrollOffset) * lineHeight;
                int y2 = y1 + (int)(7 * fontScale); // Only cover the actual text height

                AbstractContainerScreen.fill(poseStack, x1, y1, x2, y2, this.textAreaStyle.getSelectionColor().getRGB());
            }
        }
    }

    private void renderCursor(PoseStack poseStack) {
        if (!focused || !caretVisible) return;
        if (caretRow < scrollOffset || caretRow >= scrollOffset + maxVisibleLines) return;
        if (caretRow >= lines.size()) return;

        String line = lines.get(caretRow);
        String beforeCaret = line.substring(0, Math.min(caretCol, line.length()));
        float fontScale = GuiUtils.getFontScale(this.textAreaStyle.getFontSize());

        int x = getLeftPos() + this.textAreaStyle.getPadding() + (int)(Minecraft.getInstance().font.width(beforeCaret) * fontScale);
        int y1 = getTopPos() + this.textAreaStyle.getPadding() + (caretRow - scrollOffset) * lineHeight - 1;
        int y2 = y1 + (int)(7 * fontScale) + 1; // Match text height exactly

        // Scale cursor width with font size
        int cursorWidth = Math.max(1, (int)(fontScale));

        AbstractContainerScreen.fill(poseStack, x, y1, x + cursorWidth, y2, this.textAreaStyle.getTextColor().getRGB());
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);

        if (needsScrolling()) {
            scrollbar.update(x, y);
        }

        if (!isManuallyScrolling && this.isEnabled()) {
            ensureCaretVisible();
        }
    }

    @Override
    public void tick() {
        if (!focused) return;

        caretTickCounter++;
        if (caretTickCounter >= GuiPresets.CURSOR_BLINK_TICK_TIME) {
            caretVisible = !caretVisible;
            caretTickCounter = 0;
        }
    }

    @Override
    public void onScroll(double delta) {
        if (!isMouseOver() || !needsScrolling()) return;

        isManuallyScrolling = true;

        int maxScrollOffset = Math.max(0, lines.size() - maxVisibleLines);
        int newScrollOffset = scrollOffset + (delta < 0 ? 1 : -1);
        newScrollOffset = Math.max(0, Math.min(newScrollOffset, maxScrollOffset));

        if (newScrollOffset != scrollOffset) {
            scrollOffset = newScrollOffset;
            scrollbar.updateScrollIndex(scrollOffset);
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (mouseButton != 0) return;

        if (isMouseOver()) {
            if (this.isEnabled()) {
                touched = true;
                focused = true;
                caretVisible = true;
                caretTickCounter = 0;
            }

            int clickX = getMousePosition().x - getLeftPos() - this.textAreaStyle.getPadding();
            int clickY = getMousePosition().y - getTopPos() - this.textAreaStyle.getPadding();

            int clickedRow = scrollOffset + (clickY / lineHeight);
            clickedRow = Math.max(0, Math.min(clickedRow, lines.size() - 1));

            String line = lines.get(clickedRow);
            int clickedCol = getCharIndexFromPixel(line, clickX);

            long currentTime = System.currentTimeMillis();
            boolean samePosition = (clickedRow == lastClickRow && clickedCol == lastClickCol);
            boolean withinTimeWindow = (currentTime - lastClickTime) < GuiPresets.DOUBLE_CLICK_THRESHOLD_NORMAL;

            if (samePosition && withinTimeWindow) {
                clickCount++;
            } else {
                clickCount = 1;
            }

            lastClickTime = currentTime;
            lastClickRow = clickedRow;
            lastClickCol = clickedCol;

            switch (clickCount) {
                case 1 -> handleSingleClick(clickedRow, clickedCol);
                case 2 -> handleDoubleClick(clickedRow, clickedCol);
                case 3 -> handleTripleClick();
                default -> {
                    clickCount = 1;
                    handleSingleClick(clickedRow, clickedCol);
                }
            }

        } else {
            if (this.isEnabled()) {
                focused = false;
            }
        }
    }

    @Override
    public void setTopPos(int topPos) {
        int delta = this.getTopPos() - topPos;
        super.setTopPos(topPos);

        if (scrollbar != null) {
            scrollbar.setTopPos(scrollbar.getTopPos() - delta);
        }

        if (placeholderRenderer != null) {
            placeholderRenderer.setTopPos(getTopPos() + this.textAreaStyle.getPadding());
        }
    }

    @Override
    public void setLeftPos(int leftPos) {
        int delta = this.getLeftPos() - leftPos;
        super.setLeftPos(leftPos);

        if (scrollbar != null) {
            scrollbar.setLeftPos(scrollbar.getLeftPos() - delta);
        }

        if (placeholderRenderer != null) {
            placeholderRenderer.setLeftPos(getLeftPos() + this.textAreaStyle.getPadding());
        }
    }

    private void handleSingleClick(int row, int col) {
        setCaretPosition(row, col);
        clearSelection();
    }

    private void handleDoubleClick(int row, int col) {
        if (row >= lines.size()) return;

        String line = lines.get(row);
        if (line.isEmpty()) {
            handleSingleClick(row, col);
            return;
        }

        int[] wordBounds = findWordBoundaries(line, col);
        int wordStart = wordBounds[0];
        int wordEnd = wordBounds[1];

        selectionStartRow = row;
        selectionStartCol = wordStart;
        setCaretPosition(row, wordEnd);
    }

    private void handleTripleClick() {
        selectAll();
    }

    private int[] findWordBoundaries(String line, int position) {
        position = Math.max(0, Math.min(position, line.length()));

        if (position >= line.length() || Character.isWhitespace(line.charAt(position))) {
            while (position > 0 && Character.isWhitespace(line.charAt(position - 1))) {
                position--;
            }
            if (position == 0) {
                return new int[]{0, 0};
            }
            position--;
        }

        int start = position;
        while (start > 0 && !Character.isWhitespace(line.charAt(start - 1))) {
            start--;
        }

        int end = position;
        while (end < line.length() && !Character.isWhitespace(line.charAt(end))) {
            end++;
        }

        return new int[]{start, end};
    }

    @Override
    public void charTyped(char c) {
        if (!focused || !isCharAllowed(c) || getTotalCharCount() >= GuiPresets.TEXT_AREA_CHAR_LIMIT) return;

        deleteSelectionIfExists();
        insertCharAtCaret(c);
        triggerTextChanged();
    }

    @Override
    public void keyPressed(int keyCode) {
        if (!focused && !hasSelection()) return;

        boolean ctrl = Screen.hasControlDown();
        if (!this.isEnabled()) {
            if (ctrl) {
                switch (keyCode) {
                    case GLFW.GLFW_KEY_A -> selectAll();
                    case GLFW.GLFW_KEY_C -> copy();
                }
            }
            return;
        }

        boolean shift = Screen.hasShiftDown();
        switch (keyCode) {
            case GLFW.GLFW_KEY_BACKSPACE -> backspace();
            case GLFW.GLFW_KEY_DELETE -> delete();
            case GLFW.GLFW_KEY_ENTER -> insertNewLine();
            case GLFW.GLFW_KEY_LEFT -> moveCaret(-1, 0, shift);
            case GLFW.GLFW_KEY_RIGHT -> moveCaret(1, 0, shift);
            case GLFW.GLFW_KEY_UP -> moveCaret(0, -1, shift);
            case GLFW.GLFW_KEY_DOWN -> moveCaret(0, 1, shift);
            case GLFW.GLFW_KEY_HOME -> moveCaretToLineStart(shift);
            case GLFW.GLFW_KEY_END -> moveCaretToLineEnd(shift);
            default -> {
                if (ctrl) {
                    handleControlKeys(keyCode);
                }
            }
        }
    }

    @Override
    public TextFieldStyle getStyle() {
        return this.textAreaStyle;
    }

    private void handleControlKeys(int keyCode) {
        switch (keyCode) {
            case 65 -> selectAll();
            case 67 -> copy();
            case 88 -> cut();
            case 86 -> paste();
        }
    }

    private void moveCaret(int colDelta, int rowDelta, boolean extend) {
        if (extend && !hasSelection()) {
            startSelection();
        } else if (!extend) {
            clearSelection();
        }

        int newRow = Math.max(0, Math.min(caretRow + rowDelta, lines.size() - 1));
        int newCol = caretCol + colDelta;

        if (colDelta != 0) {
            String currentLine = lines.get(caretRow);
            if (newCol < 0 && caretRow > 0) {
                newRow = caretRow - 1;
                newCol = lines.get(newRow).length();
            } else if (newCol > currentLine.length() && caretRow < lines.size() - 1) {
                newRow = caretRow + 1;
                newCol = 0;
            } else {
                newCol = Math.max(0, Math.min(newCol, currentLine.length()));
            }
        } else {
            String newLine = lines.get(newRow);
            newCol = Math.min(caretCol, newLine.length());
        }

        setCaretPosition(newRow, newCol);
    }

    private void moveCaretToLineStart(boolean extend) {
        if (extend && !hasSelection()) startSelection();
        else if (!extend) clearSelection();

        setCaretPosition(caretRow, 0);
    }

    private void moveCaretToLineEnd(boolean extend) {
        if (extend && !hasSelection()) startSelection();
        else if (!extend) clearSelection();

        setCaretPosition(caretRow, lines.get(caretRow).length());
    }

    private void setCaretPosition(int row, int col) {
        caretRow = Math.max(0, Math.min(row, lines.size() - 1));
        String line = lines.get(caretRow);
        caretCol = Math.max(0, Math.min(col, line.length()));

        caretVisible = true;
        caretTickCounter = 0;
    }

    private void insertNewLine() {
        deleteSelectionIfExists();

        String currentLine = lines.get(caretRow);
        String beforeCaret = currentLine.substring(0, caretCol);
        String afterCaret = currentLine.substring(caretCol);

        lines.set(caretRow, beforeCaret);
        lines.add(caretRow + 1, afterCaret);

        setCaretPosition(caretRow + 1, 0);
        triggerTextChanged();
    }

    private void backspace() {
        if (hasSelection()) {
            deleteSelectionIfExists();
            return;
        }

        if (caretCol > 0) {
            String line = lines.get(caretRow);
            String newLine = line.substring(0, caretCol - 1) + line.substring(caretCol);
            lines.set(caretRow, newLine);
            caretCol--;
        } else if (caretRow > 0) {
            String currentLine = lines.get(caretRow);
            String previousLine = lines.get(caretRow - 1);

            lines.set(caretRow - 1, previousLine + currentLine);
            lines.remove(caretRow);

            setCaretPosition(caretRow - 1, previousLine.length());
        }

        triggerTextChanged();
    }

    private void delete() {
        if (hasSelection()) {
            deleteSelectionIfExists();
            return;
        }

        String line = lines.get(caretRow);
        if (caretCol < line.length()) {
            String newLine = line.substring(0, caretCol) + line.substring(caretCol + 1);
            lines.set(caretRow, newLine);
        } else if (caretRow < lines.size() - 1) {
            String nextLine = lines.get(caretRow + 1);
            lines.set(caretRow, line + nextLine);
            lines.remove(caretRow + 1);
        }

        triggerTextChanged();
    }

    private void startSelection() {
        selectionStartRow = caretRow;
        selectionStartCol = caretCol;
    }

    private void clearSelection() {
        selectionStartRow = -1;
        selectionStartCol = -1;
    }

    private boolean hasSelection() {
        return selectionStartRow != -1 &&
                (selectionStartRow != caretRow || selectionStartCol != caretCol);
    }

    private void selectAll() {
        if (lines.isEmpty()) return;

        selectionStartRow = 0;
        selectionStartCol = 0;
        setCaretPosition(lines.size() - 1, lines.get(lines.size() - 1).length());
    }

    private void deleteSelectionIfExists() {
        if (!hasSelection()) return;

        int startRow = Math.min(caretRow, selectionStartRow);
        int endRow = Math.max(caretRow, selectionStartRow);
        int startCol = (caretRow < selectionStartRow || (caretRow == selectionStartRow && caretCol < selectionStartCol))
                ? caretCol : selectionStartCol;
        int endCol = (caretRow > selectionStartRow || (caretRow == selectionStartRow && caretCol > selectionStartCol))
                ? caretCol : selectionStartCol;

        if (startRow == endRow) {
            String line = lines.get(startRow);
            String newLine = line.substring(0, startCol) + line.substring(endCol);
            lines.set(startRow, newLine);
        } else {
            String firstLine = lines.get(startRow).substring(0, startCol);
            String lastLine = lines.get(endRow).substring(endCol);

            if (endRow >= startRow + 1) {
                lines.subList(startRow + 1, endRow + 1).clear();
            }

            lines.set(startRow, firstLine + lastLine);
        }

        setCaretPosition(startRow, startCol);
        clearSelection();
    }

    private void copy() {
        if (!hasSelection()) return;

        String selectedText = getSelectedText();
        if (!selectedText.isEmpty()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(selectedText);
        }
    }

    private void cut() {
        copy();
        deleteSelectionIfExists();
        triggerTextChanged();
    }

    private void paste() {
        String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
        if (clipboard.isEmpty()) return;

        deleteSelectionIfExists();

        String[] pasteLines = clipboard.split(GuiPresets.TEXT_AREA_DELIMITER);
        if (pasteLines.length == 1) {
            for (char c : pasteLines[0].toCharArray()) {
                if (isCharAllowed(c) && getTotalCharCount() < GuiPresets.TEXT_AREA_CHAR_LIMIT) {
                    insertCharAtCaret(c);
                }
            }
        } else {
            String currentLine = lines.get(caretRow);
            String beforeCaret = currentLine.substring(0, caretCol);
            String afterCaret = currentLine.substring(caretCol);

            lines.set(caretRow, beforeCaret + pasteLines[0]);

            for (int i = 1; i < pasteLines.length - 1; i++) {
                lines.add(caretRow + i, pasteLines[i]);
            }

            lines.add(caretRow + pasteLines.length - 1, pasteLines[pasteLines.length - 1] + afterCaret);
            setCaretPosition(caretRow + pasteLines.length - 1, pasteLines[pasteLines.length - 1].length());
        }

        triggerTextChanged();
    }

    private String getSelectedText() {
        if (!hasSelection()) return "";

        int startRow = Math.min(caretRow, selectionStartRow);
        int endRow = Math.max(caretRow, selectionStartRow);
        int startCol = (caretRow < selectionStartRow || (caretRow == selectionStartRow && caretCol < selectionStartCol))
                ? caretCol : selectionStartCol;
        int endCol = (caretRow > selectionStartRow || (caretRow == selectionStartRow && caretCol > selectionStartCol))
                ? caretCol : selectionStartCol;

        StringBuilder result = new StringBuilder();

        for (int row = startRow; row <= endRow; row++) {
            if (row >= lines.size()) break;

            String line = lines.get(row);
            int lineStartCol = (row == startRow) ? startCol : 0;
            int lineEndCol = (row == endRow) ? endCol : line.length();

            if (lineStartCol < lineEndCol) {
                result.append(line, lineStartCol, lineEndCol);
            }

            if (row < endRow) {
                result.append(GuiPresets.TEXT_AREA_CHAR_LIMIT);
            }
        }

        return result.toString();
    }

    private void ensureCaretVisible() {
        if (caretRow < scrollOffset) {
            scrollOffset = caretRow;
            scrollbar.updateScrollIndex(scrollOffset);
        } else if (caretRow >= scrollOffset + maxVisibleLines) {
            scrollOffset = caretRow - maxVisibleLines + 1;
            scrollbar.updateScrollIndex(scrollOffset);
        }
    }

    private boolean needsScrolling() {
        return lines.size() > maxVisibleLines;
    }

    private int getCharIndexFromPixel(String line, int pixelX) {
        // Scale pixel position for accurate character detection
        float fontScale = GuiUtils.getFontScale(this.textAreaStyle.getFontSize());
        float scaledPixelX = pixelX / fontScale;

        Text tempText = new Text(0, 0, line, this.textAreaStyle.getFontSize(), this.textAreaStyle.getTextColor(),
                this.textAreaStyle.getTextHoverColor(), this.textAreaStyle.getTextDisabledColor());
        return tempText.getCharIndexFromPixel((int)scaledPixelX);
    }

    private boolean isCharAllowed(char c) {
        return !forbiddenCharacters.contains(c) && c != '\r';
    }

    private int getTotalCharCount() {
        return lines.stream().mapToInt(String::length).sum() + lines.size() - 1;
    }

    private void updateScrollbarData() {
        scrollbar.updateContentSize(lines.size());
        scrollbar.updateScrollIndex(scrollOffset);
    }

    private void triggerTextChanged() {
        isManuallyScrolling = false;
        updateScrollbarData();
        if (onTextChangeAction != null) {
            onTextChangeAction.execute(getText());
        }
    }

    private boolean needsAutoWrap(String text) {
        if (!wordWrap) return false;

        int textWidth = getTextWidth(text);
        int availableWidth = getContentWidth();

        return textWidth > availableWidth;
    }

    private int getContentWidth() {
        int padding = this.textAreaStyle.getPadding();
        int scrollbarWidth = this.scrollbar.getStyle().getScrollbarTrackWidth();

        return getWidth() - (2 * padding) - scrollbarWidth;
    }

    private int getTextWidth(String text) {
        return GuiUtils.getTextWidth(text, GuiUtils.getFontScale(this.getStyle().getFontSize()));
    }

    private void insertCharAtCaret(char c) {
        String line = lines.get(caretRow);
        String newLine = line.substring(0, caretCol) + c + line.substring(caretCol);

        if (wordWrap && getTextWidth(newLine) > getContentWidth()) {

            String beforeCaret = line.substring(0, caretCol);
            String afterCaret = line.substring(caretCol);

            if (Character.isWhitespace(c)) {
                lines.set(caretRow, beforeCaret);
                lines.add(caretRow + 1, afterCaret);
                setCaretPosition(caretRow + 1, 0);
            } else {
                lines.set(caretRow, beforeCaret);
                lines.add(caretRow + 1, c + afterCaret);
                setCaretPosition(caretRow + 1, 1);
            }
            return;
        }

        lines.set(caretRow, newLine);
        caretCol++;
    }

    private void addTextWithWordWrap(String text) {
        String[] inputLines = text.split("\n", -1);

        for (String inputLine : inputLines) {
            if (inputLine.isEmpty()) {
                lines.add("");
                continue;
            }

            String[] words = inputLine.split("\\s+");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                String testLine = !currentLine.isEmpty() ? currentLine + " " + word : word;

                if (getTextWidth(testLine) <= getContentWidth()) {
                    if (!currentLine.isEmpty()) {
                        currentLine.append(" ");
                    }
                    currentLine.append(word);
                } else {
                    if (!currentLine.isEmpty()) {
                        lines.add(currentLine.toString());
                    }

                    if (getTextWidth(word) > getContentWidth()) {
                        StringBuilder wordPart = new StringBuilder();
                        for (char c : word.toCharArray()) {
                            String testChar = wordPart + String.valueOf(c);
                            if (getTextWidth(testChar) <= getContentWidth()) {
                                wordPart.append(c);
                            } else {
                                if (!wordPart.isEmpty()) {
                                    lines.add(wordPart.toString());
                                    wordPart = new StringBuilder(String.valueOf(c));
                                } else {
                                    lines.add(String.valueOf(c));
                                }
                            }
                        }
                        currentLine = wordPart;
                    } else {
                        currentLine = new StringBuilder(word);
                    }
                }
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }
        }
    }

    public String getText() {
        return String.join(GuiPresets.TEXT_AREA_DELIMITER, lines);
    }

    public void clear() {
        setText("");
    }

    public boolean isEmpty() {
        return lines.size() == 1 && lines.get(0).isEmpty();
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        if (focused) {
            caretVisible = true;
            caretTickCounter = 0;
        }
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        placeholderRenderer.setText(placeholder);
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public void setForbiddenCharacters(Set<Character> characters) {
        this.forbiddenCharacters.clear();
        this.forbiddenCharacters.addAll(characters);
    }

    public int getLineCount() {
        return lines.size();
    }

    public int getCurrentLine() {
        return caretRow;
    }

    public int getCurrentColumn() {
        return caretCol;
    }

    public Text getPlaceholderRenderer() {
        return placeholderRenderer;
    }

    public VerticalScrollbar getScrollbar() {
        return scrollbar;
    }

    public int getWordCount() {
        if (isEmpty()) {
            return 0;
        }

        String text = getText();
        if (text.trim().isEmpty()) {
            return 0;
        }

        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    public int getCharacterCount() {
        if (isEmpty()) {
            return 0;
        }

        return getText().length();
    }

    public void scrollToTop() {
        scrollOffset = 0;
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = false;
    }

    public void scrollToBottom() {
        if (lines.isEmpty()) return;

        scrollOffset = Math.max(0, lines.size() - maxVisibleLines);
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = true;
    }

    public void scrollToLine(int lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) return;

        int targetScrollOffset = Math.max(0, lineNumber - maxVisibleLines / 2);
        int maxScrollOffset = Math.max(0, lines.size() - maxVisibleLines);

        scrollOffset = Math.min(targetScrollOffset, maxScrollOffset);
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = true;
    }

    public void scrollToCaret() {
        ensureCaretVisible();
        isManuallyScrolling = false;
    }

    public void scrollUp(int lines) {
        if (lines <= 0) return;

        scrollOffset = Math.max(0, scrollOffset - lines);
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = true;
    }

    public void scrollDown(int lines) {
        if (lines <= 0 || this.lines.isEmpty()) return;

        int maxScrollOffset = Math.max(0, this.lines.size() - maxVisibleLines);
        scrollOffset = Math.min(maxScrollOffset, scrollOffset + lines);
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = true;
    }

    public void scrollLineUp() {
        scrollUp(1);
    }

    public void scrollLineDown() {
        scrollDown(1);
    }

    public boolean isScrolledToTop() {
        return scrollOffset == 0;
    }

    public boolean isScrolledToBottom() {
        if (lines.isEmpty() || !needsScrolling()) return true;

        int maxScrollOffset = Math.max(0, lines.size() - maxVisibleLines);
        return scrollOffset >= maxScrollOffset;
    }

    public double getScrollPercentage() {
        if (lines.isEmpty() || !needsScrolling()) return 0.0;

        int maxScrollOffset = Math.max(0, lines.size() - maxVisibleLines);
        return maxScrollOffset > 0 ? (double) scrollOffset / maxScrollOffset : 0.0;
    }

    public void setScrollPercentage(double percentage) {
        if (lines.isEmpty() || !needsScrolling()) return;

        percentage = Math.max(0.0, Math.min(1.0, percentage));
        int maxScrollOffset = Math.max(0, lines.size() - maxVisibleLines);
        scrollOffset = (int) (maxScrollOffset * percentage);
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = true;
    }

    public int getFirstVisibleLine() {
        return scrollOffset;
    }

    public int getLastVisibleLine() {
        return Math.min(lines.size() - 1, scrollOffset + maxVisibleLines - 1);
    }

    public boolean isLineVisible(int lineNumber) {
        return lineNumber >= scrollOffset && lineNumber < scrollOffset + maxVisibleLines;
    }

    public boolean isCaretVisible() {
        return isLineVisible(caretRow);
    }

    public boolean scrollToText(String searchText, boolean caseSensitive) {
        if (searchText == null || searchText.isEmpty()) return false;

        String search = caseSensitive ? searchText : searchText.toLowerCase();

        for (int i = 0; i < lines.size(); i++) {
            String line = caseSensitive ? lines.get(i) : lines.get(i).toLowerCase();
            if (line.contains(search)) {
                scrollToLine(i);
                return true;
            }
        }

        return false;
    }

    public void setAutoScrollToCaret(boolean enabled) {
        if (enabled) {
            isManuallyScrolling = false;
            ensureCaretVisible();
        } else {
            isManuallyScrolling = true;
        }
    }

    public boolean isAutoScrollToCaretEnabled() {
        return !isManuallyScrolling;
    }

    public int getVisibleLineCount() {
        return maxVisibleLines;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int offset) {
        if (lines.isEmpty()) return;

        int maxScrollOffset = Math.max(0, lines.size() - maxVisibleLines);
        scrollOffset = Math.max(0, Math.min(offset, maxScrollOffset));
        scrollbar.updateScrollIndex(scrollOffset);
        isManuallyScrolling = true;
    }

    public void setText(String text) {
        lines.clear();

        if (text.isEmpty()) {
            lines.add("");
        } else {
            addTextWithWordWrap(text);
        }

        if (lines.isEmpty()) {
            lines.add("");
        }

        setCaretPosition(0, 0);
        clearSelection();
        scrollOffset = 0;
        updateScrollbarData();
        triggerTextChanged();
    }
}