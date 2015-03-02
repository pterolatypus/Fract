package com.github.pterolatypus.comp1206.coursework.fractal;

import javax.swing.JFrame;


public class AppWindow extends JFrame {

	private static final long serialVersionUID = 3254183707783343297L;

	private AppEngine eng;
	
	public AppWindow(AppEngine eng) {
		super("Fractal Visualiser");
		this.eng = eng;
		//GUI Stuff to come
		
	}
	
}
