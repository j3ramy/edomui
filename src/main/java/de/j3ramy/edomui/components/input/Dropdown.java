package de.j3ramy.edomui.components.input;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.presentation.ScrollableList;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.theme.input.DropdownStyle;
import de.j3ramy.edomui.util.style.GuiUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class Dropdown extends Button {
    private final String placeholder;
    private final IValueAction onChangeAction;
    private final DropdownStyle dropdownStyle;

    private boolean isUnfolded;
    private ScrollableList menu;
    private String lastSelectedElement = "";

    public ScrollableList getMenu() {
        return menu;
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, String placeholder, Color selectedColor, IValueAction onChangeAction) {
        super(x, y, width, height, placeholder, null, ButtonType.DROPDOWN);

        this.dropdownStyle = new DropdownStyle(ThemeManager.getDefaultDropdownStyle());
        this.setStyle(this.dropdownStyle);

        this.onChangeAction = onChangeAction;
        if (options != null) {
            setOptions(options, selectedColor);
        }

        this.title = this.createTitle(ButtonType.DROPDOWN, placeholder, this.dropdownStyle.getPadding());

        this.placeholder = placeholder;
        setTitle(placeholder);
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, String placeholder, Color selectedColor) {
        this(options, x, y, width, height, placeholder, selectedColor, null);
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, Color selectedColor) {
        this(options, x, y, width, height, "", selectedColor, null);
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, String placeholder, IValueAction onChangeAction) {
        this(options, x, y, width, height, placeholder, Color.GRAY, onChangeAction);
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, String placeholder) {
        this(options, x, y, width, height, placeholder, Color.GRAY, null);
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, IValueAction onChangeAction) {
        this(options, x, y, width, height, "", Color.GRAY, onChangeAction);
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height) {
        this(options, x, y, width, height, "", Color.GRAY, null);
    }

    public Dropdown(int x, int y, int width, int height, String placeholder) {
        this(new ArrayList<>(), x, y, width, height, placeholder, Color.GRAY, null);
    }

    public Dropdown(int x, int y, int width, int height, String placeholder, IValueAction onChangeAction) {
        this(new ArrayList<>(), x, y, width, height, placeholder, Color.GRAY, onChangeAction);
    }

    public Dropdown(int x, int y, int width, int height, String placeholder, Color selectedColor) {
        this(new ArrayList<>(), x, y, width, height, placeholder, selectedColor, null);
    }

    public Dropdown(int x, int y, int width, int height) {
        this(new ArrayList<>(), x, y, width, height, "", Color.GRAY, null);
    }

    public Dropdown(int x, int y, int width, int height, IValueAction onChangeAction) {
        this(new ArrayList<>(), x, y, width, height, "", Color.GRAY, onChangeAction);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);
        renderArrow(poseStack, getLeftPos() + getWidth() - 15, getTopPos() + (getHeight() / 2) - 2, isUnfolded);

        if (isUnfolded && menu != null) menu.render(poseStack);
    }

    @Override
    public void update(int mouseX, int mouseY) {
        if (isHidden()) return;

        super.update(mouseX, mouseY);

        getTitle().getStyle().setTextColor(hasSelection() ? this.dropdownStyle.getTextColor() : this.dropdownStyle.getPlaceholderColor());
        if (isUnfolded && menu != null) menu.update(mouseX, mouseY);
    }

    @Override
    public void onScroll(double delta) {
        if (isUnfolded && menu != null) menu.onScroll(delta);
    }

    @Override
    public void onClick(int mouseButton) {
        if (!isEnabled()) return;

        if (mouseButton == 0) {
            if (isUnfolded && menu != null) {
                menu.onClick(mouseButton);

                String currentSelection = this.menu.getSelectedTitle();
                if (this.onChangeAction != null && (!this.lastSelectedElement.equals(currentSelection) || this.lastSelectedElement.isEmpty())) {
                    this.lastSelectedElement = currentSelection;
                    this.onChangeAction.execute(currentSelection);
                }
            }

           if (isMouseOver()) {
                isUnfolded = !isUnfolded;
            } else {
                isUnfolded = false;
            }
        }
    }

    @Override
    public void setTopPos(int topPos) {
        int delta = topPos - getTopPos();
        if (menu != null) menu.setTopPos(menu.getTopPos() + delta);
        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int delta = leftPos - getLeftPos();
        if (menu != null) menu.setLeftPos(menu.getLeftPos() + delta);
        super.setLeftPos(leftPos);
    }

    public void setOptions(List<String> options, Color selectedColor) {
        final int elementHeight = this.dropdownStyle.getOptionHeight();
        int listHeight = Math.min(this.dropdownStyle.getMaxVisibleElements(), options.size()) * elementHeight;

        menu = new ScrollableList(getLeftPos(), getTopPos() + getHeight() + 2, getWidth(), listHeight, selectedColor);
        menu.clear();

        for (String option : options) {
            menu.addElement(option, () -> {
                setTitle(option);
                lastSelectedElement = option;
                isUnfolded = false;
                if (onChangeAction != null) onChangeAction.execute(this.menu.getSelectedTitle());
            });
        }
    }

    public boolean hasSelection() {
        return getOptions().contains(getSelectedText());
    }

    public String getSelectedText() {
        String raw = getTitle().getString().toString();
        return raw.equals(placeholder) ? "" : raw;
    }

    public boolean containsOption(String option) {
        return getOptions().stream().anyMatch(s -> s.equalsIgnoreCase(option));
    }

    public void clear() {
        setOptions(new ArrayList<>(), this.dropdownStyle.getBackgroundColor());
        setOption("");
        menu.clear();
    }

    public boolean hasOptions() {
        return !getOptions().isEmpty();
    }

    public void setOption(String name) {
        if (menu == null) return;

        if (menu.selectEntry(name)) {
            setTitle(name);
        } else {
            unselect();
        }
    }

    public void unselect() {
        setTitle(placeholder);
        if (menu != null) menu.unselect();
        lastSelectedElement = "";
    }

    public List<String> getOptions() {
        if (menu == null || menu.getContent().isEmpty()) return new ArrayList<>();

        List<String> options = new ArrayList<>();
        for (Button b : menu.getContent()) {
            options.add(b.getTitle().getString().toString());
        }
        return options;
    }

    public boolean isUnfolded() {
        return isUnfolded;
    }

    private void renderArrow(PoseStack poseStack, int x, int y, boolean inverted) {
        Color arrowColor = GuiUtils.getContrastColor(this.dropdownStyle.getBackgroundColor());
        if (inverted) {
            AbstractContainerScreen.fill(poseStack, x + 3, y, x + 5, y + 1, arrowColor.getRGB());
            AbstractContainerScreen.fill(poseStack, x + 2, y + 1, x + 6, y + 2, arrowColor.getRGB());
            AbstractContainerScreen.fill(poseStack, x + 1, y + 2, x + 7, y + 3, arrowColor.getRGB());
            AbstractContainerScreen.fill(poseStack, x, y + 3, x + 8, y + 4, arrowColor.getRGB());
        } else {
            AbstractContainerScreen.fill(poseStack, x, y, x + 8, y + 1, arrowColor.getRGB());
            AbstractContainerScreen.fill(poseStack, x + 1, y + 1, x + 7, y + 2, arrowColor.getRGB());
            AbstractContainerScreen.fill(poseStack, x + 2, y + 2, x + 6, y + 3, arrowColor.getRGB());
            AbstractContainerScreen.fill(poseStack, x + 3, y + 3, x + 5, y + 4, arrowColor.getRGB());
        }
    }
}