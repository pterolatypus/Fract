package com.github.pterolatypus.comp1206.coursework.fract.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class ContextMenu extends JPopupMenu {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	public ContextMenu() {
		super();
	}
	
	public void addMenuItem(JMenuItem item, int shortcut) {
		item.setMnemonic(shortcut);
		item.setAccelerator(KeyStroke.getKeyStroke(shortcut, 0));
		add(item);
	}
	
}
