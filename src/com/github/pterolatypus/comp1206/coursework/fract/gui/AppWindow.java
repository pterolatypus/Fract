package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		
		final JPanel glasspane = new JPanel();
		this.setGlassPane(glasspane);
		
		glasspane.add(pnlC);
		pnlC.enable();
		
		final JPanel pnlGraphCorner = new GraphPanel(Fractal.MANDELBROT);
		add(pnlGraphCorner);
		pnlGraphCorner.setBounds(getWidth()-128,0,128,128);
		pnlGraphCorner.setPreferredSize(new Dimension(128,128));
		
		glasspane.setOpaque(false);
		glasspane.setVisible(true);
		
		MouseAdapter zoomListener = new MouseAdapter() {
			Point p;
			@Override
			public void mouseDragged(MouseEvent e) {
				repaint();
				Graphics g = glasspane.getGraphics();
				int x = (int) Math.min(e.getX(), p.getX());
				int y = (int) Math.min(e.getY(), p.getY());
				int width = (int) Math.abs(e.getX()-p.getX());
				int height = (int) Math.abs(e.getY()-p.getY());
				g.drawRect(x,y,width,height);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
				System.out.println(e.getX()+":"+e.getY());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = (int) Math.min(e.getX(), p.getX());
				int y = (int) Math.min(e.getY(), p.getY());
				int width = (int) Math.abs(e.getX()-p.getX());
				int height = (int) Math.abs(e.getY()-p.getY());
				Rectangle r = new Rectangle(x,y,width,height);
				pnlGraphMain.setMathBounds(r);
			}
		};
		
		pnlGraphMain.addMouseMotionListener(zoomListener);
	}

}
