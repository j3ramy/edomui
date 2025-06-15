package de.j3ramy.edomui.components.presentation.context;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.presentation.ScrollableList;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.util.style.Color;

public class ContextMenu extends ScrollableList {
    private boolean isVisible = false;
    private int targetX, targetY;

    public ContextMenu(int elementHeight, int selectedColor) {
        super(0, 0, 0, 0, elementHeight, selectedColor);
    }

    @Override
    public void render(PoseStack stack) {
        if (!isVisible) return;
        renderBackground(stack);
        renderBorder(stack);

        for (Button button : content) {
            button.render(stack);
        }
    }

    @Override
    public void update(int x, int y) {
        if (!isVisible) return;

        super.update(x, y);

        for (Button button : content) {
            button.update(x, y);
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (!isVisible) return;

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
        this.isVisible = true;
        updatePosition();
        layoutButtons();
    }

    public void hide() {
        this.isVisible = false;
        unselect();
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void addMenuItem(String title, IAction action, boolean enabled) {
        addElement(title, enabled ? action : null);

        if (!content.isEmpty()) {
            Button lastButton = content.get(content.size() - 1);
            lastButton.setEnabled(enabled);

            if (!enabled) {
                lastButton.getTitle().setTextColor(Color.GRAY);
                lastButton.getStyle().setBackgroundColor(Color.DARK_GRAY);
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
        int menuHeight = content.size() * elementHeight;
        setHeight(menuHeight);

        int maxWidth = content.stream()
                .mapToInt(b -> b.getTitle().getWidth())
                .max()
                .orElse(100);
        setWidth(Math.max(100, maxWidth + 40));
    }

    @Override
    public void clear() {
        super.clear();
        updateSize();
    }
}
