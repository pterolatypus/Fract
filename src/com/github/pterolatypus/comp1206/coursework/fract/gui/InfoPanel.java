package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private JLabel coords;
	
	public InfoPanel() {
		setLayout(new GridBagLayout());
		coords = new JLabel();
		add(coords);
		setBackground(Color.white);
	}
	
	public void setCoordinates(String text) {
		coords.setText(text);
	}
	
}
