package com.github.pterolatypus.comp1206.coursework.fract.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * A simple JPopupMenu extension which handles the adding of mnemonics and
 * accelerators a little more easily.
 * 
 * @author James
 *
 */
public class ContextMenu extends JPopupMenu {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	/**
	 * Instantiates a new ContextMenu
	 */
	public ContextMenu() {
		super();
	}

	/**
	 * Add a given item to the menu with the specified keyCode as its mnemonic
	 * AND accelerator.
	 * 
	 * @param item
	 *            The item to add
	 * @param shortcut
	 *            The keyCode of the key to be used as shortcut
	 */
	public void addMenuItem(JMenuItem item, int shortcut) {
		item.setMnemonic(shortcut);
		item.setAccelerator(KeyStroke.getKeyStroke(shortcut, 0));
		add(item);
	}

}
