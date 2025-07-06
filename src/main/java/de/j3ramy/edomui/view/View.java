package de.j3ramy.edomui.view;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.chart.BarChart;
import de.j3ramy.edomui.component.chart.LineChart;
import de.j3ramy.edomui.component.input.Dropdown;
import de.j3ramy.edomui.component.input.TextArea;
import de.j3ramy.edomui.component.input.TextField;
import de.j3ramy.edomui.component.popup.PopUp;
import de.j3ramy.edomui.component.presentation.Grid;
import de.j3ramy.edomui.component.presentation.ScrollableTable;
import de.j3ramy.edomui.component.text.CenteredText;
import de.j3ramy.edomui.interfaces.IWidget;
import de.j3ramy.edomui.component.*;
import de.j3ramy.edomui.component.text.Tooltip;
import de.j3ramy.edomui.component.presentation.ScrollableList;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class View implements IWidget {
    private final CopyOnWriteArrayList<Widget> widgets = new CopyOnWriteArrayList<>();

    private boolean isHidden, isUpdating = true;
    private String playerId;
    private int currentMouseX = 0;
    private int currentMouseY = 0;

    @Override
    public void update(int mouseX, int mouseY) {
        this.currentMouseX = mouseX;
        this.currentMouseY = mouseY;

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

        boolean hasActivePopUp = hasActivePopUp();
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isHidden() && widget instanceof PopUp) {
                if (widget.isMouseOver()) {
                    widget.onClick(mouseButton);
                    return;
                }
            }
        }

        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isHidden() && widget instanceof Dropdown dropdown) {
                if (dropdown.isUnfolded()) {
                    dropdown.onClick(mouseButton);
                    return;
                }
            }
        }

        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isHidden() && !(widget instanceof PopUp)) {

                if (!shouldAllowInteraction(widget, hasActivePopUp)) {
                    continue;
                }

                if ((widget instanceof TextArea || widget instanceof TextField) && !widget.isEnabled()) {
                    widget.update(currentMouseX, currentMouseY);
                    widget.onClick(mouseButton);
                    if (widget.isMouseOver()) break;
                } else {
                    widget.onClick(mouseButton);
                    if (widget.isMouseOver() && shouldMoveToForeground(widget)) {
                        moveWidgetToForeground(widget);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(double delta) {
        if (isHidden) return;

        boolean hasActivePopUp = hasActivePopUp();
        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                if (!shouldAllowInteraction(widget, hasActivePopUp) && !(widget instanceof PopUp)) {
                    continue;
                }

                if (!widget.isEnabled()) {
                    widget.update(currentMouseX, currentMouseY);
                }
                widget.onScroll(delta);
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if (isHidden) return;

        boolean hasActivePopUp = hasActivePopUp();

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                if (!shouldAllowInteraction(widget, hasActivePopUp) && !(widget instanceof PopUp)) {
                    continue;
                }

                if ((widget instanceof TextArea || widget instanceof TextField) && !widget.isEnabled()) {
                    boolean isCtrlC = keyCode == 67 && Screen.hasControlDown();
                    boolean isCtrlA = keyCode == 65 && Screen.hasControlDown();

                    if (isCtrlC || isCtrlA) {
                        widget.keyPressed(keyCode);
                    }
                }
                else if (widget.isEnabled()) {
                    widget.keyPressed(keyCode);
                }
            }
        }
    }

    @Override
    public void charTyped(char codePoint) {
        if (isHidden) return;

        boolean hasActivePopUp = hasActivePopUp();

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                if (!shouldAllowInteraction(widget, hasActivePopUp) && !(widget instanceof PopUp)) {
                    continue;
                }

                widget.charTyped(codePoint);
            }
        }
    }

    @Override
    public void onMouseDrag(int mouseButton, double newX, double newY, int mouseX, int mouseY) {
        if (isHidden) return;

        boolean hasActivePopUp = hasActivePopUp();

        for (Widget widget : widgets) {
            if (!widget.isHidden()) {
                if (!shouldAllowInteraction(widget, hasActivePopUp) && !(widget instanceof PopUp)) {
                    continue;
                }

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

    public void renderTooltips(PoseStack poseStack) {
        if (hasActivePopUp()) {
            return;
        }

        List<Tooltip> allTooltips = new ArrayList<>();
        collectTooltipsFromWidgets(widgets, allTooltips);

        for (Tooltip tooltip : allTooltips) {
            if (!tooltip.isHidden()) {
                tooltip.render(poseStack);
            }
        }
    }

    private void collectTooltipsFromWidgets(List<Widget> widgetList, List<Tooltip> tooltipList) {
        for (Widget widget : widgetList) {
            if (widget.isHidden()) continue;

            if (widget instanceof Tooltip tooltip) {
                tooltipList.add(tooltip);
            }
            else if (widget instanceof Button button) {
                if (button.isTooltipEnabled() && button.getTooltip() != null &&
                        button.isMouseOver()) {
                    tooltipList.add(button.getTooltip());
                }
            }
            else if (widget instanceof ScrollableTable scrollableTable) {
                for (Button button : scrollableTable.getVisibleButtons()) {
                    if (button instanceof ScrollableTable.TableRow tableRow) {
                        List<CenteredText> columnTexts = tableRow.getColumnTexts();
                        List<Tooltip> columnTooltips = tableRow.getColumnTooltips();

                        for (int i = 0; i < columnTexts.size() && i < columnTooltips.size(); i++) {
                            Tooltip tooltip = columnTooltips.get(i);
                            if (tooltip != null) {
                                if (columnTexts.get(i).isMouseOver()) {
                                    tooltip.setHidden(false);
                                    tooltipList.add(tooltip);
                                    break;
                                } else {
                                    tooltip.setHidden(true);
                                }
                            }
                        }
                    }
                }
            }
            else if (widget instanceof ScrollableList scrollableList) {
                for (Button button : scrollableList.getContent()) {
                    if (button.isTooltipEnabled() && button.getTooltip() != null &&
                            button.isMouseOver() && !button.isHidden()) {
                        tooltipList.add(button.getTooltip());
                    }
                }
            }
            else if (widget instanceof Grid grid) {
                for (List<Button> row : grid.getGrid()) {
                    for (Button button : row) {
                        if (button != null && button.isTooltipEnabled() &&
                                button.getTooltip() != null && button.isMouseOver() &&
                                !button.isHidden()) {
                            tooltipList.add(button.getTooltip());
                        }
                    }
                }
            }
            else if (widget instanceof LineChart lineChart) {
                    collectTooltipsFromWidgets(lineChart.getView().getWidgets(), tooltipList);
            }
            else if (widget instanceof BarChart barChart) {
                    collectTooltipsFromWidgets(barChart.getView().getWidgets(), tooltipList);
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

    private boolean hasActivePopUp() {
        for (Widget widget : widgets) {
            if (widget instanceof PopUp && !widget.isHidden()) {
                return true;
            }
        }
        return false;
    }

    protected boolean shouldUpdateWidget(Widget widget) {
        return !widget.isHidden() && widget.isEnabled();
    }

    protected boolean shouldAllowInteraction(Widget widget, boolean hasActivePopUp) {
        if (hasActivePopUp) {
            return widget instanceof PopUp;
        }
        return true;
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