package de.j3ramy.edomui.interfaces;

import de.j3ramy.edomui.components.presentation.contextmenu.ContextMenu;

public interface IContextMenuProvider {
    void populateContextMenu(ContextMenu menu, int elementIndex, String elementTitle);
}
