package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;

public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		setBounds(0, 0, 540, 540);

		final GraphPanel pnlGraphMain = new GraphPanel(Fractal.MANDELBROT);

		this.add(pnlGraphMain);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}

		CoordinatePanel pnlC = new CoordinatePanel(pnlGraphMain);
		pnlC.setVisible(true);
		
		JPanel glasspane = new JPanel();
		this.setGlassPane(glasspane);
		
		glasspane.add(pnlC);
		pnlC.enable();
		
		final JPanel pnlGraphCorner = new GraphPanel(Fractal.MANDELBROT);
		add(pnlGraphCorner);
		pnlGraphCorner.setBounds(getWidth()-128,0,128,128);
		pnlGraphCorner.setPreferredSize(new Dimension(128,128));
		
		glasspane.setOpaque(false);
		glasspane.setVisible(true);
		
	}

}
