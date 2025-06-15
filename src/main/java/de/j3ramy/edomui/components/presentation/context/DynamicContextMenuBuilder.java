package de.j3ramy.edomui.components.presentation.context;

import de.j3ramy.edomui.interfaces.ContextMenuProvider;
import de.j3ramy.edomui.interfaces.IAction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class DynamicContextMenuBuilder {
    private final List<ContextActionProvider> actionProviders = new ArrayList<>();

    @FunctionalInterface
    public interface ContextActionProvider {
        List<ContextAction> getActions(int elementIndex, String elementTitle, Object data);
    }

    public DynamicContextMenuBuilder addAction(String label, IAction action) {
        actionProviders.add((index, title, data) ->
                List.of(new ContextAction(label, action)));
        return this;
    }

    public DynamicContextMenuBuilder addActionIf(String label, IAction action, Predicate<Integer> condition) {
        actionProviders.add((index, title, data) ->
                condition.test(index) ? List.of(new ContextAction(label, action)) : List.of());
        return this;
    }

    public DynamicContextMenuBuilder addIndexAction(String label, Function<Integer, IAction> actionFactory) {
        actionProviders.add((index, title, data) ->
                List.of(new ContextAction(label, actionFactory.apply(index))));
        return this;
    }

    public DynamicContextMenuBuilder addTitleAction(String label, Function<String, IAction> actionFactory) {
        actionProviders.add((index, title, data) ->
                List.of(new ContextAction(label, actionFactory.apply(title))));
        return this;
    }

    public DynamicContextMenuBuilder addActions(ContextActionProvider provider) {
        actionProviders.add(provider);
        return this;
    }

    public DynamicContextMenuBuilder addDeleteAction(Function<Integer, IAction> deleteFunction) {
        return addIndexAction("Delete", deleteFunction);
    }

    public DynamicContextMenuBuilder addEditAction(Function<Integer, IAction> editFunction) {
        return addIndexAction("Edit", editFunction);
    }

    public DynamicContextMenuBuilder addMoveActions(Function<Integer, IAction> moveUpFunction,
                                                    Function<Integer, IAction> moveDownFunction) {
        return addActionIf("Up", () -> moveUpFunction.apply(-1).execute(), index -> index > 0)
                .addActionIf("Down", () -> moveDownFunction.apply(-1).execute(), index -> true);
    }

    public ContextMenuProvider build() {
        return (menu, elementIndex, elementTitle) -> {
            for (ContextActionProvider provider : actionProviders) {
                List<ContextAction> actions = provider.getActions(elementIndex, elementTitle, null);
                for (ContextAction action : actions) {
                    menu.addMenuItem(action.getLabel(), action.getAction(), action.isEnabled());
                }
            }
        };
    }

    public static DynamicContextMenuBuilder create() {
        return new DynamicContextMenuBuilder();
    }
}

