package de.j3ramy.edomui.components.presentation.context;

import de.j3ramy.edomui.interfaces.IAction;

public class ContextAction {
    private final String label;
    private final IAction action;
    private boolean enabled = true;

    public ContextAction(String label, IAction action) {
        this.label = label;
        this.action = action;
    }

    public ContextAction enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getLabel() { return label; }
    public IAction getAction() { return action; }
    public boolean isEnabled() { return enabled; }
}

