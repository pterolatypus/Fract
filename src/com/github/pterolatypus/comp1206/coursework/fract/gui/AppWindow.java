package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;

public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	public static final String ACTION_SPACE_PRESSED = "action_space_pressed";

	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		setBounds(0, 0, 540, 540);
		setExtendedState(MAXIMIZED_BOTH);

		final GraphPanel pnlMandelbrot = new GraphPanel(Fractal.MANDELBROT);
		final GraphContainer pnlMain = new GraphContainer(pnlMandelbrot);
		pnlMain.setLayout(new GridLayout(1, 1));
		pnlMain.add(pnlMandelbrot);
		
		final GraphPanel pnlJulia = new GraphPanel(new Fractal.Julia(
				new Complex(0, 0)));
		final GraphContainer pnlCorner = new GraphContainer(pnlJulia);
		pnlCorner.setLayout(new GridLayout(1, 1));

		final JPanel pnlOverlay = new JPanel();
		//glassPane.setLayout(null);
		pnlOverlay.setSize(getContentPane().getWidth(), getContentPane()
				.getHeight());
		
		InputMap inputMap = pnlOverlay.getInputMap();
		ActionMap actionMap = pnlOverlay.getActionMap();
		
		final CoordinatePanel pnlCoord = new CoordinatePanel(pnlMandelbrot);
		pnlOverlay.add(pnlCoord);
		this.setGlassPane(pnlOverlay);
		pnlCoord.enable();

		this.setLayout(new BorderLayout());
		this.add(pnlMain, BorderLayout.CENTER);

		pnlCorner.setBounds(15, 15, 256, 256);
		pnlCorner.setPreferredSize(new Dimension(256, 256));
		pnlCorner.setBorderOffset(15);
		pnlCorner.setSnapDistance(30);

		pnlCorner.add(pnlJulia);
		pnlOverlay.add(pnlCorner);

		MouseAdapter zoomListener = new MouseAdapter() {
			Point p;

			@Override
			public void mouseDragged(MouseEvent e) {
				repaint();
				Graphics g = pnlOverlay.getGraphics();
				int x = (int) Math.min(e.getX(), p.getX());
				int y = (int) Math.min(e.getY(), p.getY());
				int width = (int) Math.abs(e.getX() - p.getX());
				int height = (int) Math.abs(e.getY() - p.getY());
				g.drawRect(x, y, width, height);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getPoint().distance(p) < 4) {
						return;
					}
					int x = (int) Math.min(e.getX(), p.getX());
					int y = (int) Math.min(e.getY(), p.getY());
					int width = (int) Math.abs(e.getX() - p.getX());
					int height = (int) Math.abs(e.getY() - p.getY());
					Rectangle r = new Rectangle(x, y, width, height);
					pnlMandelbrot.setMathBounds(r);
					// pnlGraphMain.
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					pnlMandelbrot.zoomOut();
				}
			}
		};

		pnlOverlay.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Complex c = pnlMain.getMathCoords(e.getPoint());
				pnlCorner.updateFractal(c);
			}
		});

		pnlOverlay.setOpaque(false);
		pnlOverlay.setVisible(true);

		pnlOverlay.addMouseMotionListener(pnlCoord.getListener(pnlMandelbrot));
		pnlOverlay.addMouseMotionListener(zoomListener);
		pnlOverlay.addMouseListener(zoomListener);

	}

}
