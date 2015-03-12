package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class OverlayPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	
	public OverlayPanel(Dimension size) {
		this.setLayout(null);
		this.setSize((int) size.getWidth(), (int) size.getHeight());
		this.setOpaque(false);
	}

}
