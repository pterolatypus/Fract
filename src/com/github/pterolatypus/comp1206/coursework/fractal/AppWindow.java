package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	FractalEngine mandelbrot;
	
	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mandelbrot = new FractalEngine(Fractal.MANDELBROT);
		mandelbrot.start();
		mandelbrot.updateImage(new Rectangle(512,512));
		
		setBounds(0,0,540,540);
		
		this.setLayout(new BorderLayout());

		final JPanel pnlGraph = new JPanel() {
			private static final long serialVersionUID = AppWindow.serialVersionUID;
			public void paint(Graphics g) {
				g.drawImage(mandelbrot.getImage(), 0, 0, this);
			}
		};
		this.add(pnlGraph, BorderLayout.CENTER);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		
		CursorPanel pnlC = new CursorPanel();
		//pnlGraph.add(pnlC);
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				mandelbrot.interrupt();
				mandelbrot.updateImage(new Rectangle(pnlGraph.getWidth(),pnlGraph.getHeight()));
				pnlGraph.repaint();
			}
		});
	}
	
}
