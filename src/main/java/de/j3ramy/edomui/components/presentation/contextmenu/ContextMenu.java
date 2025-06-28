package de.j3ramy.edomui.components.presentation.contextmenu;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.presentation.ScrollableList;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.presentation.ContextMenuStyle;
import de.j3ramy.edomui.theme.ThemeManager;

import java.awt.*;

public class ContextMenu extends ScrollableList {
    private final ContextMenuStyle contextMenuStyle;

    private int targetX, targetY;

    @Override
    public ContextMenuStyle getStyle() {
        return this.contextMenuStyle;
    }

    public ContextMenu(Color selectedColor) {
        super(0, 0, 0, 0, selectedColor);

        this.contextMenuStyle = new ContextMenuStyle(ThemeManager.getDefaultContextMenuStyle());
        this.setStyle(this.contextMenuStyle);

        this.setHidden(true);
    }

    @Override
    public void render(PoseStack stack) {
        if (isHidden()) return;
        renderBackground(stack);
        renderBorder(stack);

        for (Button button : content) {
            button.render(stack);
        }
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);

        for (Button button : content) {
            button.update(x, y);
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (this.isHidden()) return;

        for (Button button : content) {
            if (button.isMouseOver()) {
                button.onClick(mouseButton);
                hide();
                return;
            }
        }

        hide();
    }

    public void show(int x, int y) {
        this.targetX = x;
        this.targetY = y;
        this.setHidden(false);
        updatePosition();
        layoutButtons();
    }

    public void hide() {
        this.setHidden(true);
        unselect();
    }

    public void addMenuItem(String title, IAction action, boolean enabled) {
        addElement(title, enabled ? action : null);

        if (!content.isEmpty()) {
            Button lastButton = content.get(content.size() - 1);
            lastButton.setEnabled(enabled);

            if (!enabled) {
                lastButton.getTitle().getStyle().setTextColor(this.getStyle().getTextDisabledColor());
                lastButton.getStyle().setBackgroundColor(this.getStyle().getDisabledBackgroundColor());
            }
        }
    }

    public void addMenuItem(String title, IAction action) {
        addMenuItem(title, action, true);
    }

    private void updatePosition() {
        setLeftPos(targetX);
        setTopPos(targetY);
        updateSize();
        layoutButtons();
    }

    private void updateSize() {
        int menuHeight = content.size() * this.listStyle.getElementHeight();
        setHeight(menuHeight);

        int maxWidth = content.stream()
                .mapToInt(b -> b.getTitle().getWidth())
                .max()
                .orElse(this.contextMenuStyle.getMinWidth());
        setWidth(Math.max(this.contextMenuStyle.getMinWidth(), maxWidth + 40));
    }

    @Override
    public void clear() {
        super.clear();
        updateSize();
    }
}
