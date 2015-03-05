package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 0L;

	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setBounds(0,0,512,512);
		this.setLayout(new BorderLayout());

		JPanel pnlCenter = new JPanel();
		DraggablePanel pnlDragTest = new DraggablePanel(pnlCenter);
		pnlDragTest.pSetPreferredSize(new Dimension(64, 64));
		pnlDragTest.setBackground(Color.RED);
		pnlCenter.add(pnlDragTest);
		this.add(pnlCenter, BorderLayout.CENTER);
	}
	
	public static long getSVUID() {
		return serialVersionUID;
	}
	
}
