package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class AppWindow extends JFrame {

	private static final long serialVersionUID = 3254183707783343297L;

	private AppEngine eng;
	private Graph mandel = new Graph(Fractal.MANDELBROT);
	
	public AppWindow(AppEngine eng) {
		super("Fractal Visualiser");
		this.eng = eng;
		//GUI Stuff to come
		this.setBounds(0,0,512,512);
		
		JButton btn = new JButton("clickme");
		
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				queueTask(new Task() {
					@Override
					public void run() {
						System.out.println("Clicked!");
					}
				});
			}
		});
		
		this.add(btn);
		
	}
	
	public void queueTask(Task t) {
		eng.queueTask(t);
	}
	
}
