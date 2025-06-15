package de.j3ramy.edomui.interfaces;

import de.j3ramy.edomui.components.presentation.context.ContextMenu;

public interface ContextMenuProvider {
    void populateContextMenu(ContextMenu menu, int elementIndex, String elementTitle);
}
