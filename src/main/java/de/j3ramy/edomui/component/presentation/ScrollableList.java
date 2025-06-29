package de.j3ramy.edomui.component.presentation;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.presentation.contextmenu.ContextMenu;
import de.j3ramy.edomui.component.presentation.contextmenu.DynamicContextMenuBuilder;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IContextMenuProvider;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.component.basic.VerticalScrollbar;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.theme.presentation.ScrollableListStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.util.style.GuiUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;

public class ScrollableList extends Widget {
    private final int maxVisibleListElements;

    protected final ScrollableListStyle listStyle;
    protected final VerticalScrollbar scrollbar;
    protected final ArrayList<Button> content = new ArrayList<>();

    private DynamicContextMenuBuilder menuBuilder;
    private boolean dynamicMenuEnabled = false;

    protected ContextMenu contextMenu;
    protected IContextMenuProvider IContextMenuProvider;
    protected int selectedIndex = -1;
    protected int scrollIndex = 0;

    public ScrollableList(int x, int y, int width, int height, Color selectionColor) {
        super(x, y, width, height);

        this.listStyle = new ScrollableListStyle(ThemeManager.getDefaultScrollableListStyle());
        this.setStyle(this.listStyle);

        if(selectionColor != null){
            this.listStyle.setSelectionColor(selectionColor);
        }

        this.maxVisibleListElements = this.listStyle.getElementHeight() > 0 ? height / this.listStyle.getElementHeight() : 0;
        this.scrollbar = new VerticalScrollbar(this, 0, maxVisibleListElements);
    }

    public ScrollableList(int x, int y, int width, int height) {
        this(x, y, width, height, null);
    }

    @Override
    public void render(PoseStack stack) {
        if (isHidden()) return;
        super.render(stack);

        if (needsScrolling()) scrollbar.render(stack);

        for (Button b : getVisibleButtons()) {
            b.render(stack);
        }

        if (contextMenu != null && !contextMenu.isHidden()) {
            contextMenu.render(stack);
        }
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;
        super.update(x, y);

        if (contextMenu != null && !contextMenu.isHidden()) {
            if (needsScrolling()) scrollbar.update(x, y);
            contextMenu.update(x, y);
            return;
        }

        for (Button b : getVisibleButtons()) {
            b.update(x, y);
        }

        if (needsScrolling()) scrollbar.update(x, y);
    }

    @Override
    public void onClick(int mouseButton) {
        if (isHidden() || !isMouseOver()) return;

        if (contextMenu != null && !contextMenu.isHidden()) {
            if (contextMenu.isMouseOver()) {
                contextMenu.onClick(mouseButton);
                return;
            }

            if (mouseButton == 1) {
                for (int i = 0; i < getVisibleButtons().size(); i++) {
                    Button b = getVisibleButtons().get(i);
                    if (b.isMouseOver() && b.isEnabled()) {
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

            if (b.isMouseOver() && b.isEnabled()) {
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

    @Override
    public void onScroll(double delta) {
        if (!isMouseOver() || !needsScrolling()) return;

        int maxScroll = Math.max(0, content.size() - maxVisibleListElements);
        scrollIndex += delta < 0 ? 1 : -1;
        scrollIndex = Math.max(0, Math.min(scrollIndex, maxScroll));
        scrollbar.updateScrollIndex(scrollIndex);
        layoutButtons();
    }

    @Override
    public ScrollableListStyle getStyle() {
        return this.listStyle;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (Button button : content) {
            button.setEnabled(enabled);
        }
    }

    @Override
    public void setLeftPos(int newLeftPos) {
        int delta = newLeftPos - this.getLeftPos();

        super.setLeftPos(newLeftPos);

        if (scrollbar != null) {
            scrollbar.setLeftPos(scrollbar.getLeftPos() + delta);
        }

        if (contextMenu != null) {
            contextMenu.setLeftPos(contextMenu.getLeftPos() + delta);
        }

        layoutButtons();
    }

    @Override
    public void setTopPos(int newTopPos) {
        int delta = newTopPos - this.getTopPos();

        super.setTopPos(newTopPos);

        if (scrollbar != null) {
            scrollbar.setTopPos(scrollbar.getTopPos() + delta);
        }

        if (contextMenu != null) {
            contextMenu.setTopPos(contextMenu.getTopPos() + delta);
        }

        layoutButtons();
    }

    private void updateContextMenuProvider() {
        if (menuBuilder != null && dynamicMenuEnabled) {
            this.IContextMenuProvider = menuBuilder.build();
        }
    }

    private void showContextMenu(int elementIndex) {
        if(this.contextMenu == null)
            return;

        contextMenu.clear();
        IContextMenuProvider.populateContextMenu(contextMenu, elementIndex,
                content.get(elementIndex).getTitle().getString().toString());

        contextMenu.show(this.getMousePosition().x, this.getMousePosition().y);
    }

    private void selectIndex(int index) {
        if (index < 0 || index >= content.size()) return;

        unselect();
        selectedIndex = index;

        Button b = content.get(index);
        b.getStyle().setBackgroundColor(this.listStyle.getSelectionColor());

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

    private boolean needsScrolling() {
        return content.size() > maxVisibleListElements;
    }

    private void updateButtonAppearance(Button button, boolean enabled) {
        if (enabled) {
            button.getStyle().setBackgroundColor(this.listStyle.getBackgroundColor());
            button.getStyle().setHoverBackgroundColor(this.listStyle.getSelectionColor());
            button.getTitle().getStyle().setTextColor(this.listStyle.getTextColor());
            button.getTitle().getStyle().setTextHoverColor(GuiUtils.getContrastColor(button.getStyle().getHoverBackgroundColor()));
        } else {
            button.getStyle().setBackgroundColor(this.listStyle.getDisabledBackgroundColor());
            button.getStyle().setHoverBackgroundColor(this.listStyle.getDisabledBackgroundColor());
            button.getTitle().getStyle().setTextColor(this.listStyle.getTextDisabledColor());
            button.getTitle().getStyle().setTextHoverColor(this.listStyle.getTextDisabledColor());
        }
    }

    protected void layoutButtons() {
        for (int i = 0; i < content.size(); i++) {
            Button b = content.get(i);

            b.setLeftPos(getLeftPos());
            b.setWidth(getWidth() - this.scrollbar.getStyle().getScrollbarTrackWidth());
            b.setTopPos(getTopPos() + (i - scrollIndex) * this.listStyle.getElementHeight());
            b.setHeight(this.listStyle.getElementHeight());
        }
    }

    protected ArrayList<Button> getVisibleButtons() {
        int end = Math.min(content.size(), scrollIndex + maxVisibleListElements);
        return new ArrayList<>(content.subList(scrollIndex, end));
    }

    public ScrollableList enableDynamicContextMenu() {
        this.contextMenu = new ContextMenu(this.listStyle.getSelectionColor());
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

    public void setContextMenuProvider(IContextMenuProvider provider) {
        if (contextMenu == null) {
            this.contextMenu = new ContextMenu(this.listStyle.getSelectionColor());
        }
        this.IContextMenuProvider = provider;
        this.dynamicMenuEnabled = false;
    }

    public void addElement(String title, @Nullable IAction clickAction) {
        Button button = new Button(getLeftPos(), getTopPos() + content.size() * this.listStyle.getElementHeight(),
            getWidth() - this.scrollbar.getStyle().getScrollbarTrackWidth() - 2 * this.listStyle.getPadding(), this.listStyle.getElementHeight(), title,
            clickAction, ButtonType.TEXT_FIELD, this.listStyle.getPadding());

        button.noBorder();
        button.getStyle().setBackgroundColor(this.listStyle.getBackgroundColor());
        button.getStyle().setHoverBackgroundColor(this.listStyle.getSelectionColor());
        button.getTitle().getStyle().setTextColor(this.listStyle.getTextColor());
        button.getTitle().getStyle().setTextHoverColor(GuiUtils.getContrastColor(button.getStyle().getHoverBackgroundColor()));

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
        this.unselect();

        content.clear();
        scrollIndex = 0;
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
        }
        selectedIndex = -1;
    }

    public boolean hasSelection() {
        return selectedIndex >= 0 && selectedIndex < content.size();
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

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public VerticalScrollbar getScrollbar() {
        return scrollbar;
    }

    public boolean setElementEnabled(String title, boolean enabled) {
        for (Button button : content) {
            if (button.getTitle().getString().toString().equals(title)) {
                button.setEnabled(enabled);
                updateButtonAppearance(button, enabled);
                return true;
            }
        }
        return false;
    }

    public boolean setElementEnabledByIndex(int index, boolean enabled) {
        if (index >= 0 && index < content.size()) {
            Button button = content.get(index);
            button.setEnabled(enabled);
            updateButtonAppearance(button, enabled);
            return true;
        }
        return false;
    }

    public boolean isElementEnabled(String title) {
        for (Button button : content) {
            if (button.getTitle().getString().toString().equals(title)) {
                return button.isEnabled();
            }
        }
        return false;
    }

    public boolean isElementEnabledByIndex(int index) {
        if (index >= 0 && index < content.size()) {
            return content.get(index).isEnabled();
        }
        return false;
    }

    public boolean hasEntry(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        }

        for (Button button : content) {
            if (button.getTitle().getString().toString().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return this.content.isEmpty();
    }

    public int getCount(){
        return this.content.size();
    }

    public boolean isFirstElementSelectedSafe() {
        return !content.isEmpty() && hasSelection() && selectedIndex == 0;
    }

    public boolean isLastElementSelectedSafe() {
        return !content.isEmpty() && hasSelection() && selectedIndex == content.size() - 1;
    }

    public void scrollToTop() {
        scrollIndex = 0;
        scrollbar.updateScrollIndex(scrollIndex);
        layoutButtons();
    }

    public void scrollToBottom() {
        if (content.isEmpty()) return;

        scrollIndex = Math.max(0, content.size() - maxVisibleListElements);
        scrollbar.updateScrollIndex(scrollIndex);
        layoutButtons();
    }

    public void scrollToElement(int index) {
        if (index < 0 || index >= content.size()) return;

        ensureVisible(index);
    }

    public void scrollToElement(String title) {
        for (int i = 0; i < content.size(); i++) {
            Button button = content.get(i);
            if (button.getTitle().getString().toString().equals(title)) {
                scrollToElement(i);
                return;
            }
        }
    }

    public boolean isScrolledToTop() {
        return scrollIndex == 0;
    }

    public boolean isScrolledToBottom() {
        if (content.isEmpty() || !needsScrolling()) return true;

        int maxScroll = Math.max(0, content.size() - maxVisibleListElements);
        return scrollIndex >= maxScroll;
    }

    public double getScrollPercentage() {
        if (content.isEmpty() || !needsScrolling()) return 0.0;

        int maxScroll = Math.max(0, content.size() - maxVisibleListElements);
        return maxScroll > 0 ? (double) scrollIndex / maxScroll : 0.0;
    }

    public void setScrollPercentage(double percentage) {
        if (content.isEmpty() || !needsScrolling()) return;

        percentage = Math.max(0.0, Math.min(1.0, percentage));
        int maxScroll = Math.max(0, content.size() - maxVisibleListElements);
        scrollIndex = (int) (maxScroll * percentage);
        scrollbar.updateScrollIndex(scrollIndex);
        layoutButtons();
    }

    public boolean isElementVisible(int index) {
        return index >= scrollIndex && index < scrollIndex + maxVisibleListElements;
    }
}