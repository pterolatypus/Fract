package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setBounds(0,0,512,512);
		this.setLayout(new BorderLayout());

	}
	
}
