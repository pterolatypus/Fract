package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 3254183707783343297L;

	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		
		
		setBounds(0,0,512,512);
		//this.setLayout(new BorderLayout());
		
		this.add(new DraggablePanel());
	}
	
}
