package com.github.pterolatypus.comp1206.coursework.fractal;

public class AppEngine extends Thread {

	public void run() {
		AppWindow window = new AppWindow(this);
		window.setVisible(true);
	}
	
}
