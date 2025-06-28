package de.j3ramy.edomui.view;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.input.Dropdown;
import de.j3ramy.edomui.component.popup.PopUp;
import de.j3ramy.edomui.interfaces.IWidget;
import de.j3ramy.edomui.component.*;
import de.j3ramy.edomui.component.text.Tooltip;
import de.j3ramy.edomui.component.presentation.ScrollableList;

import java.util.concurrent.CopyOnWriteArrayList;

public class View implements IWidget {
    private final CopyOnWriteArrayList<Widget> widgets = new CopyOnWriteArrayList<>();

    private boolean isHidden, isUpdating = true;
    private String playerId;

    @Override
    public void update(int mouseX, int mouseY) {
        if (isHidden || !isUpdating) return;

        for (Widget widget : widgets) {
            if (shouldUpdateWidget(widget)) {
                widget.update(mouseX, mouseY);
            }
        }
    }

    @Override
    public void tick() {
        if (isHidden) return;

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                widget.tick();
            }
        }
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden) return;

        renderNormalWidgets(poseStack);
        renderDropdowns(poseStack);
        renderPopUps(poseStack);
        renderTooltips(poseStack);
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden) return;

        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isHidden()) {
                widget.onClick(mouseButton);

                if (widget.isMouseOver() && shouldMoveToForeground(widget)) {
                    moveWidgetToForeground(widget);
                    break;
                }
            }
        }
    }

    @Override
    public void onScroll(double delta) {
        if (isHidden) return;

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                widget.onScroll(delta);
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if (isHidden) return;

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                widget.keyPressed(keyCode);
            }
        }
    }

    @Override
    public void charTyped(char codePoint) {
        if (isHidden) return;

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                widget.charTyped(codePoint);
            }
        }
    }

    @Override
    public void onMouseDrag(int mouseButton, double newX, double newY, int mouseX, int mouseY) {
        if (isHidden) return;

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                widget.onMouseDrag(mouseButton, newX, newY, mouseX, mouseY);
            }
        }
    }

    private void renderNormalWidgets(PoseStack poseStack) {
        for (Widget widget : widgets) {
            if (!widget.isHidden() &&
                    !(widget instanceof PopUp) &&
                    !(widget instanceof Dropdown) &&
                    !(widget instanceof Tooltip)) {
                widget.render(poseStack);
            }
        }
    }

    private void renderDropdowns(PoseStack poseStack) {
        for (Widget widget : widgets) {
            if (!widget.isHidden() && widget instanceof Dropdown) {
                widget.render(poseStack);
            }
        }
    }

    private void renderPopUps(PoseStack poseStack) {
        for (Widget widget : widgets) {
            if (!widget.isHidden() && widget instanceof PopUp) {
                widget.render(poseStack);
            }
        }
    }

    private void renderTooltips(PoseStack poseStack) {
        for (Widget widget : widgets) {
            if (!widget.isHidden() && widget instanceof Tooltip) {
                widget.render(poseStack);
            }
        }

        renderNestedTooltips(poseStack);
    }
    private void renderNestedTooltips(PoseStack poseStack) {
        for (Widget widget : this.widgets) {
            if (widget.isHidden()) continue;

            if (widget instanceof Button button) {
                if (button.isTooltipEnabled() && button.getTooltip() != null && !button.getTooltip().isHidden()) {
                    button.getTooltip().render(poseStack);
                }
            }

            if (widget instanceof ScrollableList list) {
                for (Button button : list.getContent()) {
                    if (button.isTooltipEnabled() && button.getTooltip() != null && !button.getTooltip().isHidden()) {
                        button.getTooltip().render(poseStack);
                    }
                }
            }
        }
    }

    private boolean shouldMoveToForeground(Widget widget) {
        return widget instanceof ScrollableList;
    }

    private void moveWidgetToForeground(Widget widget) {
        if (widgets.remove(widget)) {
            widgets.add(widget);
        }
    }

    protected boolean shouldUpdateWidget(Widget widget) {
        return (widget instanceof Tooltip)
                || (!widget.isHidden() && widget.isEnabled());
    }

    public void addWidget(Widget widget) {
        widgets.add(widget);
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
    }

    public void clear() {
        widgets.clear();
    }

    public void removeLastWidget() {
        if (!widgets.isEmpty()) {
            widgets.remove(widgets.size() - 1);
        }
    }

    public CopyOnWriteArrayList<Widget> getWidgets() {
        return widgets;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public void shouldUpdate(boolean updating) {
        isUpdating = updating;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}