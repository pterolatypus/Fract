package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
		
		final GraphPanel pnlGraphMain = new GraphPanel(Fractal.MANDELBROT);
		GraphContainer mainPanel = new GraphContainer(pnlGraphMain);
		mainPanel.setLayout(new GridLayout(1,1));
		mainPanel.add(pnlGraphMain);
		
		this.setContentPane(mainPanel);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}

		final JPanel glassPane = new JPanel();
		//glassPane.setLayout(null);
		glassPane.setSize(getContentPane().getWidth(), getContentPane()
				.getHeight());
		this.setGlassPane(glassPane);

		InputMap inputMap = glassPane.getInputMap();
		inputMap.put(KeyStroke.getKeyStroke("SPACE"), ACTION_SPACE_PRESSED);

		ActionMap actionMap = glassPane.getActionMap();

		final CoordinatePanel pnlC = new CoordinatePanel(pnlGraphMain);

		actionMap.put(ACTION_SPACE_PRESSED, new AbstractAction() {
			private static final long serialVersionUID = AppWindow.serialVersionUID;

			@Override
			public void actionPerformed(ActionEvent e) {
				pnlC.setVisible(!pnlC.isVisible());
			}
		});GraphPanel pnlMandelbrot;
		GraphPanel pnlJulia;

		final DraggablePanel pnlCorner = new DraggablePanel();
		pnlCorner.setLayout(new GridLayout(1,1));
		final GraphPanel pnlGraphCorner = new GraphPanel(new Fractal.Julia(
				new Complex(0, 0)));

		glassPane.add(pnlC);
		pnlC.enable();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pnlC.setBounds(getWidth() / 2, getHeight() / 2,
						pnlC.getWidth(), pnlC.getHeight());
			}
		});

		pnlCorner.setBounds(15, 15, 256, 256);
		pnlGraphCorner.setPreferredSize(new Dimension(256, 256));
		pnlCorner.setPreferredSize(new Dimension(256,256));
		pnlCorner.setBorderOffset(15);
		pnlCorner.setSnapDistance(30);
		
		pnlCorner.add(pnlGraphCorner);
		glassPane.add(pnlCorner);

		glassPane.setOpaque(false);
		glassPane.setVisible(true);
		
		MouseAdapter zoomListener = new MouseAdapter() {
			Point p;

			@Override
			public void mouseDragged(MouseEvent e) {
				repaint();
				Graphics g = glassPane.getGraphics();
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
					int x = (int) Math.min(e.getX(), p.getX());
					int y = (int) Math.min(e.getY(), p.getY());
					int width = (int) Math.abs(e.getX() - p.getX());
					int height = (int) Math.abs(e.getY() - p.getY());
					Rectangle r = new Rectangle(x, y, width, height);
					pnlGraphMain.setMathBounds(r);
					// pnlGraphMain.
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					pnlGraphMain.zoomOut();
				}
			}
		};

		glassPane.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e) {
				Complex c = pnlGraphMain.getMathCoords(e.getPoint());
				pnlGraphCorner.updateFractal(new Fractal.Julia(c));
			}
		});
		
		glassPane.addMouseMotionListener(pnlC.getListener(pnlGraphMain));
		glassPane.addMouseMotionListener(zoomListener);
		glassPane.addMouseListener(zoomListener);
		
	}

}
