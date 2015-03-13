package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * The class used by the custom glass panel, only exists to move some boilerplate out of the main class.
 * 
 * @author James
 */
public class OverlayPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	
	public OverlayPanel(Dimension size) {
		this.setLayout(null);
		this.setSize((int) size.getWidth(), (int) size.getHeight());
		this.setOpaque(false);
	}

}
