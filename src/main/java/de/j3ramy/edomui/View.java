package de.j3ramy.edomui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.interfaces.IWidget;
import de.j3ramy.edomui.widgets.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class View implements IWidget {
    private final CopyOnWriteArrayList<Widget> widgets = new CopyOnWriteArrayList<>();
    private boolean isHidden, isUpdating = true;
    private String playerId;

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

    @Override
    public void update(int mouseX, int mouseY) {
        if (!isHidden && isUpdating) {
            PopUp activePopUp = getActivePopUp();
            if (activePopUp != null) {
                activePopUp.update(mouseX, mouseY);
                return;
            }

            for (Widget widget : widgets) {
                if ((!widget.isHidden() && (widget.isEnabled() || widget instanceof TextArea)) || widget instanceof Tooltip) {
                    widget.update(mouseX, mouseY);

                    if(!widget.isHidden() && widget instanceof Tooltip) {
                        this.moveWidgetToForeground(widget);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        if (!isHidden) {
            for (Widget widget : widgets) {
                if (!widget.isHidden()) widget.tick();
            }
        }
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!isHidden) {
            for (Widget widget : widgets) {
                if (!widget.isHidden()) widget.render(poseStack);
            }
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if (!isHidden) {
            PopUp activePopUp = getActivePopUp();
            if (activePopUp != null) {
                activePopUp.onClick(mouseButton);
                return;
            }

            Dropdown activeDropdown = getActiveDropdown();
            if (activeDropdown != null && !activeDropdown.isHidden()) {
                activeDropdown.onClick(mouseButton);
                moveWidgetToForeground(activeDropdown);
                return;
            }

            for (Widget widget : widgets) {
                if (!widget.isHidden()) {
                    widget.onClick(mouseButton);
                    if (widget.isMouseOver() && (widget instanceof Tooltip || widget instanceof Dropdown || widget instanceof List || widget instanceof PopUp)) {
                        moveWidgetToForeground(widget);
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(double delta) {
        if (!isHidden) {
            if (isPopUpVisible()) {
                getActivePopUp().onScroll(delta);
                return;
            }

            for (Widget widget : widgets) {
                if (!widget.isHidden()) {
                    widget.onScroll(delta);
                }
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if (!isHidden) {
            if (isPopUpVisible()) {
                getActivePopUp().keyPressed(keyCode);
                return;
            }

            for (Widget widget : widgets) {
                if (!widget.isHidden()) widget.keyPressed(keyCode);
            }
        }
    }

    @Override
    public void charTyped(char codePoint) {
        if (!isHidden) {
            if (isPopUpVisible()) {
                getActivePopUp().charTyped(codePoint);
                return;
            }

            for (Widget widget : widgets) {
                if (!widget.isHidden()) widget.charTyped(codePoint);
            }
        }
    }

    @Override
    public void onMouseDrag(int mouseButton, double newX, double newY, int mouseX, int mouseY) {
        if (!isHidden) {
            if (isPopUpVisible()) {
                getActivePopUp().onMouseDrag(mouseButton, newX, newY, mouseX, mouseY);
                return;
            }

            for (Widget widget : widgets) {
                if (!widget.isHidden()) widget.onMouseDrag(mouseButton, newX, newY, mouseX, mouseY);
            }
        }
    }

    public void addWidget(Widget widget) {
        if (widget instanceof Dropdown) {
            if (!isPopupActive()) {
                moveWidgetToForeground(widget);
            } else {
                widgets.add(widget);
            }
        } else if (widget instanceof PopUp) {
            moveWidgetToForeground(widget);
        } else {
            widgets.add(widget);
        }
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
    }

    private void moveWidgetToForeground(Widget widget) {
        widgets.remove(widget);
        if (widget instanceof PopUp || widget instanceof Tooltip) {
            widgets.add(widget);
        } else if(!widgets.isEmpty()) {
            widgets.add(widgets.size() - 1, widget);
        }
    }

    public void clear() {
        widgets.clear();
    }

    public void renderCurrentTooltip(PoseStack poseStack){
        if(this.getWidgets().size() > 1 &&
                this.getWidgets().get(this.getWidgets().size() - 1) instanceof Tooltip){
            this.render(poseStack);
        }
    }

    public boolean isPopupActive(){
        return this.getActivePopUp() != null;
    }

    public void removeLastWidget() {
        if(this.widgets.size() > 1){
            this.widgets.remove(this.widgets.size() - 1);
        }
    }

    private Dropdown getActiveDropdown() {
        return widgets.stream()
                .filter(widget -> widget instanceof Dropdown && !widget.isHidden() && ((Dropdown) widget).isUnfolded())
                .map(widget -> (Dropdown) widget)
                .findFirst()
                .orElse(null);
    }

    private PopUp getActivePopUp() {
        return widgets.stream()
                .filter(widget -> widget instanceof PopUp && !widget.isHidden())
                .map(widget -> (PopUp) widget)
                .findFirst()
                .orElse(null);
    }

    private boolean isPopUpVisible() {
        return getActivePopUp() != null;
    }
}
