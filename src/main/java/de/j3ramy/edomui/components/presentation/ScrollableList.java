package de.j3ramy.edomui.components.presentation;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.presentation.contextmenu.ContextMenu;
import de.j3ramy.edomui.components.presentation.contextmenu.DynamicContextMenuBuilder;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.ContextMenuProvider;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.basic.VerticalScrollbar;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.components.button.Button;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;

public class ScrollableList extends Widget {
    private final int maxVisibleListElements;

    protected final int selectedColor;
    protected final VerticalScrollbar scrollbar;
    protected final ArrayList<Button> content = new ArrayList<>();

    protected ContextMenu contextMenu;
    protected ContextMenuProvider contextMenuProvider;
    protected int selectedIndex = -1;
    protected int scrollIndex = 0;
    protected int elementHeight;

    private DynamicContextMenuBuilder menuBuilder;
    private boolean dynamicMenuEnabled = false;

    public ScrollableList(int x, int y, int width, int height, int elementHeight, int selectedColor) {
        super(x, y, width, height);
        this.elementHeight = elementHeight;
        this.selectedColor = selectedColor == -1 ? this.getStyle().getBackgroundColor() : selectedColor;
        this.maxVisibleListElements = elementHeight > 0 ? height / elementHeight : 0;

        this.scrollbar = new VerticalScrollbar(this, 0, maxVisibleListElements);
        this.getStyle().setBorderColor(Color.BLACK);
    }

    public ScrollableList(int x, int y, int width, int height, int elementHeight) {
        this(x, y, width, height, elementHeight, -1);
    }

    @Override
    public void render(PoseStack stack) {
        if (isHidden()) return;
        super.render(stack);

        if (needsScrolling()) scrollbar.render(stack);

        for (Button b : getVisibleButtons()) b.render(stack);

        if (contextMenu != null && contextMenu.isVisible()) {
            contextMenu.render(stack);
        }
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;
        super.update(x, y);

        if (contextMenu != null && contextMenu.isVisible()) {
            if (needsScrolling()) scrollbar.update(x, y);
            contextMenu.update(x, y);
            return;
        }

        for (Button b : getVisibleButtons()) b.update(x, y);
        if (needsScrolling()) scrollbar.update(x, y);
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden() || !isMouseOver()) return;

        if (contextMenu != null && contextMenu.isVisible()) {
            if (contextMenu.isMouseOver()) {
                contextMenu.onClick(mouseButton);
                return;
            }

            if (mouseButton == 1) {
                for (int i = 0; i < getVisibleButtons().size(); i++) {
                    Button b = getVisibleButtons().get(i);
                    if (b.isMouseOver()) {
                        int elementIndex = scrollIndex + i;
                        selectIndex(elementIndex);
                        showContextMenu(elementIndex);
                        return;
                    }
                }
            }

            contextMenu.hide();
            return;
        }

        boolean clicked = false;
        for (int i = 0; i < getVisibleButtons().size(); i++) {
            Button b = getVisibleButtons().get(i);
            if (b.isMouseOver()) {
                int elementIndex = scrollIndex + i;

                if (mouseButton == 1) {
                    selectIndex(elementIndex);
                    showContextMenu(elementIndex);
                    clicked = true;
                    break;
                } else if (mouseButton == 0) {
                    selectIndex(elementIndex);
                    b.onClick(mouseButton);
                    clicked = true;
                    break;
                }
            }
        }

        if (!clicked) {
            unselect();
        }
    }

    public ScrollableList enableDynamicContextMenu() {
        this.contextMenu = new ContextMenu(this.elementHeight, this.selectedColor);
        this.menuBuilder = DynamicContextMenuBuilder.create();
        this.dynamicMenuEnabled = true;
        return this;
    }

    public ScrollableList addContextAction(String label, IAction action) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addAction(label, action);
        updateContextMenuProvider();
        return this;
    }

    public ScrollableList addContextDeleteAction() {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addDeleteAction(index -> () -> removeElement(index));
        updateContextMenuProvider();
        return this;
    }

    public ScrollableList addContextEditAction(Function<Integer, IAction> editFunction) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addEditAction(editFunction);
        updateContextMenuProvider();
        return this;
    }

    public ScrollableList addContextActionIf(String label, IAction action, Predicate<Integer> condition) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addActionIf(label, action, condition);
        updateContextMenuProvider();
        return this;
    }

    public ScrollableList addContextIndexAction(String label, Function<Integer, IAction> actionFactory) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addIndexAction(label, actionFactory);
        updateContextMenuProvider();
        return this;
    }

    public ScrollableList addContextTitleAction(String label, Function<String, IAction> actionFactory) {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        menuBuilder.addTitleAction(label, actionFactory);
        updateContextMenuProvider();
        return this;
    }

    public DynamicContextMenuBuilder getMenuBuilder() {
        if (!dynamicMenuEnabled) enableDynamicContextMenu();
        return menuBuilder;
    }

    public void setContextMenuProvider(ContextMenuProvider provider) {
        if (contextMenu == null) {
            this.contextMenu = new ContextMenu(this.elementHeight, this.selectedColor);
        }
        this.contextMenuProvider = provider;
        this.dynamicMenuEnabled = false;
    }

    private void updateContextMenuProvider() {
        if (menuBuilder != null && dynamicMenuEnabled) {
            this.contextMenuProvider = menuBuilder.build();
        }
    }

    private void showContextMenu(int elementIndex) {
        if(this.contextMenu == null)
            return;

        contextMenu.clear();
        contextMenuProvider.populateContextMenu(contextMenu, elementIndex,
                content.get(elementIndex).getTitle().getString().toString());

        contextMenu.show(this.getMousePosition().x, this.getMousePosition().y);
    }

    @Override
    public void onScroll(double delta) {
        if (!isMouseOver() || !needsScrolling()) return;

        int maxScroll = Math.max(0, content.size() - maxVisibleListElements);
        scrollIndex += delta < 0 ? 1 : -1;
        scrollIndex = Math.max(0, Math.min(scrollIndex, maxScroll));
        scrollbar.updateScrollIndex(scrollIndex);
        layoutButtons();
    }

    public void addElement(String title, @Nullable IAction clickAction) {
        Button button = new Button(getLeftPos(), getTopPos() + content.size() * elementHeight,
                getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN, elementHeight, title,
                GuiPresets.SCROLLABLE_LIST_FONT_SIZE, clickAction, null, ButtonType.TEXT_FIELD);

        button.noBorder();
        button.getStyle().setBackgroundColor(this.getStyle().getBackgroundColor());
        button.getStyle().setHoverBackgroundColor(selectedColor);
        button.getTitle().setTextColor(Color.DARK_GRAY);

        if (button.getTitle().isTruncated()) {
            button.enableTooltip();
        }

        content.add(button);
        scrollbar.updateContentSize(content.size());
        layoutButtons();
    }

    public void addElement(String title) {
        addElement(title, null);
    }

    public void removeSelectedElement() {
        if (selectedIndex != -1 && selectedIndex < content.size()) {
            content.remove(selectedIndex);
            unselect();
            scrollbar.updateContentSize(content.size());
            layoutButtons();
        }
    }

    public void removeElement(int index) {
        if (index >= 0 && index < content.size()) {
            if (selectedIndex == index) {
                unselect();
            }

            content.remove(index);
            if (selectedIndex > index) {
                selectedIndex--;
            }

            scrollbar.updateContentSize(content.size());
            layoutButtons();
        }
    }

    public void clear() {
        content.clear();
        unselect();
        scrollbar.updateContentSize(0);
        if (contextMenu != null) {
            contextMenu.hide();
        }
    }

    public void selectFirstEntry() {
        if (!content.isEmpty()) {
            selectIndex(0);
        }
    }

    public boolean selectEntry(String title) {
        for (int i = 0; i < content.size(); i++) {
            Button b = content.get(i);
            if (b.getTitle().getString().toString().equals(title)) {
                selectIndex(i);
                return true;
            }
        }
        return false;
    }

    public void unselect() {
        if (hasSelection()) {
            content.get(selectedIndex).getStyle().setBackgroundColor(this.getStyle().getBackgroundColor());
            selectedIndex = -1;
        }
    }

    public boolean hasSelection() {
        return selectedIndex >= 0;
    }

    public String getSelectedTitle() {
        return hasSelection() ? content.get(selectedIndex).getTitle().getString().toString() : "";
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void moveSelectedElementUp() {
        if (selectedIndex > 0) {
            Collections.swap(content, selectedIndex, selectedIndex - 1);
            selectedIndex--;
            layoutButtons();
        }
    }

    public void moveSelectedElementDown() {
        if (selectedIndex != -1 && selectedIndex < content.size() - 1) {
            Collections.swap(content, selectedIndex, selectedIndex + 1);
            selectedIndex++;
            layoutButtons();
        }
    }

    public ArrayList<Button> getContent() {
        return content;
    }

    private void selectIndex(int index) {
        if (index < 0 || index >= content.size()) return;

        unselect();
        selectedIndex = index;

        Button b = content.get(index);
        b.getStyle().setBackgroundColor(selectedColor);
        b.getTitle().setTextColor(Color.WHITE);

        ensureVisible(index);
    }

    private void ensureVisible(int index) {
        if (index < scrollIndex) scrollIndex = index;
        else if (index >= scrollIndex + maxVisibleListElements)
            scrollIndex = index - maxVisibleListElements + 1;

        scrollIndex = Math.max(0, Math.min(scrollIndex, content.size() - maxVisibleListElements));
        scrollbar.updateScrollIndex(scrollIndex);
        layoutButtons();
    }

    protected void layoutButtons() {
        for (int i = 0; i < content.size(); i++) {
            Button b = content.get(i);
            b.setLeftPos(getLeftPos());
            b.setWidth(getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH);
            b.setTopPos(getTopPos() + (i - scrollIndex) * elementHeight);
            b.setHeight(elementHeight);
        }
    }

    protected ArrayList<Button> getVisibleButtons() {
        int end = Math.min(content.size(), scrollIndex + maxVisibleListElements);
        return new ArrayList<>(content.subList(scrollIndex, end));
    }

    private boolean needsScrolling() {
        return content.size() > maxVisibleListElements;
    }
}